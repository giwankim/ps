package boj.boj17611;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 17611 직각다각형 (Rectilinear Polygon) -- 2019 KOI first round, middle-school division #2.
 *
 * <p>A <em>simple rectilinear polygon</em> is a simple polygon whose every edge is parallel to the
 * x- or y-axis; consequently its edges alternate horizontal, vertical, horizontal, ... around the
 * boundary. One such polygon is given by its {@code n} vertices in clockwise order. Slide a
 * horizontal line {@code H} (positioned so it overlaps no horizontal edge of the polygon) and it
 * crosses some number of the polygon's <em>vertical</em> edges; let {@code h} be the largest count
 * over all positions. Symmetrically, a vertical line {@code V} (overlapping no vertical edge)
 * crosses some <em>horizontal</em> edges; let {@code v} be its maximum. The task is to output
 * {@code max(h, v)}.
 *
 * <p>Input: the first line is {@code n} ({@code 4 <= n <= 100,000}); each of the next {@code n}
 * lines is a vertex {@code "x y"} with {@code -500,000 <= x, y <= 500,000}, the vertices listed
 * clockwise. Output: {@code max(h, v)} on a single line.
 *
 * <p>The fixtures pin three things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Overlap, not edge count.</b> The answer is the most edges crossed <em>at once</em>, not
 *       the number of edges or the perimeter. The plus and staircase shapes have many edges yet
 *       answer {@code 2}, because no single sweep line meets more than two at a time.
 *   <li><b>Both directions matter.</b> {@code max(h, v)} must consider sweeps in <em>both</em>
 *       orientations. The downward comb is dominated by {@code h}; its transpose is dominated by
 *       {@code v}. An implementation that measures only one orientation fails one of the pair.
 *   <li><b>Signed coordinates to the bound.</b> Coordinates run to {@code ±500,000}, so the index
 *       shift must neither overflow nor clip -- exercised by the full-bounds square.
 * </ul>
 *
 * <p>The randomized cross-check builds simple <em>histogram</em> polygons (always simple and
 * rectilinear) -- optionally transposed to drive the {@code v} branch -- and compares the judged
 * answer against {@link #bruteForceMaxCrossings}, an oracle that counts overlaps by brute force.
 * Because the empty stub under test reads nothing and prints nothing, every assertion here is RED
 * until the solution is implemented.
 */
class MainTest {

  // --- The two official samples from the statement. ---

  // A square with corners (-1,-1)..(1,1). A vertical line through its interior meets the top and
  // bottom edges (2); a horizontal line meets the left and right edges (2). max(2, 2) = 2. Also the
  // smallest legal polygon (n = 4) and the case that proves negative coordinates are handled.
  @Test
  @StdIo({"4", "-1 -1", "-1 1", "1 1", "1 -1"})
  void officialSampleSquareCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // The statement's second figure: a battlement-like outline whose vertical edges all reach down to
  // the baseline, so a horizontal line at y = 0.5 crosses six of them (h = 6) while the widest a
  // vertical line can do is two horizontal edges (v = 2). max(6, 2) = 6.
  @Test
  @StdIo({"12", "0 0", "0 3", "1 3", "1 1", "2 1", "2 3", "5 3", "5 0", "4 0", "4 2", "3 2", "3 0"})
  void officialSampleCombShapeCountsSix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Baseline shapes whose answer is 2 no matter how many edges they have. ---

  // Any axis-aligned rectangle: a sweep line in either orientation meets exactly the two parallel
  // sides facing it. Always 2, independent of the rectangle's dimensions.
  @Test
  @StdIo({"4", "0 0", "0 3", "4 3", "4 0"})
  void axisAlignedRectangleCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // An L-shape (n = 6, an odd-ish vertex count for the family). The notch never stacks a third edge
  // under any sweep line, so the answer stays 2.
  @Test
  @StdIo({"6", "0 0", "0 2", "1 2", "1 1", "2 1", "2 0"})
  void lShapeCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A plus/cross sign: 12 edges, yet its arms never overlap in either projection, so no sweep line
  // meets more than two edges. Pins "overlap, not edge count" -- a solution that tallied edges or
  // perimeter would over-count here.
  @Test
  @StdIo({"12", "1 0", "1 1", "0 1", "0 2", "1 2", "1 3", "2 3", "2 2", "3 2", "3 1", "2 1", "2 0"})
  void plusShapeCountsTwoDespiteTwelveEdges(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A monotone staircase (8 edges climbing to the upper-right). Each step adds one horizontal and
  // one vertical edge, but a sweep line still meets only the run it sits over plus the long
  // opposite
  // side -- 2. Guards against counting every step.
  @Test
  @StdIo({"8", "0 0", "0 1", "1 1", "1 2", "2 2", "2 3", "3 3", "3 0"})
  void monotoneStaircaseCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Combs dominated by h: vertical edges reaching a common band, crossed by a horizontal line.
  // ---

  // Two downward teeth: the two tooth bodies plus the two outer walls give four vertical edges that
  // all span the band y in (0,1), so a horizontal line there crosses all four. h = 4, v = 2.
  @Test
  @StdIo({"8", "0 2", "3 2", "3 0", "2 0", "2 1", "1 1", "1 0", "0 0"})
  void downwardCombTwoTeethCountsFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Three downward teeth: six vertical edges share the band y in (0,1). h = 6, v = 2. The same
  // answer as official sample 2, reached through a cleaner comb to isolate the h branch.
  @Test
  @StdIo({"12", "0 2", "5 2", "5 0", "4 0", "4 1", "3 1", "3 0", "2 0", "2 1", "1 1", "1 0", "0 0"})
  void downwardCombThreeTeethCountsSix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- A comb dominated by v: the two-tooth comb transposed, so the heavy crossing is now a
  // vertical line meeting four horizontal edges. v = 4, h = 2. An implementation that only measured
  // the h orientation would wrongly answer 2 here. ---

  @Test
  @StdIo({"8", "2 0", "2 3", "0 3", "0 2", "1 2", "1 1", "0 1", "0 0"})
  void transposedCombTwoTeethCountsFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Coordinate bounds: the index shift must reach ±500,000 without overflow or clipping. ---

  // A square spanning the entire legal coordinate box. The reference solution offsets every
  // coordinate by +500,000 to index a difference array; this fixture drives both the minimum and
  // maximum index. Geometrically still just a rectangle, so the answer is 2.
  @Test
  @StdIo({"4", "-500000 -500000", "-500000 500000", "500000 500000", "500000 -500000"})
  void fullCoordinateBoundsSquareCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Upper bound on n and on the answer, plus randomized cross-checking against the oracle. ---

  // A maximum-size square-wave histogram: 49,999 columns of alternating height 2/1 give n = 100,000
  // vertices. Every interior column boundary plus the two outer walls -- 50,000 vertical edges --
  // span the band y in (1,2), so a horizontal line there crosses all of them. The answer is n/2,
  // the
  // largest a simple polygon with n vertices can reach, and the O(n+C) solution must stay within
  // the
  // time limit where an O(n^2) rescan would not.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxLengthSquareWaveCrossesHalfTheVertices() throws IOException {
    int w = 49_999; // odd -> heights start and end at 2; vertices = 2w + 2 = 100,000.
    int[] heights = new int[w];
    for (int i = 0; i < w; i++) {
      heights[i] = (i % 2 == 0) ? 2 : 1;
    }
    assertThat(runMain(polygonInput(histogramPolygon(heights)))).isEqualTo("50000");
  }

  // Small random histogram polygons -- half of them transposed so both the h and v branches get
  // exercised -- checked against an independent brute-force overlap count. The mix makes
  // already-correct rectangles (w = 1), tall combs, and everything between all frequent, catching
  // off-by-one boundary bugs the hand-picked cases might miss.
  @Test
  void randomizedHistogramPolygonsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(17611);
    for (int trial = 0; trial < 1000; trial++) {
      int columns = 1 + rnd.nextInt(10); // 1..10 columns -> 4..22 vertices
      int maxHeight = 2 + rnd.nextInt(5); // tops in 1..(2..6)
      int[] heights = randomDistinctAdjacentHeights(rnd, columns, maxHeight);
      int[][] polygon = histogramPolygon(heights);
      if (rnd.nextBoolean()) {
        polygon = transpose(polygon); // make the heavy crossing vertical, exercising the v branch
      }
      String expected = Integer.toString(bruteForceMaxCrossings(polygon));
      assertThat(runMain(polygonInput(polygon)))
          .as("polygon=%s", Arrays.deepToString(polygon))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: classify every edge as horizontal or vertical, then directly count, for
   * each candidate sweep-line position, how many opposite-orientation edges it strictly crosses,
   * and return the largest such count over both orientations. Re-derives {@code max(h, v)} without
   * a difference array or prefix sum, so it shares no logic with the linear judge solution.
   *
   * @implNote {@code O(C * n)} time, where {@code n} is {@code polygon.length} and {@code C} is the
   *     coordinate span swept by {@link #maxOverlap}; trivial for the short random polygons but far
   *     too slow for the judge.
   */
  private static int bruteForceMaxCrossings(int[][] polygon) {
    int n = polygon.length;
    List<int[]> horizontalXSpans = new ArrayList<>();
    List<int[]> verticalYSpans = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      int[] a = polygon[i];
      int[] b = polygon[(i + 1) % n];
      if (a[1] == b[1]) { // horizontal edge: constant y, x varies
        horizontalXSpans.add(new int[] {Math.min(a[0], b[0]), Math.max(a[0], b[0])});
      } else { // vertical edge: constant x, y varies
        verticalYSpans.add(new int[] {Math.min(a[1], b[1]), Math.max(a[1], b[1])});
      }
    }
    int v = maxOverlap(horizontalXSpans); // a vertical line crossing horizontal edges
    int h = maxOverlap(verticalYSpans); // a horizontal line crossing vertical edges
    return Math.max(h, v);
  }

  /**
   * The peak number of the given closed integer intervals that simultaneously contain a single
   * non-integer sweep position. A line at {@code p + 0.5} lies strictly inside {@code [lo, hi]} iff
   * {@code lo <= p < hi}, so the half-integer probes between the global minimum and maximum
   * endpoint find every distinct overlap count.
   *
   * @implNote {@code O(C * m)} time, where {@code m} is {@code intervals.size()} and {@code C} is
   *     the span between the smallest and largest endpoint.
   */
  private static int maxOverlap(List<int[]> intervals) {
    int lo = Integer.MAX_VALUE;
    int hi = Integer.MIN_VALUE;
    for (int[] interval : intervals) {
      lo = Math.min(lo, interval[0]);
      hi = Math.max(hi, interval[1]);
    }
    int best = 0;
    for (int p = lo; p < hi; p++) {
      int count = 0;
      for (int[] interval : intervals) {
        if (interval[0] <= p && p < interval[1]) {
          count++;
        }
      }
      best = Math.max(best, count);
    }
    return best;
  }

  /**
   * Random column heights in {@code [1, maxHeight]} with no two adjacent equal, so that every
   * column boundary of the histogram is a genuine corner (equal neighbors would create a
   * collinear, non-corner point that a polygon vertex list must not contain).
   *
   * @implNote {@code O(columns)} expected time.
   */
  private static int[] randomDistinctAdjacentHeights(Random rnd, int columns, int maxHeight) {
    int[] heights = new int[columns];
    heights[0] = 1 + rnd.nextInt(maxHeight);
    for (int i = 1; i < columns; i++) {
      int next;
      do {
        next = 1 + rnd.nextInt(maxHeight);
      } while (next == heights[i - 1]);
      heights[i] = next;
    }
    return heights;
  }

  /**
   * Builds the clockwise-free vertex list of a histogram (skyline) polygon: a flat base on {@code y
   * = 0} from {@code x = 0} to {@code x = columns}, with column {@code i} rising to
   * {@code heights[i]}. The result is always a simple rectilinear polygon with {@code 2 * columns +
   * 2} vertices.
   *
   * @implNote {@code O(columns)} time.
   */
  private static int[][] histogramPolygon(int[] heights) {
    int columns = heights.length;
    List<int[]> vertices = new ArrayList<>();
    vertices.add(new int[] {0, 0});
    vertices.add(new int[] {0, heights[0]});
    for (int i = 1; i < columns; i++) {
      vertices.add(new int[] {i, heights[i - 1]});
      vertices.add(new int[] {i, heights[i]});
    }
    vertices.add(new int[] {columns, heights[columns - 1]});
    vertices.add(new int[] {columns, 0});
    return vertices.toArray(new int[0][]);
  }

  /**
   * Reflects a polygon across the line {@code y = x}, turning an h-heavy shape into a v-heavy one.
   */
  private static int[][] transpose(int[][] polygon) {
    int[][] reflected = new int[polygon.length][2];
    for (int i = 0; i < polygon.length; i++) {
      reflected[i][0] = polygon[i][1];
      reflected[i][1] = polygon[i][0];
    }
    return reflected;
  }

  /**
   * Renders a polygon as BOJ 17611 input: the vertex count followed by one {@code "x y"} per line.
   */
  private static String polygonInput(int[][] polygon) {
    StringBuilder sb = new StringBuilder();
    sb.append(polygon.length).append('\n');
    for (int[] vertex : polygon) {
      sb.append(vertex[0]).append(' ').append(vertex[1]).append('\n');
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
