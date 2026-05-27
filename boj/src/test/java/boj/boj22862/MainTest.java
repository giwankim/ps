package boj.boj22862;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 22862 가장 긴 짝수 연속한 부분 수열 (large).
 *
 * <p>Given a sequence of N positive integers and an integer K, choose a contiguous subarray and
 * remove AT MOST K odd values from it; print the maximum number of even values that remain.
 * Equivalently: maximize {@code window_length - odd_count} over all windows whose {@code odd_count
 * <= K}. Constraints (per the problem's "(large)" variant): N up to 1,000,000 and K up to 100,000.
 */
class MainTest {

  // --- Minimum input (N = 1) with a single even value: the only window has 0 odds (<= any K) and
  // 1 even -> answer 1. Pins the answer to "even count", not "window length minus anything else".
  // ---

  @Test
  @StdIo({"1 0", "2"})
  void singleEvenValueReturnsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- N = 1 with a single odd and K = 0: no removal allowed, so any non-empty window busts the
  // budget. The empty/zero-even window wins -> 0. ---

  @Test
  @StdIo({"1 0", "3"})
  void singleOddValueWithZeroBudgetReturnsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- N = 1 with a single odd and K = 1: the odd can be removed, but the window then has 0
  // evens, so the answer is still 0. Guards a solution that confuses "removable" with "counted":
  // K only buys the right to *omit* odds, never to count them. ---

  @Test
  @StdIo({"1 1", "5"})
  void singleOddValueWithBudgetReturnsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All-even input with K = 0: the entire array is already a valid window (0 odds), and every
  // entry is counted -> answer N. Guards a solution that wrongly insists K must be spent. ---

  @Test
  @StdIo({"5 0", "2 4 6 8 10"})
  void allEvenWithZeroBudgetReturnsLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- All-odd input with K = 0: no removal, no evens, answer 0. Mirror of the all-even case. ---

  @Test
  @StdIo({"5 0", "1 3 5 7 9"})
  void allOddWithZeroBudgetReturnsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All-odd input with K = N: every odd is removable, but the resulting window holds 0 evens
  // -> answer 0. A solution that prints "window length" (here 5) instead of "even count" fails.
  // The key distinction between this problem and the simpler "max window length with at most K
  // odds" problem. ---

  @Test
  @StdIo({"5 5", "1 3 5 7 9"})
  void allOddWithFullBudgetStillReturnsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- K = 0 reduces the problem to "longest run of consecutive evens". Runs in
  // "2 4 1 6 8 10 3" are [2,4] (length 2) and [6,8,10] (length 3); the longer wins -> 3. ---

  @Test
  @StdIo({"7 0", "2 4 1 6 8 10 3"})
  void zeroBudgetReducesToLongestEvenRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- K large enough to absorb every odd in the whole array means the answer is the total even
  // count. Array "2 1 4 3 6 5 8" has 4 evens and 3 odds; K = 3 spends exactly on the 3 odds, so a
  // single window covers everything and counts all 4 evens -> 4. ---

  @Test
  @StdIo({"7 3", "2 1 4 3 6 5 8"})
  void budgetCoversAllOddsReturnsTotalEvenCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Published counterexample from BOJ Q&A board for this problem: N = 5, K = 1, sequence
  // "2 1 2 1 1". The optimal window (2, 1, 2) covers indices 0..2 and contains 2 evens after
  // spending K = 1 on the single odd. A solution that prints window length 3 here would be wrong;
  // the answer is the count of evens, 2. ---

  @Test
  @StdIo({"5 1", "2 1 2 1 1"})
  void publishedCounterexampleAnswersEvenCountNotWindowLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Optimal window sits in the INTERIOR, not anchored at either end. Array "1 2 4 1 6 8 1"
  // with K = 1: the unique optimum is indices 1..5 = (2, 4, 1, 6, 8) with 4 evens + 1 odd. Every
  // window touching index 0 or index 6 carries an extra odd that busts K. Guards an algorithm
  // that scans only from a fixed anchor. ---

  @Test
  @StdIo({"7 1", "1 2 4 1 6 8 1"})
  void interiorWindowOptimalWithSingleOddBridge(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Optimal window anchored at the array START. Array "2 4 1 6 8 1 1 1" with K = 1: the
  // best window is 0..4 = (2, 4, 1, 6, 8) with 4 evens + 1 odd. Spending K on the single odd at
  // index 2 bridges the two leading even runs. A solution that only considers windows starting
  // after the first odd would miss this. ---

  @Test
  @StdIo({"8 1", "2 4 1 6 8 1 1 1"})
  void optimalWindowAtLeadingStartIsCaptured(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Optimal window anchored at the array END (right pointer reaches N-1). Array
  // "1 1 8 1 6 4 2" with K = 1: the best window is 2..6 = (8, 1, 6, 4, 2) with 4 evens + 1 odd.
  // The trailing pure-even run "6 4 2" alone is only 3 evens; spending K on the bridge odd at
  // index 3 extends the answer by one. A solution that forgets the final max comparison after
  // the loop terminates would report 3. ---

  @Test
  @StdIo({"7 1", "1 1 8 1 6 4 2"})
  void optimalWindowAtTrailingEndIsCaptured(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- K spans MULTIPLE odd separators (not a single bridge). Array "2 1 4 1 6 1 8" with K = 2:
  // the whole array has 4 evens + 3 odds (bust), so the best window contains at most 2 odds. Two
  // tied optima: 0..4 = (2, 1, 4, 1, 6) and 2..6 = (4, 1, 6, 1, 8), each with 3 evens + 2 odds.
  // Answer 3. Guards a solution that handles only a single isolated odd inside the window. ---

  @Test
  @StdIo({"7 2", "2 1 4 1 6 1 8"})
  void budgetSpansMultipleOddBlocks(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The running maximum must survive a left-side SHRINK without being overwritten. Array
  // "2 2 2 1 4 4 4 1 6 6 6" with K = 1: the only valid windows containing one odd are 0..6 =
  // (2,2,2,1,4,4,4) -> 6 evens and 4..10 = (4,4,4,1,6,6,6) -> 6 evens. The two optima sit on
  // opposite sides of the array, separated by a forced shrink past the second odd. A solution
  // that resets its best inside the shrink loop would drop to a lower value when sliding right.
  // ---

  @Test
  @StdIo({"11 1", "2 2 2 1 4 4 4 1 6 6 6"})
  void runningMaximumHeldAcrossLeftShrink(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- K equals the total odd count exactly. Array "1 2 1 4 1 6 1" has 3 evens + 4 odds; K = 4
  // exactly spends on all odds and the whole array becomes a valid window -> 3 evens. Companion
  // to the next test, which drops K by one. ---

  @Test
  @StdIo({"7 4", "1 2 1 4 1 6 1"})
  void exactBudgetMatchesAllOddsInWholeArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Same array "1 2 1 4 1 6 1" (3 evens at indices 1,3,5; 4 odds at indices 0,2,4,6) with
  // K = 1: only one odd may sit in the window, forcing the algorithm to keep two evens at most.
  // Best windows are (1..3) = (2,1,4) and (3..5) = (4,1,6), each 2 evens + 1 odd -> answer 2.
  // K = 4 above gives 3, K = 1 here gives 2: the K-by-one effect that distinguishes a real
  // sliding-window from one that always grabs the whole array. ---

  @Test
  @StdIo({"7 1", "1 2 1 4 1 6 1"})
  void tightBudgetForcesShrinkAndDropsAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Performance at the upper N bound, all evens. N = 1,000,000 plates of value 2 with K = 0:
  // the entire array is a valid window -> answer 1,000,000. An O(N*K) or O(N^2) approach trivially
  // passes here (K = 0) but tests pure scan and the I/O path at scale. The intended O(N) sliding
  // window is instant. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllEvenInputReturnsLength() throws IOException {
    assertThat(runMain(uniformInput(1_000_000, 0, 2))).isEqualTo("1000000");
  }

  // --- Performance: N = 1,000,000 all odds with K at the upper spec bound 100,000. Every value
  // is odd, so no window contains any evens -> answer 0 regardless of K. A solution that
  // mistakenly returns "window length" or "K" instead of "even count" fails this immediately. The
  // K = 100,000 also stresses the shrink path: a naive O(N*K) shrink would push 10^11 operations.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllOddInputReturnsZero() throws IOException {
    assertThat(runMain(uniformInput(1_000_000, 100_000, 1))).isEqualTo("0");
  }

  // --- Performance with a single odd at the array's exact midpoint, K = 1. The two even halves
  // (500,000 each side of the lone odd) merge via the K = 1 bridge into a 999,999-even window.
  // This forces the right pointer to walk the entire array while only one shrink ever fires
  // (when the odd enters the window, K is exactly consumed; afterwards no more shrinks are
  // needed). Pins the "K bridges a single isolated odd at scale" behavior. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeSingleIsolatedOddBridgedByBudget() throws IOException {
    assertThat(runMain(singleOddInMiddleInput(1_000_000, 1))).isEqualTo("999999");
  }

  // --- Performance with maximum window churn at scale. N = 1,000,000 in the alternating pattern
  // 2,1,2,1,... (500,000 evens + 500,000 odds), K = 100,000. The maximum window holding 100,000
  // odds has length 2*100,000 + 1 = 200,001 starting on an even, giving 100,001 evens. Forces the
  // sliding window to expand and shrink across hundreds of thousands of values; the intended O(N)
  // algorithm handles it in milliseconds. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAlternatingPatternStressesSliding() throws IOException {
    assertThat(runMain(alternatingInput(1_000_000, 100_000))).isEqualTo("100001");
  }

  // --- Randomized cross-check against an independent O(N^2) brute-force oracle. Small N
  // (1..40) and a small value range (1..10) make all-even, all-odd, mostly-even, mostly-odd, and
  // mixed shapes all common, with K sampled across 0..6 so it lands below, at, and above the
  // typical odd count for each trial. Catches off-by-one bugs in the sliding-window add/drop
  // updates and any "window length vs even count" confusion that the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(22862);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rnd.nextInt(40); // 1..40
      int k = rnd.nextInt(7); // 0..6
      int[] values = new int[n];
      for (int i = 0; i < n; i++) {
        values[i] = 1 + rnd.nextInt(10); // 1..10 -> roughly even parity mix
      }
      String input = randomInput(n, k, values);
      String expected = Integer.toString(bruteForceMaxEvens(n, k, values));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference O(N^2) selection: from each start index, extend right while the running odd count
  // stays <= k, tracking the maximum even count seen. Obviously correct but too slow for the
  // judge; trustworthy only for the tiny N used by the oracle.
  private static int bruteForceMaxEvens(int n, int k, int[] values) {
    int best = 0;
    for (int i = 0; i < n; i++) {
      int evens = 0;
      int odds = 0;
      for (int j = i; j < n; j++) {
        if (values[j] % 2 == 0) {
          evens++;
        } else {
          odds++;
          if (odds > k) {
            break;
          }
        }
        best = Math.max(best, evens);
      }
    }
    return best;
  }

  private static String randomInput(int n, int k, int[] values) {
    StringBuilder sb = new StringBuilder(n * 4 + 16);
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(values[i]);
      sb.append(i == n - 1 ? '\n' : ' ');
    }
    return sb.toString();
  }

  private static String uniformInput(int n, int k, int value) {
    StringBuilder sb = new StringBuilder(n * 2 + 16);
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(value);
      sb.append(i == n - 1 ? '\n' : ' ');
    }
    return sb.toString();
  }

  // N values, all 2 (even) except a single 1 (odd) at index n / 2.
  private static String singleOddInMiddleInput(int n, int k) {
    int mid = n / 2;
    StringBuilder sb = new StringBuilder(n * 2 + 16);
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(i == mid ? 1 : 2);
      sb.append(i == n - 1 ? '\n' : ' ');
    }
    return sb.toString();
  }

  // N values alternating 2,1,2,1,... (even at even indices, odd at odd indices).
  private static String alternatingInput(int n, int k) {
    StringBuilder sb = new StringBuilder(n * 2 + 16);
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append((i & 1) == 0 ? 2 : 1);
      sb.append(i == n - 1 ? '\n' : ' ');
    }
    return sb.toString();
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
