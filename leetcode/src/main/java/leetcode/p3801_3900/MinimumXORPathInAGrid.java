package leetcode.p3801_3900;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/minimum-xor-path-in-a-grid/">3882. Minimum XOR Path in a
 * Grid</a>
 */
public class MinimumXORPathInAGrid {
  private int n, m;
  private int[][][] dp;

  /**
   * @implNote Time {@code O(n * m * X)} and space {@code O(n * m * X)}, where {@code n =
   *     grid.length}, {@code m = grid[0].length}, and {@code X = 1024} is the number of possible
   *     10-bit XOR values. The memo table has one entry per cell and XOR value, and each memoized
   *     state checks at most two predecessors. The {@code O(n + m)} recursion stack is dominated by
   *     the table. Since {@code X} is fixed, both bounds simplify to {@code O(n * m)}.
   */
  public int minCost(int[][] grid) {
    n = grid.length;
    m = grid[0].length;
    dp = new int[n][m][1024];
    for (int[][] plane : dp) {
      for (int[] row : plane) {
        Arrays.fill(row, -1);
      }
    }
    for (int k = 0; k < 1024; k++) {
      if (minCost(grid, n - 1, m - 1, k)) {
        return k;
      }
    }
    return -1;
  }

  private boolean minCost(int[][] grid, int i, int j, int k) {
    if (i < 0 || j < 0) {
      return false;
    }
    if (i == 0 && j == 0) {
      return grid[0][0] == k;
    }
    if (dp[i][j][k] != -1) {
      return dp[i][j][k] == 1;
    }
    boolean res =
        minCost(grid, i - 1, j, k ^ grid[i][j]) || minCost(grid, i, j - 1, k ^ grid[i][j]);
    dp[i][j][k] = res ? 1 : 0;
    return res;
  }
}
