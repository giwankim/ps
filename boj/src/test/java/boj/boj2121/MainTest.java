package boj.boj2121;

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
 * BOJ 2121 넷이 놀기 (Four Playing Together) -- counting axis-parallel rectangles of a fixed size by
 * hashing every point and probing for its three partner corners.
 *
 * <p>Four players each pick one of {@code N} distinct lattice points so that the four chosen points
 * are the corners of an axis-parallel rectangle whose width is exactly {@code A} and whose height
 * is exactly {@code B}. Count how many such rectangles the point set admits. The clean
 * characterisation: a rectangle is uniquely identified by its bottom-left corner {@code (x, y)}, so
 * the answer is the number of points {@code (x, y)} for which {@code (x+A, y)}, {@code (x, y+B)}
 * and {@code (x+A, y+B)} are all present -- one membership sweep, anchoring each rectangle once at
 * its lowest-left point.
 *
 * <p>Input: line 1 is {@code N} ({@code 5 <= N <= 500,000}); line 2 is {@code "A B"} -- the width
 * {@code A} and height {@code B} ({@code 1 <= A, B <= 1,000}); the next {@code N} lines each hold a
 * point's integer coordinates {@code "x y"} in {@code [-1,000,000,000, 1,000,000,000]}, all points
 * distinct. Output: the single count, guaranteed {@code <= 2^31 - 1}. (Spec triangulated from a
 * blog mirror of the statement -- including the worked example {@code A(0,0) B(2,0) C(0,3) D(2,3)
 * E(4,0) F(4,3)} with {@code A=2, B=3} yielding the two rectangles {@code (A,B,C,D)} and
 * {@code (B,D,E,F)} -- while acmicpc.net was unreachable.)
 *
 * <p>The fixtures pin the things a hasty corner sweep gets wrong:
 *
 * <ul>
 *   <li><b>All four corners are required.</b> A rectangle missing its diagonal corner
 *       ({@link #missingDiagonalCornerFormsNoRectangle}) or a side corner
 *       ({@link #missingSideCornerFormsNoRectangle}) must not be counted.
 *   <li><b>Anchor each rectangle once.</b> Counting from the bottom-left only -- not once per
 *       corner -- keeps a lone rectangle at one
 *       ({@link #singleRectangleIsCountedOnceNotPerCorner}), while shared corners are still counted
 *       in every rectangle that owns them ({@link #verticallyStackedRectanglesAreBothCounted},
 *       {@link #threeByThreeGridFormsFourRectangles}).
 *   <li><b>Width and height are not interchangeable.</b> When {@code A != B}, a
 *       {@code B}-by-{@code A} rectangle is the wrong shape and contributes nothing
 *       ({@link #widthAndHeightAreNotInterchangeable}); when {@code A == B} a square is a valid
 *       rectangle ({@link #squareIsCountedWhenWidthEqualsHeight}).
 *   <li><b>The full coordinate range, signs included.</b> Points hash correctly through the
 *       negative quadrant ({@link #negativeCoordinatesFormRectangles}) and at the {@code ±1e9}
 *       extremes, where {@code x+A} approaches the boundary and a 32-bit packing scheme would
 *       collide or overflow ({@link #extremeCoordinatesNearBoundsFormRectangles}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N)} hashing approach
 * (half a million points, each probing three neighbors) against the {@code O(N^2)} pairwise scan
 * that would time out, and {@link #randomizedInputsMatchBruteForceOracle} cross-checks the judged
 * output against an obviously-correct {@code O(N^4)} enumeration of point quadruples that shares no
 * logic with a hashing solution. The {@code Main} under test is an empty stub that reads nothing
 * and prints nothing, so every assertion here is RED until the corner sweep is implemented.
 */
class MainTest {

  // --- The official sample from the statement: the end-to-end smoke test. Six points, A=2 B=3, and
  // the two rectangles (A,B,C,D) and (B,D,E,F) share the edge B(2,0)-D(2,3) -- so this already pins
  // both that a rectangle is found at all and that a shared corner is reused. ---

  @Test
  @StdIo({"6", "2 3", "0 0", "2 0", "0 3", "2 3", "4 0", "4 3"})
  void officialSampleCountsTwoRectangles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- A single rectangle is counted once, anchored at its bottom-left (0,0): only that corner has
  // all three partners (2,0),(0,3),(2,3). A solution that probes from every corner -- e.g. also
  // looking back at (x-A, y), (x, y-B), (x-A, y-B) -- would re-count the rectangle from (2,3) and
  // print 2. The far-away filler keeps N at the legal minimum of 5 without forming anything. ---

  @Test
  @StdIo({"5", "2 3", "0 0", "2 0", "0 3", "2 3", "500000 500000"})
  void singleRectangleIsCountedOnceNotPerCorner(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A == B: a unit square is a legitimate width-1 height-1 rectangle. Anchor (0,0) finds
  // (1,0),(0,1),(1,1). Pins that the degenerate "square" case is counted (and counted once, despite
  // the x- and y-offset probes being symmetric when A equals B). ---

  @Test
  @StdIo({"5", "1 1", "0 0", "1 0", "0 1", "1 1", "500000 500000"})
  void squareIsCountedWhenWidthEqualsHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Three of the four corners present, the diagonal (2,3) absent: an L-shape, not a rectangle.
  // Anchor (0,0) sees (2,0) and (0,3) but the (x+A, y+B) probe must fail. A solution that forgets
  // the diagonal check would wrongly count it. ---

  @Test
  @StdIo({"5", "2 3", "0 0", "2 0", "0 3", "500000 500000", "600000 600000"})
  void missingDiagonalCornerFormsNoRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The mirror of the diagonal case: the diagonal (2,3) IS present but a side corner (0,3) is
  // missing. Anchor (0,0) has the right neighbor (2,0) and the diagonal (2,3), yet the (x, y+B)
  // top-side probe must fail. Pins that all three probes are required, not just two-of-three. ---

  @Test
  @StdIo({"5", "2 3", "0 0", "2 0", "2 3", "500000 500000", "600000 600000"})
  void missingSideCornerFormsNoRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Orientation is fixed: with A=2 B=3 we want width-2 height-3 rectangles only. The four
  // points
  // (0,0),(3,0),(0,2),(3,2) form a width-3 height-2 rectangle -- exactly B-by-A, the transpose. A
  // solution that swaps A and B, or that counts both orientations, would print 1; the correct count
  // is 0. ---

  @Test
  @StdIo({"5", "2 3", "0 0", "3 0", "0 2", "3 2", "500000 500000"})
  void widthAndHeightAreNotInterchangeable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Two rectangles stacked vertically share the horizontal edge at y=3: (0,3) and (2,3) belong
  // to both the lower rectangle (anchor (0,0)) and the upper one (anchor (0,3)). Pins that a shared
  // corner is counted in each owning rectangle and that vertical adjacency is handled, not just the
  // horizontal adjacency of the official sample. ---

  @Test
  @StdIo({"6", "2 3", "0 0", "2 0", "0 3", "2 3", "0 6", "2 6"})
  void verticallyStackedRectanglesAreBothCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- A 3x3 lattice spaced A=2 horizontally and B=3 vertically: nine points, four unit cells. The
  // bottom-left anchors (0,0),(2,0),(0,3),(2,3) each complete a rectangle; the top row and right
  // column have no partners beyond the grid. Interior points are shared across up to four cells, so
  // this pins combinatorial counting over a dense grid -- the answer is the 2x2 cell count, 4. ---

  @Test
  @StdIo({"9", "2 3", "0 0", "2 0", "4 0", "0 3", "2 3", "4 3", "0 6", "2 6", "4 6"})
  void threeByThreeGridFormsFourRectangles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Negative coordinates: a rectangle living entirely in the third quadrant. Anchor (-5,-5)
  // finds (-3,-5),(-5,-2),(-3,-2). A packing scheme that assumes non-negative coordinates -- e.g.
  // indexing an array by x, or a hash that mangles the sign bit -- breaks here. The (0,0) filler is
  // distinct and inert. ---

  @Test
  @StdIo({"5", "2 3", "-5 -5", "-3 -5", "-5 -2", "-3 -2", "0 0"})
  void negativeCoordinatesFormRectangles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The coordinate extremes, both signs, with the largest A=B=1000. One rectangle hugs +1e9 (so
  // x+A lands exactly on the boundary 1,000,000,000) and one hugs -1e9. This is the overflow / hash
  // collision pin: shifting (x+1e9) into the high bits and OR-ing (y+1e9) must not lose information
  // for coordinates spanning the full 2e9-wide range, and computing x+A near the ceiling must not
  // wrap. Two independent rectangles, far apart -> count 2. ---

  @Test
  @StdIo({
    "8",
    "1000 1000",
    "999999000 999999000",
    "1000000000 999999000",
    "999999000 1000000000",
    "1000000000 1000000000",
    "-1000000000 -1000000000",
    "-999999000 -1000000000",
    "-1000000000 -999999000",
    "-999999000 -999999000"
  })
  void extremeCoordinatesNearBoundsFormRectangles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Five scattered points with no two aligned by A=2 or B=3: no anchor completes a rectangle.
  // The basic negative case -- a correct solution prints 0, while one that miscounts membership (or
  // off-by-ones the offsets) would invent a rectangle. ---

  @Test
  @StdIo({"5", "2 3", "0 0", "5 5", "10 1", "3 8", "7 2"})
  void scatteredPointsFormNoRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Upper bound on N: 500,000 points laid out as a two-row strip y in {0,1}, x in 0..249,999,
  // with A=B=1. Each lower point (i,0) for i in 0..249,998 anchors a unit square with
  // (i+1,0),(i,1),
  // (i+1,1); the last column and the top row have no partner -> 249,999 rectangles. The O(N)
  // hashing
  // sweep handles this well within the limit, whereas an O(N^2) pairwise check (~2.5e11 ops) would
  // not finish. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int columns = 250_000;
    int[][] points = new int[2 * columns][2];
    for (int i = 0; i < columns; i++) {
      points[2 * i] = new int[] {i, 0};
      points[2 * i + 1] = new int[] {i, 1};
    }
    // columns - 1 adjacent column pairs each close one 1x1 rectangle.
    assertThat(runMain(rectanglesInput(1, 1, points))).isEqualTo(Integer.toString(columns - 1));
  }

  // --- Small random point sets on a tight 5x5 grid with A,B in {1,2} (so rectangles -- and their
  // transposes when A != B -- appear often), cross-checked against an independent O(N^4) quadruple
  // enumeration. The oracle shares no logic with a hashing anchor sweep, so the two agree only when
  // both are right; this is where missing-corner, double-count and A/B-swap bugs surface in bulk.
  // ---

  @Test
  void randomizedInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(2121);
    for (int trial = 0; trial < 1000; trial++) {
      int a = 1 + rnd.nextInt(2); // width in [1, 2]
      int b = 1 + rnd.nextInt(2); // height in [1, 2]
      int n = 5 + rnd.nextInt(6); // N in [5, 10], honoring 5 <= N
      int[][] points = distinctPoints(rnd, n, 5); // n distinct points on a 5x5 grid (25 cells)
      String expected = Long.toString(bruteForceRectangleCount(points, a, b));
      assertThat(runMain(rectanglesInput(a, b, points)))
          .as("a=%d b=%d points=%s", a, b, Arrays.deepToString(points))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: enumerate every unordered quadruple of points and count those that are the
   * four corners of an axis-parallel rectangle of width {@code a} and height {@code b}. Uses no
   * hash set and no corner-offset probing, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(N^4)} time, where {@code N} is {@code points.length} -- acceptable only
   *     because the randomized fixtures keep {@code N} small.
   */
  private static long bruteForceRectangleCount(int[][] points, int a, int b) {
    int n = points.length;
    long count = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        for (int k = j + 1; k < n; k++) {
          for (int l = k + 1; l < n; l++) {
            if (formsRectangle(new int[][] {points[i], points[j], points[k], points[l]}, a, b)) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }

  /**
   * True iff the four given (distinct) points are exactly the corners {@code (x,y)},
   * {@code (x+a,y)}, {@code (x,y+b)}, {@code (x+a,y+b)} of an axis-parallel rectangle of width
   * {@code a} and height {@code b}. Because {@code a, b >= 1} the four corners are distinct, so it
   * suffices to check the bounding box has the right dimensions and every corner is present.
   */
  private static boolean formsRectangle(int[][] quad, int a, int b) {
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int[] p : quad) {
      minX = Math.min(minX, p[0]);
      maxX = Math.max(maxX, p[0]);
      minY = Math.min(minY, p[1]);
      maxY = Math.max(maxY, p[1]);
    }
    if (maxX - minX != a || maxY - minY != b) {
      return false;
    }
    return containsPoint(quad, minX, minY)
        && containsPoint(quad, maxX, minY)
        && containsPoint(quad, minX, maxY)
        && containsPoint(quad, maxX, maxY);
  }

  private static boolean containsPoint(int[][] quad, int x, int y) {
    for (int[] p : quad) {
      if (p[0] == x && p[1] == y) {
        return true;
      }
    }
    return false;
  }

  /** Samples {@code n} distinct points from a {@code span x span} grid {@code [0, span)^2}. */
  private static int[][] distinctPoints(Random rnd, int n, int span) {
    Set<Integer> seen = new HashSet<>();
    int[][] points = new int[n][2];
    int filled = 0;
    while (filled < n) {
      int x = rnd.nextInt(span);
      int y = rnd.nextInt(span);
      if (seen.add(x * span + y)) {
        points[filled][0] = x;
        points[filled][1] = y;
        filled++;
      }
    }
    return points;
  }

  /**
   * Renders BOJ 2121 input: {@code N} on line 1, {@code "A B"} on line 2, then one point per line.
   */
  private static String rectanglesInput(int a, int b, int[][] points) {
    StringBuilder sb = new StringBuilder();
    sb.append(points.length).append('\n');
    sb.append(a).append(' ').append(b).append('\n');
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
