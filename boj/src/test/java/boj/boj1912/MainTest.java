package boj.boj1912;

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
 * BOJ 1912 연속합 (Maximum Subarray Sum) -- the canonical Kadane's-algorithm problem.
 *
 * <p>Given a sequence of {@code n} integers, find the largest sum obtainable from a contiguous,
 * non-empty slice. With {@code dp[i]} the best sum of a slice ending exactly at index {@code i},
 * the recurrence {@code dp[i] = max(arr[i], dp[i - 1] + arr[i])} -- either extend the running slice
 * or restart at {@code arr[i]} -- lets a single {@code O(n)} pass track {@code max(dp[i])}.
 *
 * <p>Constraints: {@code 1 <= n <= 100,000}; each value is an integer in {@code [-1000, 1000]}.
 *
 * <p>The defining trap is the "at least one number" rule: every slice is non-empty, so when the
 * whole sequence is negative the answer is the <em>least-negative</em> element, never {@code 0}. An
 * implementation that seeds its answer at {@code 0} (rather than below every reachable sum)
 * silently fails on all-negative input -- the cases below pin that down. The widest-magnitude
 * answer, an all-{@code 1000} sequence of length {@code 100,000}, is {@code 100,000 * 1000 =
 * 100,000,000}, which still fits a signed 32-bit {@code int}.
 *
 * <p>Output is a single line: the maximum slice sum.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"10", "10 -4 3 1 5 6 -35 12 21 -1"})
  void officialSampleFindsTheTwelveTwentyOneRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The big -35 severs the sequence; the best slice is the trailing 12 + 21 = 33.
    assertThat(out.capturedString().trim()).isEqualTo("33");
  }

  // --- Single-element sequences: the "at least one number" rule in its purest form. ---

  @Test
  @StdIo({"1", "5"})
  void singlePositiveElementIsItsOwnAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"1", "-100"})
  void singleNegativeElementMustStillBeSelected(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The only legal slice is the element itself, so a negative answer is correct here.
    assertThat(out.capturedString().trim()).isEqualTo("-100");
  }

  @Test
  @StdIo({"1", "0"})
  void singleZeroElementReturnsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All-negative sequences: the defining trap -- the answer is the least-negative element. ---

  @Test
  @StdIo({"5", "-5 -2 -9 -4 -100"})
  void allNegativeSequenceReturnsTheLeastNegativeElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every slice sum is negative; the maximum is the single element -2. A seed of 0 fails this.
    assertThat(out.capturedString().trim()).isEqualTo("-2");
  }

  @Test
  @StdIo({"3", "-7 -7 -7"})
  void allEqualNegativeElementsReturnOneOfThem(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Adding any second element only makes the sum smaller, so the best slice is a single -7.
    assertThat(out.capturedString().trim()).isEqualTo("-7");
  }

  // --- All-positive sequences: extending always helps, so the whole array wins. ---

  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void allPositiveSequenceSumsTheEntireArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No element ever lowers the running sum, so the best slice spans everything: 15.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Position of the maximum slice: it must be found wherever it sits. ---

  @Test
  @StdIo({"5", "9 8 -5 -5 -5"})
  void maximumSliceAtTheStartIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The leading 9 + 8 = 17 run is best; the trailing negatives only erode it.
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  @Test
  @StdIo({"7", "-5 -5 8 9 7 -5 -5"})
  void maximumSliceInTheMiddleIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The interior 8 + 9 + 7 = 24 run, fenced by negatives on both sides.
    assertThat(out.capturedString().trim()).isEqualTo("24");
  }

  @Test
  @StdIo({"5", "-5 -5 -5 8 9"})
  void maximumSliceAtTheEndIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The trailing 8 + 9 = 17 run; the leading negatives must be discarded.
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  @Test
  @StdIo({"5", "-1 -1 8 -1 -1"})
  void lonePositivePeakAmongNegativesIsTheAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every neighboring element is negative, so the best slice is the single 8.
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- The restart decision: drop a costly prefix, but bridge a dip that pays for itself. ---

  @Test
  @StdIo({"3", "4 -10 4"})
  void largeNegativeGapForcesARestartRatherThanBridging(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Crossing the -10 yields 4 - 10 + 4 = -2, worse than restarting at the final 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"3", "4 -1 4"})
  void smallNegativeDipIsBridgedToJoinTwoRuns(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The -1 is cheap enough to cross: 4 - 1 + 4 = 7 beats either lone 4.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"4", "2 3 -50 10"})
  void runningMaxSurvivesAResetToALaterLargerElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The 2 + 3 = 5 prefix is overtaken after the -50 reset by the single trailing 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"9", "-2 1 -3 4 -1 2 1 -5 4"})
  void textbookMixedSequenceFindsTheClassicSlice(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The well-known answer is the 4 - 1 + 2 + 1 = 6 slice (indices 4..7, 1-indexed).
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Zeros: a zero-valued slice must beat any surrounding negative slice. ---

  @Test
  @StdIo({"3", "-5 0 -5"})
  void zeroBeatsEverySurroundingNegativeSlice(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single 0 outranks any slice that includes a negative, so the answer is 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Value bounds: every element pinned to an end of [-1000, 1000]. ---

  @Test
  @StdIo({"5", "1000 1000 1000 1000 1000"})
  void allMaximumValuesSumAcrossTheWholeArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 5 * 1000 = 5000.
    assertThat(out.capturedString().trim()).isEqualTo("5000");
  }

  @Test
  @StdIo({"5", "-1000 -1000 -1000 -1000 -1000"})
  void allMinimumValuesReturnASingleLeastNegativeElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // All slices are negative; the best is one -1000. The seed must sit below this to be chosen.
    assertThat(out.capturedString().trim()).isEqualTo("-1000");
  }

  @Test
  @StdIo({"4", "-1000 1000 -1000 1000"})
  void alternatingExtremesSelectASingleMaximumElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Bridging a -1000 to join two 1000s only ties a lone 1000, so the answer is 1000.
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // --- Ties: when two disjoint slices share the maximum, that shared sum is reported. ---

  @Test
  @StdIo({"5", "3 3 -100 3 3"})
  void tiedDisjointSlicesReturnTheSharedSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The 3 + 3 = 6 run occurs before and after the -100; the answer is the shared 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Smallest sequences above n = 1, exercising both branches of the recurrence at n = 2. ---

  @Test
  @StdIo({"2", "5 5"})
  void twoPositiveElementsSumTogether(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Extending wins: 5 + 5 = 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"2", "-100 100"})
  void twoElementsRestartAtTheLargerWhenThePrefixHurts(StdOut out) throws IOException {
    Main.main(new String[0]);
    // -100 + 100 = 0 is worse than restarting at 100, so the answer is 100.
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"2", "-3 -7"})
  void twoNegativeElementsReturnTheLeastNegative(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both slices spanning more than one element are smaller; the best is the single -3.
    assertThat(out.capturedString().trim()).isEqualTo("-3");
  }

  // --- Upper bounds (generated input, driven through the stdin/stdout helper like the project's
  // other larger-input tests). ---

  @Test
  void fullLengthAllMaximumValuesReachHundredMillionWithoutOverflow() throws IOException {
    int n = 100_000;
    int[] arr = new int[n];
    Arrays.fill(arr, 1000);
    // Widest-magnitude answer: the whole array, 100,000 * 1000 = 100,000,000, within int range.
    assertThat(runMain(buildInput(arr))).isEqualTo("100000000");
  }

  @Test
  void fullLengthAllMinimumValuesReturnASingleLeastNegativeElement() throws IOException {
    int n = 100_000;
    int[] arr = new int[n];
    Arrays.fill(arr, -1000);
    // Every slice is negative at scale; the answer is one -1000, so the seed must sit below it.
    assertThat(runMain(buildInput(arr))).isEqualTo("-1000");
  }

  @Test
  void largeMixedSequenceMatchesTheNaiveOracle() throws IOException {
    int n = 2_000;
    int[] arr = new int[n];
    for (int idx = 0; idx < n; idx++) {
      arr[idx] = ((idx * 37) % 2001) - 1000; // deterministic spread across the full [-1000, 1000].
    }
    // Cross-check the O(n) implementation against an independent O(n^2) brute force.
    assertThat(runMain(buildInput(arr))).isEqualTo(Long.toString(naiveMaxSubarraySum(arr)));
  }

  /** Builds BOJ 1912 input: a length header line followed by the space-separated values. */
  private static String buildInput(int[] arr) {
    StringBuilder sb = new StringBuilder();
    sb.append(arr.length).append('\n');
    for (int idx = 0; idx < arr.length; idx++) {
      if (idx > 0) {
        sb.append(' ');
      }
      sb.append(arr[idx]);
    }
    return sb.append('\n').toString();
  }

  /** Independent O(n^2) oracle: the maximum sum over every contiguous, non-empty slice. */
  private static long naiveMaxSubarraySum(int[] arr) {
    long best = Long.MIN_VALUE;
    for (int start = 0; start < arr.length; start++) {
      long sum = 0;
      for (int end = start; end < arr.length; end++) {
        sum += arr[end];
        best = Math.max(best, sum);
      }
    }
    return best;
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
