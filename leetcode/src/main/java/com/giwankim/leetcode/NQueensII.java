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
   * working board; the recursion stack adds {@code O(n)}.
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

  /**
   * <b>Time:</b> {@code O(n × n!)}. Same recursion shape as
   * {@link #totalNQueens}: row-by-row placement bounds the search at
   * {@code n!} leaves after column and diagonal pruning. The attack
   * check here is {@code O(1)} — three array reads against
   * {@code cols}, {@code diags}, and {@code antiDiags} — versus the
   * {@code O(n)} {@code canPlace} sweep in {@link #totalNQueens}, but
   * the per-call column loop is still {@code O(n)} so the asymptotic
   * bound is unchanged. The hidden constant is meaningfully smaller
   * in practice.
   *
   * <p><b>Space:</b> {@code O(n)}, dominated by the three
   * {@code boolean[]} marker arrays — one of length {@code n} for
   * columns and two of length {@code 2n − 1} for the diagonals; the
   * recursion stack adds another {@code O(n)}.
   */
  public int totalNQueens2(int n) {
    boolean[] cols = new boolean[n];
    boolean[] diags = new boolean[2 * n - 1];
    boolean[] antiDiags = new boolean[2 * n - 1];
    return totalNQueens2(cols, diags, antiDiags, 0);
  }

  private int totalNQueens2(boolean[] cols, boolean[] diags, boolean[] antiDiags, int row) {
    if (row == cols.length) {
      return 1;
    }
    int result = 0;
    for (int col = 0; col < cols.length; col++) {
      int diag = row + col;
      int antiDiag = row - col + cols.length - 1;
      if (cols[col] || diags[diag] || antiDiags[antiDiag]) {
        continue;
      }
      cols[col] = true;
      diags[diag] = true;
      antiDiags[antiDiag] = true;
      result += totalNQueens2(cols, diags, antiDiags, row + 1);
      cols[col] = false;
      diags[diag] = false;
      antiDiags[antiDiag] = false;
    }
    return result;
  }
}
