package com.giwankim.leetcode;

public class WordSearch {
  /**
   * @implNote Time {@code O(m * n * 3^L)}, space {@code O(L)}.
   *     <p>{@code m} x {@code n} = board dimensions, {@code L} = length of {@code word}.
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
