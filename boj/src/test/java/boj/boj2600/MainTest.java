package boj.boj2600;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2600 구슬게임 (Marble Game) -- a two-pile, take-away game decided by game-theory DP.
 *
 * <p>Players A and B alternate turns, A moving first. There are two jars of marbles. On a turn the
 * mover picks <em>one</em> jar and removes exactly {@code b1}, {@code b2}, or {@code b3} marbles
 * from it (the jar must hold at least that many); a turn can never touch both jars. The player who,
 * on their turn, cannot make any legal move loses, and the opponent wins. Both players play
 * optimally.
 *
 * <p>A position {@code (k1, k2)} is a <strong>win</strong> for the player to move iff some legal
 * move leaves the opponent in a <strong>losing</strong> position; the base case is "no legal move
 * available", which is a loss for the mover. Because every move affects exactly one jar, the game
 * is the Sprague-Grundy sum of two independent single-pile subtraction games, so equivalently the
 * mover (A) wins iff {@code grundy(k1) XOR grundy(k2) != 0}. The two views agree on every reachable
 * board; the cases below pin the winner down without assuming either implementation strategy.
 *
 * <p>Input: the first line holds the three take amounts {@code b1 b2 b3} with {@code 1 <= b1 < b2 <
 * b3 <= 30}; each of the next five lines holds a query {@code k1 k2} with {@code 1 <= k1, k2 <=
 * 500}. Output is five lines, the winner ("A" or "B") of each query in order.
 *
 * <p>Two invariants anchor several cases independent of any oracle. <em>Equal jars are always a
 * second-player win:</em> whatever A removes from one jar, B removes the same count from the other,
 * restoring equality forever, so A is the first to run dry -- this holds for every bead set.
 * <em>Both jars below the smallest bead is an immediate first-player loss:</em> holding marbles is
 * not the same as having a legal move, so even with {@code k >= 1} the mover can be stuck at the
 * very first turn. The statement's worked example uses beads {@code {1, 3, 4}}: jars {@code (4, 1)}
 * are won by A, while {@code (5, 5)} are won by B.
 */
class MainTest {

  // --- Statement sample: beads {1, 3, 4}, anchored on the two pairs named in the problem. ---

  @Test
  @StdIo({"1 3 4", "4 1", "5 5", "3 4", "2 2", "7 1"})
  void statementSampleSelectsTheWinnerForEachOfTheFiveQueries(StdOut out) throws IOException {
    Main.main(new String[0]);
    // (4,1) and (5,5) are the statement's own answers; the rest follow from the same recursion:
    // (3,4) A can leave the opponent stuck, (2,2) equal jars favour B, (7,1) A wins.
    assertThat(out.capturedLines()).containsExactly("A", "B", "A", "B", "A");
  }

  // --- Jar order is irrelevant: the board is symmetric, so swapping k1 and k2 keeps the winner.
  // ---

  @Test
  @StdIo({"1 3 4", "4 1", "1 4", "7 2", "2 7", "3 4"})
  void swappingTheTwoJarsLeavesTheWinnerUnchanged(StdOut out) throws IOException {
    Main.main(new String[0]);
    // (4,1)/(1,4) both go to A and (7,2)/(2,7) both go to B -- the mirror produces identical
    // verdicts, exposing any implementation that indexes its table asymmetrically.
    assertThat(out.capturedLines()).containsExactly("A", "A", "B", "B", "A");
  }

  // --- Equal jars: B mirrors A's every move, so the second player always wins. ---

  @Test
  @StdIo({"1 3 4", "2 2", "4 4", "5 5", "7 7", "9 9"})
  void equalJarsAlwaysLetTheSecondPlayerWinByMirroring(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Whatever A draws from one jar, B copies in the other, preserving equality until A is stuck.
    assertThat(out.capturedLines()).containsExactly("B", "B", "B", "B", "B");
  }

  // --- The first player can be stuck immediately when both jars hold fewer than the smallest bead.

  @Test
  @StdIo({"2 3 4", "1 1", "1 2", "2 1", "2 2", "1 3"})
  void whenNeitherJarReachesTheSmallestBeadTheFirstPlayerLosesAtOnce(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // (1,1): no jar reaches the minimum draw of 2, so A cannot open and loses -> B. As soon as one
    // jar can be emptied to leave the opponent stuck -- (1,2),(2,1),(1,3) -- A takes it; (2,2) is
    // an
    // equal-jar mirror win for B.
    assertThat(out.capturedLines()).containsExactly("B", "A", "A", "B", "A");
  }

  // --- A jar below every bead is dead weight: a larger minimum bead leaves small jars unplayable.

  @Test
  @StdIo({"3 5 7", "1 1", "2 2", "3 3", "7 7", "10 3"})
  void jarsBelowEveryBeadActAsThoughEmpty(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With a minimum draw of 3, (1,1) and (2,2) are unplayable first turns -> B; the equal jars
    // (3,3) and (7,7) mirror to B; only the unequal (10,3) gives A a winning opening.
    assertThat(out.capturedLines()).containsExactly("B", "B", "B", "B", "A");
  }

  // --- The full {1,2,3} draw set, mixing equal-jar losses with a single unequal winning pair. ---

  @Test
  @StdIo({"1 2 3", "1 1", "2 2", "3 3", "1 2", "500 500"})
  void takeOneTwoOrThreeReducesToParityOfTheJarDifference(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Beads {1,2,3} give single-pile Grundy values that repeat with period four, so every equal
    // pair -- including the maximal (500,500) -- is a B win, and only the unequal (1,2) lets A move
    // the opponent into a loss.
    assertThat(out.capturedLines()).containsExactly("B", "B", "B", "A", "B");
  }

  // --- Upper jar bound: queries pushed to the maximum 500 marbles. ---

  @Test
  @StdIo({"1 3 4", "500 500", "500 499", "499 499", "1 500", "500 1"})
  void maximumJarSizesAreResolvedAtTheFiveHundredBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The (500,500) and (499,499) equal jars fall to B; the off-by-one (500,499) and the lopsided
    // (1,500)/(500,1) are decided by the recursion -- A wins the asymmetric off-by-one, B holds the
    // rest. Any table sized below 501 would overflow on these.
    assertThat(out.capturedLines()).containsExactly("B", "A", "B", "B", "B");
  }

  // --- Upper bead bound: the three draws crowd the top of [1, 30]. ---

  @Test
  @StdIo({"28 29 30", "1 1", "30 30", "29 1", "60 30", "500 500"})
  void largestBeadValuesNearThirtyAreHandled(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With draws of 28/29/30, (1,1) is an instant loss for A and the equal (30,30) and (500,500)
    // mirror to B; (29,1) lets A empty a jar and strand B, and (60,30) is likewise an A win.
    assertThat(out.capturedLines()).containsExactly("B", "B", "A", "A", "B");
  }

  // --- Independent oracle at the constraint ceiling: bottom-up board cross-checks the solver. ---

  @Test
  void generatedQueriesAtTheBoundMatchTheBottomUpOracle() throws IOException {
    int[] beads = {2, 5, 9}; // a draw set used by no other case, exercising fresh Grundy values.
    int[][] queries = {{500, 500}, {500, 1}, {1, 500}, {499, 500}, {317, 288}};
    // Cross-check the program against an independent O(maxK^2) win/lose board built here.
    assertThat(runMain(buildInput(beads, queries)))
        .containsExactly(expectedWinners(beads, queries, 500));
  }

  /** Builds BOJ 2600 input: the three beads on the first line, then one {@code k1 k2} per line. */
  private static String buildInput(int[] beads, int[][] queries) {
    StringBuilder sb = new StringBuilder();
    sb.append(beads[0])
        .append(' ')
        .append(beads[1])
        .append(' ')
        .append(beads[2])
        .append('\n');
    for (int[] q : queries) {
      sb.append(q[0]).append(' ').append(q[1]).append('\n');
    }
    return sb.toString();
  }

  /**
   * Independent bottom-up oracle: {@code win[i][j]} is true iff the player to move wins with
   * {@code i} and {@code j} marbles, mirroring the reference game-theory recurrence.
   */
  private static String[] expectedWinners(int[] beads, int[][] queries, int maxK) {
    int[] draws = beads.clone();
    Arrays.sort(draws);
    boolean[][] win = new boolean[maxK + 1][maxK + 1];
    for (int i = 0; i <= maxK; i++) {
      for (int j = 0; j <= maxK; j++) {
        boolean canWin = false;
        for (int d = 0; d < draws.length && !canWin; d++) {
          if (i >= draws[d] && !win[i - draws[d]][j]) {
            canWin = true;
          }
          if (j >= draws[d] && !win[i][j - draws[d]]) {
            canWin = true;
          }
        }
        win[i][j] = canWin;
      }
    }
    String[] expected = new String[queries.length];
    for (int q = 0; q < queries.length; q++) {
      expected[q] = win[queries[q][0]][queries[q][1]] ? "A" : "B";
    }
    return expected;
  }

  /** Runs {@link Main} against a generated input, returning stdout split into trimmed lines. */
  private static String[] runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim().split("\\R");
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
