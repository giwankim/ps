package boj.boj2143;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2143 두 배열의 합 (Sum of Two Arrays).
 *
 * <p>Given integer arrays {@code A[1..n]} and {@code B[1..m]} and a target {@code T}, count the
 * pairs of <em>contiguous, non-empty</em> subarrays -- one drawn from A, one from B -- whose sums
 * add up to {@code T}. Subarrays at different index ranges are distinct even when their values (and
 * therefore sums) coincide, so the answer counts index pairs, not value pairs.
 *
 * <p>Input is five lines: {@code T}; {@code n}; the {@code n} values of A; {@code m}; the {@code m}
 * values of B. Output is a single line: the count, or {@code 0} when no pair qualifies.
 *
 * <p>Constraints: {@code -1,000,000,000 <= T <= 1,000,000,000}; {@code 1 <= n, m <= 1000}; every
 * element has absolute value {@code <= 1,000,000}.
 *
 * <p><b>Overflow note.</b> A single subarray sum is bounded by {@code 1000 * 1,000,000 = 10^9}, and
 * the intermediate {@code T - sum} stays within {@code [-2*10^9, 2*10^9]} -- both fit a signed
 * 32-bit {@code int}. The <em>answer</em> does not: with {@code n = m = 1000} there are
 * {@code 1000*1001/2 = 500,500} subarrays per side, so an all-equal-sum input (e.g. all zeros with
 * {@code T = 0}) yields {@code 500,500 * 500,500 = 250,500,250,000} pairs, far beyond {@code int}.
 * The accumulator must be a {@code long}; {@link #allZerosAtMaxSizeOverflowsIntAccumulator()} pins
 * this down.
 */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // A={1,3,1,2}, B={1,3,2}, T=5. The seven pairs: A[1]+B[1..2], A[1..2]+B[1], A[2]+B[3],
  // A[2..3]+B[1], A[3]+B[1..2], A[3..4]+B[3], A[4]+B[2].
  @Test
  @StdIo({"5", "4", "1 3 1 2", "3", "1 3 2"})
  void officialSampleCountsSevenSubarrayPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Smallest input (n = m = 1): exactly one subarray per side, so the answer is 0 or 1. ---

  // The lone pair 2 + 3 = 5 = T qualifies.
  @Test
  @StdIo({"5", "1", "2", "1", "3"})
  void singleElementArraysMatchingTargetCountOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The only possible sum is 2 + 3 = 5, which is not T = 10, so the answer is 0.
  @Test
  @StdIo({"10", "1", "2", "1", "3"})
  void singleElementArraysNotMatchingCountZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Unreachable targets must print 0 (the statement calls this out explicitly). ---

  // All-positive arrays: the maximum total is 3 + 3 = 6, well below T = 100, so 0 pairs.
  @Test
  @StdIo({"100", "2", "1 2", "2", "1 2"})
  void targetAboveMaximumReachableCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // All-positive arrays: the minimum total is 1 + 1 = 2, above T = -100, so no pair can be
  // negative.
  @Test
  @StdIo({"-100", "2", "1 2", "2", "1 2"})
  void targetBelowMinimumReachableCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Negative elements and zero-valued targets. ---

  // A={1,-1} and B={1,-1} each have subarray sums {1, 0, -1}. Pairs summing to 0: (1,-1), (0,0),
  // (-1,1) -> 3. Exercises negative elements and a zero target together.
  @Test
  @StdIo({"0", "2", "1 -1", "2", "1 -1"})
  void negativeElementsWithZeroTargetCountThreePairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // A={-1,-2} and B={-1,-2} each have subarray sums {-1, -2, -3}. Pairs summing to T = -3:
  // (-1,-2) and (-2,-1) -> 2. Confirms negative targets are handled.
  @Test
  @StdIo({"-3", "2", "-1 -2", "2", "-1 -2"})
  void negativeTargetCountsMatchingPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Contiguous subarrays whose sums repeat at different indices count separately. ---

  // A={1,1} has subarray sums {1, 1, 2}: the value 1 occurs at [1..1] and [2..2]. B={1} has {1}.
  // For T = 2 the only complement is 1, met by A's two distinct length-1 subarrays -> 2.
  @Test
  @StdIo({"2", "2", "1 1", "1", "1"})
  void duplicateSubarraySumsAtDifferentIndicesCountSeparately(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A={1,2,1,2} has three distinct subarrays summing to 3 ([1..2], [2..3], [3..4]). With B={3} and
  // T = 6 each pairs with B's single 3 -> 3. Mirrors the statement's "same values, different
  // indices" remark.
  @Test
  @StdIo({"6", "4", "1 2 1 2", "1", "3"})
  void equalValuedSubarraysEachFormADistinctPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Whole-array subarrays are valid, including the (full A, full B) pair. ---

  // A={1,2} sums {1,2,3}; B={3,4} sums {3,4,7}. T = 10 is reached only by 3 + 7, i.e. all of A
  // plus all of B -> 1. Guards the maximal-window boundary on both sides.
  @Test
  @StdIo({"10", "2", "1 2", "2", "3 4"})
  void entireArraysFormThePairAtTheMaximumTotal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Asymmetric sizes: n != m, so both arrays must be parsed with their own counts. ---

  // A={5} (n=1), B={1,2,3} (m=3) with sums {1, 2, 3, 3, 5, 6} -- the value 3 appears twice ([3..3]
  // and [1..2]). T = 8 needs B-sum 3, met twice, each paired with A's only 5 -> 2.
  @Test
  @StdIo({"8", "1", "5", "3", "1 2 3"})
  void singletonAWithLongerBCountsBSubarrayDuplicates(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Zero-sum subarrays arising from cancellation (not just zero elements). ---

  // A={1,-1,1}: subarray sums are 1 (at [1..1],[3..3],[1..3]), 0 (at [1..2],[2..3]), -1 (at
  // [2..2]). B={5,-5}: sums {5, 0, -5}. For T = 0 only (0,0) matches -> 2 * 1 = 2. Confirms windows
  // that cancel to zero are counted.
  @Test
  @StdIo({"0", "3", "1 -1 1", "2", "5 -5"})
  void cancellationZeroSumSubarraysAreMatched(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Zero elements at the smallest size. ---

  // A={0}, B={0}, T=0: the single 0 + 0 pair qualifies -> 1.
  @Test
  @StdIo({"0", "1", "0", "1", "0"})
  void singleZeroElementsWithZeroTargetCountOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A={0}, B={0}, T=1: the only sum is 0, never 1 -> 0.
  @Test
  @StdIo({"1", "1", "0", "1", "0"})
  void singleZeroElementsWithNonZeroTargetCountZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Boundary targets at full scale: T = +/-10^9 reached only by a maximal-magnitude window. ---

  // A is 1000 copies of 1,000,000, so the whole-array sum is exactly 10^9 (= T) and no shorter
  // window reaches it. B={0} contributes its only subarray sum, 0. Exactly one A window qualifies
  // -> 1. Stresses the maximum positive target and the largest legal subarray sum.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxPositiveTargetMatchesOnlyTheFullArraySum() throws IOException {
    int[] a = filled(1000, 1_000_000);
    int[] b = {0};
    assertThat(runMain(buildInput(1_000_000_000L, a, b))).isEqualTo("1");
  }

  // Mirror of the above at the negative extreme: A is 1000 copies of -1,000,000 (whole sum -10^9 =
  // T), B={0} -> 1. Confirms the minimum target and most-negative subarray sum.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxNegativeTargetMatchesOnlyTheFullArraySum() throws IOException {
    int[] a = filled(1000, -1_000_000);
    int[] b = {0};
    assertThat(runMain(buildInput(-1_000_000_000L, a, b))).isEqualTo("1");
  }

  // --- The critical overflow case: the answer must be accumulated as a long. ---

  // n = m = 1000, all zeros, T = 0: every one of the 500,500 subarrays per side sums to 0, so every
  // cross pair matches -> 500,500 * 500,500 = 250,500,250,000. This exceeds Integer.MAX_VALUE
  // (~2.1*10^9); an int accumulator overflows to a wrong (often negative) value. Also rejects a
  // brute O(n^2 * m^2) approach, which would do ~2.5*10^11 comparisons and time out.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allZerosAtMaxSizeOverflowsIntAccumulator() throws IOException {
    int[] a = filled(1000, 0);
    int[] b = filled(1000, 0);
    assertThat(runMain(buildInput(0L, a, b))).isEqualTo("250500250000");
  }

  // n = m = 1000, all ones, T = 2: the only way to reach 2 is 1 + 1, and a subarray of all-ones
  // sums to 1 exactly when it has length 1 -- 1000 such windows per side -> 1000 * 1000 =
  // 1,000,000. A large, hand-checkable count that still requires the efficient O(n^2 + m^2) build.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allOnesAtMaxSizeWithTargetTwoCountsSingletonPairs() throws IOException {
    int[] a = filled(1000, 1);
    int[] b = filled(1000, 1);
    assertThat(runMain(buildInput(2L, a, b))).isEqualTo("1000000");
  }

  // Opposite extremes at full scale: A is 1000 copies of +10^6, B is 1000 copies of -10^6, T = 0.
  // An A window of length L sums to L*10^6 (1000-L+1 such windows); a B window sums to
  // -(length)*10^6, so a pair cancels iff the two lengths are equal. Summing (1000-L+1)^2 over
  // L = 1..1000 gives the sum of squares 1..1000 = 333,833,500. Exercises both sign extremes and a
  // count in the hundreds of millions without quite overflowing int.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void oppositeExtremeValuesAtMaxSizeCountByMatchingLength() throws IOException {
    int[] a = filled(1000, 1_000_000);
    int[] b = filled(1000, -1_000_000);
    long expected = 0;
    for (int length = 1; length <= 1000; length++) {
      long windows = 1000L - length + 1;
      expected += windows * windows;
    }
    assertThat(runMain(buildInput(0L, a, b))).isEqualTo(Long.toString(expected));
  }

  // --- Randomized cross-check against an independent brute-force oracle. Small arrays with
  // negatives, zeros, and positives make zero-sum windows, duplicate sums, and exact/near misses
  // frequent; T is sampled across a band that lands below, at, and above reachable totals. Catches
  // off-by-one window bugs, sign mistakes, and miscounting the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(2143);
    for (int trial = 0; trial < 300; trial++) {
      int[] a = randomArray(1 + rnd.nextInt(8), rnd); // length 1..8
      int[] b = randomArray(1 + rnd.nextInt(8), rnd);
      long t = rnd.nextInt(21) - 10; // -10..10
      String input = buildInput(t, a, b);
      String expected = Long.toString(bruteForceCount(t, a, b));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Independent O(|A|^2 * |B|^2) oracle: enumerate every subarray sum on each side, then count the
  // cross pairs equal to t. Obviously correct, far too slow for the judge, trustworthy for tiny n.
  private static long bruteForceCount(long t, int[] a, int[] b) {
    long[] sumsA = allSubarraySums(a);
    long[] sumsB = allSubarraySums(b);
    long count = 0;
    for (long sa : sumsA) {
      for (long sb : sumsB) {
        if (sa + sb == t) {
          count++;
        }
      }
    }
    return count;
  }

  private static long[] allSubarraySums(int[] arr) {
    long[] sums = new long[arr.length * (arr.length + 1) / 2];
    int idx = 0;
    for (int i = 0; i < arr.length; i++) {
      long running = 0;
      for (int j = i; j < arr.length; j++) {
        running += arr[j];
        sums[idx++] = running;
      }
    }
    return sums;
  }

  private static int[] randomArray(int length, Random rnd) {
    int[] arr = new int[length];
    for (int i = 0; i < length; i++) {
      arr[i] = rnd.nextInt(11) - 5; // -5..5, covering negatives, zero, and positives
    }
    return arr;
  }

  private static int[] filled(int length, int value) {
    int[] arr = new int[length];
    Arrays.fill(arr, value);
    return arr;
  }

  /** Builds BOJ 2143 input: {@code T}, then {@code n} and A's values, then {@code m} and B's. */
  private static String buildInput(long t, int[] a, int[] b) {
    StringBuilder sb = new StringBuilder();
    sb.append(t).append('\n');
    appendArray(sb, a);
    appendArray(sb, b);
    return sb.toString();
  }

  private static void appendArray(StringBuilder sb, int[] arr) {
    sb.append(arr.length).append('\n');
    for (int i = 0; i < arr.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(arr[i]);
    }
    sb.append('\n');
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
