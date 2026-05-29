package boj.boj11659;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 11659 구간 합 구하기 4 (Range Sum Query 4) -- the canonical prefix-sum problem.
 *
 * <p>Given {@code N} natural numbers and {@code M} queries, each query asks for the sum of the
 * elements from the {@code i}-th to the {@code j}-th, 1-indexed and inclusive. Building a
 * prefix-sum array {@code P} with {@code P[0] = 0} and {@code P[k]} the sum of the first {@code k}
 * elements answers every query in {@code O(1)} as {@code P[j] - P[i - 1]}; the {@code i = 1} case
 * relies on {@code P[0] = 0}.
 *
 * <p>Constraints: {@code 1 <= N, M <= 100,000}; each value is a natural number in {@code [1,
 * 1000]}; {@code 1 <= i <= j <= N}. The largest possible answer, {@code 100,000 * 1,000 =
 * 100,000,000}, still fits a signed 32-bit {@code int}.
 *
 * <p>Output is {@code M} lines: the query answers in order.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"5 3", "5 4 3 2 1", "1 3", "2 4", "5 5"})
  void officialSampleComputesEachRangeSumInQueryOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // [5,4,3,2,1]: 1..3 = 5+4+3 = 12; 2..4 = 4+3+2 = 9; 5..5 = 1.
    assertThat(out.capturedString().trim()).isEqualTo("12\n9\n1");
  }

  // --- Prefix-index boundaries: the off-by-one core of this problem. ---

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "1 1"})
  void firstSingleElementQueryReadsThePrefixZeroBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    // i = j = 1 must compute P[1] - P[0] = 5 - 0, never indexing P[-1].
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "5 5"})
  void lastSingleElementQueryReturnsTheFinalElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // i = j = N: P[5] - P[4] = 15 - 14 = 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "3 3"})
  void middleSingleElementQueryReturnsThatElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // P[3] - P[2] = 12 - 9 = 3.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "1 5"})
  void wholeArrayQuerySumsEveryElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 1..N: P[5] - P[0] = 15.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "1 4"})
  void queryFromFirstIndexStopsBeforeTheEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    // i = 1, j < N: P[4] - P[0] = 5+4+3+2 = 14.
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  @Test
  @StdIo({"5 1", "5 4 3 2 1", "2 5"})
  void queryToLastIndexStartsAfterTheBeginning(StdOut out) throws IOException {
    Main.main(new String[0]);
    // i > 1, j = N: P[5] - P[1] = 15 - 5 = 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Smallest possible input: N = M = 1, where every boundary collapses to one point. ---

  @Test
  @StdIo({"1 1", "7", "1 1"})
  void singleElementArrayAnswersItsOnlyQuery(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Value bounds: each number is a natural number in [1, 1000]. ---

  @Test
  @StdIo({"4 1", "1 1 1 1", "1 4"})
  void allMinimumValuesSumToTheElementCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"5 1", "1000 1000 1000 1000 1000", "1 5"})
  void allMaximumValuesSumAtTheTopOfTheRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 5 * 1000 = 5000.
    assertThat(out.capturedString().trim()).isEqualTo("5000");
  }

  // --- Multiple queries: order preserved, each answered independently of the others. ---

  @Test
  @StdIo({"5 5", "1 2 3 4 5", "5 5", "1 1", "1 5", "2 3", "3 5"})
  void multipleQueriesAreAnsweredInTheGivenOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // [1,2,3,4,5]: 5..5=5; 1..1=1; 1..5=15; 2..3=5; 3..5=12.
    assertThat(out.capturedString().trim()).isEqualTo("5\n1\n15\n5\n12");
  }

  @Test
  @StdIo({"3 3", "10 20 30", "1 3", "1 3", "1 3"})
  void repeatedIdenticalQueriesEachReturnTheSameSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Queries carry no state: 1..3 = 60 every time.
    assertThat(out.capturedString().trim()).isEqualTo("60\n60\n60");
  }

  @Test
  @StdIo({"6 3", "1 2 3 4 5 6", "1 2", "3 4", "5 6"})
  void adjacentDisjointQueriesPartitionTheArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    // [1..6]: 1..2=3; 3..4=7; 5..6=11.
    assertThat(out.capturedString().trim()).isEqualTo("3\n7\n11");
  }

  @Test
  @StdIo({"5 2", "1 2 3 4 5", "1 3", "2 4"})
  void overlappingQueriesAreComputedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ranges share indices 2 and 3 but are computed apart: 1..3 = 6; 2..4 = 9.
    assertThat(out.capturedString().trim()).isEqualTo("6\n9");
  }

  // --- Upper bounds (generated input, driven through the stdin/stdout helper like the project's
  // other larger-input tests). ---

  @Test
  void maximumArrayOfAllThousandsSumsToTenToTheEighthWithoutOverflow() throws IOException {
    int n = 100_000;
    int[] values = new int[n];
    Arrays.fill(values, 1000);
    // Largest possible answer: 100,000 * 1,000 = 100,000,000, still within a signed 32-bit int.
    assertThat(runMain(buildInput(values, new int[][] {{1, n}}))).isEqualTo("100000000");
  }

  @Test
  void everyQueryIsConsumedInOrderAcrossOneHundredThousandQueries() throws IOException {
    int n = 100_000;
    int[] values = new int[n];
    Arrays.fill(values, 1);
    // M at its maximum; query k asks for 1..k, and on an all-ones array that sum is exactly k.
    int[][] queries = new int[n][];
    StringBuilder expected = new StringBuilder();
    for (int k = 1; k <= n; k++) {
      queries[k - 1] = new int[] {1, k};
      if (k > 1) {
        expected.append('\n');
      }
      expected.append(k);
    }
    assertThat(runMain(buildInput(values, queries))).isEqualTo(expected.toString());
  }

  @Test
  void boundaryQueriesOnALargeNonConstantArrayMatchTheNaiveOracle() throws IOException {
    int n = 100_000;
    int[] values = new int[n];
    for (int idx = 0; idx < n; idx++) {
      values[idx] = (idx % 1000) + 1; // repeats the ramp 1..1000, always within [1, 1000].
    }
    int[][] queries = {{1, 1}, {n, n}, {1, n}, {12_345, 67_890}};
    StringBuilder expected = new StringBuilder();
    for (int q = 0; q < queries.length; q++) {
      if (q > 0) {
        expected.append('\n');
      }
      expected.append(naiveRangeSum(values, queries[q][0], queries[q][1]));
    }
    assertThat(runMain(buildInput(values, queries))).isEqualTo(expected.toString());
  }

  /** Builds BOJ 11659 input: {@code "N M"} header, the values line, then one line per query. */
  private static String buildInput(int[] values, int[][] queries) {
    StringBuilder sb = new StringBuilder();
    sb.append(values.length).append(' ').append(queries.length).append('\n');
    for (int idx = 0; idx < values.length; idx++) {
      if (idx > 0) {
        sb.append(' ');
      }
      sb.append(values[idx]);
    }
    sb.append('\n');
    for (int[] query : queries) {
      sb.append(query[0]).append(' ').append(query[1]).append('\n');
    }
    return sb.toString();
  }

  /**
   * Independent O(j - i) oracle for the prefix-sum implementation. {@code i} and {@code j} are
   * 1-indexed and inclusive.
   */
  private static long naiveRangeSum(int[] values, int i, int j) {
    long sum = 0;
    for (int k = i; k <= j; k++) {
      sum += values[k - 1];
    }
    return sum;
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
