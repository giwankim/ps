package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NQueensTest {
  NQueens sut = new NQueens();

  // smallest valid input — a single queen on a 1x1 board
  @Test
  void n1ReturnsOneSolution() {
    assertThat(sut.solveNQueens(1)).containsExactly(List.of("Q"));
  }

  // n=2 has no valid placement — forces an actual attack check, not a hardcoded board
  @Test
  void n2ReturnsNoSolutions() {
    assertThat(sut.solveNQueens(2)).isEmpty();
  }

  // n=3 also has no valid placement — rules out an "n == 2 is the only dead case" shortcut
  @Test
  void n3ReturnsNoSolutions() {
    assertThat(sut.solveNQueens(3)).isEmpty();
  }

  // n=4 — smallest board with multiple valid placements (LeetCode Example 1)
  @Test
  void n4ReturnsTwoSolutions() {
    assertThat(sut.solveNQueens(4))
        .containsExactlyInAnyOrder(
            List.of(".Q..", "...Q", "Q...", "..Q."), List.of("..Q.", "Q...", "...Q", ".Q.."));
  }

  // n=5 — ten solutions, exercises diagonal bookkeeping on an odd-sized board
  @Test
  void n5ReturnsTenSolutions() {
    assertThat(sut.solveNQueens(5)).hasSize(10);
  }

  // n=6 — count drops back to 4, breaking any monotonic-in-n assumption
  @Test
  void n6ReturnsFourSolutions() {
    assertThat(sut.solveNQueens(6)).hasSize(4);
  }

  // n=7 — 40 solutions, verifies scaling to mid-sized boards
  @Test
  void n7ReturnsFortySolutions() {
    assertThat(sut.solveNQueens(7)).hasSize(40);
  }

  // n=8 — the classic 8-queens problem, 92 solutions (LeetCode Example 2)
  @Test
  void n8ReturnsNinetyTwoSolutions() {
    assertThat(sut.solveNQueens(8)).hasSize(92);
  }

  // n=9 — upper bound of the LeetCode constraint (1 <= n <= 9)
  @Test
  void n9ReturnsThreeHundredFiftyTwoSolutions() {
    assertThat(sut.solveNQueens(9)).hasSize(352);
  }

  // shape: every n=5 solution is a list of n strings, each of length n
  @Test
  void everyN5SolutionHasNRowsOfLengthN() {
    assertThat(sut.solveNQueens(5)).allSatisfy(board -> {
      assertThat(board).hasSize(5);
      assertThat(board).allSatisfy(row -> assertThat(row).hasSize(5));
    });
  }

  // alphabet & arity: every row has exactly one 'Q' and (n-1) '.' characters
  @Test
  void everyN5SolutionHasOneQueenPerRow() {
    assertThat(sut.solveNQueens(5))
        .allSatisfy(board -> assertThat(board).allSatisfy(row -> {
          assertThat(row.chars().filter(c -> c == 'Q').count()).isEqualTo(1L);
          assertThat(row.chars().filter(c -> c == '.').count()).isEqualTo(4L);
        }));
  }

  // semantics: no two queens attack via column, ↘ diagonal, or ↗ anti-diagonal
  @Test
  void everyN8SolutionIsAValidPlacement() {
    assertThat(sut.solveNQueens(8)).allSatisfy(NQueensTest::assertIsValidQueensPlacement);
  }

  // distinctness: count alone can't catch a solver that returns duplicates
  @Test
  void allN8SolutionsAreDistinct() {
    assertThat(sut.solveNQueens(8)).doesNotHaveDuplicates();
  }

  private static void assertIsValidQueensPlacement(List<String> board) {
    int n = board.size();
    Set<Integer> cols = new HashSet<>();
    Set<Integer> diag = new HashSet<>(); // row - col
    Set<Integer> antiDiag = new HashSet<>(); // row + col
    for (int row = 0; row < n; row++) {
      int col = board.get(row).indexOf('Q');
      assertThat(cols.add(col)).isTrue();
      assertThat(diag.add(row - col)).isTrue();
      assertThat(antiDiag.add(row + col)).isTrue();
    }
  }
}
