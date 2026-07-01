package leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class FindTheSafestPathInAGrid {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  public int maximumSafenessFactor(List<List<Integer>> grid) {
    int n = grid.size();
    int[][] dists = getDistances(grid);
    int lo = 0;
    int hi = n * 2;
    int result = Integer.MIN_VALUE;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (pathExists(dists, mid)) {
        result = mid;
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }
    return result;
  }

  private static boolean pathExists(int[][] dists, int safeness) {
    int n = dists.length;
    if (dists[0][0] < safeness || dists[n - 1][n - 1] < safeness) {
      return false;
    }
    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] visited = new boolean[n][n];
    queue.offer(new int[] {0, 0});
    visited[0][0] = true;
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      int x = cell[0];
      int y = cell[1];
      if (x == n - 1 && y == n - 1) {
        return true;
      }
      for (int[] dir : DIRECTIONS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= n || visited[nx][ny] || dists[nx][ny] < safeness) {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }
    return false;
  }

  private static int[][] getDistances(List<List<Integer>> grid) {
    int n = grid.size();
    int[][] result = new int[n][n];
    for (int[] row : result) {
      Arrays.fill(row, Integer.MAX_VALUE);
    }

    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] visited = new boolean[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (grid.get(i).get(j) == 1) {
          queue.offer(new int[] {i, j});
          visited[i][j] = true;
        }
      }
    }

    int d = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int[] cell = queue.poll();
        int x = cell[0];
        int y = cell[1];
        result[x][y] = d;
        for (int[] dir : DIRECTIONS) {
          int nx = x + dir[0];
          int ny = y + dir[1];
          if (nx < 0 || nx >= n || ny < 0 || ny >= n || visited[nx][ny]) {
            continue;
          }
          queue.offer(new int[] {nx, ny});
          visited[nx][ny] = true;
        }
      }
      d++;
    }
    return result;
  }
}
