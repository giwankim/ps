package leetcode.p1201_1300;

/**
 * <a href="https://leetcode.com/problems/count-servers-that-communicate/">1267. Count Servers that
 * Communicate</a>
 */
public class CountServersThatCommunicate {
  /**
   * @implNote Time {@code O(n * m)} for two passes over the grid, auxiliary space {@code O(n + m)}
   *     for the {@code rows} and {@code cols} server counts, where {@code n = grid.length} is the
   *     number of rows and {@code m = grid[0].length} the number of columns.
   */
  public int countServers(int[][] grid) {
    int n = grid.length;
    int m = grid[0].length;

    int[] rows = new int[n];
    int[] cols = new int[m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (grid[i][j] == 1) {
          rows[i]++;
          cols[j]++;
        }
      }
    }

    int ans = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (grid[i][j] == 1 && (rows[i] > 1 || cols[j] > 1)) {
          ans++;
        }
      }
    }
    return ans;
  }
}
