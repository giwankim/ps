package boj.boj2559;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2559 수열 (Sequence) -- the canonical fixed-width sliding-window maximum.
 *
 * <p>Given temperatures measured over {@code N} consecutive days, find the largest sum obtainable
 * from any {@code K} consecutive days. With a prefix-sum array {@code P} ({@code P[0] = 0},
 * {@code P[i]} the sum of the first {@code i} days) every window sum is {@code P[i] - P[i - K]}, so
 * a single {@code O(N)} pass over {@code i = K..N} finds the maximum.
 *
 * <p>Constraints: {@code 2 <= N <= 100,000}; {@code 1 <= K <= N}; each temperature is an integer in
 * {@code [-100, 100]}. Because temperatures may be negative, the answer may be negative -- the
 * running maximum must start below every reachable window sum (the implementation seeds it with
 * {@code Integer.MIN_VALUE}). The widest-magnitude answer, {@code 100,000 * 100 = 10,000,000},
 * still fits a signed 32-bit {@code int}.
 *
 * <p>Output is a single line: the maximum window sum.
 */
class MainTest {

  // --- Official samples from the statement (same temperatures, two window sizes). ---

  @Test
  @StdIo({"10 2", "3 -2 -4 -9 0 3 7 13 8 -3"})
  void officialSampleTwoDayWindowFindsTheMaximumPairSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best 2-day window is days 8..9: 13 + 8 = 21.
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  @Test
  @StdIo({"10 5", "3 -2 -4 -9 0 3 7 13 8 -3"})
  void officialSampleFiveDayWindowFindsTheMaximumRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best 5-day window is days 5..9: 0 + 3 + 7 + 13 + 8 = 31.
    assertThat(out.capturedString().trim()).isEqualTo("31");
  }

  // --- Window-size boundaries: K = 1 and K = N drive the ends of the sliding loop. ---

  @Test
  @StdIo({"5 1", "3 -2 7 -9 4"})
  void windowOfOneReturnsTheSingleLargestTemperature(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each window is one day, so the answer is just max(3, -2, 7, -9, 4) = 7.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"5 5", "3 -2 7 -9 4"})
  void windowSpanningEveryDaySumsTheWholeSequence(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = N: the only window is the entire array, 3 - 2 + 7 - 9 + 4 = 3.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"5 4", "1 2 3 4 -100"})
  void windowOneShortOfTheWholeSequenceComparesTwoRuns(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = N - 1 leaves exactly two windows: 1+2+3+4 = 10 vs 2+3+4-100 = -91.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Negative temperatures: the defining trap -- a correct answer can be negative. ---

  @Test
  @StdIo({"5 2", "-1 -2 -3 -4 -5"})
  void allNegativeTemperaturesYieldTheLeastNegativeWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: -3, -5, -7, -9. The maximum is the least negative, -3 (days 1..2).
    assertThat(out.capturedString().trim()).isEqualTo("-3");
  }

  @Test
  @StdIo({"5 1", "-7 -2 -9 -4 -100"})
  void allNegativeTemperaturesWithWindowOneReturnTheLargestSingleValue(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Single-day windows, all negative: the largest is -2.
    assertThat(out.capturedString().trim()).isEqualTo("-2");
  }

  @Test
  @StdIo({"4 4", "-1 -2 -3 -4"})
  void allNegativeTemperaturesAcrossTheWholeSequenceSumToTheNegativeTotal(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // K = N with every day negative: the sole window sums to -10.
    assertThat(out.capturedString().trim()).isEqualTo("-10");
  }

  // --- Window position: the maximum must be found wherever it sits in the sequence. ---

  @Test
  @StdIo({"5 2", "10 9 -5 -5 -5"})
  void maximumWindowAtTheStartIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: 19, 4, -10, -10. The maximum 19 is the very first window.
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  @Test
  @StdIo({"5 2", "-5 -5 -5 9 10"})
  void maximumWindowAtTheEndIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: -10, -10, 4, 19. The maximum 19 is the very last window.
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  @Test
  @StdIo({"7 3", "-5 -5 8 9 7 -5 -5"})
  void maximumWindowInTheMiddleIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: -2, 12, 24, 11, -3. The maximum 24 (days 3..5) sits in the interior.
    assertThat(out.capturedString().trim()).isEqualTo("24");
  }

  // --- Zeros: a zero-sum window must beat any surrounding negative window. ---

  @Test
  @StdIo({"5 2", "-5 0 0 -5 -5"})
  void zeroWindowBeatsSurroundingNegativeWindows(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: -5, 0, -5, -10. The maximum is the all-zero window, 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Value bounds: every temperature pinned to an end of [-100, 100]. ---

  @Test
  @StdIo({"5 3", "100 100 100 100 100"})
  void allMaximumTemperaturesSumToHundredTimesTheWindowSize(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every 3-day window is identical: 3 * 100 = 300.
    assertThat(out.capturedString().trim()).isEqualTo("300");
  }

  @Test
  @StdIo({"5 3", "-100 -100 -100 -100 -100"})
  void allMinimumTemperaturesSumToNegativeHundredTimesTheWindowSize(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every 3-day window is identical: 3 * -100 = -300.
    assertThat(out.capturedString().trim()).isEqualTo("-300");
  }

  // --- Smallest legal sequence: N = 2, both window sizes. ---

  @Test
  @StdIo({"2 1", "-100 100"})
  void smallestSequenceWithWindowOneReturnsTheLargerDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    // N = 2, K = 1: max(-100, 100) = 100.
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"2 2", "-100 100"})
  void smallestSequenceWithWindowTwoSumsBothDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    // N = 2, K = 2: the single window is -100 + 100 = 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Ties: when two windows share the maximum, that shared sum is reported. ---

  @Test
  @StdIo({"6 2", "5 5 1 5 5 1"})
  void tiedMaximumWindowsReturnTheSharedSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Windows: 10, 6, 6, 10, 6. The maximum 10 occurs twice; the answer is still 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Upper bounds: full-length input, driven through the stdin/stdout helper. ---

  @Test
  void fullLengthSequenceOfMaximumTemperaturesDoesNotOverflow() throws IOException {
    int n = 100_000;
    int[] temps = new int[n];
    java.util.Arrays.fill(temps, 100);
    // Widest-magnitude answer: K = N over all 100s is 100,000 * 100 = 10,000,000, within int range.
    assertThat(runMain(buildInput(temps, n))).isEqualTo("10000000");
  }

  @Test
  void fullLengthSequenceOfMinimumTemperaturesReachesTheNegativeBound() throws IOException {
    int n = 100_000;
    int[] temps = new int[n];
    java.util.Arrays.fill(temps, -100);
    // K = N over all -100s is -10,000,000; the seed must sit below this for max() to pick it up.
    assertThat(runMain(buildInput(temps, n))).isEqualTo("-10000000");
  }

  @Test
  void largeNonConstantSequenceMatchesTheNaiveOracle() throws IOException {
    int n = 100_000;
    int[] temps = new int[n];
    for (int idx = 0; idx < n; idx++) {
      temps[idx] = (idx % 201) - 100; // cycles through every value in [-100, 100].
    }
    for (int k : new int[] {1, 2, 137, 1_000, 50_000, n}) {
      assertThat(runMain(buildInput(temps, k)))
          .as("K = %d", k)
          .isEqualTo(Long.toString(naiveMaxWindowSum(temps, k)));
    }
  }

  /** Builds BOJ 2559 input: a {@code "N K"} header line followed by the space-separated temps. */
  private static String buildInput(int[] temps, int k) {
    StringBuilder sb = new StringBuilder();
    sb.append(temps.length).append(' ').append(k).append('\n');
    for (int idx = 0; idx < temps.length; idx++) {
      if (idx > 0) {
        sb.append(' ');
      }
      sb.append(temps[idx]);
    }
    return sb.append('\n').toString();
  }

  /** Independent O(N*K) oracle: the maximum sum over every window of width {@code k}. */
  private static long naiveMaxWindowSum(int[] temps, int k) {
    long best = Long.MIN_VALUE;
    for (int start = 0; start + k <= temps.length; start++) {
      long sum = 0;
      for (int offset = 0; offset < k; offset++) {
        sum += temps[start + offset];
      }
      best = Math.max(best, sum);
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
