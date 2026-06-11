# CSES Main-Only Solutions and Stdio Testing Design

Date: 2026-06-11
Module: `cses/`
Supersedes the `Solver` shape, `run` mechanism, per-problem workflow, and
harness self-test sections of
[2026-06-11-cses-testing-strategy-design.md](2026-06-11-cses-testing-strategy-design.md).
The resources layout, bulk-runner discovery rules, and fail-fast guards from
that spec stand unchanged.

## Goal

Each CSES solution keeps exactly one entry point — the `main` method that gets
submitted — and tests exercise it directly through `System.in`/`System.out`.
This removes the per-problem DI method (`missingNumber(BufferedReader,
PrintWriter)` and friends), which duplicated `main`'s logic with different I/O
plumbing and left the submitted `FastIO` path untested.

## Decisions

| Topic | Decision |
| --- | --- |
| Solution shape | `main(String[] args)` with `FastIO` only; DI solve methods deleted |
| Fixed-input tests | junit-pioneer `@StdIo` lines + `StdOut` parameter, the boj idiom |
| Generated-input tests | `Cses.run(X::main, input)` — annotation values must be compile-time constants |
| Bulk data tests | `Cses.tests(slug, X::main)`, unchanged discovery and guards |
| Output assertion | `out.capturedString().trim()` for `@StdIo` tests; `run` keeps returning `stripTrailing()`-ed output |
| Dependencies | None added — junit-pioneer 2.3.0 is already on every module's test classpath via `ps.test-conventions` |

## Solutions (`cses/src/main`)

`MissingNumber`, `WeirdAlgorithm`, and `SumTwoValues` each keep only their
`main` + `FastIO` body. The DI methods and their now-unused imports
(`BufferedReader`, `InputStreamReader`, `PrintWriter`, `StringTokenizer`) are
deleted. `cses.support.FastIO` is untouched.

## Harness (`support.Cses`)

`Solver` reshapes to match a main method, so `MissingNumber::main` is a valid
method reference:

```java
/** The entry-point shape every CSES solution submits: a main method. */
@FunctionalInterface
public interface Solver {
  void main(String[] args) throws IOException;
}
```

`run(Solver, String input)` becomes a stream-redirecting runner, the same shape
as boj's hand-rolled `runMain` helpers:

1. Swap `System.in` for a `ByteArrayInputStream` over the input's UTF-8 bytes.
2. Swap `System.out` for a `PrintStream` over a `ByteArrayOutputStream`
   (UTF-8).
3. Invoke `solver.main(new String[0])`.
4. Restore both original streams in `finally` — even when the solver throws.
5. Return the captured output `stripTrailing()`-ed: expectations are written
   without a trailing newline, and a solver that emits leading whitespace
   still fails.

`tests(slug, solver)` keeps its eager validation (missing/empty slug folder,
orphan `.in`, non-numeric stem all throw at discovery) and per-pair
`DynamicTest`s; only the `Solver` shape flowing into `run` changes.

## Per-problem test class shape

```java
class MissingNumberTest {
  @Test
  @StdIo({"5", "2 3 1 5"})
  void officialSampleFindsMissingMiddleValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Generated input cannot be an annotation constant -> redirect helper.
  @Test
  void maxSizeSumOverflowsIntWithoutLong() throws IOException {
    String numbers =
        IntStream.rangeClosed(2, 200_000).mapToObj(Integer::toString).collect(joining(" "));
    assertThat(run(MissingNumber::main, "200000\n" + numbers)).isEqualTo("1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("missing-number", MissingNumber::main);
  }
}
```

Multi-line inputs split at newlines into `@StdIo` array elements:
`"4 8\n2 7 5 1"` becomes `@StdIo({"4 8", "2 7 5 1"})`. Test method names and
their documented intent survive the migration unchanged.

## Behavior notes

- `FastIO extends PrintWriter`, so its try-with-resources `close()` closes
  whatever `System.out` points at. Both junit-pioneer and `run` redirect
  before the solver runs and restore afterward, so that close lands on a
  throwaway buffer, never the JVM's real stdout.
- `@StdIo` tests acquire junit-pioneer's global-stream resource locks and
  serialize against each other. Parallel test execution is not configured in
  this repo, so this costs nothing today.
- No tests for `run` itself: test helpers are covered by the problem tests
  that use them.

## Error handling

| Failure | Behavior |
| --- | --- |
| Solver throws inside `run` | Streams restored in `finally`; exception propagates to the test failure |
| Slug folder missing/empty, orphan `.in`, non-numeric stem | Throws at discovery, unchanged from the prior spec |
| Output mismatch | Normal AssertJ string diff |

## Verification

`./gradlew :cses:test` green and spotless clean. The change spans seven files:
three solutions, three test classes, and `support/Cses.java`.

## Scope

Only `cses/` changes. `boj/` and `leetcode/` keep their existing patterns.
