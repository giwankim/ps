package com.giwankim.leetcode;

public class SurroundedRegions {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  public void solve(char[][] board) {
    // Time complexity: O(cols * rows), Space complexity: O(1)
    int rows = board.length;
    int cols = board[0].length;

    // Mark the 'O' cells connected to the borders
    for (int i = 0; i < rows; i++) {
      dfs(board, i, 0);
      dfs(board, i, cols - 1);
    }
    for (int j = 1; j + 1 < cols; j++) {
      dfs(board, 0, j);
      dfs(board, rows - 1, j);
    }

    // Replace 'O' cells not connected to the border with 'X'
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (board[i][j] == '#') {
          board[i][j] = 'O';
        } else if (board[i][j] == 'O') {
          board[i][j] = 'X';
        }
      }
    }
  }

  private void dfs(char[][] board, int i, int j) {
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O') {
      return;
    }
    board[i][j] = '#';
    for (int[] direction : DIRECTIONS) {
      dfs(board, i + direction[0], j + direction[1]);
    }
  }
}
