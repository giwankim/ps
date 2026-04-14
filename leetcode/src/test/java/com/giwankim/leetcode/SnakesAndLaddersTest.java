package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SnakesAndLaddersTest {
  SnakesAndLadders sut = new SnakesAndLadders();

  // Step 1: smallest board — 2×2 with no snakes or ladders.
  // From label 1 we roll 3 to reach label 4 in a single move.
  @Test
  void twoByTwoBoardReachedInOneMove() {
    int[][] board = {{-1, -1}, {-1, -1}};
    assertThat(sut.snakesAndLadders(board)).isOne();
    assertThat(sut.snakesAndLadders2(board)).isOne();
  }

  // Step 2: 3×3 empty board — target 9 is out of reach of a single roll
  // (max is 1 + 6 = 7), so BFS needs at least two hops: 1 → 7 → 9.
  @Test
  void threeByThreeBoardReachedInTwoMoves() {
    int[][] board = {
      {-1, -1, -1},
      {-1, -1, -1},
      {-1, -1, -1},
    };
    assertThat(sut.snakesAndLadders(board)).isEqualTo(2);
    assertThat(sut.snakesAndLadders2(board)).isEqualTo(2);
  }

  // Step 3: a ladder from label 2 to label 9 (the end).
  // Forces label ↔ (row, col) conversion and board[r][c] jump logic.
  //   label layout:       board array:
  //     7  8  9           {-1, -1, -1}
  //     6  5  4           {-1, -1, -1}
  //     1  2  3           {-1,  9, -1}  ← (2,1) is label 2
  @Test
  void ladderFromLabelTwoReachesEndInOneMove() {
    int[][] board = {
      {-1, -1, -1},
      {-1, -1, -1},
      {-1, 9, -1},
    };
    assertThat(sut.snakesAndLadders(board)).isOne();
    assertThat(sut.snakesAndLadders2(board)).isOne();
  }

  // Step 4: labels 3–7 all snake back to label 1, so the only forward
  // progress from 1 is roll 1 → 2, and from 2 the only non-snake
  // landing in range is roll 6 → 8. Optimal: 1 → 2 → 8 → 9.
  //   snakes: 3→1, 4→1, 5→1, 6→1, 7→1
  @Test
  void snakesForceThreeMoveDetour() {
    int[][] board = {
      {1, -1, -1},
      {1, 1, 1},
      {-1, -1, 1},
    };
    assertThat(sut.snakesAndLadders(board)).isEqualTo(3);
    assertThat(sut.snakesAndLadders2(board)).isEqualTo(3);
  }

  // Step 5: every square reachable from label 1 (labels 2–7) snakes
  // back to 1. From 1 the max roll reaches 7 — every destination
  // loops, so labels 8 and 9 are unreachable.
  //   snakes: 2→1, 3→1, 4→1, 5→1, 6→1, 7→1
  @Test
  void unreachableBoardReturnsMinusOne() {
    int[][] board = {
      {1, -1, -1},
      {1, 1, 1},
      {-1, 1, 1},
    };
    assertThat(sut.snakesAndLadders(board)).isEqualTo(-1);
    assertThat(sut.snakesAndLadders2(board)).isEqualTo(-1);
  }

  // Step 6: canonical LeetCode example (6×6). Optimal 4-move path uses
  // both a ladder and a snake strategically:
  //   1 → 2 (ladder→15) → 17 (snake→13) → 14 (ladder→35) → 36
  @Test
  void leetCodeExampleSixBySixBoard() {
    int[][] board = {
      {-1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1},
      {-1, 35, -1, -1, 13, -1},
      {-1, -1, -1, -1, -1, -1},
      {-1, 15, -1, -1, -1, -1},
    };
    assertThat(sut.snakesAndLadders(board)).isEqualTo(4);
    assertThat(sut.snakesAndLadders2(board)).isEqualTo(4);
  }
}
