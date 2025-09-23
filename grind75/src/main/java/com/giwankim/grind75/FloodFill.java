package com.giwankim.grind75;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class FloodFill {

  static int[] dx = {0, -1, 0, 1};
  static int[] dy = {-1, 0, 1, 0};

  public int[][] floodFill(int[][] image, int sr, int sc, int color) {
    int n = image.length;
    int m = image[0].length;

    int[][] result = new int[n][m];
    for (int i = 0; i < n; i++) {
      result[i] = Arrays.copyOf(image[i], m);
    }

    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visited = new boolean[n][m];
    queue.offer(new int[]{sr, sc});
    visited[sr][sc] = true;

    // BFS
    int oldColor = image[sr][sc];
    while (!queue.isEmpty()) {
      int[] coords = queue.poll();
      int x = coords[0];
      int y = coords[1];
      result[x][y] = color; // color the current cell
      for (int d = 0; d < dx.length; d++) {
        int nx = x + dx[d];
        int ny = y + dy[d];
        // out of bounds
        if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
          continue;
        }
        // already visited
        if (visited[nx][ny]) {
          continue;
        }
        // same color
        if (result[nx][ny] == oldColor) {
          queue.offer(new int[]{nx, ny});
          visited[nx][ny] = true;
        }
      }
    }

    return result;
  }
}
