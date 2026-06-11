# CSES Testing Harness Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the per-problem CSES test boilerplate with a `support.Cses` harness (inline-input `run` for TDD cases, auto-discovering `tests` for official data files), move test data to flat slug folders, and migrate the three existing problems.

**Architecture:** One static support class `support.Cses` on the cses test classpath exposes a `Solver` functional interface matching the `(BufferedReader, PrintWriter)` method every solution already has, a `run(solver, input)` helper returning trailing-whitespace-stripped output, and a `tests(slug, solver)` factory emitting one `DynamicTest` per `N.in`/`N.out` pair found under `src/test/resources/<slug>/`. Discovery is eager and fail-fast (missing/empty folder, orphan `.in`, non-numeric stem all throw), so a typo'd slug can never pass silently. Each problem's test class holds descriptive `@Test` TDD cases plus a one-line `@TestFactory`.

**Tech Stack:** Java (toolchain 26), JUnit 6 Jupiter (`@Test`, `@TestFactory`, `DynamicTest`), AssertJ, Gradle (`./gradlew :cses:test`), Spotless with palantir-java-format (GOOGLE style).

**Spec:** `docs/superpowers/specs/2026-06-11-cses-testing-strategy-design.md`

**Conventions used throughout:**
- All commands run from the repo root `/Users/gwk/Development/ps`.
- Before every commit, run `./gradlew :cses:spotlessApply` and re-run the tests; the formatter owns import order and line wrapping, so never hand-tune those.
- Commit messages follow the repo's Angular style (`type(scope): summary`, lowercase, imperative).
- The working tree contains unrelated uncommitted changes under `boj/` and `leetcode/` — never `git add` those paths. Always stage with the explicit pathspecs given in each task.

**Domain background (why the harness strips trailing whitespace):** the official CSES `.out` files themselves end with a trailing space before the newline (e.g. `weird-algorithm` outputs `"… 4 2 1 \n"`), and the existing solutions print that trailing space to byte-match them. The harness strips trailing whitespace from BOTH the actual output and the `.out` file content at load, so official data keeps passing while hand-written TDD expectations stay clean strings like `"4"` with no `\n` and no `stripTrailing()` in test code.

---

### Task 1: Commit the in-flight package refactor (green baseline)

The working tree already contains a half-staged refactor: solution classes moved from per-problem packages (`cses.missing.number`) to CSES category packages (`cses.introductory.problems`, `cses.sorting.searching`), with tests renamed to match. Tests pass in this state (resources are still at the old paths, which the old-style tests still reference). Commit it as-is so later tasks start from a clean tree.

**Files:**
- No edits — staging and committing existing changes under `cses/` only.

- [ ] **Step 1: Verify the baseline is green**

Run: `./gradlew :cses:test --rerun-tasks`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2: Stage all cses changes and inspect**

```bash
git add cses
git status --short -- cses boj leetcode
```

Expected: staged (`A`/`D`/`R` in the first column) entries only under `cses/` — new mains in `cses/introductory/` and `cses/sorting/`, deletions of the old `cses/missing/`, `cses/sum/`, `cses/weird/` mains, and the three test renames. Entries under `boj/` and `leetcode/` must still show unstaged (` M`) or untracked (`??`) — if any are staged, stop and unstage them with `git restore --staged boj leetcode`.

- [ ] **Step 3: Commit**

```bash
git commit -m "refactor(cses): regroup solutions into CSES category packages"
```

---

### Task 2: Harness self-test — fixtures and failing test (RED)

**Files:**
- Create: `cses/src/test/resources/cses-test-fixture/{1.in,1.out,2.in,2.out,10.in,10.out}`
- Create: `cses/src/test/resources/cses-test-fixture-orphan/1.in`
- Create: `cses/src/test/resources/cses-test-fixture-empty/.gitkeep`
- Create: `cses/src/test/resources/cses-test-fixture-nonnumeric/sample.in`
- Test: `cses/src/test/java/support/CsesTest.java`

- [ ] **Step 1: Create the fixture folders**

The valid fixture holds three pairs whose `.out` equals the `.in` (so an echo solver passes), including a `10` to pin numeric-vs-lexicographic ordering. The other folders each violate one discovery guard.

```bash
mkdir -p cses/src/test/resources/cses-test-fixture \
         cses/src/test/resources/cses-test-fixture-orphan \
         cses/src/test/resources/cses-test-fixture-empty \
         cses/src/test/resources/cses-test-fixture-nonnumeric
printf 'alpha\n' > cses/src/test/resources/cses-test-fixture/1.in
printf 'alpha\n' > cses/src/test/resources/cses-test-fixture/1.out
printf 'bravo\n' > cses/src/test/resources/cses-test-fixture/2.in
printf 'bravo\n' > cses/src/test/resources/cses-test-fixture/2.out
printf 'kilo\n'  > cses/src/test/resources/cses-test-fixture/10.in
printf 'kilo\n'  > cses/src/test/resources/cses-test-fixture/10.out
printf 'orphan\n' > cses/src/test/resources/cses-test-fixture-orphan/1.in
touch cses/src/test/resources/cses-test-fixture-empty/.gitkeep
printf 'x\n' > cses/src/test/resources/cses-test-fixture-nonnumeric/sample.in
```

- [ ] **Step 2: Write the failing harness self-test**

Create `cses/src/test/java/support/CsesTest.java`:

```java
package support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

class CsesTest {
  /** Echoes the first input line, the simplest solver that satisfies the valid fixture pairs. */
  private static final Cses.Solver ECHO = (r, pw) -> pw.println(r.readLine());

  @Test
  void runFeedsInputToTheSolver() throws IOException {
    assertThat(run(ECHO, "hello")).isEqualTo("hello");
  }

  @Test
  void runStripsTrailingWhitespaceFromSolverOutput() throws IOException {
    assertThat(run((r, pw) -> pw.println("answer  "), "")).isEqualTo("answer");
  }

  @Test
  void runPreservesLeadingWhitespace() throws IOException {
    assertThat(run((r, pw) -> pw.println("  answer"), "")).isEqualTo("  answer");
  }

  @Test
  void testsDiscoversAllPairsInNumericOrder() {
    List<String> names =
        tests("cses-test-fixture", ECHO).map(DynamicTest::getDisplayName).toList();
    assertThat(names).containsExactly("1", "2", "10");
  }

  @Test
  void testsExecuteTheSolverAgainstOutFiles() throws Throwable {
    for (DynamicTest test : tests("cses-test-fixture", ECHO).toList()) {
      test.getExecutable().execute();
    }
  }

  @Test
  void mismatchedOutputFailsTheDynamicTest() {
    DynamicTest first =
        tests("cses-test-fixture", (r, pw) -> pw.println("wrong")).findFirst().orElseThrow();
    assertThatThrownBy(first.getExecutable()::execute).isInstanceOf(AssertionError.class);
  }

  @Test
  void missingSlugFolderThrows() {
    assertThatThrownBy(() -> tests("no-such-slug", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("no-such-slug");
  }

  @Test
  void slugFolderWithoutInputFilesThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-empty", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("cses-test-fixture-empty");
  }

  @Test
  void inputWithoutMatchingOutputThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-orphan", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("1.out");
  }

  @Test
  void nonNumericInputFileNameThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-nonnumeric", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("sample.in");
  }
}
```

Note the design constraints this test pins down, which the implementation in Task 3 must honor:
- `tests()` must validate **eagerly** (in the call itself, not on stream consumption) — the `assertThatThrownBy(() -> tests(...))` tests depend on it.
- Dynamic test display names are the bare file stems (`"1"`, not `"1.in"`).
- Ordering is numeric: `1, 2, 10`, never the lexicographic `1, 10, 2`.

- [ ] **Step 3: Run the test to verify it fails to compile**

Run: `./gradlew :cses:test --tests 'support.CsesTest'`
Expected: `BUILD FAILED` with a compilation error on `CsesTest.java` — `cannot find symbol` for `Cses` (the class does not exist yet). Do NOT commit; Task 3 turns this green and commits both files together.

---

### Task 3: Harness implementation (GREEN) and commit

**Files:**
- Create: `cses/src/test/java/support/Cses.java`
- Test: `cses/src/test/java/support/CsesTest.java` (written in Task 2)

- [ ] **Step 1: Implement the harness**

Create `cses/src/test/java/support/Cses.java`:

```java
package support;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;

/**
 * Test harness for CSES solutions: runs a solver on inline input ({@link #run}) or on every
 * official {@code N.in}/{@code N.out} data pair under {@code src/test/resources/<slug>/}
 * ({@link #tests}).
 */
public final class Cses {
  private Cses() {}

  /** The entry-point shape every CSES solution exposes next to its {@code main} method. */
  @FunctionalInterface
  public interface Solver {
    void solve(BufferedReader r, PrintWriter pw) throws IOException;
  }

  /**
   * Runs the solver on the given input and returns its output with trailing whitespace stripped,
   * so expectations are written without a trailing newline. Leading whitespace is preserved: a
   * solver that emits it should fail.
   */
  public static String run(Solver solver, String input) throws IOException {
    StringWriter out = new StringWriter();
    try (BufferedReader r = new BufferedReader(new StringReader(input));
        PrintWriter pw = new PrintWriter(out)) {
      solver.solve(r, pw);
    }
    return out.toString().stripTrailing();
  }

  /**
   * Creates one test per {@code N.in}/{@code N.out} pair under {@code src/test/resources/<slug>/},
   * in numeric order, comparing trailing-whitespace-stripped output. Validation is eager: a
   * missing or empty folder, an input without a matching output, or a non-numeric input file name
   * throws from this call rather than producing a silently empty (and passing) stream.
   */
  public static Stream<DynamicTest> tests(String slug, Solver solver) {
    List<Path> inputs = discoverInputs(slug);
    return inputs.stream()
        .map(in -> DynamicTest.dynamicTest(stem(in), () -> {
          String expected = Files.readString(expectedOutputFor(in)).stripTrailing();
          assertThat(run(solver, Files.readString(in))).isEqualTo(expected);
        }));
  }

  private static List<Path> discoverInputs(String slug) {
    URL url = Cses.class.getClassLoader().getResource(slug);
    if (url == null) {
      throw new IllegalStateException("no test data folder on the test classpath for: " + slug);
    }
    List<Path> inputs;
    try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
      inputs = files
          .filter(p -> p.getFileName().toString().endsWith(".in"))
          .map(in -> Map.entry(number(in), in))
          .sorted(Map.Entry.comparingByKey())
          .map(Map.Entry::getValue)
          .toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("test data folder is not on the file system: " + slug, e);
    }
    if (inputs.isEmpty()) {
      throw new IllegalStateException("no .in files in test data folder: " + slug);
    }
    for (Path in : inputs) {
      if (!Files.isRegularFile(expectedOutputFor(in))) {
        throw new IllegalStateException("no matching output file: " + expectedOutputFor(in));
      }
    }
    return inputs;
  }

  private static int number(Path in) {
    try {
      return Integer.parseInt(stem(in));
    } catch (NumberFormatException e) {
      throw new IllegalStateException("input file name is not a number: " + in.getFileName(), e);
    }
  }

  private static String stem(Path in) {
    String name = in.getFileName().toString();
    return name.substring(0, name.length() - ".in".length());
  }

  private static Path expectedOutputFor(Path in) {
    return in.resolveSibling(stem(in) + ".out");
  }
}
```

- [ ] **Step 2: Run the self-test to verify it passes**

Run: `./gradlew :cses:test --tests 'support.CsesTest'`
Expected: `BUILD SUCCESSFUL` (10 tests pass).

- [ ] **Step 3: Format and run the whole module**

```bash
./gradlew :cses:spotlessApply :cses:test
```

Expected: `BUILD SUCCESSFUL`. If spotless changed either file, re-read the diff (`git diff -- cses/src/test/java/support`) to confirm only formatting moved.

- [ ] **Step 4: Commit harness, self-test, and fixtures**

```bash
git add cses/src/test/java/support/Cses.java \
        cses/src/test/java/support/CsesTest.java \
        cses/src/test/resources/cses-test-fixture \
        cses/src/test/resources/cses-test-fixture-orphan \
        cses/src/test/resources/cses-test-fixture-empty \
        cses/src/test/resources/cses-test-fixture-nonnumeric
git commit -m "test(cses): add Cses harness for TDD cases and official data files"
```

---

### Task 4: Migrate Missing Number

**Files:**
- Move: `cses/src/test/resources/cses/missing/number/` → `cses/src/test/resources/missing-number/` (14 pairs)
- Rewrite: `cses/src/test/java/cses/introductory/problems/MissingNumberTest.java`

Problem recap (CSES 1083): given `n` and `n-1` distinct numbers from `1..n`, print the missing one. The solution computes `n(n+1)/2` minus the running sum, in `long` because the sum overflows `int` at the maximum `n = 2·10^5` (sum `≈ 2·10^10`).

- [ ] **Step 1: Move the resource folder**

```bash
git mv cses/src/test/resources/cses/missing/number cses/src/test/resources/missing-number
```

- [ ] **Step 2: Rewrite the test class**

Replace the entire content of `cses/src/test/java/cses/introductory/problems/MissingNumberTest.java`:

```java
package cses.introductory.problems;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class MissingNumberTest {
  @Test
  void officialSampleFindsMissingMiddleValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "5\n2 3 1 5")).isEqualTo("4");
  }

  @Test
  void smallestCaseMissingFirstValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "2\n2")).isEqualTo("1");
  }

  @Test
  void smallestCaseMissingLastValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "2\n1")).isEqualTo("2");
  }

  @Test
  void maxSizeSumOverflowsIntWithoutLong() throws IOException {
    String numbers =
        IntStream.rangeClosed(2, 200_000).mapToObj(Integer::toString).collect(joining(" "));
    assertThat(run(MissingNumber::missingNumber, "200000\n" + numbers)).isEqualTo("1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("missing-number", MissingNumber::missingNumber);
  }
}
```

- [ ] **Step 3: Run the test class**

Run: `./gradlew :cses:test --tests 'cses.introductory.problems.MissingNumberTest'`
Expected: `BUILD SUCCESSFUL` (4 TDD tests + 14 dynamic data tests).

- [ ] **Step 4: Format, run the module, commit**

```bash
./gradlew :cses:spotlessApply :cses:test
git add cses/src/test/java/cses/introductory/problems/MissingNumberTest.java
git commit -m "test(cses): migrate Missing Number to TDD cases plus harness data tests"
```

(The resource moves were already staged by `git mv`.) Expected: `BUILD SUCCESSFUL`, then a commit containing the rewritten test and 28 renamed resource files.

---

### Task 5: Migrate Weird Algorithm

**Files:**
- Move: `cses/src/test/resources/cses/weird/algorithm/` → `cses/src/test/resources/weird-algorithm/` (14 pairs)
- Rewrite: `cses/src/test/java/cses/introductory/problems/WeirdAlgorithmTest.java`

Problem recap (CSES 1068): simulate the Collatz sequence from `n` (halve if even, `3n+1` if odd) and print every value down to `1`, space-separated. The solution tracks `n` as `long` because intermediate values exceed `int` range even for in-constraint inputs. It prints a trailing space after every value including the final `1`; `run`'s `stripTrailing` makes expectations clean.

- [ ] **Step 1: Move the resource folder**

```bash
git mv cses/src/test/resources/cses/weird/algorithm cses/src/test/resources/weird-algorithm
```

- [ ] **Step 2: Rewrite the test class**

Replace the entire content of `cses/src/test/java/cses/introductory/problems/WeirdAlgorithmTest.java`:

```java
package cses.introductory.problems;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class WeirdAlgorithmTest {
  @Test
  void officialSampleWalksThreeDownToOne() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "3")).isEqualTo("3 10 5 16 8 4 2 1");
  }

  @Test
  void oneIsAlreadyTheTerminalValue() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "1")).isEqualTo("1");
  }

  @Test
  void powerOfTwoOnlyHalves() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "16")).isEqualTo("16 8 4 2 1");
  }

  @Test
  void valueAboveIntRangeKeepsHalvingWithoutOverflow() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "2147483648"))
        .isEqualTo("2147483648 1073741824 536870912 268435456 134217728 67108864 33554432"
            + " 16777216 8388608 4194304 2097152 1048576 524288 262144 131072 65536 32768 16384"
            + " 8192 4096 2048 1024 512 256 128 64 32 16 8 4 2 1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("weird-algorithm", WeirdAlgorithm::weirdAlgorithm);
  }
}
```

- [ ] **Step 3: Run the test class**

Run: `./gradlew :cses:test --tests 'cses.introductory.problems.WeirdAlgorithmTest'`
Expected: `BUILD SUCCESSFUL` (4 TDD tests + 14 dynamic data tests).

- [ ] **Step 4: Format, run the module, commit**

```bash
./gradlew :cses:spotlessApply :cses:test
git add cses/src/test/java/cses/introductory/problems/WeirdAlgorithmTest.java
git commit -m "test(cses): migrate Weird Algorithm to TDD cases plus harness data tests"
```

Expected: `BUILD SUCCESSFUL`, then a commit containing the rewritten test and 28 renamed resource files.

---

### Task 6: Migrate Sum of Two Values

**Files:**
- Move: `cses/src/test/resources/cses/sum/two/values/` → `cses/src/test/resources/sum-of-two-values/` (27 pairs)
- Rewrite: `cses/src/test/java/cses/sorting/searching/SumTwoValuesTest.java`

Problem recap (CSES 1640): given `n`, target `x`, and `n` values, print the 1-based positions of two values summing to `x`, or `IMPOSSIBLE`. The solution pairs each value with its original index, sorts by value (stable, so equal values keep input order), and two-pointers inward. It therefore prints the lower-valued element's index first: for the statement sample `4 8 / 2 7 5 1` it prints `4 2` (value 1 at position 4, value 7 at position 2) — a valid answer that the official `.out` data matches; the TDD cases pin this implementation's deterministic choice.

- [ ] **Step 1: Move the resource folder**

```bash
git mv cses/src/test/resources/cses/sum/two/values cses/src/test/resources/sum-of-two-values
```

- [ ] **Step 2: Rewrite the test class**

Replace the entire content of `cses/src/test/java/cses/sorting/searching/SumTwoValuesTest.java`:

```java
package cses.sorting.searching;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class SumTwoValuesTest {
  @Test
  void officialSampleFindsAPairSummingToTarget() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "4 8\n2 7 5 1")).isEqualTo("4 2");
  }

  @Test
  void duplicateValuesCanFormThePair() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "4 6\n3 3 1 9")).isEqualTo("1 2");
  }

  @Test
  void valueCannotPairWithItself() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "3 4\n2 5 8")).isEqualTo("IMPOSSIBLE");
  }

  @Test
  void singleValueIsImpossible() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "1 2\n1")).isEqualTo("IMPOSSIBLE");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("sum-of-two-values", SumTwoValues::sumTwoValues);
  }
}
```

- [ ] **Step 3: Run the test class**

Run: `./gradlew :cses:test --tests 'cses.sorting.searching.SumTwoValuesTest'`
Expected: `BUILD SUCCESSFUL` (4 TDD tests + 27 dynamic data tests).

- [ ] **Step 4: Format, run the module, commit**

```bash
./gradlew :cses:spotlessApply :cses:test
git add cses/src/test/java/cses/sorting/searching/SumTwoValuesTest.java
git commit -m "test(cses): migrate Sum of Two Values to TDD cases plus harness data tests"
```

Expected: `BUILD SUCCESSFUL`, then a commit containing the rewritten test and 54 renamed resource files.

---

### Task 7: Delete ResourceUtils and the stale resource tree

**Files:**
- Delete: `cses/src/test/java/support/ResourceUtils.java`
- Delete: leftover empty directories under `cses/src/test/resources/cses/`

- [ ] **Step 1: Verify nothing references ResourceUtils anymore**

Run: `grep -rl ResourceUtils cses/src`
Expected: only `cses/src/test/java/support/ResourceUtils.java` itself. If any test still imports it, that test was not migrated — go back to the corresponding task.

- [ ] **Step 2: Delete the class and the empty resource directories**

```bash
git rm cses/src/test/java/support/ResourceUtils.java
find cses/src/test/resources/cses -type f
find cses/src/test/resources/cses -depth -type d -empty -delete
```

Expected: the first `find` prints nothing (all data files were moved by Tasks 4-6 — if it prints files, STOP, a resource move was missed); the second removes the now-empty `cses/` directory tree, which git never tracked.

- [ ] **Step 3: Run the full module**

Run: `./gradlew :cses:test --rerun-tasks`
Expected: `BUILD SUCCESSFUL` — 10 harness self-tests, 12 TDD tests, and 55 dynamic data tests.

- [ ] **Step 4: Commit**

```bash
git commit -m "chore(cses): drop ResourceUtils and the package-mirrored resource tree"
```

---

## Verification checklist (after all tasks)

- `./gradlew :cses:test --rerun-tasks` → `BUILD SUCCESSFUL`
- `ls cses/src/test/resources/` → `cses-test-fixture`, `cses-test-fixture-empty`, `cses-test-fixture-nonnumeric`, `cses-test-fixture-orphan`, `missing-number`, `sum-of-two-values`, `weird-algorithm` (no `cses/` directory)
- `git status --short -- cses` → clean
- `boj/` and `leetcode/` working-tree changes untouched and uncommitted
