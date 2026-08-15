#!/usr/bin/env python3
"""Archive BOJ problem statements and samples from a local blj catalog snapshot.

acmicpc.net is unreachable, so problem statements have to come from an archive.
golbin/blj (https://github.com/golbin/blj) ships one: a Git-LFS SQLite catalog
of BOJ problems including statement HTML, limits, and sample I/O. blj itself is
a Vue web app with a pnpm/Docker toolchain, but none of that is needed here --
the catalog is a committed release artifact, so this script reads it directly
and never runs blj.

The catalog is NOT a dependency of this repo. It lives outside the tree, it is
~141 MB of LFS binary, and the statements are BOJ's copyright. Only this script
is versioned; its output under boj/problems/ is gitignored. See
docs/boj-problem-archive.md for setup.

Usage:
    python3 boj/tools/archive_problems.py              # every solved problem not yet archived
    python3 boj/tools/archive_problems.py 2015 1753    # just these ids
    python3 boj/tools/archive_problems.py --force      # re-archive, overwriting
"""

import argparse
import json
import os
import re
import sqlite3
import sys
from html.parser import HTMLParser
from pathlib import Path

PROBLEM_SCHEMA_VERSION = 1

BOJ_ORIGIN = "https://www.acmicpc.net"

DEFAULT_CATALOG = Path.home() / "Development" / "blj" / "data" / "release" / "boj-catalog.sqlite"

# Blocks that a <br> must survive: normalization collapses whitespace runs, so a
# line break is carried through as a sentinel and restored afterwards.
_BR = "\x00"


class _MarkdownWriter(HTMLParser):
    """Converts the sanitized BOJ statement fragments to Markdown.

    The tag inventory is closed and small -- p, sub, em, li, td, code, sup, ul,
    th, strong, img, tr, pre, ol, tbody, table, a, span, blockquote, thead, div,
    br is the complete set appearing across this repo's solved problems -- so a
    stdlib parser covers it without pulling in a Markdown dependency.
    """

    def __init__(self):
        super().__init__(convert_charrefs=True)
        self._blocks = []  # (kind, text) pairs; kind drives the join separator
        self._stack = [[]]  # inline buffer stack; nested inline tags push a frame
        self._list_stack = []
        self._quote_depth = 0
        self._pre_depth = 0
        self._pre_buf = []
        self._table = None
        self._pending_kind = "p"
        self._pending_prefix = "- "
        self._href_stack = []

    # --- buffers ---

    def _emit(self, text):
        self._stack[-1].append(text)

    def _push(self):
        self._stack.append([])

    def _pop(self):
        return "".join(self._stack.pop())

    def _flush(self, kind=None):
        text = _normalize("".join(self._stack[0]))
        self._stack[0] = []
        if not text:
            return
        if self._table is not None and self._table["cell"] is not None:
            self._table["cell"].append(text)
            return
        if self._quote_depth:
            text = "\n".join("> " + line if line else ">" for line in text.split("\n"))
        self._blocks.append((kind or self._pending_kind, text))
        self._pending_kind = "p"

    # --- parser callbacks ---

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        if self._pre_depth:
            if tag == "br":
                self._pre_buf.append("\n")
            return

        if tag == "pre":
            self._flush()
            self._pre_depth = 1
            self._pre_buf = []
        elif tag == "table":
            self._flush()
            self._table = {"rows": [], "row": None, "cell": None}
        elif tag == "tr" and self._table is not None:
            self._table["row"] = []
        elif tag in ("td", "th") and self._table is not None:
            self._flush()
            self._table["cell"] = []
        elif tag in ("ul", "ol"):
            self._flush()
            self._list_stack.append({"type": tag, "n": 0})
        elif tag == "li":
            self._flush()
            if self._list_stack:
                item = self._list_stack[-1]
                item["n"] += 1
                self._pending_prefix = "- " if item["type"] == "ul" else f"{item['n']}. "
            else:
                self._pending_prefix = "- "
        elif tag == "blockquote":
            self._flush()
            self._quote_depth += 1
        elif tag in ("p", "div"):
            self._flush()
        elif tag == "br":
            self._emit(_BR)
        elif tag in ("em", "i", "strong", "b", "code", "sub", "sup", "a"):
            if tag == "a":
                self._href_stack.append(attrs.get("href", ""))
            self._push()
        elif tag == "img":
            self._emit(f"![{attrs.get('alt', '')}]({_resolve(attrs.get('src', ''))})")

    def handle_endtag(self, tag):
        if self._pre_depth:
            if tag == "pre":
                self._pre_depth = 0
                body = "".join(self._pre_buf).strip("\n")
                if body:
                    self._blocks.append(("pre", f"```\n{body}\n```"))
            return

        if tag == "table" and self._table is not None:
            rows = self._table["rows"]
            self._table = None
            if rows:
                self._blocks.append(("table", _render_table(rows)))
        elif tag == "tr" and self._table is not None:
            if self._table["row"] is not None:
                self._table["rows"].append(self._table["row"])
            self._table["row"] = None
        elif tag in ("td", "th") and self._table is not None:
            self._flush()
            cell = " ".join(self._table["cell"] or [])
            self._table["cell"] = None
            if self._table["row"] is None:
                self._table["row"] = []
            self._table["row"].append(cell)
        elif tag in ("ul", "ol"):
            self._flush()
            if self._list_stack:
                self._list_stack.pop()
        elif tag == "li":
            prefix = self._pending_prefix
            text = _normalize("".join(self._stack[0]))
            self._stack[0] = []
            if text:
                self._blocks.append(("li", prefix + text))
        elif tag == "blockquote":
            self._flush()
            self._quote_depth = max(0, self._quote_depth - 1)
        elif tag in ("p", "div"):
            self._flush()
        elif tag in ("em", "i"):
            self._wrap("*")
        elif tag in ("strong", "b"):
            self._wrap("**")
        elif tag == "code":
            self._wrap("`")
        elif tag in ("sub", "sup"):
            # BOJ pads these ("X<sub>b </sub>- X<sub>a</sub>"), so trim before
            # judging width -- otherwise a one-character index is parenthesized.
            # The trailing space is load-bearing though: it is the separator
            # before the next token, so it is re-emitted outside the marker.
            inner = self._pop()
            core = inner.strip()
            if core:
                mark = "^" if tag == "sup" else "_"
                body = core if len(core) == 1 else f"({core})"
                self._emit(mark + body + (" " if inner[-1:].isspace() else ""))
        elif tag == "a":
            inner = self._pop()
            href = self._href_stack.pop() if self._href_stack else ""
            self._emit(f"[{inner}]({href})" if href else inner)

    def handle_startendtag(self, tag, attrs):
        self.handle_starttag(tag, attrs)
        if tag not in ("br", "img"):
            self.handle_endtag(tag)

    def handle_data(self, data):
        if self._pre_depth:
            self._pre_buf.append(data)
        else:
            self._emit(data)

    def _wrap(self, marker):
        inner = self._pop()
        self._emit(f"{marker}{inner}{marker}" if inner else "")

    def to_markdown(self):
        self._flush()
        out = []
        for index, (kind, text) in enumerate(self._blocks):
            if index:
                previous = self._blocks[index - 1][0]
                out.append("\n" if kind == "li" and previous == "li" else "\n\n")
            out.append(text)
        return "".join(out).strip()


def _resolve(src):
    """Resolves a statement image URL. Site-relative srcs point at acmicpc.net."""
    if src.startswith("//"):
        return "https:" + src
    if src.startswith("/"):
        return BOJ_ORIGIN + src
    return src


def _normalize(text):
    """Collapses whitespace while preserving <br> as a hard line break.

    A str-mode \\s matches Unicode whitespace, so the U+00A0 that &nbsp;
    decodes to is folded into an ordinary space by the same pass."""
    lines = [re.sub(r"\s+", " ", part).strip() for part in text.split(_BR)]
    return "\n".join(lines).strip()


def _render_table(rows):
    """Renders rows as a GFM table. The first row is the header: GFM requires a
    separator, so a headerless BOJ table would otherwise collapse into prose."""
    width = max(len(row) for row in rows)
    padded = [row + [""] * (width - len(row)) for row in rows]

    def line(cells):
        return "| " + " | ".join(cell.replace("|", "\\|") for cell in cells) + " |"

    out = [line(padded[0]), "| " + " | ".join(["---"] * width) + " |"]
    out.extend(line(row) for row in padded[1:])
    return "\n".join(out)


def html_to_markdown(source):
    """Converts a BOJ statement HTML fragment to Markdown. Empty or NULL input
    yields an empty string -- hint_html is NULL for most problems."""
    if not source:
        return ""
    writer = _MarkdownWriter()
    writer.feed(source)
    writer.close()
    return writer.to_markdown()


def sample_name(index):
    """Names the nth sample, zero-padded to at least two digits (sample-01)."""
    return f"sample-{index + 1:02d}"


def build_problem_json(row, sample_count, catalog_updated_at):
    """Builds the problem.json sidecar, mirroring algospot/problems/*/problem.json."""
    return {
        "content": {"raw_html": "source.html", "statement_markdown": "statement.md"},
        "id": row["id"],
        "level": row["level"],
        "limits": {"time_ms": row["time_limit_ms"], "memory_mb": row["memory_limit_mb"]},
        "provenance": json.loads(row["provenance_json"] or "{}"),
        "retrieval": {
            "archived_at": row["archived_at"],
            "catalog_updated_at": catalog_updated_at,
            "coverage": row["coverage"],
        },
        "samples": [
            {
                "name": sample_name(i),
                "input": f"samples/{sample_name(i)}.in",
                "output": f"samples/{sample_name(i)}.out",
            }
            for i in range(sample_count)
        ],
        "schema_version": PROBLEM_SCHEMA_VERSION,
        "source_url": row["boj_url"],
        "tags": json.loads(row["tags_json"] or "[]"),
        "title": row["title"],
    }


def build_statement_md(row, samples):
    """Renders statement.md: heading, limits, then the BOJ section structure."""
    limits = []
    if row["level"]:
        limits.append(row["level"])
    if row["time_limit_ms"] is not None:
        limits.append(f"{row['time_limit_ms']} ms")
    if row["memory_limit_mb"] is not None:
        limits.append(f"{row['memory_limit_mb']} MB")

    lines = [f"# {row['title']}", "", f"문제 ID: `{row['id']}`"]
    if limits:
        lines[-1] += " · " + " · ".join(limits)
    lines += ["", f"출처: {row['boj_url']}"]

    for heading, column in (("문제", "statement_html"), ("입력", "input_html"), ("출력", "output_html")):
        body = html_to_markdown(row[column])
        if body:
            lines += ["", f"## {heading}", "", body]

    hint = html_to_markdown(row["hint_html"])
    if hint:
        lines += ["", "## 힌트", "", hint]

    if samples:
        lines += ["", "## 예제"]
        for index, sample in enumerate(samples):
            lines += [
                "",
                f"### {sample_name(index)}",
                "",
                "입력",
                "",
                "```",
                sample.get("input", "").rstrip("\n"),
                "```",
                "",
                "출력",
                "",
                "```",
                sample.get("output", "").rstrip("\n"),
                "```",
            ]

    return "\n".join(lines).rstrip() + "\n"


def build_source_html(row):
    """Preserves the archived fragments verbatim, so a lossy Markdown conversion
    is always recoverable without re-reading the catalog."""
    parts = [f"<!-- archived from {row['boj_url']} via golbin/blj catalog -->"]
    for label, column in (
        ("problem_description", "statement_html"),
        ("problem_input", "input_html"),
        ("problem_output", "output_html"),
        ("problem_hint", "hint_html"),
    ):
        if row[column]:
            parts.append(f'<section id="{label}">\n{row[column]}\n</section>')
    return "\n\n".join(parts) + "\n"


def solved_ids(repo_root):
    """Lists the BOJ ids this repo has solutions for. boj/support holds shared
    helpers such as FastIO, not a problem, so only bojNNNN directories count."""
    package_root = repo_root / "boj" / "src" / "main" / "java" / "boj"
    if not package_root.is_dir():
        return []
    ids = []
    for entry in package_root.iterdir():
        match = re.fullmatch(r"boj(\d+)", entry.name)
        if entry.is_dir() and match:
            ids.append(int(match.group(1)))
    return sorted(ids)


def write_problem(out_dir, row, catalog_updated_at):
    """Writes one boj/problems/<id>/ directory. Returns the sample count."""
    samples = json.loads(row["samples_json"] or "[]")
    problem_dir = out_dir / str(row["id"])
    (problem_dir / "samples").mkdir(parents=True, exist_ok=True)

    for index, sample in enumerate(samples):
        name = sample_name(index)
        (problem_dir / "samples" / f"{name}.in").write_text(sample.get("input", ""), encoding="utf-8")
        (problem_dir / "samples" / f"{name}.out").write_text(sample.get("output", ""), encoding="utf-8")

    (problem_dir / "statement.md").write_text(build_statement_md(row, samples), encoding="utf-8")
    (problem_dir / "source.html").write_text(build_source_html(row), encoding="utf-8")
    document = build_problem_json(row, len(samples), catalog_updated_at)
    (problem_dir / "problem.json").write_text(
        json.dumps(document, ensure_ascii=False, indent=2, sort_keys=True) + "\n", encoding="utf-8"
    )
    return len(samples)


def main(argv=None):
    repo_root = Path(__file__).resolve().parents[2]
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("ids", nargs="*", type=int, help="BOJ ids to archive (default: every solved problem)")
    parser.add_argument(
        "--catalog",
        type=Path,
        default=Path(os.environ.get("BLJ_CATALOG", DEFAULT_CATALOG)),
        help=f"path to boj-catalog.sqlite (default: {DEFAULT_CATALOG})",
    )
    parser.add_argument("--out", type=Path, default=repo_root / "boj" / "problems", help="output directory")
    parser.add_argument("--force", action="store_true", help="re-archive problems that are already present")
    args = parser.parse_args(argv)

    if not args.catalog.is_file():
        parser.error(f"catalog not found: {args.catalog}\nSee docs/boj-problem-archive.md for setup.")
    if args.catalog.stat().st_size < 1_000_000:
        parser.error(
            f"{args.catalog} is {args.catalog.stat().st_size} bytes -- this is a Git LFS pointer, not the "
            f"catalog.\nRun: git -C {args.catalog.parents[2]} lfs pull --include data/release/boj-catalog.sqlite"
        )

    wanted = args.ids or solved_ids(repo_root)
    if not wanted:
        print("nothing to archive: no bojNNNN packages found", file=sys.stderr)
        return 1

    if not args.force:
        pending = [i for i in wanted if not (args.out / str(i) / "problem.json").is_file()]
        skipped = len(wanted) - len(pending)
        if skipped:
            print(f"skipping {skipped} already archived (use --force to overwrite)")
        wanted = pending
    if not wanted:
        print("already up to date")
        return 0

    connection = sqlite3.connect(f"file:{args.catalog}?mode=ro", uri=True)
    connection.row_factory = sqlite3.Row
    try:
        catalog_updated_at = connection.execute(
            "SELECT value FROM metadata WHERE key = 'updatedAt'"
        ).fetchone()
        catalog_updated_at = catalog_updated_at[0] if catalog_updated_at else None

        placeholders = ",".join("?" * len(wanted))
        rows = connection.execute(
            f"SELECT id, title, boj_url, level, tags_json, time_limit_ms, memory_limit_mb, "
            f"statement_html, input_html, output_html, hint_html, samples_json, provenance_json, "
            f"coverage, archived_at FROM problems WHERE id IN ({placeholders}) ORDER BY id",
            wanted,
        ).fetchall()
    finally:
        connection.close()

    found = {row["id"] for row in rows}
    written = 0
    without_statement = []
    for row in rows:
        samples = write_problem(args.out, row, catalog_updated_at)
        written += 1
        if not row["statement_html"]:
            without_statement.append(row["id"])
        print(f"  {row['id']:>6}  {row['title']}  ({samples} samples)")

    print(f"\narchived {written} problem(s) to {args.out} from a {catalog_updated_at} snapshot")
    missing = [i for i in wanted if i not in found]
    if missing:
        print(f"NOT IN CATALOG ({len(missing)}): {' '.join(map(str, missing))}", file=sys.stderr)
    if without_statement:
        print(
            f"METADATA ONLY, no statement text ({len(without_statement)}): "
            f"{' '.join(map(str, without_statement))}",
            file=sys.stderr,
        )
    return 0


if __name__ == "__main__":
    sys.exit(main())
