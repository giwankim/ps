# LeetCode Package Taxonomy Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Move 281 flat LeetCode solution classes and their 281 tests into 36 numeric range packages keyed on the LeetCode problem number, and stamp each solution class with a javadoc link to its problem.

**Architecture:** A committed TSV maps every class to its problem number, slug, and title; it is generated from LeetCode's public problem index plus 24 hand-resolved overrides. Two one-shot scripts consume that TSV — one performs `git mv` plus `package` line rewrites, the other inserts the javadoc stamps. The mapping is a verified bijection with the source tree, so nothing can be silently dropped or duplicated.

**Tech Stack:** Java 25 (toolchain), Gradle 9.7, Spotless + palantir-java-format, JUnit 5, Python 3 for the migration scripts.

**Spec:** `docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md`

## Global Constraints

- **Never run bare `./gradlew :leetcode:test`.** It OOMs at the default heap (1133 tests, 29 failing, 1 skipped before the crash) and hangs past 10 minutes at 4 GB, dying just after `InterleavingStringTest`. The cause is uncommitted work-in-progress solutions carrying `UnsupportedOperationException` stubs — `InterleavingString`, `LongestIncreasingSubsequence`, `NumberOfZigZagArraysII`, `SmallestDivisibleDigitProductII`, `TotalWavinessOfNumbersInRangeII`, `NumberOfWaysToAssignEdgeWeightsI`. This is pre-existing, unrelated to the migration, and **out of scope for this plan**. Use the targeted sample command given in each task. (`FirstBadVersion` also throws `UnsupportedOperationException`, but deliberately — it stubs the API LeetCode injects. It is not WIP.)
- **Primary gate is compilation.** `./gradlew :leetcode:compileJava :leetcode:compileTestJava` must exit 0. No method body is edited by this plan, so compilation plus the structural assertions fully cover what can break.
- **Package formula:** for problem `n`, the package is `p{lo:04d}_{lo+99:04d}` where `lo = (n - 1) // 100 * 100 + 1`. Problem 322 → `p0301_0400`.
- **`leetcode.support` does not move.** Its 3 classes and 2 tests stay at `leetcode/support/`.
- **Run `./gradlew :leetcode:spotlessApply` before every commit** that touches `.java`. The module uses palantir-java-format; the pre-commit hook lints staged Java.
- **American spelling** in all prose, javadoc, and comments.
- **The spec appendix is the reviewed source of truth.** The generated TSV must agree with it row for row; Task 1 asserts this.
- **Never `git add` an untracked file.** Roughly 25 solutions and tests are uncommitted WIP the user chose to leave uncommitted; the migration relocates them but must not commit them. Always `git add -u` with an explicit path limit. Verify after staging that `git status --short leetcode/ | grep '^??'` still lists them.
- Work happens on branch `refactor/leetcode-package-taxonomy`, not `main`.
- Commit only the paths each task names. Unrelated modifications (e.g. `boj/`) stay out.

## File Structure

**Created (committed):**
- `leetcode/tools/fetch_problem_numbers.py` — regenerates the mapping from LeetCode's index. Follows the existing `boj/tools/` convention. Self-verifying: asserts bijection and uniqueness, exits non-zero otherwise.
- `leetcode/tools/problem_numbers.tsv` — the mapping of record: `class`, `number`, `slug`, `title`. Consumed by both migration scripts.
- `docs/leetcode-package-layout.md` — the convention, for future contributors.

**Created (throwaway, scratchpad only — never committed):**
- `migrate.py` — performs the moves and package rewrites.
- `stamp.py` — inserts the javadoc problem links.
- `verify.py` — structural assertions run after Tasks 2 and 3.

**Modified:**
- 562 `.java` files — `package` line only (Task 2).
- 281 solution `.java` files — class-level javadoc only (Task 3).
- `CLAUDE.md` — one pointer line to the new doc (Task 4).
- `docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md` — status line (Task 4).

**Note on testing the tools:** `fetch_problem_numbers.py` carries its assertions inline rather than shipping a separate `_test.py`. This follows the user's stated preference against standalone meta-tests for tooling. `boj/tools/archive_problems_test.py` is the precedent if a separate test is wanted instead.

---

### Task 1: Generate and commit the class → problem-number mapping

**Files:**
- Create: `leetcode/tools/fetch_problem_numbers.py`
- Create: `leetcode/tools/problem_numbers.tsv`

**Interfaces:**
- Consumes: nothing (first task).
- Produces: `leetcode/tools/problem_numbers.tsv`, a tab-separated file with header `class	number	slug	title`, sorted by `number` ascending, 281 data rows. Tasks 2 and 3 read it via `csv.DictReader(f, delimiter="\t")`.

- [ ] **Step 1: Write the fetch script**

Create `leetcode/tools/fetch_problem_numbers.py`:

```python
#!/usr/bin/env python3
"""Map leetcode/ solution class names to LeetCode problem numbers.

Regenerates ``problem_numbers.tsv`` from LeetCode's public problem index. Run
after adding new solution classes::

    python3 leetcode/tools/fetch_problem_numbers.py

Solution class names are PascalCase renderings of the problem title, so most
resolve by a normalized string compare. Names that cannot be recovered that way
are listed in OVERRIDES; each was resolved by reading the class's method
signature. See docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md.
"""

from __future__ import annotations

import json
import pathlib
import re
import sys
import urllib.request

API = "https://leetcode.com/api/problems/all/"
TOOLS = pathlib.Path(__file__).resolve().parent
SRC = TOOLS.parent / "src/main/java/leetcode"
OUT = TOOLS / "problem_numbers.tsv"

# Class names whose LeetCode title normalization cannot bridge, resolved by
# inspecting each class's method signature.
OVERRIDES = {
    "AddBoldTag": 616,
    "CheckIfThereIsValidPath": 1391,
    "CountCompletePairPairs2": 3185,
    "FindGreatesCommonDivisorOfArray": 1979,
    "HitCounter": 362,
    "ImplementTrie": 208,
    "LeastNumberOfUniqueIntegers": 1481,
    "LongestIncreasingPathInMatrix": 329,
    "MaxOccurrencesOfASubstring": 1297,
    "MergeKSortedList": 23,
    "MiddleOfLinkedList": 876,
    "MinRemoveToMakeValidParentheses": 1249,
    "MinStepsToMakeTwoStringsAnagram": 1347,
    "NonOverlapIntervals": 435,
    "Pow": 50,
    "RankTransformationOfAnArray": 1331,
    "RemoveNthFromEndOfList": 19,
    "ReorderLogFiles": 937,
    "SerializeDeserializeBinaryTree": 297,
    "SplitStringMaxNumberUniqueSubstrings": 1593,
    "Sqrt": 69,
    "ThreeSum": 15,
    "TwoEventsHaveConflict": 2446,
    "TwoSumII": 167,
}


def norm(text: str) -> str:
    """Strip everything but lowercase alphanumerics, so 'Sqrt(x)' == 'Sqrt'."""
    return re.sub(r"[^a-z0-9]", "", text.lower())


def fetch_index() -> dict[int, tuple[str, str]]:
    """Return {number: (title, slug)} from LeetCode's public index."""
    request = urllib.request.Request(API, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(request, timeout=60) as response:
        payload = json.load(response)
    index = {}
    for pair in payload["stat_status_pairs"]:
        stat = pair["stat"]
        # frontend_question_id is the number shown on the site. question_id is
        # LeetCode's internal row id and diverges for reworked problems.
        number = stat.get("frontend_question_id") or stat["question_id"]
        index[number] = (stat["question__title"], stat["question__title_slug"])
    return index


def main() -> int:
    index = fetch_index()
    by_title = {norm(title): number for number, (title, _) in index.items()}
    classes = sorted(path.stem for path in SRC.glob("*.java"))

    resolved, unresolved = {}, []
    for name in classes:
        number = OVERRIDES.get(name) or by_title.get(norm(name))
        if number is None:
            unresolved.append(name)
        else:
            resolved[name] = number

    if unresolved:
        print("unresolved class names:", file=sys.stderr)
        for name in unresolved:
            print(f"  {name}", file=sys.stderr)
        print("add each to OVERRIDES after checking its method signature", file=sys.stderr)
        return 1

    duplicates = {}
    for name, number in resolved.items():
        duplicates.setdefault(number, []).append(name)
    collisions = {n: names for n, names in duplicates.items() if len(names) > 1}
    if collisions:
        print("two classes map to the same problem number:", file=sys.stderr)
        for number, names in sorted(collisions.items()):
            print(f"  {number}: {', '.join(sorted(names))}", file=sys.stderr)
        return 1

    rows = sorted(
        (number, name, index[number][1], index[number][0])
        for name, number in resolved.items()
    )
    with OUT.open("w", encoding="utf-8") as out:
        out.write("class\tnumber\tslug\ttitle\n")
        for number, name, slug, title in rows:
            out.write(f"{name}\t{number}\t{slug}\t{title}\n")

    print(f"wrote {OUT.relative_to(TOOLS.parent.parent)} — {len(rows)} classes")
    return 0


if __name__ == "__main__":
    sys.exit(main())
```

- [ ] **Step 2: Run it and confirm it resolves every class**

Run: `python3 leetcode/tools/fetch_problem_numbers.py`
Expected: `wrote leetcode/tools/problem_numbers.tsv — 281 classes`, exit 0.
If it prints unresolved names, a new solution was added since the spec was written — resolve each by reading its method signature, add to `OVERRIDES`, and rerun.

- [ ] **Step 3: Verify the TSV is a bijection with the source tree**

Run:

```bash
cd /Users/gwk/Development/ps
python3 - <<'PY'
import csv, pathlib
src = {p.stem for p in pathlib.Path("leetcode/src/main/java/leetcode").glob("*.java")}
rows = list(csv.DictReader(open("leetcode/tools/problem_numbers.tsv"), delimiter="\t"))
mapped = {r["class"] for r in rows}
assert len(rows) == len(mapped) == len(src), (len(rows), len(mapped), len(src))
assert src == mapped, f"on disk only: {sorted(src-mapped)}  mapped only: {sorted(mapped-src)}"
numbers = [int(r["number"]) for r in rows]
assert len(set(numbers)) == len(numbers), "duplicate problem numbers"
assert numbers == sorted(numbers), "TSV not sorted by number"
print(f"OK bijection over {len(rows)} classes, numbers {numbers[0]}..{numbers[-1]}")
PY
```

Expected: `OK bijection over 281 classes, numbers 1..3867`

- [ ] **Step 4: Verify the TSV agrees with the reviewed spec appendix**

The spec appendix is what the user approved; the TSV must not diverge from it.

Run:

```bash
cd /Users/gwk/Development/ps
python3 - <<'PY'
import csv, re
spec = open("docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md").read()
appendix = dict(
    (m.group(2), int(m.group(1)))
    for m in re.finditer(r"^\| (\d+) \| `(\w+)` \|", spec, re.M)
)
tsv = {r["class"]: int(r["number"])
       for r in csv.DictReader(open("leetcode/tools/problem_numbers.tsv"), delimiter="\t")}
assert appendix == tsv, {
    "in spec only": sorted(set(appendix) - set(tsv)),
    "in tsv only": sorted(set(tsv) - set(appendix)),
    "number differs": {k: (appendix[k], tsv[k]) for k in set(appendix) & set(tsv) if appendix[k] != tsv[k]},
}
print(f"OK TSV matches spec appendix on all {len(tsv)} rows")
PY
```

Expected: `OK TSV matches spec appendix on all 281 rows`

- [ ] **Step 5: Commit**

```bash
cd /Users/gwk/Development/ps
git add leetcode/tools/fetch_problem_numbers.py leetcode/tools/problem_numbers.tsv
git commit -m "feat(leetcode): map every solution class to its problem number

Generates leetcode/tools/problem_numbers.tsv from LeetCode's public problem
index. 257 of 281 classes resolve by normalized title compare; the remaining 24
are hand-resolved overrides. Verified as a bijection with the source tree and
against the reviewed spec appendix."
```

---

### Task 2: Move solutions and tests into numeric range packages

**Files:**
- Create (scratchpad): `migrate.py`, `verify.py`
- Modify: all 281 files in `leetcode/src/main/java/leetcode/*.java` (moved + `package` line)
- Modify: all 281 files in `leetcode/src/test/java/leetcode/*.java` (moved + `package` line)

**Interfaces:**
- Consumes: `leetcode/tools/problem_numbers.tsv` from Task 1.
- Produces: 36 packages `leetcode.p0001_0100` … `leetcode.p3801_3900`, each containing solution classes and their `*Test` counterparts. `leetcode.support` unchanged.

- [ ] **Step 1: Record the pre-move baseline**

The move must not change these numbers. Capture them first.

Run:

```bash
cd /Users/gwk/Development/ps
ls leetcode/src/main/java/leetcode/*.java | wc -l   # expect 281
ls leetcode/src/test/java/leetcode/*.java | wc -l   # expect 281
git rev-parse HEAD                                   # note for rollback
```

Expected: 281 and 281.

- [ ] **Step 2: Write the migration script**

Create `migrate.py` in the scratchpad directory (not the repo):

```python
#!/usr/bin/env python3
"""One-shot: move leetcode solutions and tests into numeric range packages."""

import csv
import pathlib
import subprocess
import sys

ROOT = pathlib.Path("/Users/gwk/Development/ps")
MAIN = ROOT / "leetcode/src/main/java/leetcode"
TEST = ROOT / "leetcode/src/test/java/leetcode"
TSV = ROOT / "leetcode/tools/problem_numbers.tsv"


def package_for(number: int) -> str:
    lo = (number - 1) // 100 * 100 + 1
    return f"p{lo:04d}_{lo + 99:04d}"


def is_tracked(path: pathlib.Path) -> bool:
    return subprocess.run(
        ["git", "ls-files", "--error-unmatch", str(path)],
        cwd=ROOT,
        capture_output=True,
    ).returncode == 0


def move(path: pathlib.Path, package: str) -> None:
    target = path.parent / package / path.name
    target.parent.mkdir(exist_ok=True)
    # git mv preserves history but exits 128 on untracked files ("fatal: not
    # under version control"). Roughly 25 files here are new uncommitted
    # solves; they have no history to preserve, so a plain rename loses
    # nothing. Note `git mv -n` does NOT reproduce the failure — the dry run
    # skips the tracking check — so this must be decided per file at runtime.
    if is_tracked(path):
        subprocess.run(["git", "mv", str(path), str(target)], check=True, cwd=ROOT)
    else:
        path.rename(target)
    text = target.read_text(encoding="utf-8")
    if not text.startswith("package leetcode;\n"):
        raise SystemExit(f"{target}: unexpected first line, refusing to rewrite")
    target.write_text(
        text.replace("package leetcode;\n", f"package leetcode.{package};\n", 1),
        encoding="utf-8",
    )


def main() -> int:
    rows = list(csv.DictReader(TSV.open(encoding="utf-8"), delimiter="\t"))
    for row in rows:
        name, package = row["class"], package_for(int(row["number"]))
        move(MAIN / f"{name}.java", package)
        move(TEST / f"{name}Test.java", package)
    print(f"moved {len(rows) * 2} files into {len({package_for(int(r['number'])) for r in rows})} packages")
    return 0


if __name__ == "__main__":
    sys.exit(main())
```

- [ ] **Step 3: Run the migration**

Run: `python3 /private/tmp/claude-501/-Users-gwk-Development-ps/96b0ab8d-f87b-4290-88c5-8be1045ff01b/scratchpad/migrate.py`
Expected: `moved 562 files into 36 packages`

If it exits with "unexpected first line", nothing further is moved — inspect that file, fix, and rerun from a clean tree (`git reset --hard` to the SHA from Step 1).

- [ ] **Step 4: Write the structural verification script**

Create `verify.py` in the scratchpad:

```python
#!/usr/bin/env python3
"""Assert the leetcode tree matches the mapping after migration."""

import csv
import pathlib
import re
import sys

ROOT = pathlib.Path("/Users/gwk/Development/ps")
MAIN = ROOT / "leetcode/src/main/java/leetcode"
TEST = ROOT / "leetcode/src/test/java/leetcode"
TSV = ROOT / "leetcode/tools/problem_numbers.tsv"


def package_for(number: int) -> str:
    lo = (number - 1) // 100 * 100 + 1
    return f"p{lo:04d}_{lo + 99:04d}"


def main() -> int:
    rows = list(csv.DictReader(TSV.open(encoding="utf-8"), delimiter="\t"))
    problems = []

    stragglers = sorted(p.name for p in MAIN.glob("*.java")) + sorted(
        p.name for p in TEST.glob("*.java")
    )
    if stragglers:
        problems.append(f"files left in the flat package: {stragglers}")

    for row in rows:
        name, package = row["class"], package_for(int(row["number"]))
        for base, suffix in ((MAIN, ""), (TEST, "Test")):
            path = base / package / f"{name}{suffix}.java"
            if not path.exists():
                problems.append(f"missing: {path.relative_to(ROOT)}")
                continue
            declared = re.match(r"package (\S+);", path.read_text(encoding="utf-8"))
            expected = f"leetcode.{package}"
            if not declared or declared.group(1) != expected:
                problems.append(
                    f"{path.relative_to(ROOT)}: declares "
                    f"{declared.group(1) if declared else '<none>'}, expected {expected}"
                )

    for support in (MAIN / "support", TEST / "support"):
        if not support.is_dir():
            problems.append(f"support package vanished: {support.relative_to(ROOT)}")

    if problems:
        for problem in problems:
            print(f"  {problem}", file=sys.stderr)
        print(f"{len(problems)} problems", file=sys.stderr)
        return 1

    packages = sorted({package_for(int(r["number"])) for r in rows})
    print(f"OK {len(rows)} solutions + {len(rows)} tests across {len(packages)} packages")
    return 0


if __name__ == "__main__":
    sys.exit(main())
```

- [ ] **Step 5: Run structural verification**

Run: `python3 /private/tmp/claude-501/-Users-gwk-Development-ps/96b0ab8d-f87b-4290-88c5-8be1045ff01b/scratchpad/verify.py`
Expected: `OK 281 solutions + 281 tests across 36 packages`

- [ ] **Step 6: Verify it compiles**

This is the gate that matters — a package move can only break symbol resolution.

Run: `./gradlew :leetcode:compileJava :leetcode:compileTestJava --console=plain`
Expected: `BUILD SUCCESSFUL`, exit 0.

- [ ] **Step 7: Run the known-green test sample**

These four exercise `ListNode` and `TreeNode`, proving the `leetcode.support` imports still resolve from the deeper packages. Do not run the full suite (see Global Constraints).

Run:

```bash
cd /Users/gwk/Development/ps
./gradlew :leetcode:test --console=plain \
  --tests 'leetcode.p0001_0100.TwoSumTest' \
  --tests 'leetcode.p0001_0100.AddTwoNumbersTest' \
  --tests 'leetcode.p0201_0300.BinaryTreePathsTest' \
  --tests 'leetcode.p0001_0100.MinimumPathSumTest'
```

Expected: `BUILD SUCCESSFUL`, exit 0.

If a filter matches nothing, Gradle fails with "No tests found for given includes" — that itself proves the class is not where the mapping says. Re-check with `verify.py`.

- [ ] **Step 8: Confirm git preserved history**

Run: `git log --follow --oneline -- leetcode/src/main/java/leetcode/p0001_0100/TwoSum.java | tail -3`
Expected: commits predating this migration are listed.

- [ ] **Step 9: Format and commit**

Use `git add -u` with an explicit path limit, **not** a bare `git add <dir>`.
`git mv` already staged the tracked renames; `-u` picks up the package-line
edits on top of them. A bare `git add` would additionally stage the ~25
untracked WIP solutions, committing work the user chose to leave uncommitted.
The path limit keeps unrelated modifications (e.g. `boj/`) out of the commit.

```bash
cd /Users/gwk/Development/ps
./gradlew :leetcode:spotlessApply --console=plain
git add -u leetcode/src/main/java/leetcode leetcode/src/test/java/leetcode
git status --short leetcode/ | grep '^??' | wc -l   # expect ~25, still untracked
git commit -m "refactor(leetcode): group solutions into numeric range packages

Move 281 solutions and their 281 tests from the flat leetcode package into 36
fixed-width-100 packages keyed on the LeetCode problem number, so the IDE tree
is browsable. Package is a pure function of the number, so it is stable as new
solutions land. leetcode.support is unchanged; no imports needed rewriting."
```

---

### Task 3: Stamp each solution class with its problem link

**Files:**
- Create (scratchpad): `stamp.py`
- Modify: 281 solution files under `leetcode/src/main/java/leetcode/p*/`

**Interfaces:**
- Consumes: `leetcode/tools/problem_numbers.tsv` (Task 1); the package layout from Task 2.
- Produces: every solution class carries a class-level javadoc whose first line is `<a href="https://leetcode.com/problems/{slug}/">{number}. {title}</a>`.

- [ ] **Step 1: Write the stamp script**

Two cases. 279 files have no class-level javadoc and take a plain insert. `LRUCache` and `MinStack` already have one and must have the link merged in — Java ignores a second adjacent javadoc block, so prepending would silently lose their `@implNote`.

Create `stamp.py` in the scratchpad:

```python
#!/usr/bin/env python3
"""One-shot: add a javadoc problem link to each leetcode solution class."""

import csv
import pathlib
import re
import sys

ROOT = pathlib.Path("/Users/gwk/Development/ps")
MAIN = ROOT / "leetcode/src/main/java/leetcode"
TSV = ROOT / "leetcode/tools/problem_numbers.tsv"


def package_for(number: int) -> str:
    lo = (number - 1) // 100 * 100 + 1
    return f"p{lo:04d}_{lo + 99:04d}"


def stamp(path: pathlib.Path, name: str, number: int, slug: str, title: str):
    """Return (new_text, merged) where merged is True if existing javadoc was folded in."""
    text = path.read_text(encoding="utf-8")
    link = f'<a href="https://leetcode.com/problems/{slug}/">{number}. {title}</a>'

    # Anchor on the public type matching the filename. FirstBadVersion.java
    # declares a package-private VersionControl helper first, so "the first
    # class in the file" is the wrong anchor.
    declaration = re.search(rf"^public class {name}\b", text, re.M)
    if declaration is None:
        raise SystemExit(f"{path}: no 'public class {name}' declaration")

    preceding = text[: declaration.start()]
    # (?:(?!\*/).)* forbids crossing a comment terminator, so this matches only
    # the javadoc block immediately above the declaration. A plain (.*?) would
    # span from an earlier block through this one, swallowing the code between.
    existing = re.search(r"/\*\*((?:(?!\*/).)*)\*/\s*$", preceding, re.S)
    if existing:
        body = re.sub(r"^\s*\*[ ]?", "", existing.group(1).strip(), flags=re.M).strip()
        replacement = f"/**\n * {link}\n *\n * {body}\n */\n"
        return text[: existing.start()] + replacement + text[declaration.start():], True
    return preceding + f"/** {link} */\n" + text[declaration.start():], False


def main() -> int:
    rows = list(csv.DictReader(TSV.open(encoding="utf-8"), delimiter="\t"))
    merged = []
    for row in rows:
        name, number = row["class"], int(row["number"])
        path = MAIN / package_for(number) / f"{name}.java"
        text, was_merged = stamp(path, name, number, row["slug"], row["title"])
        if was_merged:
            merged.append(name)
        path.write_text(text, encoding="utf-8")
    print(f"stamped {len(rows)} classes ({len(merged)} merged into existing javadoc: {merged})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
```

- [ ] **Step 2: Run it**

Run: `python3 /private/tmp/claude-501/-Users-gwk-Development-ps/96b0ab8d-f87b-4290-88c5-8be1045ff01b/scratchpad/stamp.py`
Expected: `stamped 281 classes (2 merged into existing javadoc: ['LRUCache', 'MinStack'])`

If the merged count is not exactly 2, a file has class-level javadoc that was not expected — inspect before proceeding.

- [ ] **Step 3: Verify every class got exactly one stamp**

Run:

```bash
cd /Users/gwk/Development/ps
echo -n "files with a problem link: "
grep -rl 'leetcode.com/problems/' leetcode/src/main/java/leetcode/p*/ | wc -l
echo -n "duplicate stamps (expect 0): "
grep -rc 'leetcode.com/problems/' leetcode/src/main/java/leetcode/p*/*.java | grep -v ':1$' | wc -l
```

Expected: `281` and `0`.

- [ ] **Step 4: Spot-check the two merged files and the two-type file**

Run:

```bash
cd /Users/gwk/Development/ps
sed -n '1,14p' leetcode/src/main/java/leetcode/p0101_0200/LRUCache.java
grep -n -B2 'class VersionControl\|public class FirstBadVersion' \
  leetcode/src/main/java/leetcode/p0201_0300/FirstBadVersion.java
```

Expected: `LRUCache` shows one javadoc block containing both the link and the original `@implNote`. `FirstBadVersion` shows the link immediately above `public class FirstBadVersion`, with `class VersionControl` unstamped.

- [ ] **Step 5: Verify it still compiles**

Run: `./gradlew :leetcode:compileJava :leetcode:compileTestJava --console=plain`
Expected: `BUILD SUCCESSFUL`, exit 0.

- [ ] **Step 6: Format and commit**

```bash
cd /Users/gwk/Development/ps
./gradlew :leetcode:spotlessApply --console=plain
git add -u leetcode/src/main/java/leetcode
git commit -m "docs(leetcode): link each solution class to its problem

Add a class-level javadoc link carrying the problem number, title, and URL, so
Ctrl+Shift+F on a number finds the class and the number lives in the source
rather than only in the directory name."
```

---

### Task 4: Document the convention

**Files:**
- Create: `docs/leetcode-package-layout.md`
- Modify: `CLAUDE.md`
- Modify: `docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md`

**Interfaces:**
- Consumes: the layout established in Tasks 2 and 3.
- Produces: nothing consumed by later tasks. Final task.

- [ ] **Step 1: Write the convention doc**

Create `docs/leetcode-package-layout.md`:

```markdown
# LeetCode package layout

Solutions in `leetcode/` live in numeric range packages keyed on the LeetCode
problem number, not in one flat package.

## Where a new solution goes

For problem `n`, the package is `p{lo:04d}_{lo+99:04d}` where
`lo = (n - 1) // 100 * 100 + 1`. Problem 322 goes in `leetcode.p0301_0400`.

The test mirrors it exactly: `CoinChangeTest` sits beside `CoinChange` in
`leetcode/src/test/java/leetcode/p0301_0400/`.

Shared node types (`ListNode`, `TreeNode`, `Node`) stay in `leetcode.support`.

## Class-level javadoc

Every solution class carries a link to its problem:

```java
/** <a href="https://leetcode.com/problems/coin-change/">322. Coin Change</a> */
public class CoinChange {
```

This makes `Ctrl+Shift+F "322."` find the class and keeps the number in the
source, so the packages can be re-bucketed later by script.

## Finding a problem number

`leetcode/tools/problem_numbers.tsv` maps every class to its number, slug, and
title. Regenerate it after adding solutions:

    python3 leetcode/tools/fetch_problem_numbers.py

It resolves class names against LeetCode's public problem index by normalized
title compare, and fails loudly listing any class it could not resolve — add
those to `OVERRIDES` in the script after checking the class's method signature.

## Caveat: the test suite

`./gradlew :leetcode:test` currently OOMs and then hangs on work-in-progress
solutions that contain `UnsupportedOperationException` stubs. Prefer
`./gradlew :leetcode:compileJava :leetcode:compileTestJava` plus a targeted
`--tests` filter.

Rationale and rejected alternatives:
[docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md](superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md)
```

- [ ] **Step 2: Add the pointer to CLAUDE.md**

Append to `CLAUDE.md`, after the `cpp/` paragraph:

```markdown
Solutions in `leetcode/` are grouped into numeric range packages keyed on the
LeetCode problem number, and each class carries a javadoc link to its problem.
See [docs/leetcode-package-layout.md](docs/leetcode-package-layout.md) before
adding a solution.
```

- [ ] **Step 3: Mark the spec implemented**

Run:

```bash
cd /Users/gwk/Development/ps
sed -i '' 's/^\*\*Status:\*\* approved 2026-08-20$/**Status:** implemented 2026-08-20/' \
  docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md
grep -n '^\*\*Status' docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md
```

Expected: `**Status:** implemented 2026-08-20`

- [ ] **Step 4: Final verification sweep**

Run:

```bash
cd /Users/gwk/Development/ps
python3 /private/tmp/claude-501/-Users-gwk-Development-ps/96b0ab8d-f87b-4290-88c5-8be1045ff01b/scratchpad/verify.py
./gradlew :leetcode:compileJava :leetcode:compileTestJava :leetcode:spotlessCheck --console=plain
echo -n "flat-package leftovers (expect 0): "
ls leetcode/src/main/java/leetcode/*.java leetcode/src/test/java/leetcode/*.java 2>/dev/null | wc -l
```

Expected: `OK 281 solutions + 281 tests across 36 packages`, `BUILD SUCCESSFUL`, `0`.

- [ ] **Step 5: Commit**

```bash
cd /Users/gwk/Development/ps
git add docs/leetcode-package-layout.md CLAUDE.md \
  docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md
git commit -m "docs(leetcode): document the numeric package layout convention"
```

---

## Rollback

Every task is a single commit touching only paths it names. To undo the
migration entirely: `git revert --no-commit <task4>..<task1>` then commit, or
`git reset --hard <SHA from Task 2 Step 1>` if nothing else has landed since.
