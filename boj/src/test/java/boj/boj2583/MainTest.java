package boj.boj2583;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2583 영역 구하기 ("Finding areas") -- paint {@code K} axis-aligned rectangles on an {@code M x N}
 * sheet of grid paper and report the uncovered regions: their count, then their areas in ascending
 * order.
 *
 * <p>The paper is {@code M} units tall and {@code N} units wide ({@code M}, {@code N}, {@code K}
 * natural numbers {@code <= 100}), with its lower-left corner at {@code (0, 0)} and its upper-right
 * corner at {@code (N, M)}. The first input line is {@code M N K}; each of the next {@code K} lines
 * gives one rectangle as {@code x1 y1 x2 y2} -- its lower-left vertex then its upper-right vertex,
 * all on grid lines. Rectangles may overlap but never cover the whole paper. Two uncovered unit
 * squares belong to the same region when they share an edge; corner contact does not connect. Print
 * the region count on the first line, then every region's area in ascending order on the second
 * line separated by single spaces. Limits: 1 second, 128 MB.
 *
 * <ul>
 *   <li><b>The output contract: a count line, then one line of ascending areas.</b> The official
 *       sample pins the full format on a real paper
 *       ({@link #officialSampleThreeRegionsWithAscendingAreasOnOneLine}). Areas must be sorted, not
 *       echoed in discovery order -- a paper whose scan from {@code x = 0} finds the larger region
 *       first isolates that ({@link #areasArePrintedAscendingNotInDiscoveryOrder}) -- and the sort
 *       must be numeric, not lexicographic, which only diverges once an area reaches two digits
 *       ({@link #areasSortNumericallyNotLexicographically}). Equal areas each get printed
 *       ({@link #equalAreasAreEachPrinted}).
 *   <li><b>Geometry: the coordinate system and rectangle painting.</b> On a wide non-square paper,
 *       {@code x} spans the width {@code N} and {@code y} the height {@code M} -- a solver that
 *       swaps them indexes out of bounds ({@link #rectangleCoordinatesUseXAcrossAndYUp}). A center
 *       rectangle leaves a single ring that bends through all four directions
 *       ({@link #ringAroundACenterRectangleIsOneRegion}). Overlapped cells stay covered -- a
 *       toggle-style painter would flip them back to empty
 *       ({@link #overlappedCellsStayCoveredWhenRectanglesIntersect}) -- and uncovered squares
 *       touching only at a corner are separate regions
 *       ({@link #regionsTouchingOnlyAtCornersAreSeparate}). The degenerate one-row paper splits
 *       cleanly ({@link #singleRowPaperSplitsIntoTwoRegions}).
 *   <li><b>Boundaries at {@code M = N = 100} and {@code K = 100}.</b> A unit notch in the corner of
 *       the maximal paper leaves one region of 9999
 *       ({@link #cornerNotchOnMaximumPaperLeavesOneRegion}), a serpentine corridor forces the
 *       deepest traversal a 100 x 100 paper allows
 *       ({@link #serpentineCorridorOnMaximumPaperIsOneRegion}), 100 alternating strips carve the
 *       paper into 2500 isolated unit regions
 *       ({@link #maximumRectangleCountGridYieldsIsolatedUnitRegions}), and 100 identical rectangles
 *       behave exactly like one ({@link #repeatedIdenticalRectanglesAtMaximumCountActAsOne}).
 *   <li><b>Randomized cross-check.</b> Random papers across the full {@code M}, {@code N} range and
 *       rectangle counts are compared against an independent union-find oracle
 *       ({@link #randomPapersMatchTheUnionFindOracle}).
 * </ul>
 *
 * <p>The oracle ({@link #unionFindOracle(boolean[][])}) merges edge-adjacent uncovered cells with a
 * disjoint set union instead of the flood fill (BFS/DFS) an intended solution would use, so its
 * region count and areas come from a genuinely different evaluation order; agreement across 250
 * random papers is independent evidence rather than the same algorithm checked against itself.
 */
class MainTest {

  // --- The output contract: a count line, then one line of ascending areas. ---

  // The official sample: a 5-tall, 7-wide paper with three rectangles leaving regions of 1, 7,
  // and 13. Pins the full output format -- the count on the first line, then all areas ascending
  // on one space-separated line.
  @Test
  @StdIo({"5 7 3", "0 2 4 4", "1 1 2 5", "4 0 6 2"})
  void officialSampleThreeRegionsWithAscendingAreasOnOneLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3\n1 7 13");
  }

  // A one-row paper split by a full-height rectangle at x = 4: the scan from x = 0 discovers the
  // area-4 region before the area-2 region, so echoing areas in discovery order prints 4 2. The
  // ascending contract demands 2 4, isolating the sort from the traversal order.
  @Test
  @StdIo({"1 7 1", "4 0 5 1"})
  void areasArePrintedAscendingNotInDiscoveryOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n2 4");
  }

  // Regions of area 10 and 2: a lexicographic (string) sort puts "10" before "2", the numeric
  // ascending contract demands 2 10. Only observable once some area reaches two digits.
  @Test
  @StdIo({"1 13 1", "10 0 11 1"})
  void areasSortNumericallyNotLexicographically(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n2 10");
  }

  // A full-height strip down the middle leaves two regions of the same area: both must appear --
  // a solver that deduplicates areas (e.g. via a set) collapses them to one.
  @Test
  @StdIo({"5 5 1", "2 0 3 5"})
  void equalAreasAreEachPrinted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n10 10");
  }

  // --- Geometry: the coordinate system and rectangle painting. ---

  // A 2-tall, 5-wide paper: x runs across the width (0..5), y up the height (0..2). The rectangle
  // reaches x = 5, which only fits the width axis -- a solver that swaps x and y (or M and N)
  // indexes out of bounds or paints the wrong cells.
  @Test
  @StdIo({"2 5 1", "3 0 5 2"})
  void rectangleCoordinatesUseXAcrossAndYUp(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n6");
  }

  // One unit rectangle in the center of a 3 x 3 paper: the eight surrounding squares form a
  // single ring bending through all four directions, so no single-axis merge covers it.
  @Test
  @StdIo({"3 3 1", "1 1 2 2"})
  void ringAroundACenterRectangleIsOneRegion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n8");
  }

  // Two rectangles overlapping on the center cell of a 3 x 3 paper leave only the two far
  // corners uncovered. A painter that toggles cells (instead of setting them) flips the
  // twice-painted center back to empty and reports three regions instead of two.
  @Test
  @StdIo({"3 3 2", "0 0 2 2", "1 1 3 3"})
  void overlappedCellsStayCoveredWhenRectanglesIntersect(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n1 1");
  }

  // On a 2 x 2 paper, covering the top-left and bottom-right squares leaves the other two
  // touching only at the center point. Corner contact does NOT connect: two regions of one,
  // not one region of two.
  @Test
  @StdIo({"2 2 2", "0 1 1 2", "1 0 2 1"})
  void regionsTouchingOnlyAtCornersAreSeparate(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n1 1");
  }

  // The minimum height M = 1: a single-row paper split by one rectangle into two equal halves.
  @Test
  @StdIo({"1 5 1", "2 0 3 1"})
  void singleRowPaperSplitsIntoTwoRegions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n2 2");
  }

  // --- Boundaries at M = N = 100 and K = 100. ---

  // The largest possible single region: a lone unit rectangle notched into the corner of the
  // maximal 100 x 100 paper leaves one region of 9999.
  @Test
  void cornerNotchOnMaximumPaperLeavesOneRegion() throws IOException {
    assertThat(runMain(buildInput(100, 100, new int[][] {{0, 0, 1, 1}}))).isEqualTo("1\n9999");
  }

  // A serpentine corridor across the maximal paper: every odd row is walled off except one end,
  // alternating right and left, so the 50 full even rows chain into a single winding region of
  // 50 * 100 + 50 = 5050 squares. Traversing it demands the deepest single chain a 100 x 100
  // paper allows -- a stress on recursive flood fills.
  @Test
  void serpentineCorridorOnMaximumPaperIsOneRegion() throws IOException {
    int[][] rects = new int[50][];
    for (int i = 0; i < 50; i++) {
      int y = 2 * i + 1;
      rects[i] = i % 2 == 0 ? new int[] {0, y, 99, y + 1} : new int[] {1, y, 100, y + 1};
    }
    assertThat(runMain(buildInput(100, 100, rects))).isEqualTo("1\n5050");
  }

  // The maximum K = 100 rectangles as 50 vertical and 50 horizontal strips over every odd grid
  // line: the uncovered squares are exactly the (even x, even y) cells, 2500 isolated unit
  // regions -- the count line followed by 2500 ones on one line.
  @Test
  void maximumRectangleCountGridYieldsIsolatedUnitRegions() throws IOException {
    int[][] rects = new int[100][];
    for (int i = 0; i < 50; i++) {
      int odd = 2 * i + 1;
      rects[i] = new int[] {odd, 0, odd + 1, 100};
      rects[50 + i] = new int[] {0, odd, 100, odd + 1};
    }
    assertThat(runMain(buildInput(100, 100, rects)))
        .isEqualTo("2500\n" + String.join(" ", Collections.nCopies(2500, "1")));
  }

  // K = 100 copies of the same center rectangle behave exactly like one: repeated painting must
  // be idempotent and must not distort the surrounding ring's area.
  @Test
  void repeatedIdenticalRectanglesAtMaximumCountActAsOne() throws IOException {
    int[][] rects = new int[100][];
    Arrays.fill(rects, new int[] {1, 1, 2, 2});
    assertThat(runMain(buildInput(3, 3, rects))).isEqualTo("1\n8");
  }

  // --- Randomized cross-check against the independent union-find oracle. ---

  // Random papers spanning the full legal M and N range (1..100) with rectangle counts scaled to
  // the paper, checked against the union-find oracle. Regenerates any draw the problem forbids
  // (rectangles covering the whole paper), so every case has at least one region.
  @Test
  void randomPapersMatchTheUnionFindOracle() throws IOException {
    Random rng = new Random(2583L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 250; trial++) {
      int m;
      int n;
      do {
        m = 1 + rng.nextInt(100);
        n = 1 + rng.nextInt(100);
      } while (m * n < 2); // on a 1 x 1 paper any rectangle would cover everything
      int maxK = Math.min(100, Math.max(1, m * n / 4));
      int[][] rects;
      boolean[][] covered;
      do {
        rects = new int[1 + rng.nextInt(maxK)][];
        for (int i = 0; i < rects.length; i++) {
          int x1 = rng.nextInt(n);
          int y1 = rng.nextInt(m);
          rects[i] = new int[] {x1, y1, x1 + 1 + rng.nextInt(n - x1), y1 + 1 + rng.nextInt(m - y1)};
        }
        covered = paint(m, n, rects);
      } while (fullyCovered(covered)); // the problem promises the paper is never fully covered
      assertThat(runMain(buildInput(m, n, rects)))
          .as("m=%d n=%d k=%d%n%s", m, n, rects.length, buildInput(m, n, rects))
          .isEqualTo(unionFindOracle(covered));
    }
  }

  /**
   * Independent oracle: merges every edge-adjacent pair of uncovered cells with a disjoint set
   * union, then tallies each root's cell count and sorts. A different evaluation order from the
   * flood fill (BFS/DFS) an intended solution would run, so agreement is real cross-checking.
   *
   * @implNote {@code O(m * n * a(m * n))} time and {@code O(m * n)} space, where {@code m} and
   *     {@code n} are the paper's height and width and {@code a} is the inverse Ackermann function
   *     from path-halving union-find.
   */
  private static String unionFindOracle(boolean[][] covered) {
    int m = covered.length;
    int n = covered[0].length;
    int[] parent = new int[m * n];
    for (int i = 0; i < parent.length; i++) {
      parent[i] = i;
    }
    for (int y = 0; y < m; y++) {
      for (int x = 0; x < n; x++) {
        if (covered[y][x]) {
          continue;
        }
        if (x + 1 < n && !covered[y][x + 1]) {
          union(parent, y * n + x, y * n + x + 1);
        }
        if (y + 1 < m && !covered[y + 1][x]) {
          union(parent, y * n + x, (y + 1) * n + x);
        }
      }
    }
    int[] area = new int[m * n];
    for (int y = 0; y < m; y++) {
      for (int x = 0; x < n; x++) {
        if (!covered[y][x]) {
          area[find(parent, y * n + x)]++;
        }
      }
    }
    List<Integer> areas = new ArrayList<>();
    for (int a : area) {
      if (a > 0) {
        areas.add(a);
      }
    }
    Collections.sort(areas);
    StringBuilder sb = new StringBuilder();
    sb.append(areas.size()).append('\n');
    for (int i = 0; i < areas.size(); i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(areas.get(i));
    }
    return sb.toString();
  }

  private static int find(int[] parent, int x) {
    while (parent[x] != x) {
      parent[x] = parent[parent[x]]; // path halving
      x = parent[x];
    }
    return x;
  }

  private static void union(int[] parent, int a, int b) {
    parent[find(parent, a)] = find(parent, b);
  }

  /**
   * Paints the rectangles onto an {@code m}-tall, {@code n}-wide paper; cells are {@code [y][x]}.
   */
  private static boolean[][] paint(int m, int n, int[][] rects) {
    boolean[][] covered = new boolean[m][n];
    for (int[] rect : rects) {
      for (int y = rect[1]; y < rect[3]; y++) {
        for (int x = rect[0]; x < rect[2]; x++) {
          covered[y][x] = true;
        }
      }
    }
    return covered;
  }

  private static boolean fullyCovered(boolean[][] covered) {
    for (boolean[] row : covered) {
      for (boolean cell : row) {
        if (!cell) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Builds BOJ 2583 input: {@code M N K} on the first line, then one rectangle per line as
   * {@code x1 y1 x2 y2} (lower-left vertex, then upper-right vertex).
   */
  private static String buildInput(int m, int n, int[][] rects) {
    StringBuilder sb = new StringBuilder();
    sb.append(m).append(' ').append(n).append(' ').append(rects.length).append('\n');
    for (int[] rect : rects) {
      sb.append(rect[0])
          .append(' ')
          .append(rect[1])
          .append(' ')
          .append(rect[2])
          .append(' ')
          .append(rect[3])
          .append('\n');
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
