package leetcode.p3601_3700;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * <a href="https://leetcode.com/problems/count-islands-with-total-value-divisible-by-k/">3619.
 * Count Islands With Total Value Divisible by K</a>
 */
public class CountIslandsWithTotalValueDivisibleByK {
  public static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  private int n, m;
  private boolean[][] visited;

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for {@code visited} and the
   *     BFS queue, where {@code n = grid.length} is the number of rows and {@code m =
   *     grid[0].length} the number of columns. A land cell is marked when enqueued, so it is polled
   *     once and its four neighbors are checked once. The island total is a {@code long} because up
   *     to 1e5 cells of 1e6 reach 1e11, past {@code int}.
   */
  public int countIslands(int[][] grid, int k) {
    n = grid.length;
    m = grid[0].length;
    visited = new boolean[n][m];

    int ans = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (visited[i][j] || grid[i][j] == 0) {
          continue;
        }
        if (bfs(grid, i, j, k)) {
          ans++;
        }
      }
    }
    return ans;
  }

  private boolean bfs(int[][] grid, int i, int j, int k) {
    Queue<int[]> q = new ArrayDeque<>();
    q.add(new int[] {i, j});
    visited[i][j] = true;
    long total = 0L;
    while (!q.isEmpty()) {
      int[] curr = q.poll();
      int x = curr[0];
      int y = curr[1];
      total += grid[x][y];
      for (int[] dir : DIRS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || grid[nx][ny] == 0) {
          continue;
        }
        visited[nx][ny] = true;
        q.add(new int[] {nx, ny});
      }
    }
    return total % k == 0;
  }
}
