package com.giwankim.leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class CheckIfThereIsValidPath {

  static final int[][][] DIRECTIONS =
      new int[][][] {
        {{}, {}}, // x
        {{0, 1}, {0, -1}}, // 1
        {{-1, 0}, {1, 0}}, // 2
        {{0, -1}, {1, 0}}, // 3
        {{0, 1}, {1, 0}}, // 4
        {{0, -1}, {-1, 0}}, // 5
        {{0, 1}, {-1, 0}} // 6
      };

  public boolean hasValidPath(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;

    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visited = new boolean[m][n];
    queue.offer(new int[] {0, 0});
    visited[0][0] = true;

    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      int x = cell[0];
      int y = cell[1];
      int street = grid[x][y];
      for (int[] direction : DIRECTIONS[street]) {
        int nx = x + direction[0];
        int ny = y + direction[1];
        if (nx < 0 || nx >= m || ny < 0 || ny >= n || visited[nx][ny]) {
          continue;
        }
        // check current street and next street are connected
        int nextStreet = grid[nx][ny];
        for (int[] backDirection : DIRECTIONS[nextStreet]) {
          if (x == nx + backDirection[0] && y == ny + backDirection[1]) {
            visited[nx][ny] = true;
            queue.offer(new int[] {nx, ny});
          }
        }
      }
    }

    return visited[m - 1][n - 1];
  }
}
