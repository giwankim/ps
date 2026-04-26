package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
  /**
   * <b>Time:</b> {@code O(n × n!)}. The recursion explores a tree of
   * row-by-row placements — at most {@code n} choices for row 0,
   * {@code n-1} for row 1 after column pruning, down to 1 — bounding
   * the search at {@code n!} leaves. Each recursive call performs an
   * {@code O(n)} attack check. Diagonal pruning makes practical runs
   * much smaller than this bound but does not improve it asymptotically.
   *
   * <p><b>Space:</b> {@code O(n^2)} auxiliary, dominated by the
   * {@code char[n][n]} working board; the recursion stack adds {@code O(n)}.
   * The returned list is excluded — it can hold up to {@code O(S × n^2)}
   * characters, where {@code S} is the number of valid placements
   * ({@code S = 92} for {@code n = 8}).
   */
  public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    solveNQueens(newBoard(n), 0, result);
    return result;
  }

  private void solveNQueens(char[][] board, int row, List<List<String>> result) {
    if (row == board.length) {
      result.add(from(board));
      return;
    }
    for (int col = 0; col < board[row].length; col++) {
      if (!canPlaceQueen(board, row, col)) {
        continue;
      }
      board[row][col] = 'Q';
      solveNQueens(board, row + 1, result);
      board[row][col] = '.';
    }
  }

  private boolean canPlaceQueen(char[][] board, int row, int col) {
    // check row, col, diagonal, anti-diagonal for any existing queen
    for (int i = 0; i < board.length; i++) {
      if (board[i][col] == 'Q') {
        return false;
      }
    }
    for (int j = 0; j < board[0].length; j++) {
      if (board[row][j] == 'Q') {
        return false;
      }
    }
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
      if (board[i][j] == 'Q') {
        return false;
      }
    }
    for (int i = row - 1, j = col + 1; i >= 0 && j < board[0].length; i--, j++) {
      if (board[i][j] == 'Q') {
        return false;
      }
    }
    return true;
  }

  private static List<String> from(char[][] board) {
    List<String> result = new ArrayList<>();
    for (char[] row : board) {
      result.add(String.valueOf(row));
    }
    return result;
  }

  private static char[][] newBoard(int n) {
    char[][] board = new char[n][n];
    for (char[] row : board) {
      Arrays.fill(row, '.');
    }
    return board;
  }
}
