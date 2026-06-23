package boj.boj23880;

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
 * BOJ 23880 Walking Home (USACO 2022 February, Bronze, Problem 3) -- counting short, low-turn
 * monotone paths through a grid.
 *
 * <p>Bessie walks from her pasture in the top-left corner of an {@code N x N} grid ({@code 2 <= N
 * <= 50}) to her barn in the bottom-right corner. To get home as soon as possible she only ever
 * moves <b>down</b> or <b>right</b>. Some cells hold haybales ({@code 'H'}) she cannot enter; empty
 * cells are {@code '.'}. Feeling tired, she will change walking direction <b>at most K</b> times
 * ({@code 1 <= K <= 3}). The task is to count the distinct down/right paths from pasture to barn
 * that avoid every haybale and turn at most K times; two paths differ if they occupy a square one
 * does and the other does not.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code T} ({@code 1 <= T <= 50}), the number of independent
 * sub-test cases. Each sub-test case is a line {@code "N K"} followed by {@code N} rows of
 * {@code N} characters. The program prints {@code T} lines, the i-th holding the path count for the
 * i-th sub-test case, in input order. The top-left and bottom-right cells are guaranteed empty.
 *
 * <p><b>What "at most K direction changes" means.</b> Write a path as a string over {@code D}
 * (down) and {@code R} (right). A direction change is an adjacent pair of unequal moves, so the
 * number of changes is (number of maximal same-letter runs) - 1. {@code DDRR} turns once,
 * {@code DRRD} turns twice, and {@code DRDR} turns three times. K caps that turn count -- it does
 * <em>not</em> cap the number of moves (which is always {@code 2*(N-1)}). The canonical sample
 * below makes the K boundary concrete: the same empty 3x3 grid yields 2, 4, and 6 paths as K rises
 * through 1, 2, 3.
 *
 * <p>Because {@code K <= 3} every counted path has at most four runs, so the answer is bounded by
 * roughly {@code O(N^3)} and always fits in a signed 32-bit {@code int}; the intended solution is a
 * memoized recursion over {@code (row, col, turnsUsed, lastDirection)}.
 *
 * <p>Hand-picked cases below are cross-checked against two oracles built from different algorithms
 * than that recursion: an exhaustive enumeration of every monotone path
 * ({@link #countValidPathsBrute(char[][], int)}) for small grids, and the closed form
 * {@link #emptyGridPathsWithAtMostThreeChanges(int)} for the maximum-size empty grid.
 */
class MainTest {

  // --- Base case: the smallest legal grid, where both monotone paths turn exactly once. ---

  // A 2x2 empty grid has exactly two down/right paths, DR and RD, each a single turn. With K = 1
  // both fit the budget, so the answer is 2. This pins the floor of the problem: there is always at
  // least the pair of "L-shaped" corner-hugging walks when the grid is open.
  @Test
  @StdIo({"1", "2 1", "..", ".."})
  void smallestGridHasBothMonotonePaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: raising K admits strictly more paths through the same open 3x3 grid. ---

  // K = 1 allows at most one turn, so only the two-run walks survive: DDRR and RRDD. The zig-zag
  // walks (DRDR, DRRD, RDDR, RDRD) all turn at least twice and must be rejected -> 2. This is the
  // first canonical sub-test case and the strongest guard against ignoring K entirely (which would
  // print 6).
  @Test
  @StdIo({"1", "3 1", "...", "...", "..."})
  void turnBudgetOfOneAdmitsOnlyTheTwoCornerPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // K = 2 additionally admits the two three-run walks DRRD and RDDR (each turning twice), for 4
  // total. The two three-change zig-zags DRDR and RDRD are still excluded. Second canonical
  // sub-test
  // case; pairing it with the K = 1 and K = 3 cases brackets the turn count from both sides.
  @Test
  @StdIo({"1", "3 2", "...", "...", "..."})
  void turnBudgetOfTwoAdmitsTheThreeRunPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // K = 3 is enough turns for every monotone path on a 3x3 grid (the most any 4-move path needs is
  // three, e.g. DRDR), so all C(4,2) = 6 paths count. Third canonical sub-test case; catches an
  // off-by-one that rejects a path turning exactly K times.
  @Test
  @StdIo({"1", "3 3", "...", "...", "..."})
  void turnBudgetOfThreeAdmitsEveryMonotonePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Family: haybales remove exactly the paths that would step on them. ---

  // A single haybale in the center of an open 3x3 grid (K = 3) blocks the four paths that pass
  // through the middle cell (DRDR, DRRD, RDDR, RDRD); only the edge-hugging DDRR and RRDD remain ->
  // 2. Fourth canonical sub-test case. With K large enough to allow all six paths, this isolates
  // the
  // haybale-avoidance logic from the turn-budget logic.
  @Test
  @StdIo({"1", "3 3", "...", ".H.", "..."})
  void centerHaybaleRemovesEveryPathThatWouldCrossIt(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Haybales walling off the entire grid except the two endpoints leave Bessie no legal first step,
  // so the count is 0. Fifth canonical sub-test case. A solver that forgets to check the
  // destination
  // is reachable -- or that miscounts the empty path -- would not print 0 here.
  @Test
  @StdIo({"1", "3 2", ".HH", "HHH", "HH."})
  void haybalesSealingTheWholeInteriorYieldNoPath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Both cells adjacent to the start hold haybales, so neither a first down nor a first right move
  // is
  // possible and the answer is 0. Sixth canonical sub-test case; the smallest "trapped at the gate"
  // grid, distinct from the walled-off case above.
  @Test
  @StdIo({"1", "3 3", ".H.", "H..", "..."})
  void haybalesBlockingBothExitsFromTheStartYieldNoPath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The seventh canonical sub-test case: a 4x4 grid with three scattered haybales and K = 3. The
  // six
  // surviving walks are DDRDRR, DDRRDR, DDRRRD, RRDDDR, RRDDRD, and RRDRDD -> 6. A 4x4 grid needs
  // 2*(4-1) = 6 moves per path, so this also guards against an N hard-coded to 3.
  @Test
  @StdIo({"1", "4 3", "...H", ".H..", "....", "H..."})
  void fourByFourGridWithScatteredHaybalesCountsSixPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- The judge's official sample, all seven sub-test cases answered line for line. ---

  // The complete sample from the statement, fed as a single run of T = 7 sub-test cases. The
  // expected output is 2, 4, 6, 2, 0, 0, 6 -- one line per sub-test case, in order. This is the
  // canonical example: it exercises the rising-K family (2, 4, 6), the center-haybale case (2), the
  // two impossible grids (0, 0), and the larger 4x4 grid (6) together, and would fail for any run
  // that dropped a line, reordered the answers, or printed a header.
  @Test
  @StdIo({
    "7", "3 1", "...", "...", "...", "3 2", "...", "...", "...", "3 3", "...", "...", "...", "3 3",
    "...", ".H.", "...", "3 2", ".HH", "HHH", "HH.", "3 3", ".H.", "H..", "...", "4 3", "...H",
    ".H..", "....", "H..."
  })
  void officialSampleIsAnsweredOneLinePerSubTestCase(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "4", "6", "2", "0", "0", "6");
  }

  // --- Each sub-test case gets its own line, emitted in input order. ---

  // Three sub-test cases with the deliberately distinctive answer shape 2, 0, 6: an open grid with
  // K = 1 (2), a start-trapped grid (0), and an open grid with K = 3 (6). The 2, 0, 6 sequence
  // would
  // be scrambled by any run that sorted, grouped, or merged the sub-test answers instead of
  // printing
  // them one per line in order.
  @Test
  @StdIo({"3", "3 1", "...", "...", "...", "3 3", ".H.", "H..", "...", "3 3", "...", "...", "..."})
  void eachSubTestCaseGetsItsOwnLineInInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "0", "6");
  }

  // --- Family: a haybale that forces a detour, where the turn budget then decides the count. ---
  //
  // A haybale just right of the start (top-middle of a 3x3 grid) forbids the first move from being
  // R, so every legal path must begin with D. The three paths that start with D are DDRR (1 turn),
  // DRRD (2 turns), and DRDR (3 turns). Raising K therefore admits them one at a time: 1, then 2,
  // then 3. This family couples haybale avoidance with the turn budget, which neither the pure-K
  // family nor the center-haybale case does alone.

  // K = 1: only DDRR fits the single-turn budget -> 1.
  @Test
  @StdIo({"1", "3 1", ".H.", "...", "..."})
  void haybaleAtTheStartRowWithTurnBudgetOneAllowsOnePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // K = 2: DDRR and DRRD fit -> 2.
  @Test
  @StdIo({"1", "3 2", ".H.", "...", "..."})
  void haybaleAtTheStartRowWithTurnBudgetTwoAllowsTwoPaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // K = 3: all three down-first paths fit -> 3.
  @Test
  @StdIo({"1", "3 3", ".H.", "...", "..."})
  void haybaleAtTheStartRowWithTurnBudgetThreeAllowsThreePaths(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A haybale corridor that leaves exactly one path, regardless of the turn budget. ---

  // Haybales fill the right two columns of the top two rows, so the only way through is straight
  // down
  // the first column and then right along the bottom: DDRR, one turn. No other first move is legal
  // at
  // any step, so the count is 1 even with the maximum K = 3. Guards against a solver that conjures
  // extra paths where the geometry permits only one.
  @Test
  @StdIo({"1", "3 3", ".HH", ".HH", "..."})
  void aSingleForcedCorridorHasExactlyOnePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Randomized cross-check: many small grids against the exhaustive path-enumeration oracle.
  // ---

  // Small grids (N up to 6) with random haybales and random K are each answered and compared to a
  // brute-force oracle that enumerates every monotone path and filters by haybales and turn count.
  // The small sizes pack the grids with blocked cells and dead ends, so this single sweep exercises
  // haybale avoidance, the K boundary, the down/right-only constraint, and the empty (zero-path)
  // case
  // across hundreds of random shapes. A fixed seed keeps it reproducible across JVMs.
  @Test
  void randomSmallGridsMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(23880L);
    for (int trial = 0; trial < 400; trial++) {
      int n = 2 + rng.nextInt(5); // 2..6
      int k = 1 + rng.nextInt(3); // 1..3
      char[][] grid = randomGrid(rng, n, 0.25);
      String expected = Integer.toString(countValidPathsBrute(grid, k));
      assertThat(runMain(buildInput(k, grid)))
          .as("n=%d k=%d grid=%s", n, k, render(grid))
          .isEqualTo(expected);
    }
  }

  // --- Several sub-test cases in one run must each be answered independently. ---

  // Six random grids are concatenated into a single T = 6 input and run once; every output line is
  // compared to the oracle. This is the multi-sub-test analogue of the sweep above and specifically
  // guards the per-case reset: a solver that carries its memo table or grid buffer over from the
  // previous sub-test case (instead of clearing it) would corrupt later answers and fail here.
  @Test
  void multipleSubTestCasesInOneRunAreAnsweredIndependently() throws IOException {
    Random rng = new Random(238800L);
    int t = 6;
    char[][][] grids = new char[t][][];
    int[] ks = new int[t];
    for (int i = 0; i < t; i++) {
      int n = 2 + rng.nextInt(5); // 2..6
      ks[i] = 1 + rng.nextInt(3); // 1..3
      grids[i] = randomGrid(rng, n, 0.25);
    }
    assertThat(runMainLines(buildInput(ks, grids))).containsExactly(expectedAnswers(ks, grids));
  }

  // --- The maximum grid size is parsed and counted correctly within the time limit. ---

  // An open 50x50 grid with the maximum K = 3. For an empty grid the count of paths turning at most
  // three times has the closed form 2 + 2(N-2) + 2(N-2)^2 (one + two + three turns; see the
  // helper),
  // which for N = 50 is 4706 -- a multi-digit answer well inside int. The @Timeout guards against a
  // solution that mishandles the largest input, and the closed form is an oracle entirely unlike
  // the
  // intended recursion.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumSizeEmptyGridIsHandledWithinTheTimeLimit() throws IOException {
    int n = 50;
    int k = 3;
    char[][] grid = new char[n][n];
    for (char[] row : grid) {
      Arrays.fill(row, '.');
    }
    assertThat(runMain(buildInput(k, grid)))
        .isEqualTo(Integer.toString(emptyGridPathsWithAtMostThreeChanges(n)));
  }

  /**
   * Independent brute-force oracle: enumerates <em>every</em> monotone down/right path across
   * {@code grid} -- one per choice of which of the {@code 2(n-1)} moves are downs -- and counts
   * those that avoid all haybales and make at most {@code k} direction changes. Transparently
   * correct and therefore trustworthy only for tiny grids, and built by full enumeration rather
   * than the memoized recursion under test so agreement is genuine evidence.
   *
   * @implNote {@code O(2^(2(n-1)) * n)} time -- where {@code n} is the grid side length
   *     {@code grid.length} -- so callers must keep the grid small (here at most {@code 6x6}).
   */
  private static int countValidPathsBrute(char[][] grid, int k) {
    int n = grid.length;
    int moves = 2 * (n - 1);
    int downs = n - 1;
    int count = 0;
    for (int mask = 0; mask < (1 << moves); mask++) {
      if (Integer.bitCount(mask) == downs && isWalkable(grid, mask, moves, k)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Walks the path encoded by {@code mask} -- bit {@code i} set means move {@code i} is a down,
   * otherwise a right -- from the top-left corner, reporting whether it stays off haybales and
   * turns at most {@code k} times. The mask has exactly {@code n-1} set bits, so the walk
   * necessarily ends at the bottom-right corner.
   */
  private static boolean isWalkable(char[][] grid, int mask, int moves, int k) {
    int row = 0;
    int col = 0;
    int previousMove = -1;
    int changes = 0;
    for (int i = 0; i < moves; i++) {
      int move = (mask >> i) & 1; // 1 = down, 0 = right
      if (move == 1) {
        row++;
      } else {
        col++;
      }
      if (grid[row][col] == 'H') {
        return false;
      }
      if (previousMove != -1 && previousMove != move) {
        changes++;
      }
      previousMove = move;
    }
    return changes <= k;
  }

  /**
   * Closed-form oracle for an open {@code n x n} grid and {@code K = 3}: the number of monotone
   * paths turning at most three times is {@code 2 + 2(n-2) + 2(n-2)^2}. The three terms count the
   * paths with one turn ({@code D..R..} and {@code R..D..}, 2 of them), two turns (a single
   * interior run, {@code 2(n-2)}), and three turns (two independent interior split points,
   * {@code 2(n-2)^2}). For {@code n = 3} this gives {@code 6}, matching the all-paths sub-test
   * case.
   */
  private static int emptyGridPathsWithAtMostThreeChanges(int n) {
    return 2 + 2 * (n - 2) + 2 * (n - 2) * (n - 2);
  }

  /** Maps each sub-test case to its oracle answer as a string, preserving input order. */
  private static String[] expectedAnswers(int[] ks, char[][][] grids) {
    String[] expected = new String[grids.length];
    for (int i = 0; i < grids.length; i++) {
      expected[i] = Integer.toString(countValidPathsBrute(grids[i], ks[i]));
    }
    return expected;
  }

  /**
   * Builds an {@code n x n} grid of {@code '.'} and {@code 'H'}, each cell a haybale with
   * probability {@code hayProbability}, then forces the two corner cells empty as the problem
   * guarantees.
   */
  private static char[][] randomGrid(Random rng, int n, double hayProbability) {
    char[][] grid = new char[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = rng.nextDouble() < hayProbability ? 'H' : '.';
      }
    }
    grid[0][0] = '.';
    grid[n - 1][n - 1] = '.';
    return grid;
  }

  /** Builds BOJ 23880 input for a single sub-test case with budget {@code k} and the given grid. */
  private static String buildInput(int k, char[][] grid) {
    return buildInput(new int[] {k}, new char[][][] {grid});
  }

  /**
   * Builds BOJ 23880 input: {@code T} on the first line, then for each sub-test case a {@code "N
   * K"} line followed by the {@code N} grid rows.
   */
  private static String buildInput(int[] ks, char[][][] grids) {
    StringBuilder sb = new StringBuilder();
    sb.append(grids.length).append('\n');
    for (int t = 0; t < grids.length; t++) {
      char[][] grid = grids[t];
      sb.append(grid.length).append(' ').append(ks[t]).append('\n');
      for (char[] row : grid) {
        sb.append(row).append('\n');
      }
    }
    return sb.toString();
  }

  /** Renders a grid as slash-separated rows, for readable assertion failure messages. */
  private static String render(char[][] grid) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < grid.length; i++) {
      if (i > 0) {
        sb.append('/');
      }
      sb.append(grid[i]);
    }
    return sb.toString();
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
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

  /** Runs {@link Main} on {@code input}, returning stdout split into trimmed non-empty lines. */
  private static String[] runMainLines(String input) throws IOException {
    String captured = runMain(input);
    return captured.isEmpty() ? new String[0] : captured.split("\\R");
  }
}
