#!/usr/bin/env python3
"""Resolve a LeetCode problem number to everything a stub needs.

    python3 .claude/skills/leetcode-red-specs/scripts/problem_meta.py 1431
    python3 .../problem_meta.py 1431 --statement   # also dump the statement as text
    python3 .../problem_meta.py --slug two-sum

Prints the number, title, slug, difficulty, paid-only flag, the target package
and file paths per docs/leetcode-package-layout.md, and the Java method
signature LeetCode hands the solver -- so the stub's signature is copied rather
than guessed.

Two public endpoints back this, both reachable without a login:

* ``https://leetcode.com/api/problems/all/`` maps every number to its slug.
  Cached in the temp dir for a week; ``--refresh`` re-fetches.
* ``https://leetcode.com/graphql`` returns the metadata and ``codeSnippets``.

``content`` (the statement) comes back only for free problems. When
``isPaidOnly`` is true it is null, which is exactly the case the signed-in
Chrome session exists to cover -- read the page there instead.
"""

from __future__ import annotations

import argparse
import html
import json
import pathlib
import re
import sys
import tempfile
import time
import urllib.request

INDEX_API = "https://leetcode.com/api/problems/all/"
GRAPHQL_API = "https://leetcode.com/graphql"
CACHE = pathlib.Path(tempfile.gettempdir()) / "leetcode_problem_index.json"
CACHE_TTL_SECONDS = 7 * 24 * 60 * 60
UA = "Mozilla/5.0"

QUESTION_QUERY = """
query question($titleSlug: String!) {
  question(titleSlug: $titleSlug) {
    questionFrontendId
    title
    titleSlug
    difficulty
    isPaidOnly
    content
    exampleTestcases
    topicTags { name }
    hints
    codeSnippets { langSlug code }
  }
}
"""


def repo_root() -> pathlib.Path:
    """Walk up from this file to the directory holding settings.gradle.kts."""
    here = pathlib.Path(__file__).resolve()
    for candidate in [*here.parents, *pathlib.Path.cwd().resolve().parents]:
        if (candidate / "settings.gradle.kts").exists():
            return candidate
    return pathlib.Path.cwd()


def load_index(refresh: bool = False) -> dict[int, str]:
    """Return {frontend number: slug} for every public problem."""
    fresh = CACHE.exists() and time.time() - CACHE.stat().st_mtime < CACHE_TTL_SECONDS
    if fresh and not refresh:
        payload = json.loads(CACHE.read_text())
    else:
        request = urllib.request.Request(INDEX_API, headers={"User-Agent": UA})
        with urllib.request.urlopen(request, timeout=60) as response:
            payload = json.load(response)
        CACHE.write_text(json.dumps(payload))
    index = {}
    for pair in payload["stat_status_pairs"]:
        stat = pair["stat"]
        # frontend_question_id is the number shown on the site; question_id is
        # LeetCode's internal row id and diverges for reworked problems.
        number = stat.get("frontend_question_id") or stat["question_id"]
        index[int(number)] = stat["question__title_slug"]
    return index


def fetch_question(slug: str) -> dict:
    body = json.dumps({"query": QUESTION_QUERY, "variables": {"titleSlug": slug}}).encode()
    request = urllib.request.Request(
        GRAPHQL_API,
        data=body,
        headers={
            "Content-Type": "application/json",
            "User-Agent": UA,
            "Referer": f"https://leetcode.com/problems/{slug}/",
        },
    )
    with urllib.request.urlopen(request, timeout=60) as response:
        payload = json.load(response)
    question = payload.get("data", {}).get("question")
    if not question:
        raise SystemExit(f"leetcode.com/graphql returned no question for slug {slug!r}")
    return question


def package_of(number: int) -> str:
    lo = (number - 1) // 100 * 100 + 1
    return f"p{lo:04d}_{lo + 99:04d}"


def class_name_of(title: str) -> str:
    """PascalCase the title, preserving existing capitalization inside words.

    'Kids With the Greatest Number of Candies' -> KidsWithTheGreatestNumberOfCandies
    'GCD of Odd and Even Sums'                 -> GCDOfOddAndEvenSums
    """
    words = re.split(r"[^A-Za-z0-9]+", title)
    return "".join(word[:1].upper() + word[1:] for word in words if word)


def java_snippet(question: dict) -> str | None:
    for snippet in question.get("codeSnippets") or []:
        if snippet["langSlug"] == "java":
            return snippet["code"]
    return None


def stub_body(snippet: str) -> tuple[str, list[str]]:
    """Re-indent the members of the snippet's class to the repo's 2 spaces.

    Returns (class name, member lines). Design problems (LRUCache and friends)
    declare a constructor plus several methods under their own class name; those
    come through here too, since every member is stubbed the same way.
    """
    body = re.search(r"^class (\w+)[^\n]*\{\n(.*?)\n\}$", snippet, re.S | re.M)
    if not body:
        return "", []
    signatures = re.findall(r"^\s{4}(public [^\n{]*)\{", body.group(2), re.M)
    lines = []
    for signature in signatures:
        lines.append(f"  {signature.strip()} {{")
        lines.append('    throw new UnsupportedOperationException("Not implemented yet");')
        lines.append("  }")
    return body.group(1), lines


JAVA_UTIL = ["List", "Map", "Set", "Deque", "Queue", "TreeMap", "TreeSet"]
SUPPORT = ["TreeNode", "ListNode", "Node"]


def imports_for(signatures: list[str]) -> list[str]:
    """Guess the imports the signature needs: java.util for collections, leetcode.support for nodes.

    A guess is enough because compileJava is the arbiter -- an import that is
    wrong or missing shows up as a compile error within seconds.
    """
    joined = " ".join(signatures)
    java_util = [f"import java.util.{name};" for name in JAVA_UTIL if re.search(rf"\b{name}\b", joined)]
    support = [f"import leetcode.support.{name};" for name in SUPPORT if re.search(rf"\b{name}\b", joined)]
    return sorted(java_util) + sorted(support)


def to_text(content: str) -> str:
    """Flatten the statement HTML into readable text, keeping example blocks."""
    text = re.sub(r"<sup>(.*?)</sup>", r"^\1", content, flags=re.S)
    text = re.sub(r"</(p|div|li|pre|ul|ol|h[1-6])>", "\n", text)
    text = re.sub(r"<li>", "- ", text)
    text = re.sub(r"<br\s*/?>", "\n", text)
    text = re.sub(r"<[^>]+>", "", text)
    text = html.unescape(text).replace(" ", " ")
    return re.sub(r"\n{3,}", "\n\n", text).strip()


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("number", nargs="?", type=int, help="LeetCode problem number")
    parser.add_argument("--slug", help="resolve by slug instead of number")
    parser.add_argument("--statement", action="store_true", help="also print the statement as text")
    parser.add_argument("--refresh", action="store_true", help="re-fetch the problem index")
    args = parser.parse_args()

    if not args.number and not args.slug:
        parser.error("give a problem number or --slug")

    slug = args.slug
    if not slug:
        index = load_index(args.refresh)
        slug = index.get(args.number)
        if not slug:
            raise SystemExit(
                f"No problem {args.number} in the public index. Re-run with --refresh, "
                f"or pass --slug if you know it."
            )

    question = fetch_question(slug)
    number = int(question["questionFrontendId"])
    title = question["title"]
    package = package_of(number)
    class_name = class_name_of(title)
    root = repo_root()
    main_path = root / f"leetcode/src/main/java/leetcode/{package}/{class_name}.java"
    test_path = root / f"leetcode/src/test/java/leetcode/{package}/{class_name}Test.java"

    print(f"number      {number}")
    print(f"title       {title}")
    print(f"slug        {slug}")
    print(f"difficulty  {question['difficulty']}")
    print(f"paid only   {question['isPaidOnly']}")
    print(f"url         https://leetcode.com/problems/{slug}/description/")
    print(f"topics      {', '.join(tag['name'] for tag in question.get('topicTags') or []) or '-'}")
    print()
    print(f"package     leetcode.{package}")
    print(f"class       {class_name}" + ("  <-- starts with a digit, rename by hand"
                                         if class_name[:1].isdigit() else ""))
    print(f"main        {main_path.relative_to(root)}" + ("  (EXISTS)" if main_path.exists() else ""))
    print(f"test        {test_path.relative_to(root)}" + ("  (EXISTS)" if test_path.exists() else ""))
    print()

    snippet = java_snippet(question)
    if not snippet:
        print("No Java code snippet -- read the signature off the page in Chrome.")
        return 0

    snippet_class, lines = stub_body(snippet)
    print("--- LeetCode Java snippet ---")
    print(snippet)
    print()
    if not lines:
        print("Could not parse the snippet -- write the stub from it by hand.")
        return 0
    if snippet_class != "Solution":
        # A design problem: LeetCode names the class itself and instantiates it,
        # so the class name is fixed by the judge rather than derived from the title.
        print(f"Design problem -- the judge instantiates {snippet_class}, so keep that name.")
        class_name = snippet_class
        main_path = root / f"leetcode/src/main/java/leetcode/{package}/{class_name}.java"
        test_path = root / f"leetcode/src/test/java/leetcode/{package}/{class_name}Test.java"
        print(f"main        {main_path.relative_to(root)}" + ("  (EXISTS)" if main_path.exists() else ""))
        print(f"test        {test_path.relative_to(root)}" + ("  (EXISTS)" if test_path.exists() else ""))
        print()
    print("--- stub ---")
    print(f"package leetcode.{package};")
    print()
    imports = imports_for([line for line in lines if line.lstrip().startswith("public")])
    if imports:
        print("\n".join(imports))
        print()
    print(f'/** <a href="https://leetcode.com/problems/{slug}/">{number}. {title}</a> */')
    print(f"public class {class_name} {{")
    for line in lines:
        print(line)
    print("}")

    if args.statement:
        print()
        print("--- statement ---")
        content = question.get("content")
        if content:
            print(to_text(content))
        else:
            print("content is null (premium problem) -- read the page in Chrome instead.")
        if question.get("hints"):
            print("\n--- hints ---")
            for hint in question["hints"]:
                print(f"- {to_text(hint)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
