package com.giwankim.leetcode;

public class WordSearch {
  /**
   * @implNote Time {@code O(m·n·3^L)}, space {@code O(L)}, where
   *     {@code m × n} are the board dimensions and {@code L = word.length()}.
   *     <p><b>Time:</b> the outer double loop tries every cell as a
   *     starting position, contributing the {@code m·n} factor. From each
   *     start, {@link #exists} explores a tree of depth at most {@code L}
   *     — the recursion only deepens when the current character matches,
   *     and {@code index} advances by one each level. The branching factor
   *     is {@code 4} at the start cell but only {@code 3} thereafter,
   *     because the cell we just came from was overwritten with the
   *     sentinel {@code '#'} and immediately fails the character check on
   *     the back-direction call. Each recursive call does {@code O(1)}
   *     work (bounds check, character compare, mark and unmark), so every
   *     starting cell visits at most {@code O(4·3^(L−1)) = O(3^L)} nodes.
   *     <p><b>Space:</b> the recursion stack depth is bounded by {@code L}.
   *     No separate {@code visited} array is allocated — {@link #exists}
   *     marks visited cells in place by overwriting them with {@code '#'}
   *     and restoring the original character on backtrack — so auxiliary
   *     space is {@code O(L)} rather than {@code O(m·n)}.
   */
  public boolean exist(char[][] board, String word) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (exists(board, i, j, word, 0)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean exists(char[][] board, int row, int column, String word, int index) {
    if (index == word.length()) {
      return true;
    }
    if (row < 0 || row >= board.length || column < 0 || column >= board[row].length) {
      return false;
    }
    if (word.charAt(index) != board[row][column]) {
      return false;
    }
    char c = board[row][column];
    board[row][column] = '#';
    if (exists(board, row + 1, column, word, index + 1)
        || exists(board, row - 1, column, word, index + 1)
        || exists(board, row, column + 1, word, index + 1)
        || exists(board, row, column - 1, word, index + 1)) {
      return true;
    }
    board[row][column] = c;
    return false;
  }
}
