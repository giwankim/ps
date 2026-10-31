package leetcode.p3601_3700;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/twisted-mirror-path-count/">3665. Twisted Mirror Path
 * Count</a>
 */
public class TwistedMirrorPathCount {
  private static final int MOD = 1_000_000_007;
  private static int n, m;
  private static int[][] dp;

  /**
   * @implNote Time {@code O(n * m)} and space {@code O(n * m)}, where {@code n = grid.length} and
   *     {@code m = grid[0].length}. The memo table has one entry per cell, since an empty cell's
   *     count does not depend on the direction the robot entered it, and each memoized cell makes
   *     two {@link #uniquePaths(int[][], int, int, int)} calls. Mirror states are not memoized, but
   *     each has at most one predecessor state, so every reflection chain hangs off a single empty
   *     cell and is walked only when that cell is first computed. Every call advances {@code i + j}
   *     by one, so the {@code O(n + m)} recursion stack is dominated by the table.
   */
  public int uniquePaths(int[][] grid) {
    n = grid.length;
    m = grid[0].length;
    dp = new int[n][m];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    return (uniquePaths(grid, 1, 0, 0) + uniquePaths(grid, 0, 1, 1)) % MOD;
  }

  private static int uniquePaths(int[][] grid, int i, int j, int dir) {
    if (i >= n || j >= m) {
      return 0;
    }
    if (i == n - 1 && j == m - 1) {
      return 1;
    }
    if (grid[i][j] == 1) {
      if (dir == 0) {
        return uniquePaths(grid, i, j + 1, 1);
      }
      return uniquePaths(grid, i + 1, j, 0);
    }
    if (dp[i][j] != -1) {
      return dp[i][j];
    }
    int res = (uniquePaths(grid, i + 1, j, 0) + uniquePaths(grid, i, j + 1, 1)) % MOD;
    dp[i][j] = res;
    return res;
  }
}
