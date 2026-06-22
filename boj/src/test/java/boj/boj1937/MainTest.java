package boj.boj1937;

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
 * BOJ 1937 욕심쟁이 판다 ("Greedy Panda") -- the longest strictly-increasing path through a grid.
 *
 * <p>The map is an {@code n x n} bamboo forest; each cell holds an amount of bamboo. The panda may
 * start on <b>any</b> cell and repeatedly step to a four-directionally adjacent cell that holds
 * <em>strictly more</em> bamboo than the cell it is leaving. Print the maximum number of
 * <b>cells</b> the panda can visit -- counting the starting cell -- over every possible start.
 *
 * <p>Constraints: {@code 1 <= n <= 500}; every amount is a natural number {@code <= 1,000,000}. The
 * answer is therefore at least {@code 1} (a lone cell with no greater neighbor) and at most
 * {@code n*n = 250,000}, so it always fits in a signed 32-bit {@code int}. This is the classic
 * "longest increasing path in a matrix": the value strictly increases along any legal walk, so no
 * cell can repeat and every path is finite.
 *
 * <ul>
 *   <li><b>Counting moves instead of cells.</b> The answer is the number of cells visited,
 *       including the start, not the number of steps. A single step visits two cells, so the
 *       minimal non-empty move must report {@code 2}, never {@code 1}
 *       ({@link #longestPathCountsCellsNotMoves(StdOut)}); the official sample's four-cell route is
 *       printed as {@code 4}, not its three moves
 *       ({@link #officialSampleHasLongestPathOfFourCells(StdOut)}).
 *   <li><b>Non-strict movement ({@code >=} instead of {@code >}).</b> The next cell must hold
 *       strictly more bamboo. Treating equal amounts as traversable both invents extra length and,
 *       because two equal adjacent cells point at each other, opens an infinite cycle that a
 *       recursive solver cannot terminate. An all-equal grid pins the floor at {@code 1}
 *       ({@link #allEqualGridCannotMoveSoAnswerIsOne(StdOut)}), and a grid of equal pairs shows the
 *       plateau is never crossed ({@link #equalNeighborsDoNotExtendThePath(StdOut)}).
 *   <li><b>Allowing diagonal moves.</b> Adjacency is the four orthogonal neighbors only. A grid
 *       whose only increasing chain runs along the diagonal -- fenced off orthogonally by a plateau
 *       -- separates the eight-direction mistake from the correct four
 *       ({@link #movementIsOrthogonalNotDiagonal(StdOut)}).
 *   <li><b>Fixing the start at a corner.</b> The panda starts wherever is best, so the answer is
 *       the maximum over <em>all</em> cells, not the path from {@code (0, 0)}. A grid whose values
 *       descend toward the bottom-right forces the optimal start away from the top-left corner
 *       ({@link #optimalStartIsNotNecessarilyTheTopLeftCorner(StdOut)}).
 *   <li><b>Omitting memoization (and surviving the recursion depth).</b> Plain DFS re-walks shared
 *       sub-paths and is exponential on a branching grid; caching each cell's best length makes it
 *       {@code O(n*n)}. And on a single increasing chain the recursion is {@code n*n} frames deep,
 *       so a naive recursive solver overflows the stack at full size. Two clocked cases force a
 *       polynomial, depth-safe solution ({@link #overlappingSubpathsForceMemoizationAtScale()},
 *       {@link #maximumGridIsHandledWithinTheTimeLimit()}).
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by two independent oracles built from algorithms
 * unlike the intended recursion: an exhaustive, un-memoized depth-first search for tiny grids
 * ({@link #bruteForceLongest(int[][])}) and an iterative value-descending sweep for larger ones
 * ({@link #iterativeOracleLongest(int[][])}). The randomized sweeps
 * ({@link #randomTinyGridsMatchTheBruteForceOracle()},
 * {@link #randomModerateGridsMatchTheIterativeOracle()}) drive both, and the {@code @Timeout}
 * max-size cases force the memoized, depth-safe solution.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // The canonical 4x4 forest from the problem. The longest strictly-increasing walk visits four
  // cells -- e.g. 2 -> 5 -> 11 -> 15 -- and the statement draws exactly three such routes. The
  // answer counts the four cells, not the three moves between them, so a move-counting solver
  // prints
  // 3 and fails here.
  @Test
  @StdIo({"4", "14 9 12 10", "1 11 5 4", "7 15 2 13", "6 3 16 8"})
  void officialSampleHasLongestPathOfFourCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Base case: the smallest grid. ---

  // A 1x1 forest: the panda starts and ends on the only cell, having visited exactly one cell. Pins
  // the base case length(cell) = 1 and guards against a solver that counts moves (and would report
  // 0) or that demands at least one move.
  @Test
  @StdIo({"1", "7"})
  void singleCellGridHasOnePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family: cells, not moves. ---

  // Only one cell (the 2) exceeds its neighbors, so the longest walk is the single step 1 -> 2,
  // visiting two cells. The answer is 2 -- the count of cells -- not 1, the count of moves. The
  // smallest case that separates the two conventions.
  @Test
  @StdIo({"2", "1 2", "1 1"})
  void longestPathCountsCellsNotMoves(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: strictly greater, not greater-or-equal. ---

  // Every cell holds the same amount, so no cell has a strictly greater neighbor and the panda can
  // never move. The answer is the floor value 1 (the start cell alone). A solver using >= would
  // chain
  // across the equal cells -- and, because equal neighbors point at each other, recurse forever.
  @Test
  @StdIo({"2", "5 5", "5 5"})
  void allEqualGridCannotMoveSoAnswerIsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The top row is a flat pair of 1s and the bottom a flat pair of 9s. A move from a 1 must jump
  // straight to a 9 (the equal partner is not strictly greater), so the longest walk is 1 -> 9: two
  // cells. A >= solver would also walk 1 -> 1 and 9 -> 9, inflating the length (or looping) -- here
  // it would find 1 -> 1 -> 9 -> 9 = 4. Strictness pins the answer at 2.
  @Test
  @StdIo({"2", "1 1", "9 9"})
  void equalNeighborsDoNotExtendThePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: movement is four-directional, never diagonal. ---

  // The values 1, 2, 3 lie on the main diagonal and strictly increase, but they touch only
  // diagonally; every orthogonal neighbor is a 9, an equal-valued plateau that cannot be crossed.
  // So
  // a correct four-direction solver can only manage a single step onto a 9 (length 2), while a
  // solver
  // that also walks diagonals would chain 1 -> 2 -> 3 and report 3.
  @Test
  @StdIo({"3", "1 9 9", "9 2 9", "9 9 3"})
  void movementIsOrthogonalNotDiagonal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: the panda may start on any cell, not a fixed corner. ---

  // Values descend toward the bottom-right, where the global minimum 1 sits. The longest increasing
  // walk therefore starts at the bottom-right corner and climbs up/left to the 9, visiting five
  // cells. A solver that always starts the search at the top-left 9 (a local maximum) finds a path
  // of
  // length 1 and is badly wrong; the answer is 5, the maximum over all starts.
  @Test
  @StdIo({"3", "9 8 7", "6 5 4", "3 2 1"})
  void optimalStartIsNotNecessarilyTheTopLeftCorner(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Family: structured grids with closed-form answers. ---

  // A boustrophedon ("snake") numbering 1..9 winds through the grid so that consecutive integers
  // are
  // always orthogonally adjacent: 1,2,3 then down to 4,5,6 then down to 7,8,9. The whole forest is
  // a
  // single increasing chain, so the panda visits every one of the n*n = 9 cells. Pins the maximal
  // case where one walk covers the entire grid.
  @Test
  @StdIo({"3", "1 2 3", "6 5 4", "7 8 9"})
  void snakeOrderingVisitsEveryCellThreeByThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // The same snake at 4x4: numbers 1..16 wind row by row, so the single chain covers all 16 cells.
  // A second size guards against an answer hardcoded to the 3x3 grid above.
  @Test
  @StdIo({"4", "1 2 3 4", "8 7 6 5", "9 10 11 12", "16 15 14 13"})
  void snakeOrderingVisitsEveryCellFourByFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // A plain row-major grid (value = row*n + col + 1) increases by 1 to the right and by n downward,
  // so every legal step moves right or down. The longest such walk runs corner to corner: (n-1) +
  // (n-1) moves over 2n-1 cells. For n = 3 that is 5. Unlike the snake, this DAG branches heavily,
  // so
  // it also exercises taking the max over several increasing neighbors.
  @Test
  @StdIo({"3", "1 2 3", "4 5 6", "7 8 9"})
  void rowMajorGridLongestPathIsTwoNMinusOneThreeByThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // The same row-major structure at n = 4: the corner-to-corner walk visits 2n-1 = 7 cells. Pairing
  // it with the 3x3 case pins the 2n-1 growth and catches an off-by-one in n.
  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 11 12", "13 14 15 16"})
  void rowMajorGridLongestPathIsTwoNMinusOneFourByFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Family: memoization is mandatory, and the recursion must survive full depth. ---

  // A 30x30 row-major grid (value = row*n + col + 1). Every right/down step increases the value, so
  // the number of monotone walks from the top-left is the central binomial C(58, 29) --
  // astronomically many. A memoized solver evaluates only 900 cells and returns the
  // corner-to-corner
  // length 2n-1 = 59 instantly; an un-memoized one re-walks every monotone path and cannot finish
  // in
  // time.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void overlappingSubpathsForceMemoizationAtScale() throws IOException {
    int n = 30;
    int[][] grid = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = i * n + j + 1; // 1..900, well within 1,000,000
      }
    }
    assertThat(runMain(buildInput(grid))).isEqualTo(Integer.toString(2 * n - 1));
  }

  // The maximum legal size, 500x500, laid out as a snake so that 1..250000 form one increasing
  // chain
  // covering every cell -- the answer is n*n = 250000. This stresses parsing 250,000 numbers and,
  // crucially, a chain that is 250,000 cells long: a naive recursive DFS recurses that deep and
  // overflows the stack, so the case also forces an iterative DP or an enlarged stack.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumGridIsHandledWithinTheTimeLimit() throws IOException {
    int n = 500;
    int[][] grid = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        // Boustrophedon: even rows left-to-right, odd rows right-to-left, so consecutive integers
        // stay orthogonally adjacent and the whole grid is a single increasing path.
        grid[i][j] = (i % 2 == 0) ? i * n + j + 1 : i * n + (n - j);
      }
    }
    assertThat(runMain(buildInput(grid))).isEqualTo(Integer.toString(n * n));
  }

  // --- Randomized cross-checks against the two independent oracles. ---

  // Tiny grids cross-checked against an exhaustive, un-memoized search that transparently tries
  // every
  // increasing walk from every start. Small amounts (1..6) pack the grids with equal-valued
  // plateaus
  // and dead ends, so this sweep alone exercises strictness, four-direction movement, the free
  // start,
  // and the cells-not-moves count across hundreds of random shapes.
  @Test
  void randomTinyGridsMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(19370L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rng.nextInt(4); // 1..4
      int[][] grid = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          grid[i][j] = 1 + rng.nextInt(6); // 1..6, many ties
        }
      }
      assertThat(runMain(buildInput(grid)))
          .as("grid=%s", Arrays.deepToString(grid))
          .isEqualTo(Integer.toString(bruteForceLongest(grid)));
    }
  }

  // Larger grids cross-checked against the iterative value-descending oracle, a completely
  // different
  // algorithm from the intended recursion. Trials alternate between sparse amounts (up to
  // 1,000,000,
  // long branching chains) and tie-heavy amounts (small ceilings, many plateaus), so the sweep
  // reaches the scale where memoization matters while still exercising the strict-inequality
  // boundary.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomModerateGridsMatchTheIterativeOracle() throws IOException {
    Random rng = new Random(1937L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 200; trial++) {
      int n = 5 + rng.nextInt(36); // 5..40
      int cap = (trial % 2 == 0) ? 1_000_000 : 4 + rng.nextInt(12); // sparse vs. tie-heavy
      int[][] grid = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          grid[i][j] = 1 + rng.nextInt(cap);
        }
      }
      assertThat(runMain(buildInput(grid)))
          .as("n=%d cap=%d", n, cap)
          .isEqualTo(Integer.toString(iterativeOracleLongest(grid)));
    }
  }

  /**
   * Independent exhaustive oracle: from every cell, recursively explores every strictly-increasing
   * walk with no memoization, returning the maximum number of cells visited (the start counts as
   * 1). Transparently correct and therefore trustworthy only for tiny grids. Because amounts
   * strictly increase along any walk, no cell repeats and the recursion always terminates.
   *
   * @implNote Worst case exponential in the number of cells -- where {@code n} is the grid side
   *     length {@code grid.length} -- so callers must keep the grid tiny (here at most
   *     {@code 4x4}).
   */
  private static int bruteForceLongest(int[][] grid) {
    int n = grid.length;
    int best = 1;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        best = Math.max(best, longestFrom(grid, i, j));
      }
    }
    return best;
  }

  private static int longestFrom(int[][] grid, int r, int c) {
    int n = grid.length;
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    int best = 1; // the current cell on its own
    for (int d = 0; d < 4; d++) {
      int nr = r + dr[d];
      int nc = c + dc[d];
      if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] > grid[r][c]) {
        best = Math.max(best, 1 + longestFrom(grid, nr, nc));
      }
    }
    return best;
  }

  /**
   * Independent iterative oracle: a sweep that processes cells in <em>descending</em> amount order.
   * By the time a cell is visited, every strictly greater neighbor -- the only cells a legal step
   * can move onto -- has already been resolved, so {@code dp[r][c]} is one plus the best of those
   * neighbors. The answer is the largest {@code dp} value. This is a completely different algorithm
   * from the intended recursive memoization, so agreement is real evidence.
   *
   * @implNote {@code O(n*n*log(n))} time -- where {@code n} is the grid side length
   *     {@code grid.length} -- dominated by the sort of the {@code n*n} cells.
   */
  private static int iterativeOracleLongest(int[][] grid) {
    int n = grid.length;
    Integer[] order = new Integer[n * n];
    for (int i = 0; i < n * n; i++) {
      order[i] = i;
    }
    Arrays.sort(order, (a, b) -> Integer.compare(grid[b / n][b % n], grid[a / n][a % n]));

    int[][] dp = new int[n][n];
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    int best = 1;
    for (int idx : order) {
      int r = idx / n;
      int c = idx % n;
      int cur = 1; // the cell itself
      for (int d = 0; d < 4; d++) {
        int nr = r + dr[d];
        int nc = c + dc[d];
        if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] > grid[r][c]) {
          cur = Math.max(cur, 1 + dp[nr][nc]);
        }
      }
      dp[r][c] = cur;
      best = Math.max(best, cur);
    }
    return best;
  }

  /**
   * Builds BOJ 1937 input: {@code n} on the first line, then {@code n} rows of {@code n} amounts.
   */
  private static String buildInput(int[][] grid) {
    int n = grid.length;
    StringBuilder sb = new StringBuilder(n * n * 4 + 16);
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (j > 0) {
          sb.append(' ');
        }
        sb.append(grid[i][j]);
      }
      sb.append('\n');
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
