package boj.boj1149;

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
 * BOJ 1149 RGB거리 ("RGB Street") -- the canonical "paint a path, no two neighbors alike" DP.
 *
 * <p>{@code N} houses stand in a row, numbered 1 to {@code N}. Each must be painted red, green, or
 * blue, and house {@code i} carries three costs {@code R[i] G[i] B[i]}. The only rule is that
 * adjacent houses differ in color: house 1 differs from house 2, house {@code N} differs from house
 * {@code N - 1}, and every interior house {@code i (2 <= i <= N - 1)} differs from both house
 * {@code i - 1} and house {@code i + 1}. Minimize the total painting cost. Note the two endpoints
 * are NOT neighbors of each other, so they may share a color.
 *
 * <p>Constraints: {@code 2 <= N <= 1,000}; every cost is a natural number {@code 1 <= cost <=
 * 1,000}. The most expensive legal answer therefore paints all {@code N} houses at the cap,
 * {@code N * 1,000 = 1,000,000}, which sits comfortably inside a signed 32-bit {@code int}.
 *
 * <p>Three families of mistakes drive these fixtures:
 *
 * <ul>
 *   <li><b>Ignoring the adjacency rule.</b> Summing each house's own cheapest color is a lower
 *       bound that is usually illegal -- it can repeat a color across neighbors
 *       ({@link #repeatingTheCheapestColorAcrossNeighborsIsForbidden(StdOut)}).
 *   <li><b>Greedy left-to-right.</b> Locking in the cheapest color at house 1 (or at any single
 *       house) can force ruinously expensive neighbors downstream; the optimum sometimes pays a
 *       little more early to save a lot later
 *       ({@link #committingToTheCheapestColorAtHouseOneIsSuboptimal(StdOut)}).
 *   <li><b>Dropping or mishandling one color index.</b> A solution that reasons over only two of
 *       the three colors, or transposes an index, breaks when the cheap chain must alternate
 *       through the third color ({@link #blueIsCheapestEverywhereButCannotRepeat(StdOut)}).
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by two independent oracles: an exhaustive
 * try-every-color recursion for small inputs
 * ({@link #randomizedSmallInputsMatchBruteForceOracle()}) and a separately written rolling DP for
 * the max-scale fuzz ({@link #largeRandomInputMatchesTheDpOracle()}). The {@code @Timeout} max-size
 * cases also guard the {@code 1,000,000} accumulator against an overflow- or truncation-prone
 * width.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // Houses (R,G,B): (26,40,83) (49,60,57) (13,89,99). The cheapest legal painting is house 1 red
  // (26), house 2 blue (57), house 3 red (13) -> 96. Per-color DP: dp1 = (89, 86, 83) and dp2 =
  // (96, 172, 185), so the answer is dp2[R] = 96.
  @Test
  @StdIo({"3", "26 40 83", "49 60 57", "13 89 99"})
  void officialSampleMinimizesToNinetySix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("96");
  }

  // --- The smallest input: N = 2, the only size where both houses are endpoints. ---

  // Two houses, (1,100,100) and (100,1,100). They must differ, but the globally cheapest pick --
  // house 1 red (1), house 2 green (1) -- already differs, so the answer is 2. The minimum input
  // that yields a non-trivial choice.
  @Test
  @StdIo({"2", "1 100 100", "100 1 100"})
  void twoHousesTakeTheTwoCheapestDistinctColors(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family 1: the adjacency rule cannot be ignored. ---

  // Both houses are cheapest in red (1) and tie at 50 for the other two colors. The illegal
  // "cheapest of each house" sum is 1 + 1 = 2, but red-then-red is forbidden, so one house must
  // pay 50: the optimum is 1 + 50 = 51 (e.g. red then green). A solution that sums per-house minima
  // prints 2 and fails here.
  @Test
  @StdIo({"2", "1 50 50", "1 50 50"})
  void repeatingTheCheapestColorAcrossNeighborsIsForbidden(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("51");
  }

  // --- Family 2: greedy left-to-right loses. ---

  // Houses (1,2,100) (1,100,100) (100,1,100). A greedy that grabs house 1 red (1) is then forced
  // off red at house 2 into a 100, wrecking the total (>= 201). The optimum instead pays 2 for
  // house 1 green, unlocking house 2 red (1) and house 3 green (1): 2 + 1 + 1 = 4. The huge gap
  // (4 vs 201) cleanly separates the DP from any first-house-greedy approach.
  @Test
  @StdIo({"3", "1 2 100", "1 100 100", "100 1 100"})
  void committingToTheCheapestColorAtHouseOneIsSuboptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Family 3: all three color indices must be handled. ---

  // Blue is cheapest (1) at houses 1 and 3; red is cheapest (1) at houses 2 and 4; everything else
  // costs 100. The optimum alternates the two cheap colors -- blue, red, blue, red -- for
  // 1 + 1 + 1 + 1 = 4. A solution that reasons over only red and green (never blue) is trapped at
  // the 100s and reports 202 instead.
  @Test
  @StdIo({"4", "100 100 1", "1 100 100", "100 100 1", "1 100 100"})
  void blueIsCheapestEverywhereButCannotRepeat(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // When every color of every house costs the same, the adjacency rule never forces a penalty
  // (three
  // colors always properly 2-color a path), so the answer is simply N * cost: 4 * 5 = 20. Catches
  // an
  // accumulator that double-counts a house or adds a phantom transition cost.
  @Test
  @StdIo({"4", "5 5 5", "5 5 5", "5 5 5", "5 5 5"})
  void uniformCostsTotalNTimesThePrice(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- Max-size inputs (N up to 1,000): the largest legal answer and an overflow guard. ---

  // Every color of all 1,000 houses costs the cap, 1,000. No choice avoids paying 1,000 per house,
  // so the answer is the heaviest possible: 1,000 * 1,000 = 1,000,000. Pins the accumulator width.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxHousesAllAtTheCostCapSumToTheLargestAnswer() throws IOException {
    int n = 1_000;
    int[][] cost = new int[n][3];
    for (int[] row : cost) {
      row[0] = 1_000;
      row[1] = 1_000;
      row[2] = 1_000;
    }
    assertThat(runMain(buildInput(cost))).isEqualTo("1000000");
  }

  // A full-size pseudo-random street cross-checked against an independently written rolling DP.
  // Random costs in [1, 1000] across 1,000 houses exercise parsing, I/O, and the recurrence at the
  // judge's scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeRandomInputMatchesTheDpOracle() throws IOException {
    int n = 1_000;
    Random rng = new Random(1149L); // fixed seed -> deterministic across JVMs.
    int[][] cost = randomCosts(n, rng);
    assertThat(runMain(buildInput(cost))).isEqualTo(Integer.toString(dpOracle(cost)));
  }

  // --- Randomized cross-check against an exhaustive oracle on small inputs: the truly independent
  // check that catches adjacency, greedy, and color-index mistakes the hand-picked cases might
  // miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rng = new Random(11490L);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rng.nextInt(7); // 2..8, small enough for the exhaustive oracle
      int[][] cost = randomCosts(n, rng);
      String expected = Integer.toString(bruteForceOracle(cost, 0, -1));
      assertThat(runMain(buildInput(cost)))
          .as("n=%d cost=%s", n, java.util.Arrays.deepToString(cost))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent exhaustive oracle: the cheapest way to paint houses {@code house..N-1} given that
   * the previous house was painted {@code prevColor} (or {@code -1} before house 0). It simply
   * tries every legal color at each house, so it is obviously correct and trustworthy only for tiny
   * inputs.
   *
   * @implNote {@code O(2^N)} time in the worst case (at most two legal colors per house) -- where
   *     {@code N} is the number of houses {@code cost.length}. Callers must keep {@code N} small
   *     (here {@code <= 8}).
   */
  private static int bruteForceOracle(int[][] cost, int house, int prevColor) {
    if (house == cost.length) {
      return 0;
    }
    int best = Integer.MAX_VALUE;
    for (int c = 0; c < 3; c++) {
      if (c == prevColor) {
        continue;
      }
      best = Math.min(best, cost[house][c] + bruteForceOracle(cost, house + 1, c));
    }
    return best;
  }

  /**
   * Independent rolling DP oracle: a separately written copy of the forward recurrence for
   * cross-checking the solution at full scale. {@code prev[c]} holds the minimum cost to paint up
   * to the previous house ending in color {@code c}; each step folds in the current house's three
   * costs. The answer is the minimum over the last house's three colors.
   *
   * @implNote {@code O(N)} time and {@code O(1)} extra space -- where {@code N} is the number of
   *     houses {@code cost.length}.
   */
  private static int dpOracle(int[][] cost) {
    int prevR = cost[0][0];
    int prevG = cost[0][1];
    int prevB = cost[0][2];
    for (int i = 1; i < cost.length; i++) {
      int curR = cost[i][0] + Math.min(prevG, prevB);
      int curG = cost[i][1] + Math.min(prevR, prevB);
      int curB = cost[i][2] + Math.min(prevR, prevG);
      prevR = curR;
      prevG = curG;
      prevB = curB;
    }
    return Math.min(prevR, Math.min(prevG, prevB));
  }

  /** A house-by-house cost table with each of the three colors drawn uniformly from [1, 1000]. */
  private static int[][] randomCosts(int n, Random rng) {
    int[][] cost = new int[n][3];
    for (int i = 0; i < n; i++) {
      cost[i][0] = 1 + rng.nextInt(1000);
      cost[i][1] = 1 + rng.nextInt(1000);
      cost[i][2] = 1 + rng.nextInt(1000);
    }
    return cost;
  }

  /** Builds BOJ 1149 input: the count {@code N} on the first line, then {@code "R G B"} rows. */
  private static String buildInput(int[][] cost) {
    StringBuilder sb = new StringBuilder(cost.length * 12 + 8);
    sb.append(cost.length).append('\n');
    for (int[] row : cost) {
      sb.append(row[0]).append(' ').append(row[1]).append(' ').append(row[2]).append('\n');
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
