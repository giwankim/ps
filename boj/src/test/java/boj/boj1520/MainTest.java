package boj.boj1520;

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
 * BOJ 1520 내리막 길 ("Downhill Path") -- count the strictly descending lattice paths across a map.
 *
 * <p>The map is a rectangle of {@code M} rows by {@code N} columns; each cell holds a height.
 * Starting at the top-left cell {@code (0, 0)} you want to reach the bottom-right cell {@code (M-1,
 * N-1)}, moving only between four-directionally adjacent cells and only ever <b>downhill</b> --
 * each step must land on a <em>strictly</em> lower height. Print how many distinct such paths
 * exist.
 *
 * <p>Constraints: {@code 1 <= M, N <= 500}; every height is a natural number {@code <= 10,000}. The
 * statement additionally promises that the answer {@code H} is always a non-negative integer
 * {@code <= 1,000,000,000}. That promise is load-bearing twice over: it keeps the result inside a
 * signed 32-bit {@code int} (no {@code long} needed), and it bounds the <em>valid input domain</em>
 * -- so any grid these fixtures synthesize must itself keep its true path count within that
 * billion, or it is not a legal instance of the problem.
 *
 * <ul>
 *   <li><b>Non-strict movement ({@code <=} instead of {@code <}).</b> Downhill means strictly
 *       lower. Treating equal heights as traversable both invents paths that should not exist and,
 *       because two equal adjacent cells point at each other, opens infinite cycles. A grid with an
 *       equal-height pair on the route separates the two
 *       ({@link #equalHeightNeighborsAreNotTraversable(StdOut)}).
 *   <li><b>Restricting moves to down and right.</b> "Downhill" is defined by height, not by grid
 *       position, so a legal path may step <em>up</em> or <em>left</em> as long as the height
 *       drops. A solver that only ever moves down/right -- a common confusion with the monotone
 *       lattice-path problem -- undercounts whenever the optimum doubles back
 *       ({@link #aPathMayMoveLeftWhenItDescends(StdOut)},
 *       {@link #aPathMayMoveUpWhenItDescends(StdOut)}).
 *   <li><b>Counting reachability instead of summing paths.</b> The answer is a <em>count</em>: a
 *       cell reachable by several routes must add their counts, not collapse to "reachable: yes". A
 *       destination that no descent can reach must yield exactly {@code 0}
 *       ({@link #increasingSingleRowCannotReachDestination(StdOut)},
 *       {@link #destinationWalledOffByUphillNeighborsHasZeroPaths(StdOut)},
 *       {@link #twoDistinctDescendingPathsAreBothCounted(StdOut)},
 *       {@link #everyMonotonePathInAFullyDescendingGridIsCounted(StdOut)}).
 *   <li><b>Omitting memoization (and the {@code -1} sentinel).</b> Plain DFS re-walks shared
 *       subpaths and is exponential; the cache makes it {@code O(M*N)}. The cache must distinguish
 *       "not yet computed" from "computed as zero paths" -- using {@code 0} for both silently
 *       degrades back to re-computation. An overlap-heavy grid forces the issue under a clock
 *       ({@link #overlappingSubpathsForceMemoizationAtScale()},
 *       {@link #maximumDimensionsAreHandledWithinTheTimeLimit()}).
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by two independent oracles built from algorithms
 * unlike the intended recursion: an exhaustive, un-memoized path enumeration for tiny grids
 * ({@link #bruteForcePathCount(int[][])}) and an iterative height-ascending sweep for larger ones
 * ({@link #iterativeOraclePathCount(int[][])}). The randomized sweeps
 * ({@link #randomTinyGridsMatchTheBruteForceOracle()},
 * {@link #randomModerateGridsWithinTheGuaranteeMatchTheIterativeOracle()}) drive both, and the
 * {@code @Timeout} max-size cases force a polynomial, memoized solution.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // The canonical 4x5 map from the problem. Three downhill routes thread from the top-left 50 to
  // the
  // bottom-right 10; the statement itself illustrates exactly these three. A non-square grid also
  // guards against swapping the roles of M (rows) and N (columns).
  @Test
  @StdIo({"4 5", "50 45 37 32 30", "35 50 40 20 25", "30 30 25 17 28", "27 24 22 15 10"})
  void officialSampleHasThreeDownhillPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Base cases: the smallest grids, where the start is the destination or the grid is a line.
  // ---

  // A 1x1 map: the start cell already is the destination, so the single trivial (zero-move) path
  // counts. Pins the recursion base case paths(dest) = 1 and guards against a solver that demands
  // at
  // least one move.
  @Test
  @StdIo({"1 1", "5"})
  void singleCellGridHasOneTrivialPath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A single row that strictly decreases left to right: the only route is to keep stepping right,
  // and
  // it descends the whole way, so there is exactly one path.
  @Test
  @StdIo({"1 4", "4 3 2 1"})
  void strictlyDecreasingSingleRowHasOnePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A single column that strictly decreases top to bottom: the unique route steps straight down.
  // Together with the single-row case this exercises both the M = 1 and N = 1 degenerate shapes.
  @Test
  @StdIo({"3 1", "9", "5", "1"})
  void strictlyDecreasingSingleColumnHasOnePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family 3 (zero paths): the destination is unreachable by any descent. ---

  // A single row that increases left to right. From the start (height 1) the only neighbor (height
  // 2)
  // is uphill, so no descent exists and the answer is 0. Confirms that an uphill step is forbidden
  // and
  // that an unreachable destination yields zero, not one.
  @Test
  @StdIo({"1 3", "1 2 3"})
  void increasingSingleRowCannotReachDestination(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // A 2x2 map whose start (height 1) is a local minimum: both of its neighbors are higher, so the
  // walker is stranded at the corner and cannot reach the destination. Answer 0.
  @Test
  @StdIo({"2 2", "1 2", "2 3"})
  void destinationWalledOffByUphillNeighborsHasZeroPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Family 1: strictly lower, not lower-or-equal. ---

  // The top row is a flat pair of 4s. Because movement must be strictly downhill, the start cannot
  // step right onto the equal-height 4; its only descent is down to the 3, then right to the 1 --
  // one
  // path. A solver using <= would also slip across the flat pair (a second path) and, since the two
  // 4s point at each other, risks an infinite cycle. Strictness pins the answer at 1.
  @Test
  @StdIo({"2 2", "4 4", "3 1"})
  void equalHeightNeighborsAreNotTraversable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family 2: a legal path may move in any of the four directions, not just down and right. ---

  // Heights spiral so that some descents must step LEFT (decreasing column). The full
  // four-direction
  // count is 6, but a solver restricted to down/right moves finds only 3 of them -- the cleanest
  // separation of "downhill by height" from "monotone by position".
  @Test
  @StdIo({"3 3", "9 8 7", "4 5 6", "3 2 1"})
  void aPathMayMoveLeftWhenItDescends(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // A route that must step UP (decreasing row) to stay downhill: from the bottom-left 5 the descent
  // climbs to the interior 3 before finishing. The true count is 3; a down/right-only solver sees
  // only
  // the 2 routes that never climb.
  @Test
  @StdIo({"3 3", "7 9 9", "6 3 2", "5 4 1"})
  void aPathMayMoveUpWhenItDescends(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Family 3 (summation): several routes converging must all be counted. ---

  // The minimal grid with more than one route: from the corner 4 the walker may go right-then-down
  // or
  // down-then-right, both strictly descending to the 1. Two paths. Guards against a reachability
  // check
  // that would report 1.
  @Test
  @StdIo({"2 2", "4 3", "2 1"})
  void twoDistinctDescendingPathsAreBothCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A 3x4 grid whose height is 7 - (row + col): every right or down step drops by exactly one and
  // every up or left step rises, so each of the C(5,2) = 10 monotone lattice paths is downhill and
  // no
  // other path exists. Exercises summation over a moderately branching DAG with a closed-form
  // answer.
  @Test
  @StdIo({"3 4", "7 6 5 4", "6 5 4 3", "5 4 3 2"})
  void everyMonotonePathInAFullyDescendingGridIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Family 4: memoization is mandatory (the @Timeout guards against an exponential search). ---

  // A 17x17 grid with height 10000 - (row + col): every right/down step descends by one, every
  // up/left
  // step rises, and equal heights occur only on anti-diagonals (never adjacent). So the count is
  // the
  // number of monotone lattice paths, the central binomial C(32, 16) = 601,080,390 -- which still
  // fits
  // in an int and within the statement's billion. A memoized DFS evaluates only 289 cells; an
  // un-memoized one re-walks 601 million paths and cannot finish in time.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void overlappingSubpathsForceMemoizationAtScale() throws IOException {
    int size = 17;
    int[][] grid = new int[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        grid[i][j] = 10_000 - (i + j);
      }
    }
    assertThat(runMain(buildInput(grid))).isEqualTo("601080390");
  }

  // The maximum legal dimensions, 500x500. Heights step down a single corridor -- straight down the
  // first column, then straight along the last row -- while every off-corridor cell is 10000,
  // higher
  // than anything on the route, so exactly one path exists. This stresses parsing 250,000 numbers
  // and
  // an O(M*N) sweep at full size without exceeding the billion-path domain.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumDimensionsAreHandledWithinTheTimeLimit() throws IOException {
    int m = 500;
    int n = 500;
    int[][] grid = new int[m][n];
    for (int[] row : grid) {
      Arrays.fill(row, 10_000);
    }
    int height = 9_999;
    for (int i = 0; i < m; i++) {
      grid[i][0] = height--; // descend column 0: 9999 .. 9500
    }
    for (int j = 1; j < n; j++) {
      grid[m - 1][j] = height--; // then descend along the last row: 9499 .. 9001
    }
    assertThat(runMain(buildInput(grid))).isEqualTo("1");
  }

  // --- Randomized cross-checks against the two independent oracles. ---

  // Tiny grids cross-checked against an exhaustive, un-memoized enumeration that obviously counts
  // every
  // downhill path. Small heights (1..6) pack the grids with equal-height blocks and dead ends, so
  // this
  // sweep alone exercises strictness, four-direction movement, summation, and the unreachable case.
  @Test
  void randomTinyGridsMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(15200L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 500; trial++) {
      int m = 1 + rng.nextInt(5); // 1..5
      int n = 1 + rng.nextInt(5); // 1..5
      int[][] grid = new int[m][n];
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          grid[i][j] = 1 + rng.nextInt(6); // 1..6
        }
      }
      assertThat(runMain(buildInput(grid)))
          .as("grid=%s", Arrays.deepToString(grid))
          .isEqualTo(Long.toString(bruteForcePathCount(grid)));
    }
  }

  // Larger grids cross-checked against the iterative height-sweep oracle. The oracle is computed
  // first;
  // grids whose true count exceeds the statement's billion are out of the valid domain and skipped,
  // so
  // the program is only ever asked legal instances and never observed through an int overflow.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomModerateGridsWithinTheGuaranteeMatchTheIterativeOracle() throws IOException {
    Random rng = new Random(1520L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 400; trial++) {
      int m = 1 + rng.nextInt(25); // 1..25
      int n = 1 + rng.nextInt(25); // 1..25
      int cap = 1 + rng.nextInt(40); // small ceilings keep counts modest and create blocking ties
      int[][] grid = new int[m][n];
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          grid[i][j] = 1 + rng.nextInt(cap);
        }
      }
      long expected = iterativeOraclePathCount(grid);
      if (expected > 1_000_000_000L) {
        continue; // outside the problem's H <= 1e9 domain -- not a legal instance.
      }
      assertThat(runMain(buildInput(grid)))
          .as("grid=%s", Arrays.deepToString(grid))
          .isEqualTo(Long.toString(expected));
    }
  }

  /**
   * Independent exhaustive oracle: enumerates every downhill path from the start to the destination
   * by naive recursion with no memoization, so it is transparently correct and trustworthy only for
   * tiny grids. Because heights strictly decrease along any path, no cell can repeat and the
   * recursion always terminates.
   *
   * @implNote Worst case {@code O(4^(M*N))} time -- where {@code M} and {@code N} are the grid's
   *     row and column counts {@code grid.length} and {@code grid[0].length}. Callers must keep the
   *     grid tiny (here at most {@code 5x5}).
   */
  private static long bruteForcePathCount(int[][] grid) {
    return countFrom(grid, 0, 0);
  }

  private static long countFrom(int[][] grid, int r, int c) {
    int m = grid.length;
    int n = grid[0].length;
    if (r == m - 1 && c == n - 1) {
      return 1L;
    }
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    long total = 0L;
    for (int d = 0; d < 4; d++) {
      int nr = r + dr[d];
      int nc = c + dc[d];
      if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] < grid[r][c]) {
        total += countFrom(grid, nr, nc);
      }
    }
    return total;
  }

  /**
   * Independent iterative oracle: a bottom-up sweep that processes cells in ascending height order.
   * By the time a cell is visited, every strictly lower neighbor (the only cells a downhill step
   * can move to) has already been resolved, so {@code dp[r][c]} is just the sum of those neighbors'
   * counts, with the destination seeded to 1. This is a completely different algorithm from the
   * intended recursive memoization, so agreement is real evidence. Uses {@code long} to detect any
   * int overflow in the program rather than silently sharing it.
   *
   * @implNote {@code O(M*N*log(M*N))} time -- where {@code M} and {@code N} are the grid's row and
   *     column counts {@code grid.length} and {@code grid[0].length} -- dominated by the height
   *     sort.
   */
  private static long iterativeOraclePathCount(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    Integer[] order = new Integer[m * n];
    for (int i = 0; i < m * n; i++) {
      order[i] = i;
    }
    Arrays.sort(order, (a, b) -> Integer.compare(grid[a / n][a % n], grid[b / n][b % n]));

    long[][] dp = new long[m][n];
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    for (int idx : order) {
      int r = idx / n;
      int c = idx % n;
      if (r == m - 1 && c == n - 1) {
        dp[r][c] = 1L; // the destination: exactly one path ends here.
        continue;
      }
      long sum = 0L;
      for (int d = 0; d < 4; d++) {
        int nr = r + dr[d];
        int nc = c + dc[d];
        if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] < grid[r][c]) {
          sum += dp[nr][nc];
        }
      }
      dp[r][c] = sum;
    }
    return dp[0][0];
  }

  /**
   * Builds BOJ 1520 input: {@code "M N"} on the first line, then {@code M} rows of {@code N}
   * heights.
   */
  private static String buildInput(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    StringBuilder sb = new StringBuilder(m * n * 4 + 16);
    sb.append(m).append(' ').append(n).append('\n');
    for (int i = 0; i < m; i++) {
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
