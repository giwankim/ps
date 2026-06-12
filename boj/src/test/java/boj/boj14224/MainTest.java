package boj.boj14224;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 14224 작은 정사각형 2 (Small Square 2) -- the smallest axis-parallel, integer-vertex square that
 * strictly encloses at least {@code K} of the given points.
 *
 * <p>{@code N} points lie on the plane. Find the minimum-area square such that (1) at least
 * {@code K} points are strictly <em>inside</em> it -- a point exactly on an edge does <em>not</em>
 * count -- (2) the sides are parallel to the axes, and (3) every vertex has integer coordinates.
 * Output that minimum area.
 *
 * <p>Input: line 1 is {@code "N K"} ({@code 2 <= N <= 100}, {@code 1 <= K <= N}); the next
 * {@code N} lines each hold a point's integer coordinates {@code "x y"} in {@code [-1,000,000,000,
 * 1,000,000,000]}, all points distinct. Output: the single minimum area. (Spec read from
 * acmicpc.net, including the three worked samples below.)
 *
 * <p>The strict-boundary rule yields the characterization the fixtures lean on: for any chosen
 * subset of points with x-span {@code dx = maxX - minX} and y-span {@code dy = maxY - minY}, the
 * smallest strictly-enclosing square has side {@code max(dx, dy) + 2}. The {@code +2} is the crux
 * -- an open interval {@code (a, a+s)} with integer {@code a} holds only the {@code s-1} integers
 * {@code a+1 .. a+s-1}, so strictly covering {@code dx+1} consecutive lattice columns needs
 * {@code s >= dx + 2}. Because enlarging a subset never shrinks its span, an optimal square
 * encloses exactly {@code K} points, so the answer is {@code (min over K-subsets of max(dx, dy) +
 * 2)^2}.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Boundary points are outside.</b> One point still needs a 2-by-2 square, not a 1-by-1
 *       ({@link #singlePointNeedsTwoBySquareBecauseBoundaryExcluded}); two diagonally adjacent
 *       points need side 3, not 2 ({@link #twoDiagonallyAdjacentPointsNeedSideThree}).
 *   <li><b>It is a square, and the larger span wins.</b> Height can drive the side
 *       ({@link #officialSampleTwoPointsHeightDominatesArea}) and so can width
 *       ({@link #widthSpanCanDominateSquareSide}); a degenerate zero span is still padded
 *       ({@link #verticallyAlignedPointsStillPadTheWidth}).
 *   <li><b>Choose the best {@code K}, not all {@code N}.</b> The tightest pair beats the others
 *       ({@link #officialSampleChoosesClosestPairAmongThree}) and a tight cluster beats distant
 *       outliers ({@link #tightClusterIsPreferredOverDistantOutliers}); when {@code K == N} every
 *       point must fit ({@link #everyPointMustFitWhenKEqualsN}).
 *   <li><b>Shared coordinates and the full numeric range.</b> Points may share an axis
 *       ({@link #officialSampleGridContainsFourPointsWithSideThree}), live in the negative quadrant
 *       ({@link #officialSampleChoosesClosestPairAmongThree}), and span the {@code +/-1e9} extremes
 *       where the side and the area overflow 32 bits and demand {@code long} math
 *       ({@link #extremeCoordinateSpanRequiresLongArithmetic}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeGridIsSolvedWithoutSubsetEnumeration} pins that a correct solution is
 * polynomial: with {@code N = 100} and {@code K = 50}, enumerating {@code K}-subsets
 * ({@code C(100,50)}) would never finish, whereas the answer is found instantly. And
 * {@link #randomizedInputsMatchGeometricBruteForceOracle} cross-checks the judged output against an
 * independent oracle that slides every integer-aligned square over a small grid and counts strictly
 * enclosed points -- it never invokes the {@code max(dx,dy)+2} formula, so the two agree only when
 * both are right.
 */
class MainTest {

  // --- Official sample 1: two points (0,0) and (3,7) that both must be enclosed (K=N=2). The
  // y-span 7 dominates the x-span 3, so the side is max(3,7)+2 = 9 and the area is 81. Pins the +2
  // strict-boundary padding and that the square's side tracks the larger of the two spans. ---

  @Test
  @StdIo({"2 2", "0 0", "3 7"})
  void officialSampleTwoPointsHeightDominatesArea(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("81");
  }

  // --- Official sample 2: three points, K=2, all with negative coordinates in play. The three
  // candidate pairs give sides 9, 7 and 4; the closest pair (3,-1)&(1,-2) (dx=2, dy=1 -> side 4)
  // wins with area 16. Pins choosing the best K-subset rather than enclosing everything, and that
  // negative coordinates are handled. ---

  @Test
  @StdIo({"3 2", "-4 3", "3 -1", "1 -2"})
  void officialSampleChoosesClosestPairAmongThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Official sample 3: a 3x2 lattice (x in {0,1,2}, y in {0,1}), K=4. A side-3 square holds a
  // 2x2 block of lattice points (an open interval of length 3 admits exactly two integers), i.e.
  // four points -> area 9; a side-2 square admits only one integer per axis, so at most one point.
  // Pins shared coordinates and the strict-boundary counting that makes side 3 the minimum. ---

  @Test
  @StdIo({"6 4", "0 0", "0 1", "1 0", "1 1", "2 0", "2 1"})
  void officialSampleGridContainsFourPointsWithSideThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- K=1: a single point still cannot sit on the boundary, so the smallest enclosing square is
  // 2-by-2 (the open square (p-1, p+1) on each axis), area 4 -- never the degenerate 1 that a
  // solution ignoring the strict-boundary rule would print. The far second point is irrelevant
  // since one point already satisfies K. ---

  @Test
  @StdIo({"2 1", "5 5", "100 100"})
  void singlePointNeedsTwoBySquareBecauseBoundaryExcluded(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The transpose of sample 1: now the x-span 5 dominates the y-span 2, so the side is
  // max(5,2)+2 = 7 and the area is 49. Pins that width drives the side symmetrically to height -- a
  // solution that only ever looks at the y-span would miss this. ---

  @Test
  @StdIo({"2 2", "0 0", "5 2"})
  void widthSpanCanDominateSquareSide(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("49");
  }

  // --- K=N=3 forces all three points (0,0),(1,0),(0,1) inside. Both spans are 1, so the side is
  // max(1,1)+2 = 3 and the area is 9. Pins that when K equals N nothing may be dropped, and that a
  // unit span needs side 3 (two distinct integers cannot both be strict-interior to a length-2
  // interval). ---

  @Test
  @StdIo({"3 3", "0 0", "1 0", "0 1"})
  void everyPointMustFitWhenKEqualsN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- Three collinear points sharing x=0 (K=N=3): the x-span is 0 but the y-span is 5, so the
  // side is max(0,5)+2 = 7 and the area is 49. Pins that a zero span is still padded to width 2 in
  // that axis while the other axis drives the (square) side -- and that the answer is a true
  // square,
  // not a 2-by-7 rectangle hugging the points. ---

  @Test
  @StdIo({"3 3", "0 0", "0 5", "0 2"})
  void verticallyAlignedPointsStillPadTheWidth(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("49");
  }

  // --- Five points, K=3: three of them cluster near the origin ((0,0),(1,1),(2,0): dx=2, dy=1 ->
  // side 4, area 16) while two sit far away. Any K-subset that pulls in a distant point explodes
  // the
  // span, so the optimum is the tight cluster. Pins that outliers are excluded and the densest
  // K-subset is selected. ---

  @Test
  @StdIo({"5 3", "0 0", "1 1", "2 0", "100 100", "200 200"})
  void tightClusterIsPreferredOverDistantOutliers(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Two points one lattice step apart on a diagonal, K=2: dx=dy=1, so the side is 3 (a length-2
  // interval cannot strict-enclose two integers 1 apart) and the area is 9. The minimal non-trivial
  // strict-boundary pin; a solution off-by-one on the padding would print 4. ---

  @Test
  @StdIo({"2 2", "0 0", "1 1"})
  void twoDiagonallyAdjacentPointsNeedSideThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- The coordinate extremes, K=2: (-1e9, 0) and (1e9, 0). The x-span is 2,000,000,000 (the
  // y-span 0), so the side is 2,000,000,002 -- already past Integer.MAX_VALUE -- and the area is
  // 2,000,000,002^2 = 4,000,000,008,000,000,004. Pins that both the side and its square are
  // computed
  // and printed as long; a 32-bit int would overflow and corrupt the answer. ---

  @Test
  @StdIo({"2 2", "-1000000000 0", "1000000000 0"})
  void extremeCoordinateSpanRequiresLongArithmetic(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4000000008000000004");
  }

  // --- Upper bound on N: a full 10x10 lattice (100 distinct points), K=50. A side-s square
  // encloses
  // an (s-1)x(s-1) block of lattice points; (7)^2 = 49 < 50 but (8)^2 = 64 >= 50, so the minimum
  // side is 9 and the area is 81. A subset-enumeration solution would weigh C(100,50) ~ 1e29
  // subsets
  // and time out; a polynomial search returns at once. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeGridIsSolvedWithoutSubsetEnumeration() throws IOException {
    int side = 10;
    int[][] points = new int[side * side][2];
    int idx = 0;
    for (int x = 0; x < side; x++) {
      for (int y = 0; y < side; y++) {
        points[idx++] = new int[] {x, y};
      }
    }
    // (s-1)^2 must reach 50: 7^2=49 falls short, 8^2=64 clears it -> side 9, area 81.
    assertThat(runMain(squareInput(50, points))).isEqualTo("81");
  }

  // --- Small random point sets on a tight grid with K in [1, N], cross-checked against an
  // independent oracle that slides every integer-aligned square and counts strictly enclosed
  // points.
  // The oracle shares no logic with a max(dx,dy)+2 derivation, so the two agree only when both are
  // right; this is where strict-boundary, K-subset and overflow-free bugs surface in bulk. ---

  @Test
  void randomizedInputsMatchGeometricBruteForceOracle() throws IOException {
    Random rnd = new Random(14224);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(7); // N in [2, 8]
      int k = 1 + rnd.nextInt(n); // K in [1, n]
      int[][] points = distinctPoints(rnd, n, 6); // n distinct points on a 6x6 grid, x,y in [-3, 2]
      String expected = Long.toString(bruteForceMinArea(points, k));
      assertThat(runMain(squareInput(k, points)))
          .as("k=%d points=%s", k, Arrays.deepToString(points))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the minimum area of an axis-parallel, integer-vertex square that strictly
   * encloses at least {@code k} of the points. It tries every side length from 2 upward and, for
   * each, slides the square over every integer placement that can overlap the points, counting
   * strictly interior points directly from the definition -- no {@code max(dx,dy)+2} shortcut.
   *
   * @implNote {@code O(S * R^2 * N)} time, where {@code S} is the number of candidate side lengths
   *     tried, {@code R} is the width of the integer placement range scanned per axis, and
   *     {@code N} is {@code points.length} -- acceptable only because the randomized fixtures keep
   *     the grid tiny.
   */
  private static long bruteForceMinArea(int[][] points, int k) {
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int[] p : points) {
      minX = Math.min(minX, p[0]);
      maxX = Math.max(maxX, p[0]);
      minY = Math.min(minY, p[1]);
      maxY = Math.max(maxY, p[1]);
    }
    // A square of this side, placed to straddle the whole bounding box, encloses all N >= k points.
    int maxSide = Math.max(maxX - minX, maxY - minY) + 2;
    for (int s = 2; s <= maxSide; s++) {
      if (enclosesAtLeastK(points, k, s, minX, maxX, minY, maxY)) {
        return (long) s * s;
      }
    }
    return (long) maxSide * maxSide; // unreachable: maxSide always encloses everything
  }

  /**
   * True iff some integer placement of a side-{@code s} open square strictly encloses at least
   * {@code k} of the points. The left edge {@code a} only matters within {@code [minX - s, maxX]}
   * (outside that the square cannot overlap the points), and likewise the bottom edge {@code b}.
   */
  private static boolean enclosesAtLeastK(
      int[][] points, int k, int s, int minX, int maxX, int minY, int maxY) {
    for (int a = minX - s; a <= maxX; a++) {
      for (int b = minY - s; b <= maxY; b++) {
        int count = 0;
        for (int[] p : points) {
          if (a < p[0] && p[0] < a + s && b < p[1] && p[1] < b + s) {
            count++;
          }
        }
        if (count >= k) {
          return true;
        }
      }
    }
    return false;
  }

  /** Samples {@code n} distinct points from a {@code span x span} grid centered on the origin. */
  private static int[][] distinctPoints(Random rnd, int n, int span) {
    Set<Long> seen = new HashSet<>();
    int[][] points = new int[n][2];
    int filled = 0;
    int half = span / 2;
    while (filled < n) {
      int x = rnd.nextInt(span) - half;
      int y = rnd.nextInt(span) - half;
      if (seen.add((long) x * 1000 + y)) {
        points[filled][0] = x;
        points[filled][1] = y;
        filled++;
      }
    }
    return points;
  }

  /** Renders BOJ 14224 input: {@code "N K"} on line 1, then one point {@code "x y"} per line. */
  private static String squareInput(int k, int[][] points) {
    StringBuilder sb = new StringBuilder();
    sb.append(points.length).append(' ').append(k).append('\n');
    for (int[] p : points) {
      sb.append(p[0]).append(' ').append(p[1]).append('\n');
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
