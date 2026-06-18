package boj.boj12002;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 12002 Field Reduction (USACO 2016 US Open, Silver) -- remove at most three cows so the
 * axis-aligned bounding box of the survivors is as small as possible.
 *
 * <p>Input: line 1 holds N ({@code 5 <= N <= 50,000}); each of the next N lines holds a cow's two
 * integer coordinates {@code x y}, every coordinate in {@code 1 .. 40,000} and every cow at a
 * distinct position. Output: a single integer, the minimum area of a rectangle with sides parallel
 * to the axes that encloses all but at most three of the cows (cows on the boundary count as
 * enclosed). The area of a point set is {@code (maxX - minX) * (maxY - minY)}, so a set whose
 * survivors share an x (or a y) has area zero.
 *
 * <p>Removing a cow can only shrink a bounding box (a subset's box is contained in its superset's),
 * so the optimum always spends the full budget -- removing fewer than three never helps -- and each
 * surviving wall must be one of the four most-extreme coordinates on its side. That monotonicity is
 * what lets a correct solution try only the handful of extreme candidates instead of all
 * {@code C(N, 3)} removals, and it is also what lets the oracle below enumerate removals of exactly
 * three (never fewer) and still be exact.
 *
 * <p>The fixtures pin the behaviors that distinguish a correct solver from a plausible-but-wrong
 * one: the official sample ({@link #officialSampleRemovesThreeOutliersForTwelve}); the lower bound
 * N = 5 where only two cows survive, so the answer is the closest-pair rectangle
 * ({@link #minimumHerdOfFiveLeavesTheClosestPairRectangle}); degenerate zero-area boxes when the
 * survivors are collinear ({@link #collinearSurvivorsCollapseTheBoxToZeroArea}); the ordinary case
 * of trimming scattered outliers off a dense cluster
 * ({@link #typicalDenseClusterRemovesThreeScatteredOutliers},
 * {@link #oneFarOutlierIsRemovedThenTheClusterIsTrimmed}); the four-extremes trap where only three
 * of four outlying walls can be removed so one must survive
 * ({@link #fourExtremesButOnlyThreeRemovalsSoOneMustSurvive}); and redundant walls held by four
 * cows each, which removing three cannot breach -- doubling as an overflow guard near the maximum
 * area ({@link #redundantWallsMeanRemovalCannotShrinkTheFullSpan}).
 *
 * <p>The randomized cases drive distinct point sets through stdin/stdout and cross-check the
 * program against an independent brute-force oracle that tries every {@code C(N, 3)} removal -- a
 * deliberately different route to the answer than the extreme-candidate pruning a real solver uses.
 * Because that oracle cannot scale to N = 50,000, the largest instances are built with a
 * constructively known answer: walls of four cows pin each side and at most three strict outliers
 * must all be removed, so the answer is provably the cluster box. The same builder is checked
 * against the brute oracle at small scale ({@link #constructiveBuilderMatchesBruteForceOracle})
 * before being trusted at full scale.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"6", "1 1", "7 8", "10 9", "8 12", "4 100", "50 7"})
  void officialSampleRemovesThreeOutliersForTwelve(StdOut out) throws IOException {
    Main.main(new String[0]);
    // (1,1), (4,100) and (50,7) are the outliers on the low corner, the top and the right; removing
    // all three leaves the cluster (7,8), (10,9), (8,12) spanning x in [7,10] and y in [8,12], for
    // an area of 3 * 4 = 12.
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Lower bound N = 5: removing three leaves exactly two cows, so the answer is the smallest
  // rectangle over all pairs -- the closest-pair box. ---

  @Test
  @StdIo({"5", "10 10", "12 11", "1 40", "40 1", "30 25"})
  void minimumHerdOfFiveLeavesTheClosestPairRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every x and every y is distinct, so no pair collapses to zero. With only two survivors
    // allowed, the tightest pair (10,10) and (12,11) wins at (12-10) * (11-10) = 2.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Degenerate boxes: when the survivors line up, a side has zero length and the area is zero.
  // ---

  @Test
  @StdIo({"5", "3 1", "3 2", "3 3", "1 50", "50 1"})
  void collinearSurvivorsCollapseTheBoxToZeroArea(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Drop the two far cows (1,50) and (50,1) and the three survivors all sit on the vertical line
    // x = 3, so maxX - minX = 0 and the enclosed area is 0 -- the smallest area possible.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The ordinary shape: a dense cluster plus a few far outliers that the budget removes. ---

  @Test
  @StdIo({
    "12",
    "10 10",
    "10 11",
    "10 12",
    "11 10",
    "11 11",
    "11 12",
    "12 10",
    "12 11",
    "12 12",
    "1 1",
    "40000 40000",
    "20000 1"
  })
  void typicalDenseClusterRemovesThreeScatteredOutliers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The 3x3 block fills x,y in [10,12]; the three outliers each lie far outside it. Spending the
    // whole budget on the outliers leaves the block, area (12-10) * (12-10) = 4. Removing a block
    // wall instead would strand an outlier and blow the box up, so 4 is optimal.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"6", "10 10", "11 11", "12 10", "11 12", "13 11", "9000 9000"})
  void oneFarOutlierIsRemovedThenTheClusterIsTrimmed(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Only (9000,9000) is truly remote; removing it is mandatory. The budget then has two removals
    // to spare, and trimming two of the cluster's own boundary cows shrinks it to a best three-cow
    // box of area 2 -- illustrating that spare removals are spent, never wasted.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The four-extremes trap: four outlying walls but a budget of three, so exactly one survives
  // and the solver must keep the cheapest one. ---

  @Test
  @StdIo({"8", "50 50", "51 51", "50 51", "51 50", "1 50", "40000 50", "50 1", "50 40000"})
  void fourExtremesButOnlyThreeRemovalsSoOneMustSurvive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A tight 2x2 core at [50,51] is surrounded by one outlier on each side. Three removals cannot
    // clear all four, so one wall stays. Keeping the cheap left wall (1,50) -- or equally the
    // bottom
    // (50,1) -- yields (51-1) * (51-50) = 50, while keeping either far wall at 40000 would cost
    // thousands. A solver that blindly drops the three most distant cows keeps a 40000 wall and is
    // wrong here.
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }

  // --- Redundant walls: four cows hold each extreme, so removing three breaches none of them and
  // the answer is the full bounding box. Coordinates reach 40000 to guard the near-maximum area.
  // ---

  @Test
  @StdIo({
    "16",
    "1 10",
    "1 11",
    "1 12",
    "1 13",
    "40000 10",
    "40000 11",
    "40000 12",
    "40000 13",
    "10 1",
    "11 1",
    "12 1",
    "13 1",
    "10 40000",
    "11 40000",
    "12 40000",
    "13 40000"
  })
  void redundantWallsMeanRemovalCannotShrinkTheFullSpan(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each of the four walls (x = 1, x = 40000, y = 1, y = 40000) carries four cows, so any three
    // removals leave every wall standing. The answer is the untouched span (40000-1) * (40000-1) =
    // 1599920001 -- a value that overflows nothing only if the area is computed in a wide enough
    // type.
    assertThat(out.capturedString().trim()).isEqualTo("1599920001");
  }

  // --- Randomized point sets, cross-checked against an independent C(N, 3) brute-force oracle. ---

  @Test
  void randomSmallHerdWideFieldMatchesBruteForceOracle() throws IOException {
    // A handful of cows scattered across the full field: coordinates rarely collide, so the answer
    // is usually a positive area and the run exercises the ordinary "remove three extremes" path.
    List<int[]> cows = randomDistinctPoints(9, 40000, new Random(12002L));
    assertThat(runMain(buildInput(cows))).isEqualTo(solve(cows));
  }

  @Test
  void randomTinyFieldStressesDegenerateBoxes() throws IOException {
    // Twelve cows squeezed into a 6x6 field force many shared rows and columns, so survivors often
    // line up and the answer collapses toward zero -- the degenerate boxes the wide-field case
    // rarely produces.
    List<int[]> cows = randomDistinctPoints(12, 6, new Random(120020L));
    assertThat(runMain(buildInput(cows))).isEqualTo(solve(cows));
  }

  @Test
  void randomMidHerdMatchesBruteForceOracle() throws IOException {
    // A larger herd over a mid-size field varies the shape away from both extremes; C(22, 3) = 1540
    // removals keep the brute oracle fast while still covering every removal the solver might pick.
    List<int[]> cows = randomDistinctPoints(22, 200, new Random(120021L));
    assertThat(runMain(buildInput(cows))).isEqualTo(solve(cows));
  }

  // --- Constructive instances with a known answer, so correctness can be checked past the reach of
  // the brute oracle. Walls of four cows pin each side and strict outliers must all be removed, so
  // the answer is provably the cluster box. ---

  @Test
  void constructiveBuilderMatchesBruteForceOracle() throws IOException {
    // Validate the builder at a scale the brute oracle still reaches: its answer must equal both
    // the
    // brute-force result and the cluster box (9-2) * (9-2) = 49. This pins the construction before
    // it is trusted at N = 50,000 below.
    int[][] outliers = {{1, 5}, {10, 5}, {5, 1}};
    List<int[]> cows = buildProtectedCluster(2, 9, 2, 9, 3, outliers, new Random(120022L));
    String expected = Long.toString(clusterArea(2, 9, 2, 9));
    assertThat(solve(cows)).isEqualTo(expected);
    assertThat(runMain(buildInput(cows))).isEqualTo(expected);
  }

  @Test
  void fullScaleHerdRemovesItsThreeOutliersForTheClusterBox() throws IOException {
    // The maximum herd N = 50,000 across the full field. Three strict outliers sit just past the
    // four walls; removing them is forced and leaves the cluster box (39998-2) * (39998-2) =
    // 1599680016. A solution slower than the extreme-candidate pruning would time out at this size.
    int[][] outliers = {{1, 20000}, {39999, 20000}, {20000, 1}};
    // 50000 cows = 16 wall cows + 3 outliers + 49981 interior filler.
    List<int[]> cows =
        buildProtectedCluster(2, 39998, 2, 39998, 49981, outliers, new Random(1200250L));
    assertThat(cows).hasSize(50000);
    assertThat(runMain(buildInput(cows))).isEqualTo(Long.toString(clusterArea(2, 39998, 2, 39998)));
  }

  @Test
  void fullScaleHerdWithoutOutliersStillReportsTheClusterBox() throws IOException {
    // No outliers at all, so the three removals are wasted: every wall keeps four cows and the box
    // never shrinks. The answer is the full cluster box (20000-5) * (20000-5) = 399800025,
    // confirming the monotonicity rule -- spare removals never enlarge the answer -- holds at
    // scale.
    // 12345 cows = 16 wall cows + 0 outliers + 12329 interior filler.
    List<int[]> cows =
        buildProtectedCluster(5, 20000, 5, 20000, 12329, new int[0][], new Random(1200260L));
    assertThat(cows).hasSize(12345);
    assertThat(runMain(buildInput(cows))).isEqualTo(Long.toString(clusterArea(5, 20000, 5, 20000)));
  }

  /**
   * Independent oracle: brute force over every {@code C(N, 3)} choice of three cows to remove,
   * returning the smallest enclosing-box area of the survivors as a decimal string. Removing a cow
   * can only shrink a box, so removing exactly three is always at least as good as removing fewer
   * -- no smaller removal count need be tried. Runs in {@code O(N^4)} time, where N is the number
   * of cows, so it is for small N only.
   */
  private static String solve(List<int[]> cows) {
    int n = cows.size();
    long best = Long.MAX_VALUE;
    for (int a = 0; a < n; a++) {
      for (int b = a + 1; b < n; b++) {
        for (int c = b + 1; c < n; c++) {
          int minX = Integer.MAX_VALUE;
          int maxX = Integer.MIN_VALUE;
          int minY = Integer.MAX_VALUE;
          int maxY = Integer.MIN_VALUE;
          for (int i = 0; i < n; i++) {
            if (i == a || i == b || i == c) {
              continue;
            }
            int[] cow = cows.get(i);
            minX = Math.min(minX, cow[0]);
            maxX = Math.max(maxX, cow[0]);
            minY = Math.min(minY, cow[1]);
            maxY = Math.max(maxY, cow[1]);
          }
          best = Math.min(best, (long) (maxX - minX) * (maxY - minY));
        }
      }
    }
    return Long.toString(best);
  }

  /** Builds BOJ 12002 input: a line holding N then one {@code "x y"} line per cow. */
  private static String buildInput(List<int[]> cows) {
    StringBuilder sb = new StringBuilder();
    sb.append(cows.size()).append('\n');
    for (int[] cow : cows) {
      sb.append(cow[0]).append(' ').append(cow[1]).append('\n');
    }
    return sb.toString();
  }

  /** {@code n} cows at distinct positions, each coordinate uniform in {@code 1 .. maxCoord}. */
  private static List<int[]> randomDistinctPoints(int n, int maxCoord, Random rng) {
    Set<Long> used = new HashSet<>();
    List<int[]> cows = new ArrayList<>(n);
    while (cows.size() < n) {
      int x = rng.nextInt(maxCoord) + 1;
      int y = rng.nextInt(maxCoord) + 1;
      if (used.add(key(x, y))) {
        cows.add(new int[] {x, y});
      }
    }
    return cows;
  }

  /**
   * A cluster whose minimum-area answer is known by construction: four cows hold each wall of the
   * box {@code [x0, x1] x [y0, y1]} (so removing three breaches none), {@code fillerCount} more
   * cows sit strictly inside (so they never move a wall), and the given {@code outliers} -- at most
   * three cows strictly outside -- must all be removed. The answer is therefore exactly the cluster
   * box area. Cows are returned shuffled-by-insertion so the solver cannot lean on input order.
   */
  private static List<int[]> buildProtectedCluster(
      int x0, int x1, int y0, int y1, int fillerCount, int[][] outliers, Random rng) {
    Set<Long> used = new HashSet<>();
    List<int[]> cows = new ArrayList<>();
    for (int d = 1; d <= 4; d++) {
      addCow(cows, used, x0, y0 + d); // x = x0 wall (minX)
      addCow(cows, used, x1, y0 + d); // x = x1 wall (maxX)
      addCow(cows, used, x0 + d, y0); // y = y0 wall (minY)
      addCow(cows, used, x0 + d, y1); // y = y1 wall (maxY)
    }
    for (int[] outlier : outliers) {
      addCow(cows, used, outlier[0], outlier[1]);
    }
    int placed = 0;
    while (placed < fillerCount) {
      int x = x0 + 1 + rng.nextInt(x1 - x0 - 1); // strictly inside [x0+1, x1-1]
      int y = y0 + 1 + rng.nextInt(y1 - y0 - 1);
      if (addCow(cows, used, x, y)) {
        placed++;
      }
    }
    return cows;
  }

  private static long clusterArea(int x0, int x1, int y0, int y1) {
    return (long) (x1 - x0) * (y1 - y0);
  }

  private static boolean addCow(List<int[]> cows, Set<Long> used, int x, int y) {
    if (used.add(key(x, y))) {
      cows.add(new int[] {x, y});
      return true;
    }
    return false;
  }

  private static long key(int x, int y) {
    return (long) x * 100_000 + y;
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
