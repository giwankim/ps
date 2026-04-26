package com.giwankim.leetcode;

public class NQueensII {
  /**
   * <b>Time:</b> {@code O(n × n!)}. The recursion explores a tree of
   * row-by-row placements — at most {@code n} choices for row 0,
   * {@code n-1} for row 1 after column pruning, down to 1 — bounding
   * the search at {@code n!} leaves. Each recursive call performs an
   * {@code O(n)} attack check. Diagonal pruning makes practical runs
   * much smaller than this bound but does not improve it asymptotically.
   *
   * <p><b>Space:</b> {@code O(n^2)}, dominated by the {@code boolean[n][n]}
   * working board; the recursion stack adds {@code O(n)}. Because the
   * result is a single {@code int}, there is no output term to exclude.
   * Tracking only the occupied columns, diagonals, and anti-diagonals
   * (three {@code boolean[]} or three bitmasks) would lower auxiliary
   * space to {@code O(n)} without affecting time.
   */
  public int totalNQueens(int n) {
    return totalNQueens(new boolean[n][n], 0);
  }

  private int totalNQueens(boolean[][] board, int row) {
    if (row == board.length) {
      return 1;
    }
    int result = 0;
    for (int col = 0; col < board[0].length; col++) {
      if (!canPlace(board, row, col)) {
        continue;
      }
      board[row][col] = true;
      result += totalNQueens(board, row + 1);
      board[row][col] = false;
    }
    return result;
  }

  private boolean canPlace(boolean[][] board, int row, int col) {
    // check column
    for (int i = 0; i < board.length; i++) {
      if (board[i][col]) {
        return false;
      }
    }
    // check row
    for (int j = 0; j < board[row].length; j++) {
      if (board[row][j]) {
        return false;
      }
    }
    // check diagonal
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
      if (board[i][j]) {
        return false;
      }
    }
    // check anti-diagonal
    for (int i = row - 1, j = col + 1; i >= 0 && j < board[0].length; i--, j++) {
      if (board[i][j]) {
        return false;
      }
    }
    return true;
  }
}
