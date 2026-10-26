package leetcode.p1201_1300;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <a href="https://leetcode.com/problems/number-of-closed-islands/">1254. Number of Closed
 * Islands</a>
 */
public class NumberOfClosedIslands {
  public static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  private int n, m;
  private int[][] grid;
  private boolean[][] visited;

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for {@code visited} and the
   *     BFS queue, where {@code n = grid.length} is the number of rows and {@code m =
   *     grid[0].length} the number of columns. The border sweep first floods every land region
   *     touching the edge, so the second pass counts only closed islands; across both passes each
   *     land cell is enqueued once and its four neighbors are probed once.
   */
  public int closedIsland(int[][] grid) {
    n = grid.length;
    m = grid[0].length;
    this.grid = grid;
    visited = new boolean[n][m];

    for (int i = 0; i < n; i++) {
      if (grid[i][0] == 0) {
        bfs(i, 0);
      }
      if (grid[i][m - 1] == 0) {
        bfs(i, m - 1);
      }
    }
    for (int j = 0; j < m; j++) {
      if (grid[0][j] == 0) {
        bfs(0, j);
      }
      if (grid[n - 1][j] == 0) {
        bfs(n - 1, j);
      }
    }

    int ans = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (grid[i][j] == 1 || visited[i][j]) {
          continue;
        }
        ans++;
        bfs(i, j);
      }
    }
    return ans;
  }

  private void bfs(int i, int j) {
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[] {i, j});
    visited[i][j] = true;
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      for (int[] dir : DIRS) {
        int nx = curr[0] + dir[0];
        int ny = curr[1] + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || grid[nx][ny] == 1) {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }
  }
}
