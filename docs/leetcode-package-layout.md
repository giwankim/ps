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

## Caveats

**The problem-number script is stale.** Its class scan
(`SRC.glob("*.java")` in `fetch_problem_numbers.py`) only looks directly
inside `leetcode/src/main/java/leetcode/`, not the numeric packages this doc
describes. Since the migration, running it finds zero classes and silently
overwrites `problem_numbers.tsv` with an empty mapping instead of failing
loudly. The glob needs to become recursive (`rglob`) before the script can be
trusted again — until then, update the TSV by hand.

**The test suite hangs.** `./gradlew :leetcode:test` currently OOMs and then
hangs on work-in-progress solutions that contain `UnsupportedOperationException`
stubs. Prefer `./gradlew :leetcode:compileJava :leetcode:compileTestJava` plus
a targeted `--tests` filter.

Rationale and rejected alternatives:
[docs/superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md](superpowers/specs/2026-08-20-leetcode-package-taxonomy-design.md)
