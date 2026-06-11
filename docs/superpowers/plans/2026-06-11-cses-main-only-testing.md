# CSES Main-Only Solutions and Stdio Testing Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Each CSES solution keeps only its submitted `main` method, and tests drive that `main` through `System.in`/`System.out` — junit-pioneer `@StdIo` for fixed inputs, a stream-redirecting `Cses.run` for generated input and bulk official data.

**Architecture:** Reshape `support.Cses.Solver` from `(BufferedReader, PrintWriter)` to `void main(String[])` so method references to real entry points satisfy it; `Cses.run` swaps the JVM's stdin/stdout around the call and restores them in `finally`. Fixed-input tests adopt the boj idiom (`@StdIo` + `StdOut` + `capturedString().trim()`). The DI solve methods then come out of the three solutions as dead code.

**Tech Stack:** Java 21+, JUnit 5, junit-pioneer 2.3.0 (already on the test classpath via `ps.test-conventions` — no build changes), AssertJ, Gradle.

**Spec:** `docs/superpowers/specs/2026-06-11-cses-main-only-testing-design.md`

**Working-tree caution:** The repo has unrelated uncommitted changes under `boj/` and `leetcode/`. Stage ONLY the explicit paths given in the commit steps — never `git add -A` or `git add .`. The working tree also already contains related in-flight changes that ride along in these commits: `cses/src/test/java/support/CsesTest.java` and the `cses-test-fixture*` resource folders are deleted (commit 1), and `MissingNumber.java` is already in its final main-only form with a nested `FastIO` (commit 2). The untracked `cses/src/main/java/cses/support/FastIO.java` is orphaned — nothing references it — and is deleted in Task 5, never committed. Both commits are staged by directory (`cses/src/test`, `cses/src/main`) to capture these.

**Solution-file layout note:** All three solutions carry their own nested `private static class FastIO` so each file is self-contained for single-file CSES submission (decided 2026-06-11, superseding the spec's original shared-`cses.support.FastIO` arrangement for `MissingNumber`). Leave that arrangement exactly as it is — this plan deletes the DI methods and nothing else in those files.

---

### Task 1: Reshape `support.Cses` into a stdio-redirecting harness

**Files:**
- Modify: `cses/src/test/java/support/Cses.java`
- (No test for the harness itself — repo rule: test helpers are covered by the problem tests that use them.)

- [ ] **Step 1: Replace the file contents**

Replace `cses/src/test/java/support/Cses.java` with exactly:

```java
package support;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;

/**
 * Test harness for CSES solutions: runs a solution's {@code main} on inline stdin ({@link #run}) or
 * on every official {@code N.in}/{@code N.out} data pair under {@code src/test/resources/<slug>/}
 * ({@link #tests}).
 */
public final class Cses {
  private Cses() {}

  /** The entry-point shape every CSES solution submits: a main method. */
  @FunctionalInterface
  public interface Solver {
    void main(String[] args) throws IOException;
  }

  /**
   * Runs the solution's main with the given stdin content and returns its stdout with trailing
   * whitespace stripped, so expectations are written without a trailing newline. Leading whitespace
   * is preserved: a solution that emits it should fail. The real streams are restored even when the
   * solution throws.
   */
  public static String run(Solver solver, String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      solver.main(new String[0]);
      return out.toString(StandardCharsets.UTF_8).stripTrailing();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }

  /**
   * Creates one test per {@code N.in}/{@code N.out} pair under {@code src/test/resources/<slug>/},
   * in numeric order, comparing trailing-whitespace-stripped output. Validation is eager: a missing
   * or empty folder, an input without a matching output, or a non-numeric input file name throws
   * from this call rather than producing a silently empty (and passing) stream.
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

Only the imports, class javadoc, `Solver`, and `run` change; `tests` and the private helpers are byte-for-byte what they were.

- [ ] **Step 2: Verify the reshape forces the test migration (red)**

Run: `./gradlew :cses:compileTestJava`
Expected: FAIL — compilation errors in `MissingNumberTest`, `WeirdAlgorithmTest`, and `SumTwoValuesTest` because `MissingNumber::missingNumber`, `WeirdAlgorithm::weirdAlgorithm`, and `SumTwoValues::sumTwoValues` no longer match `Cses.Solver`. No other files may appear in the errors.

Do not commit yet — the module compiles again after Task 4, and Tasks 1–4 land as one commit.

---

### Task 2: Migrate `MissingNumberTest`

**Files:**
- Modify: `cses/src/test/java/cses/introductory/problems/MissingNumberTest.java`

- [ ] **Step 1: Replace the file contents**

Replace the file with exactly:

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
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MissingNumberTest {
  @Test
  @StdIo({"5", "2 3 1 5"})
  void officialSampleFindsMissingMiddleValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"2", "2"})
  void smallestCaseMissingFirstValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "1"})
  void smallestCaseMissingLastValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
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

No verification step yet — `WeirdAlgorithmTest` and `SumTwoValuesTest` still reference the old `Solver` shape, so the test source set compiles only after Task 4.

---

### Task 3: Migrate `WeirdAlgorithmTest`

**Files:**
- Modify: `cses/src/test/java/cses/introductory/problems/WeirdAlgorithmTest.java`

- [ ] **Step 1: Replace the file contents**

Replace the file with exactly (note: `run` is no longer imported — every fixed input here is a single-line constant, so all four tests use `@StdIo`):

```java
package cses.introductory.problems;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class WeirdAlgorithmTest {
  @Test
  @StdIo("3")
  void officialSampleWalksThreeDownToOne(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 10 5 16 8 4 2 1");
  }

  @Test
  @StdIo("1")
  void oneIsAlreadyTheTerminalValue(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("16")
  void powerOfTwoOnlyHalves(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16 8 4 2 1");
  }

  @Test
  @StdIo("2147483648")
  void valueAboveIntRangeKeepsHalvingWithoutOverflow(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim())
        .isEqualTo("2147483648 1073741824 536870912 268435456 134217728 67108864 33554432"
            + " 16777216 8388608 4194304 2097152 1048576 524288 262144 131072 65536 32768 16384"
            + " 8192 4096 2048 1024 512 256 128 64 32 16 8 4 2 1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("weird-algorithm", WeirdAlgorithm::main);
  }
}
```

`WeirdAlgorithm.main` prints a trailing `" "` before its final newline; `capturedString().trim()` strips both, same as the old harness's `stripTrailing` did.

---

### Task 4: Migrate `SumTwoValuesTest`, go green, commit

**Files:**
- Modify: `cses/src/test/java/cses/sorting/searching/SumTwoValuesTest.java`

- [ ] **Step 1: Replace the file contents**

Replace the file with exactly (again no `run` import — all fixed inputs are constants):

```java
package cses.sorting.searching;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class SumTwoValuesTest {
  @Test
  @StdIo({"4 8", "2 7 5 1"})
  void officialSampleFindsAPairSummingToTarget(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 2");
  }

  @Test
  @StdIo({"4 6", "3 3 1 9"})
  void duplicateValuesCanFormThePair(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2");
  }

  @Test
  @StdIo({"3 4", "2 5 8"})
  void valueCannotPairWithItself(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("IMPOSSIBLE");
  }

  @Test
  @StdIo({"1 2", "1"})
  void singleValueIsImpossible(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("IMPOSSIBLE");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("sum-of-two-values", SumTwoValues::main);
  }
}
```

- [ ] **Step 2: Run the cses tests (green)**

Run: `./gradlew :cses:test`
Expected: BUILD SUCCESSFUL, all tests pass. The DI methods still exist in the solutions at this point — they are dead code now, deleted in Task 5.

- [ ] **Step 3: Commit the test side**

Stage the whole test tree — this intentionally includes the in-flight deletions of `support/CsesTest.java` and the `cses-test-fixture*` resource folders, which the new harness shape requires:

```bash
git add cses/src/test
git commit -m "test(cses): drive solutions through main() with @StdIo and a stdio-redirecting harness"
```

Run `git status` afterward and confirm nothing under `cses/src/test` remains modified; `boj/` and `leetcode/` changes must remain untouched.

---

### Task 5: Delete the DI solve methods, go green, commit

**Files:**
- Verify: `cses/src/main/java/cses/introductory/problems/MissingNumber.java` (already final in the working tree)
- Modify: `cses/src/main/java/cses/introductory/problems/WeirdAlgorithm.java`
- Modify: `cses/src/main/java/cses/sorting/searching/SumTwoValues.java`
- Delete: `cses/src/main/java/cses/support/FastIO.java` (untracked, orphaned)

- [ ] **Step 1: Verify `MissingNumber` is main-only with a nested `FastIO`**

The working tree already holds the final form (a prior manual edit). Confirm the file is exactly the following; replace it if it differs:

```java
package cses.introductory.problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class MissingNumber {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      long sum = (long) n * (n + 1) / 2;
      long runningSum = 0L;
      for (int i = 0; i + 1 < n; i++) {
        runningSum += io.nextInt();
      }
      io.println(sum - runningSum);
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
      r = new BufferedReader(new InputStreamReader(in));
    }

    /** Next whitespace-delimited token, pulling fresh lines across boundaries as needed. */
    public String next() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        st = new StringTokenizer(r.readLine());
      }
      return st.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
      return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
      return Double.parseDouble(next());
    }
  }
}
```

Then delete the orphaned shared-FastIO file (untracked, so plain `rm` suffices) and its now-empty package folder:

```bash
rm cses/src/main/java/cses/support/FastIO.java
rmdir cses/src/main/java/cses/support
```

- [ ] **Step 2: Reduce `WeirdAlgorithm` to main-only**

Delete only the `weirdAlgorithm` method. Every import stays — the nested `FastIO` still uses them all. The file becomes exactly:

```java
package cses.introductory.problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class WeirdAlgorithm {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      long n = io.nextLong();
      while (n != 1L) {
        io.print(n + " ");
        if (n % 2L == 0L) {
          n /= 2L;
        } else {
          n *= 3L;
          n += 1L;
        }
      }
      io.println(1L + " ");
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
      r = new BufferedReader(new InputStreamReader(in));
    }

    /** Next whitespace-delimited token, pulling fresh lines across boundaries as needed. */
    public String next() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        st = new StringTokenizer(r.readLine());
      }
      return st.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
      return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
      return Double.parseDouble(next());
    }
  }
}
```

- [ ] **Step 3: Reduce `SumTwoValues` to main-only**

Delete only the `sumTwoValues` method. Every import stays (`Arrays`/`Comparator` used by `main`, the rest by the nested `FastIO`). The file becomes exactly:

```java
package cses.sorting.searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class SumTwoValues {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int x = io.nextInt();
      int[][] nums = new int[n][2];
      for (int i = 0; i < n; i++) {
        nums[i][0] = io.nextInt();
        nums[i][1] = i + 1;
      }

      Arrays.sort(nums, Comparator.comparingInt(a -> a[0]));

      int lo = 0;
      int hi = n - 1;
      while (lo < hi) {
        long sum = (long) nums[lo][0] + nums[hi][0];
        if (sum == x) {
          io.println(nums[lo][1] + " " + nums[hi][1]);
          return;
        } else if (sum < x) {
          lo += 1;
        } else {
          hi -= 1;
        }
      }
      io.println("IMPOSSIBLE");
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
      r = new BufferedReader(new InputStreamReader(in));
    }

    /** Next whitespace-delimited token, pulling fresh lines across boundaries as needed. */
    public String next() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        st = new StringTokenizer(r.readLine());
      }
      return st.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
      return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
      return Double.parseDouble(next());
    }
  }
}
```

- [ ] **Step 4: Run tests and the formatter check (green)**

Run: `./gradlew :cses:test :cses:spotlessCheck`
Expected: BUILD SUCCESSFUL. If `spotlessCheck` fails, run `./gradlew :cses:spotlessApply` and re-run the check.

- [ ] **Step 5: Commit the main side**

Stage the whole main tree (the orphaned `cses/support/FastIO.java` was already removed in Step 1 and was never tracked, so it cannot appear):

```bash
git add cses/src/main
git commit -m "refactor(cses): drop DI solve methods, keep main() as the only entry point"
```

Run `git status` afterward and confirm nothing under `cses/` remains modified or untracked; `boj/` and `leetcode/` changes must remain untouched.

---

## Done criteria

- `./gradlew :cses:test` green: 11 fixed-input tests via `@StdIo`, 1 generated-input test via `run`, 3 `@TestFactory` bulk runners over official data.
- No `solve(BufferedReader, PrintWriter)`-shaped methods remain under `cses/src/main` (`grep -rn "PrintWriter pw" cses/src/main` matches nothing).
- Two commits on `main`, each compiling and passing on its own; `boj/` and `leetcode/` working-tree changes untouched.
