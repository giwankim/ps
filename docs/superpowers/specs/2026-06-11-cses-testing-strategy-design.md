# CSES Testing Strategy Design

Date: 2026-06-11
Module: `cses/`

> Partially superseded by
> [2026-06-11-cses-main-only-testing-design.md](2026-06-11-cses-main-only-testing-design.md):
> solutions now expose `main` only, `Solver`/`run` redirect
> `System.in`/`System.out`, fixed-input tests use junit-pioneer `@StdIo`, and
> the harness self-tests were dropped. The resources layout, bulk-runner
> discovery rules, and fail-fast guards below still apply.

## Goal

Give every CSES problem two complementary test layers — hand-written TDD cases
and bulk official test data — while eliminating the per-problem harness
boilerplate and the package-coupled resources layout that broke during the
category-package refactor.

## Decisions

| Topic | Decision |
| --- | --- |
| Test layout | One test class per problem: descriptive `@Test` TDD cases plus one `@TestFactory` bulk runner |
| Resources layout | Flat kebab-case slug folders at the resources root (e.g. `missing-number/1.in`) |
| Comparison | Harness strips trailing whitespace from both actual output and `.out` content; test code writes plain expected values with no `stripTrailing()` |
| Harness mechanism | `@TestFactory` + static helpers in a single `support.Cses` class |
| Data policy | Commit all official `.in`/`.out` pairs for every solved problem |

## Architecture

One support class replaces `support.ResourceUtils`:

```java
public final class Cses {
  private Cses() {}

  /** Entry-point shape every CSES solution already has. */
  @FunctionalInterface
  public interface Solver {
    void solve(BufferedReader r, PrintWriter pw) throws IOException;
  }

  /** Runs the solver on the given input; returns output with trailing whitespace stripped. */
  public static String run(Solver solver, String input) throws IOException { ... }

  /** One DynamicTest per N.in/N.out pair under src/test/resources/<slug>/. */
  public static Stream<DynamicTest> tests(String slug, Solver solver) { ... }
}
```

### `run(solver, input)` — TDD helper

- Wraps `input` in a `BufferedReader`, captures output via `PrintWriter` over
  `StringWriter`, flushes, returns `output.stripTrailing()`.
- `stripTrailing` rather than `trim`: a bug that emits leading whitespace must
  still fail.
- Test code reads `assertThat(run(MissingNumber::missingNumber, "5\n2 3 1 5")).isEqualTo("4")`.

### `tests(slug, solver)` — bulk runner

- Resolves `src/test/resources/<slug>/` from the test classpath.
- Discovers all `*.in` files; sorts numerically by file stem (1, 2, … 10 — not
  lexicographic). A non-numeric stem throws at discovery. No hardcoded file
  counts anywhere.
- Emits one `DynamicTest` per pair, named after the file stem. Each test runs
  the solver on the `.in` content and compares against the `.out` content;
  both sides are `stripTrailing`'d at load.
- Fail-fast guards (an empty `DynamicTest` stream passes silently, so a typo'd
  slug must throw, not pass):
  - missing or empty slug folder → throws with a message naming the slug;
  - `.in` without a matching `.out` → throws with a message naming the file.
- `IOException` propagates; `Solver` declares it and `DynamicTest` executables
  may throw, so tests need no try/catch.

### Per-problem test class shape

```java
class MissingNumberTest {
  @Test
  void officialSampleFindsMissingMiddleValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "5\n2 3 1 5")).isEqualTo("4");
  }
  // ... more descriptive TDD cases ...

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("missing-number", MissingNumber::missingNumber);
  }
}
```

`run` and `tests` are static imports from `support.Cses`.

## Resources layout

```
cses/src/test/resources/
├── missing-number/        1.in 1.out … 14.in 14.out
├── sum-of-two-values/     1.in 1.out … 27.in 27.out
└── weird-algorithm/       1.in 1.out … 14.in 14.out
```

Slug folders are named after the CSES problem title in kebab-case. Resources
never mirror Java packages, so package refactors cannot break resource paths
again.

## Per-problem workflow

1. Create the solution class: CSES-category package, `main` plus a
   `solve(BufferedReader, PrintWriter)` static method, javadoc with the
   problem spec.
2. Write the test class first: descriptive `@Test` methods for the statement
   sample(s) and reasoned edge cases (bounds, overflow, degenerate sizes) —
   red.
3. Implement until green; submit to CSES.
4. After AC, download the official data zip, unzip into
   `src/test/resources/<slug>/`, and add the one-line `@TestFactory` runner.

## Migration (completes the in-flight package refactor)

- `git mv` resource folders:
  - `cses/missing/number/` → `missing-number/`
  - `cses/sum/two/values/` → `sum-of-two-values/`
  - `cses/weird/algorithm/` → `weird-algorithm/`
- Rewrite the three existing test classes to the new shape: a few descriptive
  TDD cases each (statement sample plus interesting edges, e.g.
  MissingNumber's `long` overflow at max N) and the one-line bulk runner.
- Delete `support.ResourceUtils`.

## Harness self-tests

`support/CsesTest.java`, written before the harness implementation, drives
`Cses` against the checked-in fixture folders (`cses-test-fixture/` holds 2–3
valid pairs, including a `10.in` to pin numeric ordering;
`cses-test-fixture-orphan/` holds an `.in` with no `.out`):

- `run` strips trailing whitespace from solver output;
- `tests` discovers all pairs in numeric order;
- missing slug folder throws;
- `.in` without `.out` throws (orphan fixture folder);
- a mismatching pair produces an assertion failure (a deliberately wrong
  solver against the valid fixture).

## Error handling

| Failure | Behavior |
| --- | --- |
| Slug folder missing or empty | Throws at discovery, message names the slug |
| `.in` without `.out` | Throws at discovery, message names the file |
| Solver `IOException` | Propagates to the test failure |
| Output mismatch | Normal AssertJ string diff |

## Scope

Only `cses/` changes. `boj/` and `leetcode/` keep their existing patterns.
