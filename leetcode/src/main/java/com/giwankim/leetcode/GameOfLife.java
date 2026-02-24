package com.giwankim.leetcode;

public class GameOfLife {
  static int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
  static int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

  public void gameOfLife(int[][] board) {
    // Time complexity: O(mn), Space complexity: O(mn)
    int[][] result = new int[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        int count = countNeighbors(board, i, j);
        if (board[i][j] == 0) {
          if (count == 3) {
            result[i][j] = 1;
          }
        } else {
          if (count < 2) {
            result[i][j] = 0;
          } else if (count <= 3) {
            result[i][j] = 1;
          } else {
            result[i][j] = 0;
          }
        }
      }
    }
    for (int i = 0; i < board.length; i++) {
      System.arraycopy(result[i], 0, board[i], 0, board[i].length);
    }
  }

  private int countNeighbors(int[][] board, int i, int j) {
    int result = 0;
    for (int k = 0; k < dx.length; k++) {
      int nx = i + dx[k];
      int ny = j + dy[k];
      if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[nx].length && board[nx][ny] == 1) {
        result += 1;
      }
    }
    return result;
  }
}
