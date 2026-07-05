package boj.boj2667;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2667 단지번호붙이기 ("Numbering complexes") -- count the 4-directionally connected house groups on a
 * square map and list their sizes in ascending order.
 *
 * <p>An {@code N x N} map ({@code 5 <= N <= 25}) marks each cell with {@code 1} (a house) or
 * {@code 0} (empty). A <em>complex</em> is a maximal group of houses connected left/right or
 * up/down; diagonal adjacency does <b>not</b> connect. Print the number of complexes on the first
 * line, then each complex's house count in ascending order, one per line. The input is {@code N} on
 * the first line followed by {@code N} rows, each a contiguous string of {@code N} digits with no
 * separators. Limits: 1 second, 128 MB.
 *
 * <ul>
 *   <li><b>The output contract: a count line, then ascending sizes.</b> The official sample pins
 *       the full format on a real map ({@link #officialSampleThreeComplexesInAscendingOrder}).
 *       Sizes must be sorted, not echoed in discovery order -- a map whose row-major scan finds the
 *       largest complex first isolates that ({@link #sizesArePrintedAscendingNotInDiscoveryOrder}),
 *       and equal sizes must each get their own line ({@link #equalSizedComplexesAreEachPrinted}).
 *   <li><b>Base cases.</b> A map with no houses has zero complexes and no size lines
 *       ({@link #emptyMapHasZeroComplexesAndNoSizeLines}); one lone house is one complex of size
 *       one ({@link #singleHouseFormsASingleComplexOfOne}).
 *   <li><b>Connectivity is exactly 4-directional.</b> A horizontal run and a vertical run are each
 *       one complex ({@link #horizontalRunOfHousesIsOneComplex},
 *       {@link #verticalRunOfHousesIsOneComplex}) -- a solver scanning only one axis splits one of
 *       them apart. Houses touching only at corners stay separate
 *       ({@link #diagonalHousesAreNotConnected}), and a hollow ring exercises a component that no
 *       straight-line scan can cover ({@link #ringOfHousesIsASingleComplex}).
 *   <li><b>Boundaries at {@code N = 25}.</b> The all-houses map is one complex of 625
 *       ({@link #fullMapIsOneComplexAtMaximumSize}), a serpentine corridor forces the deepest
 *       possible single-path traversal ({@link #serpentineCorridorIsOneComplexAtMaximumDepth}), and
 *       a checkerboard yields the maximum possible complex count of 313
 *       ({@link #checkerboardYieldsTheMaximumComplexCount}).
 *   <li><b>Randomized cross-check.</b> Random maps across the full {@code N} range and a spread of
 *       house densities are compared against an independent union-find oracle
 *       ({@link #randomMapsMatchTheUnionFindOracle}).
 * </ul>
 *
 * <p>The oracle ({@link #unionFindOracle(char[][])}) merges adjacent houses with a disjoint set
 * union instead of the flood fill (BFS/DFS) an intended solution would use, so its component count
 * and sizes come from a genuinely different evaluation order; agreement across 300 random maps is
 * independent evidence rather than the same algorithm checked against itself.
 */
class MainTest {

  // --- The output contract: a count line, then ascending sizes. ---

  // The official sample: a 7x7 map with three complexes of 7, 8, and 9 houses. Pins the full
  // output format -- the count on the first line, then one ascending size per line.
  @Test
  @StdIo({"7", "0110100", "0110101", "1110101", "0000111", "0100000", "0111110", "0111000"})
  void officialSampleThreeComplexesInAscendingOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3\n7\n8\n9");
  }

  // A row-major scan discovers the 2x2 block (4 houses) first, the pair (2) second, and the lone
  // house (1) last, so echoing sizes in discovery order prints 4 2 1. The ascending contract
  // demands 1 2 4, isolating the sort from the traversal order.
  @Test
  @StdIo({"5", "11000", "11000", "00000", "00110", "00001"})
  void sizesArePrintedAscendingNotInDiscoveryOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3\n1\n2\n4");
  }

  // Two complexes of the same size: both size lines must appear -- a solver that deduplicates
  // sizes (e.g. via a set) collapses them to one.
  @Test
  @StdIo({"5", "11000", "00000", "00011", "00000", "00000"})
  void equalSizedComplexesAreEachPrinted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2\n2\n2");
  }

  // --- Base cases. ---

  // The degenerate map: no houses at all. The count line is 0 and no size lines follow.
  @Test
  @StdIo({"5", "00000", "00000", "00000", "00000", "00000"})
  void emptyMapHasZeroComplexesAndNoSizeLines(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // One lone house in the interior: exactly one complex of size one.
  @Test
  @StdIo({"5", "00000", "00000", "00100", "00000", "00000"})
  void singleHouseFormsASingleComplexOfOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n1");
  }

  // --- Connectivity is exactly 4-directional. ---

  // A full horizontal run along the top edge is one complex of five. A solver that only links
  // vertically would report five complexes of one.
  @Test
  @StdIo({"5", "11111", "00000", "00000", "00000", "00000"})
  void horizontalRunOfHousesIsOneComplex(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n5");
  }

  // The mirror case: a full vertical run down the left edge is one complex of five. A solver that
  // only links horizontally would report five complexes of one.
  @Test
  @StdIo({"5", "10000", "10000", "10000", "10000", "10000"})
  void verticalRunOfHousesIsOneComplex(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n5");
  }

  // Houses along the main diagonal touch only at corners, which the problem says does NOT
  // connect: five complexes of one, not one complex of five.
  @Test
  @StdIo({"5", "10000", "01000", "00100", "00010", "00001"})
  void diagonalHousesAreNotConnected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5\n1\n1\n1\n1\n1");
  }

  // A hollow ring around an empty interior: all 16 border houses form one complex. The component
  // bends through all four directions, so no single-axis or single-pass merge covers it.
  @Test
  @StdIo({"5", "11111", "10001", "10001", "10001", "11111"})
  void ringOfHousesIsASingleComplex(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n16");
  }

  // --- Boundaries at N = 25. ---

  // The largest possible single complex: every cell of the maximal 25x25 map is a house, one
  // complex of 625.
  @Test
  void fullMapIsOneComplexAtMaximumSize() throws IOException {
    char[][] map = new char[25][25];
    for (char[] row : map) {
      java.util.Arrays.fill(row, '1');
    }
    assertThat(runMain(buildInput(map))).isEqualTo("1\n625");
  }

  // A serpentine corridor across the maximal map: full even rows joined alternately at the right
  // and left ends by a single house on each odd row. The whole path is one complex of
  // 13 * 25 + 12 = 337 houses, and traversing it demands the deepest possible single chain a
  // 25x25 map allows -- a stress on recursive flood fills.
  @Test
  void serpentineCorridorIsOneComplexAtMaximumDepth() throws IOException {
    char[][] map = new char[25][25];
    for (int r = 0; r < 25; r++) {
      java.util.Arrays.fill(map[r], '0');
      if (r % 2 == 0) {
        java.util.Arrays.fill(map[r], '1');
      } else {
        map[r][r % 4 == 1 ? 24 : 0] = '1';
      }
    }
    assertThat(runMain(buildInput(map))).isEqualTo("1\n337");
  }

  // A checkerboard on the maximal map: 313 isolated houses (no two share an edge), the maximum
  // possible complex count. The output is the count line followed by 313 size-one lines.
  @Test
  void checkerboardYieldsTheMaximumComplexCount() throws IOException {
    char[][] map = new char[25][25];
    for (int r = 0; r < 25; r++) {
      for (int c = 0; c < 25; c++) {
        map[r][c] = (r + c) % 2 == 0 ? '1' : '0';
      }
    }
    assertThat(runMain(buildInput(map))).isEqualTo("313" + "\n1".repeat(313));
  }

  // --- Randomized cross-check against the independent union-find oracle. ---

  // Random maps spanning the full legal N range (5..25) and house densities from sparse scatter
  // to near-full coverage, checked against the union-find oracle. Sparse maps pack in many tiny
  // complexes (exercising the count and the sort), dense maps grow large winding ones
  // (exercising the traversal).
  @Test
  void randomMapsMatchTheUnionFindOracle() throws IOException {
    Random rng = new Random(2667L); // fixed seed -> deterministic across JVMs.
    double[] densities = {0.1, 0.35, 0.6, 0.9};
    for (int trial = 0; trial < 300; trial++) {
      int n = 5 + rng.nextInt(21); // 5..25, the full legal range of N
      double density = densities[trial % densities.length];
      char[][] map = randomMap(n, density, rng);
      assertThat(runMain(buildInput(map)))
          .as("n=%d density=%.2f%n%s", n, density, buildInput(map))
          .isEqualTo(unionFindOracle(map));
    }
  }

  /**
   * Independent oracle: merges every edge-adjacent pair of houses with a disjoint set union, then
   * tallies each root's house count and sorts. A different evaluation order from the flood fill
   * (BFS/DFS) an intended solution would run, so agreement is real cross-checking.
   *
   * @implNote {@code O(n^2 * a(n^2))} time and {@code O(n^2)} space, where {@code n} is the map's
   *     side length and {@code a} is the inverse Ackermann function from path-halving union-find.
   */
  private static String unionFindOracle(char[][] map) {
    int n = map.length;
    int[] parent = new int[n * n];
    for (int i = 0; i < parent.length; i++) {
      parent[i] = i;
    }
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        if (map[r][c] != '1') {
          continue;
        }
        if (c + 1 < n && map[r][c + 1] == '1') {
          union(parent, r * n + c, r * n + c + 1);
        }
        if (r + 1 < n && map[r + 1][c] == '1') {
          union(parent, r * n + c, (r + 1) * n + c);
        }
      }
    }
    int[] size = new int[n * n];
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        if (map[r][c] == '1') {
          size[find(parent, r * n + c)]++;
        }
      }
    }
    List<Integer> sizes = new ArrayList<>();
    for (int s : size) {
      if (s > 0) {
        sizes.add(s);
      }
    }
    Collections.sort(sizes);
    StringBuilder sb = new StringBuilder();
    sb.append(sizes.size());
    for (int s : sizes) {
      sb.append('\n').append(s);
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

  /** A random {@code n x n} map where each cell is a house with the given probability. */
  private static char[][] randomMap(int n, double density, Random rng) {
    char[][] map = new char[n][n];
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        map[r][c] = rng.nextDouble() < density ? '1' : '0';
      }
    }
    return map;
  }

  /**
   * Builds BOJ 2667 input: {@code N} on the first line, then each row as a contiguous digit string
   * with no separators.
   */
  private static String buildInput(char[][] map) {
    StringBuilder sb = new StringBuilder();
    sb.append(map.length).append('\n');
    for (char[] row : map) {
      sb.append(row).append('\n');
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
