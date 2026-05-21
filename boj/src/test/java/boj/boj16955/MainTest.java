package boj.boj16955;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 16955 오목, 이길 수 있을까? (Omok, can I win?).
 *
 * <p>The input is a fixed 10x10 board where {@code '.'} is empty, {@code 'X'} is the player's
 * stone, and {@code 'O'} is the opponent's stone. The answer is {@code 1} iff there exists an empty
 * cell such that turning it into {@code 'X'} produces a run of <em>five or more</em> consecutive
 * {@code 'X'} stones horizontally, vertically, or along either diagonal; otherwise {@code 0}. Note
 * the rule is "five or more": an overline of six still wins, unlike strict Renju.
 *
 * <p>The board is always exactly 10 lines of 10 characters and there is no count line. Every
 * expected value below is hand-derived against that model and cross-checked across two independent
 * problem mirrors (the acmicpc.net site being offline at authoring time).
 */
class MainTest {

  /** A fully empty board row; usable in {@code @StdIo} because it is a constant expression. */
  private static final String E = "..........";

  // --- Horizontal: completing a run from the right end. ---

  @Test
  @StdIo({"XXXX......", E, E, E, E, E, E, E, E, E})
  void horizontalFourThenGapOnTheRightCompletesFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({".XXXX.....", E, E, E, E, E, E, E, E, E})
  void horizontalStoneOnTheLeftExtendsFourIntoFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Horizontal: filling an interior gap that bridges two short runs. ---

  @Test
  @StdIo({"XX.XX.....", E, E, E, E, E, E, E, E, E})
  void horizontalTwoGapTwoIsBridgedIntoFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"XXX.X.....", E, E, E, E, E, E, E, E, E})
  void horizontalThreeGapOneIsBridgedIntoFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Vertical. ---

  @Test
  @StdIo({"X.........", "X.........", "X.........", "X.........", E, E, E, E, E, E})
  void verticalFourCompletesFiveWhenTheCellBelowIsFilled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Diagonals: both orientations, including an interior gap. ---

  @Test
  @StdIo({"X.........", ".X........", "..X.......", "...X......", E, E, E, E, E, E})
  void mainDiagonalFourCompletesFiveAtTheLowerEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"....X.....", "...X......", "..X.......", ".X........", E, E, E, E, E, E})
  void antiDiagonalFourCompletesFiveAtTheLowerEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"X.........", ".X........", E, "...X......", "....X.....", E, E, E, E, E})
  void mainDiagonalInteriorGapIsBridgedIntoFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- "Five or more": an overline of six still wins under this problem's rule. ---

  @Test
  @StdIo({"XXXX.X....", E, E, E, E, E, E, E, E, E})
  void fillingTheGapToMakeSixInARowStillCountsAsAWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Boundary: the winning run ends exactly in the bottom-right corner. ---

  @Test
  @StdIo({E, E, E, E, E, ".........X", ".........X", ".........X", ".........X", E})
  void verticalRunCompletingAtTheBottomRightCornerIsAWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A genuine win must not be suppressed by unrelated opponent stones. ---

  @Test
  @StdIo({"XXXX.....O", E, E, E, E, E, E, E, E, E})
  void opponentStoneElsewhereDoesNotHideAnAvailableWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Negative: no single placement can reach five. ---

  @Test
  @StdIo({E, E, E, E, E, E, E, E, E, E})
  void emptyBoardCannotWinWithOnlyOneStone(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"XXX.......", E, E, E, E, E, E, E, E, E})
  void threeInARowCanReachOnlyFourSoNoWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"XX.X......", E, E, E, E, E, E, E, E, E})
  void bridgingTwoAndOneReachesOnlyFourSoNoWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"XXOXX.....", E, E, E, E, E, E, E, E, E})
  void opponentStoneInTheMiddleBlocksTheOnlyPathToFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Degenerate: board already has five before the move. ---
  // The whole-board scan means a pre-existing five counts as a win; this pins that decision.

  @Test
  @StdIo({"XXXXX.....", E, E, E, E, E, E, E, E, E})
  void preExistingFiveWithAnEmptyCellElsewhereIsTreatedAsAWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }
}
