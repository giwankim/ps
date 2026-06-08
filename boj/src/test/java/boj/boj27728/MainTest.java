package boj.boj27728;

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
 * BOJ 27728 개구리와 쿼리 (Frog and Queries) -- shake! 2022 (경인지역 대학 연합) problem E.
 *
 * <p>An {@code N x N} grid {@code A} (1-indexed; row {@code i}, column {@code j} holds
 * {@code A[i][j]}) models a pond. Each of {@code Q} frogs answers an independent query {@code (Sx,
 * Sy, L)}: starting at cell {@code (Sx, Sy)}, the frog must reach the land at the right edge,
 * column {@code N + 1}. Moving one cell to the right (column {@code j -> j + 1}) costs the value of
 * the cell it leaves, {@code A[row][j]}. A frog may make <b>at most one</b> upward jump: a jump
 * moves it from row {@code Sx} to row {@code Sx - T} for some positive integer {@code T >= L},
 * costs no time, and must land on a real row ({@code Sx - T >= 1}). Print, per query, the minimum
 * total time to exit.
 *
 * <p>Constraints : {@code 3 <= N <= 500}; {@code 1 <= Q <= 200000}; {@code 0 <= A[i][j] <= 10000};
 * per query {@code 0 <= L < Sx <= N} and {@code 1 <= Sy <= N}. Time limit 1 s, memory 128 MB.
 */
class MainTest {

  // --- The published sample, verbatim. Three queries over one 5x5 grid, answers 5 / 3 / 12. ---

  // Grid rows 1..5 are [2 6 4 4 4], [3 12 5 4 2], [13 10 2 1 0], [1 1 2 2 3], [3 2 10 6 4].
  // Q1 (Sx=5,Sy=2,L=1): jump from row 5 to row 3 at column 3, then exit -> 5.
  // Q2 (Sx=3,Sy=3,L=0): no jump beats every jump; suf[3][3] = 2+1+0 = 3.
  // Q3 (Sx=5,Sy=3,L=4): L forces the single reachable row 1; switch at column 3 -> 12.
  @Test
  @StdIo({
    "5 3",
    "2 6 4 4 4",
    "3 12 5 4 2",
    "13 10 2 1 0",
    "1 1 2 2 3",
    "3 2 10 6 4",
    "5 2 1",
    "3 3 0",
    "5 3 4"
  })
  void officialSampleThreeQueries(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("5", "3", "12");
  }

  // --- (2) A jump must be used when a higher row is cheaper than staying put. ---

  // Same 5x5 grid as the sample. Query (Sx=5, Sy=1, L=1): staying in row 5 costs suf[5][1] =
  // 3+2+10+6+4 = 25, but traveling two cells in row 5 (3+2), jumping to row 3, then exiting via
  // 2+1+0 costs only 8. The solver must prefer the jump, and the optimal switch happens partway
  // across (column 3), not at the start or the exit.
  @Test
  @StdIo({"5 1", "2 6 4 4 4", "3 12 5 4 2", "13 10 2 1 0", "1 1 2 2 3", "3 2 10 6 4", "5 1 1"})
  void jumpStrictlyBeatsStayingInStartRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- (3) Not jumping must remain an option when every reachable row is costlier. ---

  // Rows 1 and 2 are all 9s; the bottom row 3 is all 1s. Query (Sx=3, Sy=1, L=0): the frog starts
  // on the cheap bottom row and can only jump *up* into the expensive rows. Every real jump lands
  // on
  // a 9-row and costs more, so the optimum is to stay put: suf[3][1] = 1+1+1 = 3. A solver that is
  // forced to jump, or that forgets the no-jump option, would overpay.
  @Test
  @StdIo({"3 1", "9 9 9", "9 9 9", "1 1 1", "3 1 0"})
  void stayingInStartRowBeatsEveryUpwardJump(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- (4) Jumping onto a free (all-zero) row drives the cost to zero. ---

  // Top row is all 0s; the start row 3 is [5 5 1]. Query (Sx=3, Sy=1, L=1): jumping immediately
  // (switch column = Sy) up to the zero row lets the frog cross the whole pond for free -> 0. This
  // exercises the A[i][j] = 0 boundary and the switch-at-start-column case.
  @Test
  @StdIo({"3 1", "0 0 0", "5 5 5", "5 5 1", "3 1 1"})
  void jumpingToAnAllZeroRowCostsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- (5) The optimal switch column is interior, not at either endpoint. ---

  // Start row 4 is cheap on the left then expensive ([1 1 9 9]); the only reachable row under L=3,
  // row 1, is expensive on the left then cheap ([8 8 1 1]). The best plan crosses columns 1-2 in
  // row 4 (1+1), jumps at column 3, then crosses columns 3-4 in row 1 (1+1) -> 4. Switching at the
  // start (cost 18) or at the exit (cost 20) is strictly worse, so the interior column 3 must win.
  @Test
  @StdIo({"4 1", "8 8 1 1", "9 9 9 9", "9 9 9 9", "1 1 9 9", "4 1 3"})
  void optimalSwitchColumnIsInterior(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- (6) The jump lower bound L can forbid the otherwise-best row. ---

  // Row 3 (one above the start row 4) is all 0s; rows 1-2 are all 9s; start row 4 is all 5s. Two
  // queries differ only in L. With L=1 the frog may jump a single row up onto the free row 3 -> 0.
  // With L=2 that single-row hop is illegal; the only reachable rows are the 9-rows, none of which
  // beat simply staying, so the answer jumps to suf[4][1] = 5*4 = 20. This pins the L constraint.
  @Test
  @StdIo({"4 2", "9 9 9 9", "9 9 9 9", "0 0 0 0", "5 5 5 5", "4 1 1", "4 1 2"})
  void raisingTheJumpLowerBoundForbidsTheCheapRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "20");
  }

  // --- (7) A frog already in the top row cannot jump (jumps only go upward). ---

  // Rows below are cheap (all 1s), but the frog starts in row 1 and jumps only *up*, so it can
  // never reach them. Query (Sx=1, Sy=2, L=0): the answer is just the start row's suffix from
  // column 2, suf[1][2] = 2+7 = 9. A solver that mistakenly allows a downward jump would return 2.
  @Test
  @StdIo({"3 1", "4 2 7", "1 1 1", "1 1 1", "1 2 0"})
  void topRowFrogCannotJumpUpward(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- (8) Starting on the last column, a jump still lowers the single remaining cell's cost. ---

  // Start at the rightmost column (Sy = N = 3) of row 3, whose last cell is 5; the only reachable
  // row under L=2 is row 1, whose last cell is 2. Query (Sx=3, Sy=3, L=2): jump immediately and pay
  // 2 instead of 5. Even with a single cell left to leave, the upward jump helps.
  @Test
  @StdIo({"3 1", "9 9 2", "9 9 9", "9 9 5", "3 3 2"})
  void startingAtTheLastColumnAJumpStillLowersCost(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- (9) When every cell is equal, jumping to an identical row never helps. ---

  // A uniform 3x3 grid of 7s. Query (Sx=3, Sy=2, L=1): every row's suffix from column 2 is 7+7=14,
  // so no jump can undercut staying. The answer is the plain suffix sum (N - Sy + 1) * 7 = 14.
  @Test
  @StdIo({"3 1", "7 7 7", "7 7 7", "7 7 7", "3 2 1"})
  void allEqualCellsMakeJumpingUseless(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // --- (10) An all-zero pond is free to cross from anywhere. ---

  // The A[i][j] = 0 lower boundary across the whole grid. Query (Sx=3, Sy=2, L=0): the answer is 0
  // regardless of jumping.
  @Test
  @StdIo({"3 1", "0 0 0", "0 0 0", "0 0 0", "3 2 0"})
  void zeroGridCostsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- (11) Maximum cell values sum cleanly within int (no overflow trap). ---

  // Every cell is the maximum 10000. Query (Sx=3, Sy=1, L=0): the answer is the full row sum
  // 3*10000 = 30000. Jumping to identical rows cannot help, and the result stays well within int.
  @Test
  @StdIo({"3 1", "10000 10000 10000", "10000 10000 10000", "10000 10000 10000", "3 1 0"})
  void maximumCellValuesSumWithoutOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30000");
  }

  // --- (12) Randomised cross-check against an independent brute-force oracle. Tiny grids with
  // small
  // values make exact ties, near misses, and beneficial jumps all frequent, exposing off-by-one
  // errors in switch columns, the L bound, or the no-jump option that hand-picked cases might miss.

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(27728);
    for (int trial = 0; trial < 300; trial++) {
      int n = 3 + rnd.nextInt(4); // 3..6
      int[][] grid = new int[n][n];
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
          grid[r][c] = rnd.nextInt(10); // 0..9 keeps ties and free paths common
        }
      }
      int q = 1 + rnd.nextInt(8); // 1..8 queries
      int[][] queries = new int[q][3];
      String[] expected = new String[q];
      for (int k = 0; k < q; k++) {
        int sx = 1 + rnd.nextInt(n); // 1..n
        int low = rnd.nextInt(sx); // 0 <= L < sx
        int sy = 1 + rnd.nextInt(n); // 1..n
        queries[k] = new int[] {sx, sy, low};
        expected[k] = Long.toString(solveQuery(grid, n, sx, sy, low));
      }
      assertThat(runMainLines(buildInput(grid, queries)))
          .as("grid=%s queries=%s", gridString(grid), queryString(queries))
          .containsExactly(expected);
    }
  }

  // --- (13) Full-scale performance guard: maximum N and Q within the judge's input bounds. ---

  // The grid fills row i with the constant value i, so the cheapest exit from any query is to jump
  // immediately onto row 1 (cost N - Sy + 1) rather than stay on the much costlier start row. With
  // N = 500 and Q = 200,000 this exercises reading a 250,000-cell grid plus answering every query
  // fast; an O(N) (or worse) scan of reachable rows *per query* would blow the 1 s judge limit, so
  // the intended solution must precompute per-column minima over rows. The answers depend only on
  // Sy, giving an exact analytic expectation without invoking the (cubic) oracle.
  @Test
  @Timeout(value = 20, unit = TimeUnit.SECONDS)
  void maxSizeGridAndQueriesAnswerFastAndCorrectly() throws IOException {
    int n = 500;
    int q = 200_000;
    int[][] grid = new int[n][n];
    for (int r = 0; r < n; r++) {
      int value = r + 1; // row i holds the constant i, so row 1 (value 1) is globally cheapest
      for (int c = 0; c < n; c++) {
        grid[r][c] = value;
      }
    }
    int[][] queries = new int[q][3];
    String[] expected = new String[q];
    for (int k = 0; k < q; k++) {
      int sy = (k % n) + 1; // sweep all start columns 1..n
      queries[k] = new int[] {n, sy, 0}; // start in the bottom row, no jump-length floor
      expected[k] = Integer.toString(n - sy + 1); // jump to row 1, then exit
    }
    assertThat(runMainLines(buildInput(grid, queries))).containsExactly(expected);
  }

  /**
   * Independent oracle for one query: the minimum exit time, computed by brute force. Enumerates
   * the no-jump path plus every (final row {@code rp}, switch column {@code c}) pair, summing the
   * row segments directly. Obviously correct, far too slow for the judge, trustworthy for tiny
   * grids.
   *
   * @implNote Runs in {@code O(R * N^2)} time per query, where {@code R} is the number of reachable
   *     rows ({@code Sx - max(1, L)}) and {@code N} is the grid side length; the running cost is a
   *     {@code long} so the oracle never overflows.
   */
  private static long solveQuery(int[][] grid, int n, int sx, int sy, int low) {
    long best = 0; // no-jump: travel the start row from column sy to the exit
    for (int j = sy; j <= n; j++) {
      best += grid[sx - 1][j - 1];
    }
    int topRow = sx - Math.max(1, low); // reachable final rows are 1..topRow
    for (int rp = 1; rp <= topRow; rp++) {
      for (int c = sy; c <= n + 1; c++) {
        long cost = 0;
        for (int j = sy; j < c; j++) {
          cost += grid[sx - 1][j - 1]; // start row, columns sy..c-1
        }
        for (int j = c; j <= n; j++) {
          cost += grid[rp - 1][j - 1]; // final row, columns c..n
        }
        best = Math.min(best, cost);
      }
    }
    return best;
  }

  /**
   * Builds a BOJ 27728 input: {@code N Q}, then {@code N} grid rows, then {@code Q} query lines.
   */
  private static String buildInput(int[][] grid, int[][] queries) {
    int n = grid.length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(queries.length).append('\n');
    for (int[] row : grid) {
      appendSpaced(sb, row);
    }
    for (int[] query : queries) {
      appendSpaced(sb, query);
    }
    return sb.toString();
  }

  /** Appends one space-separated row of integers followed by a newline. */
  private static void appendSpaced(StringBuilder sb, int[] values) {
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(values[i]);
    }
    sb.append('\n');
  }

  /** Runs {@link Main} on {@code input}, returning stdout split into trimmed non-empty lines. */
  private static String[] runMainLines(String input) throws IOException {
    String captured = capture(input).trim();
    if (captured.isEmpty()) {
      return new String[0];
    }
    return captured.split("\\R");
  }

  private static String capture(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      Main.main(new String[0]);
      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }

  /** Renders a grid as a list of row lists, for readable assertion failure messages. */
  private static String gridString(int[][] grid) {
    List<String> rows = new ArrayList<>();
    for (int[] row : grid) {
      rows.add(Arrays.toString(row));
    }
    return rows.toString();
  }

  /** Renders queries as {@code [sx, sy, L]} triples, for readable assertion failure messages. */
  private static String queryString(int[][] queries) {
    List<String> triples = new ArrayList<>();
    for (int[] query : queries) {
      triples.add(Arrays.toString(query));
    }
    return triples.toString();
  }
}
