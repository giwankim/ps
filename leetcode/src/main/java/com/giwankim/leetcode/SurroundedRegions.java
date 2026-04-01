package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Queue;

public class SurroundedRegions {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  public void solve(char[][] board) {
    // Time complexity: O(m * n), Space complexity: O(m * n)
    int n = board.length;
    int m = board[0].length;

    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] visited = new boolean[n][m];
    for (int i = 0; i < n; i++) {
      if (board[i][0] == 'O') {
        queue.offer(new int[] {i, 0});
        visited[i][0] = true;
      }
      if (board[i][m - 1] == 'O') {
        queue.offer(new int[] {i, m - 1});
        visited[i][m - 1] = true;
      }
    }
    for (int j = 1; j + 1 < m; j++) {
      if (board[0][j] == 'O') {
        queue.offer(new int[] {0, j});
        visited[0][j] = true;
      }
      if (board[n - 1][j] == 'O') {
        queue.offer(new int[] {n - 1, j});
        visited[n - 1][j] = true;
      }
    }
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      for (int[] dir : DIRECTIONS) {
        int nx = cell[0] + dir[0];
        int ny = cell[1] + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || board[nx][ny] != 'O') {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (board[i][j] == 'O' && !visited[i][j]) {
          board[i][j] = 'X';
        }
      }
    }
  }
}
