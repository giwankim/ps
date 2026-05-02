package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NQueensIITest {
  NQueensII sut = new NQueensII();

  // smallest valid input — a single queen on a 1x1 board
  @Test
  void n1ReturnsOneSolution() {
    assertThat(sut.totalNQueens(1)).isOne();
    assertThat(sut.totalNQueens2(1)).isOne();
  }

  // n=2 has no valid placement — forces an actual attack check, not a hardcoded 1
  @Test
  void n2ReturnsZeroSolutions() {
    assertThat(sut.totalNQueens(2)).isZero();
    assertThat(sut.totalNQueens2(2)).isZero();
  }

  // n=3 also has no valid placement — rules out an "n == 2 is the only dead case" shortcut
  @Test
  void n3ReturnsZeroSolutions() {
    assertThat(sut.totalNQueens(3)).isZero();
    assertThat(sut.totalNQueens2(3)).isZero();
  }

  // n=4 — smallest board with multiple valid placements (LeetCode Example 1)
  @Test
  void n4ReturnsTwoSolutions() {
    assertThat(sut.totalNQueens(4)).isEqualTo(2);
    assertThat(sut.totalNQueens2(4)).isEqualTo(2);
  }

  // n=5 — ten solutions, exercises diagonal bookkeeping on an odd-sized board
  @Test
  void n5ReturnsTenSolutions() {
    assertThat(sut.totalNQueens(5)).isEqualTo(10);
    assertThat(sut.totalNQueens2(5)).isEqualTo(10);
  }

  // n=6 — count drops back to 4, breaking any monotonic-in-n assumption
  @Test
  void n6ReturnsFourSolutions() {
    assertThat(sut.totalNQueens(6)).isEqualTo(4);
    assertThat(sut.totalNQueens2(6)).isEqualTo(4);
  }

  // n=7 — 40 solutions, verifies scaling to mid-sized boards
  @Test
  void n7ReturnsFortySolutions() {
    assertThat(sut.totalNQueens(7)).isEqualTo(40);
    assertThat(sut.totalNQueens2(7)).isEqualTo(40);
  }

  // n=8 — the classic 8-queens problem, 92 solutions (LeetCode Example 2)
  @Test
  void n8ReturnsNinetyTwoSolutions() {
    assertThat(sut.totalNQueens(8)).isEqualTo(92);
    assertThat(sut.totalNQueens2(8)).isEqualTo(92);
  }

  // n=9 — upper bound of the LeetCode constraint (1 <= n <= 9)
  @Test
  void n9ReturnsThreeHundredFiftyTwoSolutions() {
    assertThat(sut.totalNQueens(9)).isEqualTo(352);
    assertThat(sut.totalNQueens2(9)).isEqualTo(352);
  }

  // consistency: the count must match NQueens.solveNQueens(n).size() for every valid n,
  // turning the per-n magic numbers above into a derived invariant
  @Test
  void countMatchesNQueensSolutionCountForAllN() {
    NQueens reference = new NQueens();
    for (int n = 1; n <= 9; n++) {
      assertThat(sut.totalNQueens(n))
          .as("n=%d", n)
          .isEqualTo(reference.solveNQueens(n).size());
      assertThat(sut.totalNQueens2(n))
          .as("n=%d", n)
          .isEqualTo(reference.solveNQueens(n).size());
    }
  }

  // idempotency: repeated invocations must return the same count — guards against any future
  // refactor that moves search state into instance fields
  @Test
  void repeatedInvocationsReturnSameCount() {
    int first = sut.totalNQueens(8);
    int second = sut.totalNQueens(8);
    assertThat(first).isEqualTo(second);

    int first2 = sut.totalNQueens2(8);
    int second2 = sut.totalNQueens2(8);
    assertThat(first2).isEqualTo(second2);
  }
}
