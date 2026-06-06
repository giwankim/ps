package boj.boj2805;

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
 * BOJ 2805 나무 자르기 (Cutting Trees) -- the textbook parametric binary search ("binary search on the
 * answer").
 *
 * <p>Lumberjack Sang-geun must bring home at least {@code M} metres of wood. His machine is set to
 * a single integer height {@code H}: it lowers a sawblade to {@code H} metres and shears off the
 * part of every tree taller than {@code H}, leaving shorter trees untouched. A tree of height
 * {@code h} therefore yields {@code max(0, h - H)} metres. Being ecologically minded, Sang-geun
 * wants the blade as <em>high</em> as possible while still collecting at least {@code M} metres in
 * total -- find that maximum integer {@code H}.
 *
 * <p>Input: line 1 is {@code "N M"} -- the tree count {@code N} ({@code 1 <= N <= 1,000,000}) and
 * the required wood {@code M} ({@code 1 <= M <= 2,000,000,000}). Line 2 is the {@code N} tree
 * heights, space-separated, each a positive integer less than {@code 1,000,000,000}. The sum of all
 * heights is guaranteed {@code >= M}, so a feasible blade height always exists (at worst {@code H =
 * 0}, which takes the entire forest). Output: the single maximum blade height. (Spec triangulated
 * across GitHub accepted solutions and blog mirrors of the statement while acmicpc.net was
 * unreachable; the published samples {@code 4 7 / 20 15 10 17 -> 15} and {@code 5 20 / 4 42 40 26
 * 46 -> 36} corroborate it.)
 *
 * <p>The fixtures pin the things a hasty parametric search gets wrong:
 *
 * <ul>
 *   <li><b>Maximise the blade, don't stop at the first feasible height.</b> Feasible heights form a
 *       downward-closed range {@code [0, answer]}; the answer is its top, not the first {@code H}
 *       the search happens to find feasible ({@link #returnsTheMaximumFeasibleHeightNotTheFirst}).
 *   <li><b>Clamp each tree at zero.</b> Trees no taller than the blade yield nothing, not a
 *       negative; a solution that subtracts on every tree drives the total down and misjudges
 *       feasibility ({@link #treesShorterThanTheBladeContributeZeroNotNegative}).
 *   <li><b>The blade may sit at {@code 0} or at a tree's exact height.</b> When {@code M} equals
 *       the whole forest the answer is {@code 0} ({@link #targetEqualToTotalForcesBladeToZero});
 *       when the answer coincides with a tree height that tree simply yields {@code 0}
 *       ({@link #answerCoincidingWithATreeHeightYieldsZeroFromThatTree}). With {@code M} tiny the
 *       blade rises to one below the tallest tree
 *       ({@link #tinyTargetRaisesBladeToJustBelowTheTallestTree}).
 *   <li><b>The wood total needs 64 bits.</b> {@code N} trees near {@code 1e9} sum to ~{@code 1e15}
 *       at a low blade, overflowing a 32-bit accumulator and corrupting the feasibility test
 *       ({@link #largeWoodTotalsRequireLongAccumulator}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N log H)} search (~30
 * feasibility sweeps of a million trees) against an {@code O(N * H)} linear walk of the
 * billion-tall height range, and {@link #randomizedInputsMatchLinearScanOracle} cross-checks the
 * judged output against an obviously-correct top-down linear scan over candidate heights. The
 * {@code Main} under test is an empty stub that reads nothing and prints nothing, so every
 * assertion here is RED until the parametric search is implemented.
 */
class MainTest {

  // --- The official samples from the statement: the end-to-end smoke tests, and already enough to
  // pin that the blade is *maximised* -- both have lower feasible heights the search must climb
  // past
  // to reach the published answer. ---

  // 4 7 / 20 15 10 17: at H=15 the cuts are 5, 0, 0, 2 -> 7 >= 7; at H=16 they fall to 5 < 7.
  @Test
  @StdIo({"4 7", "20 15 10 17"})
  void firstOfficialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // 5 20 / 4 42 40 26 46: at H=36 the cuts are 0, 6, 4, 0, 10 -> 20 >= 20; at H=37 they fall to 17.
  @Test
  @StdIo({"5 20", "4 42 40 26 46"})
  void secondOfficialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("36");
  }

  // --- Smallest input (N = 1): the lone tree yields exactly height - H, so the answer is height -
  // M
  // when that is non-negative. height 10, M 5 -> blade at 5 yields 5 >= 5; at 6 it yields 4 < 5.
  // ---

  @Test
  @StdIo({"1 5", "10"})
  void singleTreeYieldsHeightMinusTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- The crux of parametric search: the answer is the *top* of the feasible range, not the first
  // feasible height met. trees {3,5,7}, M 4: heights 0..4 are all feasible (e.g. H=3 gives 2+4=6),
  // and the answer is the maximum of them, 4 (cuts 1+3=4); H=5 gives only 2 < 4. A search that
  // returns the midpoint where it first sees feasibility -- or returns `lo` past the boundary --
  // misprints. ---

  @Test
  @StdIo({"3 4", "3 5 7"})
  void returnsTheMaximumFeasibleHeightNotTheFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Each tree contributes max(0, h - H), never a negative: trees shorter than the blade yield
  // nothing. trees {1,2,30,25}, M 10: only the two tall trees pay out. At H=22 the cuts are 8 and 3
  // -> 11 >= 10; at H=23 they are 7 and 2 -> 9 < 10, so the answer is 22. A solution that subtracts
  // on every tree would see (1-22)+(2-22) drag the total to -31 and wrongly reject feasible blades.
  // ---

  @Test
  @StdIo({"4 10", "1 2 30 25"})
  void treesShorterThanTheBladeContributeZeroNotNegative(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("22");
  }

  // --- Lower boundary of the search space: when M equals the entire forest, only a blade of 0
  // takes
  // it all. trees {1,2,3} sum to 6 = M: H=0 yields 6 >= 6; H=1 yields 0+1+2 = 3 < 6. Pins that the
  // answer can be 0 and the search does not stop short at 1 (or undershoot to -1). ---

  @Test
  @StdIo({"3 6", "1 2 3"})
  void targetEqualToTotalForcesBladeToZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The answer may land exactly on a tree's height, in which case that tree yields 0 and that
  // is
  // fine (the cut is "strictly taller than H"). trees {10,10,20}, M 10: at H=10 the two 10s yield 0
  // and the 20 yields 10 -> 10 >= 10; at H=11 the 20 yields 9 < 10. Answer 10 coincides with two
  // tree heights. ---

  @Test
  @StdIo({"3 10", "10 10 20"})
  void answerCoincidingWithATreeHeightYieldsZeroFromThatTree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Upper boundary of the search space (rt = tallest tree): a tiny target lets the blade rise
  // to
  // one below the tallest tree. trees {5,8,8}, M 1: at H=7 the two 8s each yield 1 -> 2 >= 1; at
  // H=8
  // every tree yields 0 < 1. Answer 7 = maxHeight - 1, pinning that the search's high end reaches
  // the tallest tree and the answer is never the tallest height itself when M > 0. ---

  @Test
  @StdIo({"3 1", "5 8 8"})
  void tinyTargetRaisesBladeToJustBelowTheTallestTree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Uniform forest: identical heights split evenly. trees {4,4,4}, M 6: at H=2 each yields 2 ->
  // 6 >= 6; at H=3 each yields 1 -> 3 < 6. Answer 2. A clean check that the feasibility total
  // scales
  // with the tree count. ---

  @Test
  @StdIo({"3 6", "4 4 4"})
  void uniformHeightsSplitEvenly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The 64-bit pin: three trees of height 999,999,999 sum to ~3e9 of wood at a low blade, past
  // the 2,147,483,647 ceiling of a signed 32-bit int. M = 1,500,000,000 needs each tree to give
  // 500,000,000, so the answer is 999,999,999 - 500,000,000 = 499,999,999 (at H = 500,000,000 the
  // total is 1,499,999,997 < M). An int accumulator overflows to a negative total when the search
  // probes low blades and corrupts the monotonic predicate -- so this fails unless the wood sum is
  // a
  // long. ---

  @Test
  @StdIo({"3 1500000000", "999999999 999999999 999999999"})
  void largeWoodTotalsRequireLongAccumulator(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("499999999");
  }

  // --- Upper bound on N: a million trees, each 999,999,999 tall, with M = 2,000,000,000 (the
  // maximum). With a uniform forest the closed form is wood(H) = N * (height - H); feasibility
  // wood(H) >= M reduces to height - H >= M / N = 2000, so the largest feasible blade is height -
  // 2000 = 999,997,999. The O(N log H) search runs ~30 sweeps of a million trees (~3e7 ops) where
  // an
  // O(N * H) walk of the billion-tall range would not finish; it also re-exercises the long
  // accumulator, since a low blade sums ~1e15 of wood across the forest. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 1_000_000;
    int height = 999_999_999;
    long m = 2_000_000_000L; // == n * 2000
    int[] heights = new int[n];
    Arrays.fill(heights, height);
    // wood(H) = n * (height - H) >= m  <=>  height - H >= m / n = 2000  =>  Hmax = height - 2000.
    assertThat(runMain(cutTreesInput(heights, m))).isEqualTo("999997999");
  }

  // --- Small random forests with narrow heights -- so feasible ranges are short and boundary
  // off-by-ones (return lo vs hi, `<` vs `<=` in the predicate) surface -- checked against an
  // independent top-down linear scan. The scan shares no binary-search logic with a judge solution,
  // so the two agree only when both are right. ---

  @Test
  void randomizedInputsMatchLinearScanOracle() throws IOException {
    Random rnd = new Random(2805);
    for (int trial = 0; trial < 1000; trial++) {
      int n = 1 + rnd.nextInt(8);
      int[] heights = new int[n];
      long total = 0;
      for (int i = 0; i < n; i++) {
        heights[i] = 1 + rnd.nextInt(20); // heights in [1, 20]
        total += heights[i];
      }
      long m = 1 + rnd.nextInt((int) total); // M in [1, total], honouring sum-of-heights >= M
      String expected = Long.toString(maxBladeHeight(heights, m));
      assertThat(runMain(cutTreesInput(heights, m)))
          .as("heights=%s M=%d", Arrays.toString(heights), m)
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the obviously-correct top-down linear scan. Walk candidate blade heights
   * from the tallest tree down to {@code 0} and return the first that cuts at least {@code m}
   * metres -- which, because feasibility is monotonic in the blade height, is the maximum feasible
   * height. Uses no binary search, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(maxHeight * N)} time, where {@code N} is {@code heights.length} and
   *     {@code maxHeight} is the tallest tree -- acceptable only because the randomized fixtures
   *     keep heights small.
   */
  private static long maxBladeHeight(int[] heights, long m) {
    int maxHeight = 0;
    for (int h : heights) {
      maxHeight = Math.max(maxHeight, h);
    }
    for (int blade = maxHeight; blade >= 0; blade--) {
      long wood = 0;
      for (int h : heights) {
        if (h > blade) {
          wood += h - blade;
        }
      }
      if (wood >= m) {
        return blade;
      }
    }
    return 0; // unreachable: blade 0 takes the whole forest, which is >= m by the problem guarantee
  }

  /** Renders the forest as BOJ 2805 input: {@code "N M"} on line 1, the heights on line 2. */
  private static String cutTreesInput(int[] heights, long m) {
    StringBuilder sb = new StringBuilder();
    sb.append(heights.length).append(' ').append(m).append('\n');
    for (int i = 0; i < heights.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(heights[i]);
    }
    sb.append('\n');
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
