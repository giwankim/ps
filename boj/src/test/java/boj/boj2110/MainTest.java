package boj.boj2110;

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
 * BOJ 2110 공유기 설치 (Install Routers) -- a <em>maximin</em> "binary search on the answer" (parametric
 * search), the maximize-the-minimum-gap sibling of 2805/1654's maximize-the-count cuts.
 *
 * <p>Do-hyeon's {@code N} houses sit at distinct integer coordinates {@code x1, ..., xN} on a
 * number line, and he wants to install {@code C} Wi-Fi routers -- at most one per house -- so that
 * the distance between the <em>closest</em> pair of routers is as large as possible. Find that
 * largest achievable minimum gap. The optimal strategy seats a router in the leftmost house and
 * then, scanning the sorted coordinates left to right, drops the next router at the first house at
 * least {@code d} away; this {@code place(d)} count is monotonically non-increasing in {@code d},
 * so the feasible gaps form a downward-closed range {@code [1, answer]} and the answer is its top
 * -- the largest {@code d} with {@code place(d) >= C}.
 *
 * <p>Input: line 1 is {@code "N C"} -- the house count {@code N} ({@code 2 <= N <= 200,000}) and
 * the router count {@code C} ({@code 2 <= C <= N}), separated by one or more spaces. The next
 * {@code N} lines each hold one coordinate {@code xi} ({@code 0 <= xi <= 1,000,000,000});
 * coordinates are distinct and arrive in arbitrary order, so the solution must sort them. Output:
 * the single maximum minimum gap. (Spec triangulated across blog mirrors of the statement while
 * acmicpc.net was unreachable; the published sample {@code 5 3 / 1 2 8 4 9 -> 3} corroborates it --
 * routers at 1, 4, 8 (or 1, 4, 9) leave a closest pair 3 apart, and no placement of three routers
 * does better.)
 *
 * <p>The fixtures pin the things a hasty parametric search gets wrong:
 *
 * <ul>
 *   <li><b>Sort first.</b> Coordinates arrive shuffled; the greedy walk is meaningless unless they
 *       are sorted ({@link #officialSampleProducesPublishedResult},
 *       {@link #shuffledInputIsSortedBeforePlacing}).
 *   <li><b>Maximize the gap, don't stop at the first feasible one.</b> Feasible gaps form
 *       {@code [1, answer]}; the answer is its top, not the first gap the search finds feasible
 *       ({@link #returnsTheMaximumFeasibleGapNotTheFirst}).
 *   <li><b>The endpoints of the placement.</b> With only two routers the closest pair is the span
 *       itself, {@code max - min} ({@link #twoRoutersSpreadToTheExtremes}); with a router for every
 *       house the closest pair is the smallest adjacent gap
 *       ({@link #routerForEveryHouseGivesSmallestAdjacentGap}).
 *   <li><b>The coordinate range is {@code [0, 1e9]}.</b> Zero is a legal coordinate
 *       ({@link #zeroIsAValidCoordinate}) and the far end reaches a billion
 *       ({@link #largeCoordinatesSpanUpToOneBillion}). Note the search ceiling is {@code max - min
 *       <= 1e9}, so {@code lo + hi} peaks just under {@code 2e9 < }{@link Integer#MAX_VALUE} --
 *       plain {@code int} midpoint arithmetic does <em>not</em> overflow here, unlike 1654 where a
 *       cable can reach {@code 2^31 - 1}.
 *   <li><b>One or more spaces split the header.</b> The {@code "N C"} line may carry extra blanks,
 *       so the parser must not assume a single space ({@link #extraSpacesInHeaderAreTolerated}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N log range)} search
 * (~18 sweeps of 200,000 houses) against an {@code O(range * N)} linear walk of the billion-wide
 * gap range, and {@link #randomizedInputsMatchBruteForceOracle} cross-checks the judged output
 * against a brute force that enumerates every {@code C}-subset and takes its true minimum gap --
 * the literal definition of the problem, sharing no greedy or binary-search logic with a judge
 * solution. The {@code Main} under test is an empty stub that reads nothing and prints nothing, so
 * every assertion here is RED until the parametric search is implemented.
 */
class MainTest {

  // --- The official sample: five houses, three routers. The end-to-end smoke test, and already
  // enough to pin two things -- that coordinates are *sorted* (they arrive 1,2,8,4,9, out of order)
  // and that the gap is *maximized* (routers at 1,4,8 give a closest pair of 3, and no triple does
  // better). ---

  @Test
  @StdIo({"5 3", "1", "2", "8", "4", "9"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Sort before placing: the same five coordinates as the sample, heavily shuffled. The greedy
  // seats its first router in the *leftmost* house and walks rightward, so a solution that scans
  // the
  // raw input order 9,4,1,8,2 -- without sorting -- counts placements against backward jumps and
  // misprints. The sorted multiset is identical to the sample, so the answer is still 3. ---

  @Test
  @StdIo({"5 3", "9", "4", "1", "8", "2"})
  void shuffledInputIsSortedBeforePlacing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Two routers spread to the extremes: with C=2 the closest (only) pair is as far apart as the
  // houses allow, so the answer is the whole span max - min. houses {1,5,9}, C=2: routers at 1 and
  // 9
  // give gap 8; one centimeter more (d=9) leaves no second house >= 9 away, so place(9)=1 < 2. This
  // pins the *upper bound* of the search -- the answer can reach max - min -- and that the interior
  // house (5) is skipped to widen the pair. ---

  @Test
  @StdIo({"3 2", "1", "5", "9"})
  void twoRoutersSpreadToTheExtremes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- A router for every house: with C=N every house is used, so the closest pair is forced to be
  // the smallest *adjacent* gap in the sorted coordinates. houses {0,5,12,20}, C=4: gaps are 5,7,8,
  // so the minimum is 5; at d=6 the 5-wide gap can no longer hold both its routers, place(6)=3 < 4.
  // This is the opposite endpoint from the C=2 case -- the answer bottoms out at the tightest
  // neighbor spacing, not the span. ---

  @Test
  @StdIo({"4 4", "0", "5", "12", "20"})
  void routerForEveryHouseGivesSmallestAdjacentGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- The crux of parametric search: the answer is the *top* of the feasible range, not the first
  // feasible gap met. houses {1,6,9,14,21}, C=3: every gap d in [1,8] is feasible (at d=8, routers
  // land at 1,9,21 -- a closest pair of 8), and d=9 is the first infeasible one (1, then 14, then
  // nothing -> place(9)=2 < 3). A search that returns the midpoint where it first sees feasibility,
  // or returns `lo` one past the boundary, misprints; only the maximum, 8, is correct. ---

  @Test
  @StdIo({"5 3", "1", "6", "9", "14", "21"})
  void returnsTheMaximumFeasibleGapNotTheFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Zero is a legal coordinate (the lower bound of the xi range). houses {0,10}, C=2: the only
  // pair spans 0..10, so the answer is 10. Pins that a coordinate of 0 is read and placed normally
  // rather than treated as missing/sentinel, and doubles as the minimal N=2 case. ---

  @Test
  @StdIo({"2 2", "0", "10"})
  void zeroIsAValidCoordinate(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- The far end of the coordinate range: a house at 1,000,000,000. houses {0,1000000000}, C=2
  // ->
  // answer 1e9, the whole span. The binary search runs over gaps in [1, 1e9], so as `lo` climbs `lo
  // + hi` approaches 2e9 -- still under Integer.MAX_VALUE (2,147,483,647), so an int midpoint is
  // safe
  // here even though 1654's can overflow. Pins the upper coordinate bound and that the wide search
  // range is handled without truncation. ---

  @Test
  @StdIo({"2 2", "0", "1000000000"})
  void largeCoordinatesSpanUpToOneBillion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000000000");
  }

  // --- The header is split on "one or more spaces": "5  3" carries two blanks between N and C. A
  // parser that splits on a single literal space (rather than a whitespace run or a tokenizer)
  // would
  // read C as an empty token and break; the answer is still the sample's 3. ---

  @Test
  @StdIo({"5  3", "1", "2", "8", "4", "9"})
  void extraSpacesInHeaderAreTolerated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Upper bounds on N: 200,000 houses at unit spacing (coordinates 0..199,999), C=1000 routers.
  // For evenly spaced points the optimum is the closed form floor((N-1)/(C-1)) -- the span N-1
  // split
  // into C-1 equal gaps -- here floor(199999/999) = 200 (routers at 0,200,...,199800 give exactly
  // 1000 placements; d=201 yields only 996). The O(N log range) search runs ~18 sweeps of 200,000
  // houses (~3.6e6 ops) where an O(range * N) walk of the billion-wide gap range would never
  // finish. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 200_000;
    int c = 1000;
    int[] coords = new int[n];
    for (int i = 0; i < n; i++) {
      coords[i] = i; // unit-spaced: coordinate i at index i
    }
    // floor((N-1)/(C-1)): the span N-1 divided into C-1 equal gaps.
    String expected = Integer.toString((n - 1) / (c - 1));
    assertThat(runMain(routerInput(coords, c))).isEqualTo(expected);
  }

  // --- Small random batches checked against the brute-force definition of the problem. N houses at
  // distinct coordinates in [0,49] arrive shuffled (also stressing the sort), C drawn in [2,N]. The
  // oracle enumerates every C-subset and takes its smallest adjacent gap, then keeps the largest
  // such gap across all subsets -- the literal maximin, sharing no greedy or binary-search logic
  // with a judge solution, so the two agree only when both are right. ---

  @Test
  void randomizedInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(2110);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(9); // N in [2, 10]
      int[] coords = distinctCoords(rnd, n, 50); // distinct coordinates in [0, 49]
      int c = 2 + rnd.nextInt(n - 1); // C in [2, N]
      String expected = Integer.toString(bruteForceMaxMinGap(coords, c));
      assertThat(runMain(routerInput(coords, c)))
          .as("coords=%s C=%d", Arrays.toString(coords), c)
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the brute-force definition of the maximum minimum gap. Enumerates every way
   * to choose {@code c} of the houses and returns the largest, over all such choices, of the chosen
   * set's smallest adjacent gap. Uses neither the greedy placement nor binary search, so it shares
   * no logic with a judge solution.
   *
   * @implNote {@code O(C * (N choose C))} time, where {@code N} is {@code coords.length} (the house
   *     count) and {@code C} is the router count -- acceptable only because the randomized fixtures
   *     keep {@code N <= 10}.
   */
  private static int bruteForceMaxMinGap(int[] coords, int c) {
    int[] sorted = coords.clone();
    Arrays.sort(sorted);
    return choose(sorted, c, 0, new int[c], 0);
  }

  /**
   * Recursively chooses {@code c} coordinates from {@code sorted[start..]} (in ascending index
   * order, so picks stay sorted) and returns the maximum, over all completions, of the picked set's
   * smallest adjacent gap.
   */
  private static int choose(int[] sorted, int c, int start, int[] picked, int count) {
    if (count == c) {
      int minGap = Integer.MAX_VALUE;
      for (int i = 1; i < c; i++) {
        minGap = Math.min(minGap, picked[i] - picked[i - 1]);
      }
      return minGap; // c >= 2 always, so at least one gap is measured
    }
    int best = -1;
    for (int i = start; i <= sorted.length - (c - count); i++) {
      picked[count] = sorted[i];
      best = Math.max(best, choose(sorted, c, i + 1, picked, count + 1));
    }
    return best;
  }

  /**
   * Draws {@code n} distinct coordinates from {@code [0, bound)} in random order via a partial
   * Fisher-Yates shuffle -- distinct (one router per house) and unsorted (to exercise the sort).
   */
  private static int[] distinctCoords(Random rnd, int n, int bound) {
    int[] pool = new int[bound];
    for (int i = 0; i < bound; i++) {
      pool[i] = i;
    }
    for (int i = bound - 1; i > 0; i--) {
      int j = rnd.nextInt(i + 1);
      int tmp = pool[i];
      pool[i] = pool[j];
      pool[j] = tmp;
    }
    return Arrays.copyOf(pool, n);
  }

  /**
   * Renders the houses as BOJ 2110 input: {@code "N C"} on line 1, then one coordinate per line.
   */
  private static String routerInput(int[] coords, int c) {
    StringBuilder sb = new StringBuilder();
    sb.append(coords.length).append(' ').append(c).append('\n');
    for (int x : coords) {
      sb.append(x).append('\n');
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
