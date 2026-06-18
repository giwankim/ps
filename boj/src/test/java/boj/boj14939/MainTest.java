package boj.boj14939;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 14939 불 끄기 (Lights Out) -- press switches on a 10x10 grid of bulbs until every bulb is off,
 * using as few presses as possible.
 *
 * <p>Input: 10 lines of 10 characters each, where {@code O} (capital letter o) is a lit bulb and
 * {@code #} is an unlit one; no other character appears. Pressing the switch at a cell toggles that
 * bulb together with its up, down, left, and right neighbors -- a five-cell "plus" clamped at the
 * borders, so an edge press toggles four cells and a corner press only three. Output: the minimum
 * number of presses that turns the whole grid off, or {@code -1} if that is impossible.
 *
 * <p>Pressing a switch twice is the same as never pressing it (two toggles cancel), so an optimal
 * solution presses each switch at most once and the order of presses does not matter. That makes
 * the answer a count of distinct switches. The standard solver fixes the first row's presses by
 * brute force -- {@code 2^10 = 1024} choices -- and then has no remaining freedom: once row
 * {@code i} is settled, the only switch that can still flip a bulb in row {@code i} without
 * disturbing the rows above is the one directly below it, so a lit bulb in row {@code i} forces a
 * press in row {@code i + 1}. After chasing the forced presses down all ten rows, the configuration
 * is solvable for that seed exactly when the last row came out clear; the minimum press count over
 * the 1024 seeds is the answer.
 *
 * <p>There is no {@code -1} fixture here, and that is deliberate. Over GF(2) the 100x100 toggle
 * matrix of the 10x10 grid is full rank (nullity 0), so every one of the 2^100 boards is solvable
 * and a correct solver never prints {@code -1}. The branch exists only to honor the statement; no
 * valid 10x10 input can exercise it, so no test can assert it.
 *
 * <p>The single-press fixtures pin the toggle shape and its border clamping from worked examples: a
 * lit interior plus clears in one interior press ({@link #singleInteriorPlusClearsInOnePress}), a
 * three-cell corner pattern in one corner press ({@link #cornerSwitchPlusIsClampedToThreeCells}),
 * and a four-cell top-edge pattern in one edge press
 * ({@link #topEdgeSwitchPlusIsClampedToFourCells}). Because none of these boards is already clear
 * the minimum is at least one, and exhibiting a one-press solution makes it exactly one. Two lit
 * patterns sitting in disjoint corners cannot both be cleared by a single plus, so they need two
 * presses ({@link #twoDisjointCornerPatternsNeedTwoPresses}).
 *
 * <p>The larger golden values come from the chase machinery, not from hand counting: a lone lit
 * bulb costs a surprising 33 presses ({@link #loneLitBulbNeedsThirtyThreePresses}), an all-lit
 * board costs 44 ({@link #everyBulbOnNeedsFortyFourPresses}), and a fixed pseudo-random board costs
 * 54 ({@link #fixedMediumBoardNeedsFiftyFourPresses}). Each was computed by a reference solver and
 * is a fixed property of the problem, so it pins the full first-row-search-plus-chase against
 * regressions.
 *
 * <p>The randomized cases drive whole boards through stdin/stdout and cross-check the program
 * against an independent oracle that brute-forces the <em>last</em> row and chases <em>upward</em>
 * -- the mirror image of the intended top-down solver, so the two routes share no edge-handling
 * code and disagree on any off-by-one at a border.
 */
class MainTest {

  // --- The two trivial extremes: nothing to do, and the official worked example. ---

  @Test
  @StdIo({
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
  })
  void boardAlreadyClearNeedsNoPresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every bulb is already off, so the empty set of presses solves it and the minimum is zero.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({
    "#O########",
    "OOO#######",
    "#O########",
    "####OO####",
    "###O##O###",
    "####OO####",
    "##########",
    "########O#",
    "#######OOO",
    "########O#",
  })
  void officialSampleNeedsFourPresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The statement's sample: the lit cells form four separate plus-shaped clusters, each cleared
    // by pressing its center switch once, for four presses total.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Single presses: the "plus" toggle shape and how it clamps at the borders. ---

  @Test
  @StdIo({
    "#O########",
    "OOO#######",
    "#O########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
  })
  void singleInteriorPlusClearsInOnePress(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The lit cells are exactly the five-cell plus toggled by the interior switch at row 1, col 1.
    // Pressing it once turns all five off, and since the board was not already clear the minimum is
    // one.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({
    "OO########",
    "O#########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
  })
  void cornerSwitchPlusIsClampedToThreeCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The top-left switch has no up or left neighbor, so its plus clamps to just three cells --
    // (0,0), (0,1), (1,0). Those are exactly the lit bulbs, so one press clears the board.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({
    "####OOO###",
    "#####O####",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
  })
  void topEdgeSwitchPlusIsClampedToFourCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A switch on the top edge (row 0, col 5) has no up neighbor, so its plus clamps to four cells
    // -- (0,4), (0,5), (0,6), (1,5). Those are exactly the lit bulbs, so one press clears the
    // board.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({
    "OO########",
    "O#########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "#########O",
    "########OO",
  })
  void twoDisjointCornerPatternsNeedTwoPresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The top-left and bottom-right corner patterns sit in disjoint neighborhoods, so no single
    // plus reaches both; clearing each takes its own corner press, for two presses total.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Golden press counts from the full chase: not hand-derivable, fixed by a reference solver.
  // ---

  @Test
  @StdIo({
    "O#########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
    "##########",
  })
  void loneLitBulbNeedsThirtyThreePresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A single lit corner bulb is still solvable -- the grid is full rank -- but expensively: the
    // forced chase from the only seed that clears the board presses 33 switches.
    assertThat(out.capturedString().trim()).isEqualTo("33");
  }

  @Test
  @StdIo({
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
    "OOOOOOOOOO",
  })
  void everyBulbOnNeedsFortyFourPresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The fully lit board is solvable in 44 presses -- the minimum over all 1024 first-row seeds.
    assertThat(out.capturedString().trim()).isEqualTo("44");
  }

  @Test
  @StdIo({
    "O#OOO##OO#",
    "O#O####OO#",
    "##OO##OO##",
    "O#O##OO##O",
    "OOO#OO#O##",
    "OO#O#OOO##",
    "#OOOOOOO##",
    "###O###O##",
    "#O#O####OO",
    "###OO#O###",
  })
  void fixedMediumBoardNeedsFiftyFourPresses(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A fixed pseudo-random board with no special structure, included as a medium-weight regression
    // anchor; the reference solver puts its minimum at 54 presses.
    assertThat(out.capturedString().trim()).isEqualTo("54");
  }

  // --- Randomized scale, cross-checked against an independent bottom-up oracle. ---

  @Test
  void randomBalancedBoardMatchesBottomUpOracle() throws IOException {
    // A 50/50 board is the generic case: the answer lands in the middle of the range and the chase
    // runs to full depth on most of the 1024 seeds.
    boolean[][] board = randomBoard(new Random(14939L), 0.5);
    assertThat(runMain(buildInput(board))).isEqualTo(String.valueOf(solve(board)));
  }

  @Test
  void randomSparseBoardMatchesBottomUpOracle() throws IOException {
    // A sparse board (few lit bulbs) tends toward small answers, stressing the seeds that press
    // almost nothing in the first row.
    boolean[][] board = randomBoard(new Random(114939L), 0.2);
    assertThat(runMain(buildInput(board))).isEqualTo(String.valueOf(solve(board)));
  }

  @Test
  void randomDenseBoardMatchesBottomUpOracle() throws IOException {
    // A dense board (most bulbs lit) pushes the answer toward the high end, exercising the seeds
    // that press many first-row switches before chasing down.
    boolean[][] board = randomBoard(new Random(1414939L), 0.85);
    assertThat(runMain(buildInput(board))).isEqualTo(String.valueOf(solve(board)));
  }

  /**
   * Independent oracle: brute-force the last row's presses ({@code 2^10 = 1024} choices) and chase
   * upward. Once row {@code i + 1} is settled, the only switch that can still flip one of its bulbs
   * without disturbing the rows below is the one directly above it, so a lit bulb in row {@code i +
   * 1} forces a press in row {@code i}. After the sweep the seed solves the board exactly when the
   * top row is clear; the answer is the minimum press count over all seeds. This mirrors the
   * intended top-down solver across the horizontal axis, sharing none of its edge-handling code.
   * Returns {@code -1} if no seed clears the board, though for a full-rank 10x10 grid that never
   * happens.
   */
  private static int solve(boolean[][] board) {
    int best = Integer.MAX_VALUE;
    for (int seed = 0; seed < (1 << 10); seed++) {
      boolean[][] g = copyBoard(board);
      int presses = 0;
      for (int j = 0; j < 10; j++) {
        if ((seed >> j & 1) == 1) {
          pressSwitch(g, 9, j);
          presses++;
        }
      }
      for (int i = 8; i >= 0; i--) {
        for (int j = 0; j < 10; j++) {
          if (g[i + 1][j]) {
            pressSwitch(g, i, j);
            presses++;
          }
        }
      }
      boolean topClear = true;
      for (int j = 0; j < 10; j++) {
        if (g[0][j]) {
          topClear = false;
          break;
        }
      }
      if (topClear) {
        best = Math.min(best, presses);
      }
    }
    return best == Integer.MAX_VALUE ? -1 : best;
  }

  /** Toggles the bulb at {@code (r, c)} and each of its in-bounds up/down/left/right neighbors. */
  private static void pressSwitch(boolean[][] g, int r, int c) {
    int[][] deltas = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] step : deltas) {
      int nr = r + step[0];
      int nc = c + step[1];
      if (nr >= 0 && nr < 10 && nc >= 0 && nc < 10) {
        g[nr][nc] = !g[nr][nc];
      }
    }
  }

  private static boolean[][] copyBoard(boolean[][] board) {
    boolean[][] g = new boolean[10][];
    for (int i = 0; i < 10; i++) {
      g[i] = board[i].clone();
    }
    return g;
  }

  /** A 10x10 board with each bulb independently lit with probability {@code density}. */
  private static boolean[][] randomBoard(Random rng, double density) {
    boolean[][] board = new boolean[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        board[i][j] = rng.nextDouble() < density;
      }
    }
    return board;
  }

  /**
   * Builds BOJ 14939 input: ten lines of ten characters, {@code O} for a lit bulb and {@code #}.
   */
  private static String buildInput(boolean[][] board) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        sb.append(board[i][j] ? 'O' : '#');
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
