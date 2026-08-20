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
