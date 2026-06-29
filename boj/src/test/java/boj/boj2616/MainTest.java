package boj.boj2616;

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
 * BOJ 2616 소형기관차 ("Small Locomotives") -- a fixed-window selection DP: choose three disjoint blocks
 * of consecutive passenger cars to carry the most people.
 *
 * <p>A large locomotive was pulling {@code N} passenger cars in a line; car {@code i} holds
 * {@code p[i]} passengers. The large locomotive is replaced by <b>three</b> small locomotives, each
 * of which can pull <b>at most {@code K} consecutive cars</b>. The three small locomotives pull
 * pairwise <b>non-overlapping</b> blocks (a car may be pulled by at most one of them) and the cars
 * keep their original order. Maximize the total number of passengers carried.
 *
 * <p><b>Why "at most K" collapses to "exactly K".</b> Passenger counts are non-negative, so growing
 * a block to its full {@code K} cars can never lose passengers. Therefore an optimal plan always
 * uses three full length-{@code K} blocks, and the search reduces to: pick start positions
 * {@code s1 < s2 < s3} with {@code s2 >= s1 + K}, {@code s3 >= s2 + K} and {@code s3 + K - 1 <= N},
 * maximizing the three length-{@code K} window sums. Both oracles below model exactly this.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code N} ({@code N <= 50,000}). Line 2 is the {@code N}
 * space-separated passenger counts {@code p[1..N]} (each at most 100). Line 3 is {@code K},
 * strictly less than {@code N / 3}. The program prints a single line: the maximum passengers three
 * small locomotives can carry. Because {@code K < N / 3} we always have {@code 3K < N}, so three
 * blocks always fit (with at least one spare car) and the answer is well defined and non-negative.
 *
 * <p><b>Why this is not a simple greedy.</b> Selecting the single highest-sum window, then the
 * highest disjoint window, then the highest disjoint window again is sub-optimal: the locally fat
 * window can box the remaining picks into thin ones. The official sample already exhibits this
 * (greedy 235 vs optimum 240), and {@link #valueGreedyWindowSelectionUndercountsTheOptimum(StdOut)}
 * isolates it on smaller numbers. Correctness needs the global DP, not per-pick maximization.
 *
 * <p><b>Range of the answer.</b> At most {@code 3K} cars are carried and each holds at most 100, so
 * the answer is below {@code 300K < 100N <= 5,000,000} -- comfortably inside a signed 32-bit
 * {@code int}. The oracles nonetheless accumulate in {@code long} so the cross-checks cannot
 * overflow regardless of the instance.
 *
 * <p><b>Oracles.</b> Small cases are cross-checked against {@link #bruteForceMaxPassengers(int[],
 * int)}, which enumerates every triple of disjoint length-{@code K} windows and re-sums each window
 * by direct iteration -- algorithmically independent of the tabulated prefix-sum DP, so agreement
 * is genuine evidence rather than a re-run of the same idea. The maximum-size case, where
 * enumeration is infeasible, is checked against {@link #dpMaxPassengers(int[], int)}, an
 * independently written {@code O(N)} prefix-sum DP; the randomized sweep additionally asserts the
 * two oracles agree with each other so the large-case oracle is itself trustworthy.
 */
class MainTest {

  // --- The judge's official sample: the canonical seven-car train. ---

  // N = 7, passengers [35, 40, 50, 10, 30, 45, 60], K = 2. Window sums (length 2) are
  // 75, 90, 60, 40, 75, 105. The optimum takes [1,2]=75, [3,4]=60 and [6,7]=105 for 240. Note a
  // value-greedy would grab [6,7]=105, then the locally fattest disjoint window [2,3]=90, and be
  // forced onto [4,5]=40 for a total of only 235 -- so this sample alone already rejects greedy.
  @Test
  @StdIo({"7", "35 40 50 10 30 45 60", "2"})
  void officialSampleTransportsTwoHundredForty(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("240");
  }

  // --- Base case: the smallest legal train, where blocks are single cars. ---

  // K must be strictly below N/3, so the smallest legal train is N = 4 with K = 1. With K = 1 each
  // locomotive pulls one car and the three blocks are just three distinct cars, so the answer is
  // the sum of the three largest -- here [1, 2, 3, 4] drops the smallest (1) to give 2+3+4 = 9.
  // Pins parsing of the minimum input and the K = 1 degenerate shape.
  @Test
  @StdIo({"4", "1 2 3 4", "1"})
  void smallestLegalTrainPicksTheTopThreeSingleCars(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- K = 1 in general: the three single-car blocks are simply the three richest cars. ---

  // N = 5, K = 1, [10, 20, 30, 40, 50]. Three disjoint single cars maximizing passengers are the
  // top three: 30 + 40 + 50 = 120. A solver that hard-wired "block length K" into a fixed stride
  // or mishandled the length-1 window would diverge here from the intended top-three selection.
  @Test
  @StdIo({"5", "10 20 30 40 50", "1"})
  void singleCarLocomotivesSelectTheThreeLargestCars(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("120");
  }

  // --- Greedy trap on small numbers: the optimum refuses the locally fattest middle window. ---

  // N = 7, K = 2, [3, 5, 5, 3, 0, 50, 50]. Window sums are 8, 10, 8, 3, 50, 100. A value-greedy
  // takes [6,7]=100, then the fattest disjoint single window [2,3]=10, and is then boxed into
  // [4,5]=3, totalling 113. The optimum instead pairs [6,7]=100 with the disjoint pair
  // [1,2]=8 and [3,4]=8 for 116. The expected answer is the LARGER value, so a one-window-at-a-time
  // implementation surfaces here as an under-count of exactly 3.
  @Test
  @StdIo({"7", "3 5 5 3 0 50 50", "2"})
  void valueGreedyWindowSelectionUndercountsTheOptimum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("116");
  }

  // --- Uniform train: every chosen car contributes the same, so the answer is purely structural.
  // ---

  // N = 9, K = 2, every car holds 100. Any placement of three disjoint length-2 blocks covers
  // exactly 3*K = 6 cars, so the answer is 6 * 100 = 600 regardless of where the blocks sit. This
  // isolates the block-count-times-capacity arithmetic from any value-dependent choice; an
  // off-by-one
  // in block length or count (covering 5 or 7 cars) would print 500 or 700 instead.
  @Test
  @StdIo({"9", "100 100 100 100 100 100 100 100 100", "2"})
  void uniformTrainFillsThreeFullWindows(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("600");
  }

  // --- Floor case: an empty train carries nobody. ---

  // N = 7, K = 2, every car empty. Three locomotives must still be placed, but every window sum is
  // 0, so the maximum is 0. Anchors the lower bound of the answer and guards against an
  // implementation that seeds its DP with a sentinel (e.g. a negative "unset" marker) and forgets
  // to
  // clamp the all-zero result back to 0.
  @Test
  @StdIo({"7", "0 0 0 0 0 0 0", "2"})
  void emptyTrainTransportsNobody(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Wider locomotives: each block spans three cars. ---

  // N = 10, K = 3, [1..10]. Three disjoint length-3 blocks cover nine of the ten cars; dropping the
  // single smallest car (1) and taking [2,3,4]=9, [5,6,7]=18, [8,9,10]=27 gives 54, the maximum.
  // Exercises a block length other than 1 or 2 and the non-overlap spacing of s >= prev + K at
  // width 3, where an off-by-one in the stride would let adjacent blocks share a car.
  @Test
  @StdIo({"10", "1 2 3 4 5 6 7 8 9 10", "3"})
  void widerLocomotivesSpanThreeCarsEach(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("54");
  }

  // --- Randomized cross-check: many small trains against the independent brute-force oracle. ---

  // Random trains (N in 4..18, every legal K, passenger counts 0..100) are each answered and
  // compared to a brute force that enumerates every triple of disjoint length-K windows. The small
  // N
  // keeps the cubic enumeration cheap while mixing block widths, tie patterns, and greedy traps
  // across hundreds of shapes, so this single sweep exercises the window-sum recharge and the
  // disjointness spacing far more thoroughly than any hand case. Each trial also asserts the
  // brute-force and prefix-sum DP oracles agree, validating the oracle the maximum-size test relies
  // on. A fixed seed keeps the run reproducible across JVMs.
  @Test
  void randomSmallTrainsMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(2616L);
    for (int trial = 0; trial < 400; trial++) {
      int n = 4 + rng.nextInt(15); // 4..18
      int kMax = (n - 1) / 3; // largest K with 3K < N, i.e. K strictly below N/3
      int k = 1 + rng.nextInt(kMax); // 1..kMax
      int[] p = new int[n];
      for (int i = 0; i < n; i++) {
        p[i] = rng.nextInt(101); // 0..100, matching the per-car cap
      }
      long brute = bruteForceMaxPassengers(p, k);
      assertThat(dpMaxPassengers(p, k))
          .as("oracle cross-check p=%s k=%d", Arrays.toString(p), k)
          .isEqualTo(brute);
      assertThat(runMain(buildInput(p, k)))
          .as("p=%s k=%d", Arrays.toString(p), k)
          .isEqualTo(Long.toString(brute));
    }
  }

  // --- The maximum-length train is parsed and solved within the time limit. ---

  // A full N = 50,000 train with K = (N-1)/3 (the largest legal value) and deterministic
  // pseudo-random passenger counts, cross-checked against the independent O(N) prefix-sum DP oracle
  // since enumeration is hopeless at this size. The intended DP is O(3N); the @Timeout guards
  // against
  // an accidentally super-linear (e.g. per-window re-summation) implementation, and the oracle
  // guards
  // correctness at the largest legal input.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumLengthTrainIsSolvedWithinTheTimeLimit() throws IOException {
    int n = 50_000;
    int k = (n - 1) / 3;
    int[] p = new int[n];
    Random rng = new Random(50_000L); // fixed seed -> reproducible
    for (int i = 0; i < n; i++) {
      p[i] = rng.nextInt(101);
    }
    String expected = Long.toString(dpMaxPassengers(p, k));
    assertThat(runMain(buildInput(p, k))).isEqualTo(expected);
  }

  /**
   * Independent brute-force oracle: returns the most passengers carried by three pairwise-disjoint
   * length-{@code k} windows of {@code p}, enumerating every valid triple of start positions and
   * re-summing each window by direct iteration. Modelling the physical choice of three blocks
   * directly -- rather than tabulating a recurrence -- makes agreement with {@link Main} an
   * independent cross-check.
   *
   * @implNote {@code O(W^3 * k)} time, where {@code W = p.length - k + 1} is the number of
   *     length-{@code k} windows; callers must keep {@code p.length} small (here at most 18). The
   *     {@code K < N/3} input guarantee ensures at least one valid triple exists, so the returned
   *     maximum is real.
   */
  private static long bruteForceMaxPassengers(int[] p, int k) {
    int n = p.length;
    long best = 0L;
    for (int i = 0; i + k <= n; i++) {
      long s1 = windowSum(p, i, k);
      for (int j = i + k; j + k <= n; j++) {
        long s2 = windowSum(p, j, k);
        for (int l = j + k; l + k <= n; l++) {
          best = Math.max(best, s1 + s2 + windowSum(p, l, k));
        }
      }
    }
    return best;
  }

  /** Sums the {@code k} passengers in the window of {@code p} starting at index {@code start}. */
  private static long windowSum(int[] p, int start, int k) {
    long sum = 0L;
    for (int i = start; i < start + k; i++) {
      sum += p[i];
    }
    return sum;
  }

  /**
   * Independent prefix-sum DP oracle used where enumeration is infeasible: {@code dp[j][i]} is the
   * most passengers carried by {@code j} length-{@code k} blocks among the first {@code i} cars,
   * either skipping car {@code i} ({@code dp[j][i-1]}) or ending a block exactly at car {@code i}
   * ({@code dp[j-1][i-k]} plus that block's prefix-sum window). Written separately from
   * {@link Main} so it can stand in as an expected-value source at the maximum input size.
   *
   * @implNote {@code O(N)} time and space, where {@code N = p.length} (the three locomotive layers
   *     are a constant factor); accumulates in {@code long} so the oracle cannot overflow.
   */
  private static long dpMaxPassengers(int[] p, int k) {
    int n = p.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) {
      prefix[i + 1] = prefix[i] + p[i];
    }
    long[][] dp = new long[4][n + 1];
    for (int j = 1; j <= 3; j++) {
      for (int i = j * k; i <= n; i++) {
        long window = prefix[i] - prefix[i - k];
        dp[j][i] = Math.max(dp[j][i - 1], dp[j - 1][i - k] + window);
      }
    }
    return dp[3][n];
  }

  /**
   * Builds BOJ 2616 input: {@code N} on line 1, the {@code N} space-separated passenger counts on
   * line 2, and {@code K} on line 3.
   */
  private static String buildInput(int[] p, int k) {
    StringBuilder sb = new StringBuilder();
    sb.append(p.length).append('\n');
    for (int i = 0; i < p.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(p[i]);
    }
    sb.append('\n').append(k).append('\n');
    return sb.toString();
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
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
