package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class NumberOfPathsWithMaxScoreTest {
  NumberOfPathsWithMaxScore sut = new NumberOfPathsWithMaxScore();

  // Step 1: smallest board (2x2) — of the three moves from S, going through the '2'
  //         corner beats both the '1' corner and the empty diagonal
  @Test
  void smallestBoardCollectsTheRicherCorner() {
    assertThat(sut.pathsWithMaxScore(List.of("E1", "2S"))).containsExactly(2, 1);
  }

  // Step 2: equal corners tie at the max score — both routes must be counted
  @Test
  void tiedCornersCountBothPaths() {
    assertThat(sut.pathsWithMaxScore(List.of("E1", "1S"))).containsExactly(1, 2);
  }

  // Step 3: both orthogonal neighbors of S are obstacles, but the diagonal jumps
  //         straight to E — a valid path that collects nothing is [0,1], not [0,0]
  @Test
  void diagonalEscapesWhenBothCornersAreBlocked() {
    assertThat(sut.pathsWithMaxScore(List.of("EX", "XS"))).containsExactly(0, 1);
  }

  // Step 4: LeetCode Example 3 — a solid obstacle row severs the board; no path
  //         at all yields [0,0]
  @Test
  void fullyBlockedRowHasNoPath() {
    assertThat(sut.pathsWithMaxScore(List.of("E11", "XXX", "11S"))).containsExactly(0, 0);
  }

  // Step 5: all-ones 3x3 — a diagonal move skips a scoring cell, so only the six
  //         pure up/left lattice paths reach the max of 3
  @Test
  void allOnesBoardCountsEveryLongestPath() {
    assertThat(sut.pathsWithMaxScore(List.of("E11", "111", "11S"))).containsExactly(3, 6);
  }

  // Step 6: obstacles force a zigzag where every move is diagonal — up and left
  //         alone can never reach E here
  @Test
  void diagonalOnlyCorridorIsTheSolePath() {
    assertThat(sut.pathsWithMaxScore(List.of("EX1", "X1X", "1XS"))).containsExactly(1, 1);
  }

  // Step 7: the max must chain through both 9s — five other length-3 routes all
  //         score 11 and must lose to the single 19 route
  @Test
  void bestPathCollectsBothNines() {
    assertThat(sut.pathsWithMaxScore(List.of("E91", "111", "19S"))).containsExactly(19, 1);
  }

  // Step 8: LeetCode Example 1 — the center obstacle splits the board and only the
  //         top-right route reaches the max of 7
  @Test
  void leetCodeExample1() {
    assertThat(sut.pathsWithMaxScore(List.of("E23", "2X2", "12S"))).containsExactly(7, 1);
  }

  // Step 9: LeetCode Example 2 — symmetric detours around the center obstacle tie
  //         at 4, so both are counted
  @Test
  void leetCodeExample2() {
    assertThat(sut.pathsWithMaxScore(List.of("E12", "1X1", "21S"))).containsExactly(4, 2);
  }

  // Step 10: all-ones 4x4 — the count of max-score paths grows as the central
  //          binomial C(6,3) = 20, so counts must accumulate, not just propagate
  @Test
  void fourByFourAllOnesCountsTwentyPaths() {
    assertThat(sut.pathsWithMaxScore(List.of("E111", "1111", "1111", "111S")))
        .containsExactly(5, 20);
  }

  // Step 11: the upper constraint bound (100x100 all ones) — 197 scoring cells on
  //          every longest path and C(198,99) such paths, which only fits after
  //          reduction modulo 1e9+7
  @Test
  void maximumBoardAppliesTheModulusToTheCount() {
    List<String> board = new ArrayList<>();
    board.add("E" + "1".repeat(99));
    for (int i = 0; i < 98; i++) {
      board.add("1".repeat(100));
    }
    board.add("1".repeat(99) + "S");
    assertThat(sut.pathsWithMaxScore(board)).containsExactly(197, 690285631);
  }
}
