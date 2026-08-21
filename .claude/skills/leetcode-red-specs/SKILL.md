---
name: leetcode-red-specs
description: Given a LeetCode problem number (or URL/title), create the solution stub that throws UnsupportedOperationException in the ps repo's leetcode/ module and write its failing TDD spec suite, without implementing anything. Use this whenever someone names a LeetCode problem and asks for tests, "TDD tests", "iterative test cases", "red specs", a stub, or tests "without touching the implementation" — including bare asks like "add tests for 1431", "set up 3612", or "stub leetcode 300 and spec it out". It resolves the number to its package, class name, and exact Java signature, reads the statement from the signed-in Chrome session, and verifies the suite fails on nothing but the stub exception.
---

# LeetCode red specs

The deliverable is two files and a verified red test run:

- `leetcode/src/main/java/leetcode/p{range}/{Class}.java` — a stub whose every
  method throws `UnsupportedOperationException("Not implemented yet")`
- `leetcode/src/test/java/leetcode/p{range}/{Class}Test.java` — a comprehensive
  spec suite that currently fails, and fails **only** with that exception

**Do not implement the solution.** Tests that go red on
`UnsupportedOperationException` are the finished product, not a problem to fix.
The suite is an executable spec: someone (often the user, as practice) makes it
green later, and the value is entirely in how precisely the specs pin the
problem down. If the request seems to want a working solution, ask before
writing one — "add tests without touching the implementation" means exactly what
it says.

## Step 1 — Resolve the problem

```bash
python3 .claude/skills/leetcode-red-specs/scripts/problem_meta.py 1431
```

Prints the title, slug, difficulty, whether it is premium, the target package
and paths (per `docs/leetcode-package-layout.md`), LeetCode's Java code snippet,
and a ready-to-paste stub built from it. Pass `--slug two-sum` when you were
given a URL or title instead of a number, and `--refresh` if a very recent
problem is missing from the cached index.

Take the signature from the snippet rather than inferring it from the examples:
the return type is often the thing an inference gets wrong (`List<Boolean>` vs
`boolean[]`), and a mismatch would only surface much later when the implementer
pastes their solution into the judge.

If the paths print `(EXISTS)`, stop and look before writing — see *Variations*.

## Step 2 — Read the statement in Chrome

LeetCode problem pages return 403 to `WebFetch`, and premium problems return a
null statement to the API. The user's Chrome is signed in, so read the page
there. The browser tools may be deferred; load them in **one** batched call:

```
ToolSearch: select:mcp__claude-in-chrome__tabs_context_mcp,mcp__claude-in-chrome__tabs_create_mcp,mcp__claude-in-chrome__navigate,mcp__claude-in-chrome__get_page_text,mcp__claude-in-chrome__tabs_close_mcp
```

Call `tabs_context_mcp` first (never reuse a tab id from an earlier session),
open a new tab, `navigate` to
`https://leetcode.com/problems/<slug>/description/`, then `get_page_text`. Close
the tab when you are done. Read out and keep:

- every rule, including the Note sentences — those exist to head off a specific
  wrong reading, and each one deserves its own spec
- the **verbatim constraints block**: the floor on `n` decides which "smallest
  input" spec is even legal, and the ceilings decide the stress steps
- every example with its Explanation, which is ground truth for expected values

If Chrome is unavailable, fall back in this order: the script's `--statement`
flag (free problems only), then the doocs mirror
`https://raw.githubusercontent.com/doocs/leetcode/main/solution/<hundreds>/<id>.<Title with %20>/README_EN.md`.
Mirrors occasionally disagree with the real statement, so when you use one, say
so in your report.

## Step 3 — Write the stub

Paste the script's stub. It already carries the package, the imports, the
javadoc problem link the repo requires on every solution class, and the throwing
body. Two things to check by hand:

- collection and node imports are inferred, so confirm them against the
  signature (`TreeNode`/`ListNode`/`Node` come from `leetcode.support`)
- no `@implNote` complexity javadoc yet — nothing is implemented, so there is no
  complexity to state. That javadoc gets added along with the real body.

## Step 4 — Pin the expected values before writing specs

Everything in the suite fails identically at this stage, so a wrong expected
value cannot be caught by running the tests — it silently misdirects the
implementer instead. Before asserting a value the problem statement does not
print outright, confirm it in the scratchpad (never in the repo) with two
independent implementations: a brute force and the intended algorithm, agreed on
every small case first. Python is fine and usually faster to write.

Do the same for the claims the Step comments make about wrong approaches ("a
greedy that commits to the earliest profitable trade answers 10") — compute the
wrong answer instead of guessing it.

## Step 5 — Write the specs

Read `references/test-suite-style.md` now. It describes the progression these
suites follow (floor → one rule per step → interactions → official examples →
constraint bounds with timeouts → hygiene), what a `// Step N:` comment has to
say to earn its place, the AssertJ idiom for each return type, and the lint
traps.

Write in batches of roughly five steps and run the filtered command from Step 6
after each batch. The point is not to see them fail — they all will — but to
catch a typo or a bad import while the batch is small enough to locate it in.

As a sense of scale, the exemplars run from about ten steps for an Easy problem
to twenty-plus for a Hard one. Fewer than eight usually means an interaction or a
constraint bound went unspecified — go back to the statement rather than padding
with variations of a step you already wrote.

## Step 6 — Verify the run is red for the right reason

```bash
./gradlew :leetcode:compileJava :leetcode:compileTestJava
./gradlew :leetcode:test --tests 'leetcode.p1401_1500.KidsWithTheGreatestNumberOfCandiesTest'
./gradlew :leetcode:spotlessJavaCheck
```

Always filter with `--tests`. A bare `:leetcode:test` OOMs and then hangs on the
module's other stubs, which is a wasted ten minutes and tells you nothing.

Success looks like `BUILD FAILED` with every failure reading
`java.lang.UnsupportedOperationException at {Class}Test.java:NN` and the count
line accounting for all of them (`23 tests completed, 23 failed`). Anything else
is a real defect:

- a failure at `:leetcode:compileTestJava` — a typo, a missing import, or a
  signature that disagrees with the stub
- an `AssertionFailedError` — impossible against a throwing stub, so it means
  something in the file is not calling `sut`
- a test that passes — same story, or an assertion that asserts nothing

When you pipe gradle through `tail`, the shell reports the pipeline's exit
status, so read the summary lines rather than trusting `$?`.

For `spotlessJavaCheck`, hand-apply the diff to your two files. The worktree
usually holds other work-in-progress leetcode files, and a module-wide
`spotlessApply` would sweep them up.

## Step 7 — Report

Say what the suite pins down, not just that it ran: the count, the exception all
of them fail with, and a short list of the traps the steps encode — the reading
each official example rules out, the complexity the stress steps enforce, the
state bug the hygiene steps catch. That list is what tells the user whether the
spec matches the problem they had in mind. Leave the files uncommitted.

## Variations

**The class already exists as a stub.** Leave it alone and write only the tests.
Confirm the existing signature is what the specs call.

**The class already exists, implemented.** Then this is not a red-spec job: the
suite should go green. Write it the same way, and report any test that fails —
that is a real bug found, and worth raising before anything else.

**The class exists with a second stubbed method** (`lengthOfLIS2` beside a
working `lengthOfLIS`, an alternative approach the user intends to write). Assert
both in every test, as `LongestIncreasingSubsequenceTest` does.

**A design problem** (constructor plus methods, `LRUCache` and friends). The
script keeps the judge's class name and stubs every member. Specs become
operation sequences rather than single calls — see the reference file.

**A premium or interactive problem.** No Java snippet comes back from the API;
read the signature off the page in Chrome along with the statement.
