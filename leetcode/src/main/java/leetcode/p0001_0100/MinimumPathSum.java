package leetcode.p0001_0100;

import java.util.Arrays;

public class MinimumPathSum {
  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the {@code dp} grid,
   *     where {@code n = grid.length} is the number of rows and {@code m = grid[0].length} the
   *     number of columns.
   */
  public int minPathSum(int[][] grid) {
    int n = grid.length;
    int m = grid[0].length;
    int[][] dp = new int[n][m];
    dp[0][0] = grid[0][0];
    for (int i = 1; i < n; i++) {
      dp[i][0] = dp[i - 1][0] + grid[i][0];
    }
    for (int j = 1; j < m; j++) {
      dp[0][j] = dp[0][j - 1] + grid[0][j];
    }
    for (int i = 1; i < n; i++) {
      for (int j = 1; j < m; j++) {
        dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
      }
    }
    return dp[n - 1][m - 1];
  }

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the {@code cache} grid
   *     plus {@code O(n + m)} recursion depth, where {@code n = grid.length} is the number of rows
   *     and {@code m = grid[0].length} the number of columns.
   */
  public int minPathSum2(int[][] grid) {
    int[][] cache = new int[grid.length][grid[0].length];
    for (int[] row : cache) {
      Arrays.fill(row, -1);
    }
    return minPathSum2(0, 0, grid, cache);
  }

  private int minPathSum2(int row, int column, int[][] grid, int[][] cache) {
    if (row < 0 || row >= grid.length || column < 0 || column >= grid[0].length) {
      return Integer.MAX_VALUE;
    }

    // reached the bottom right corner
    if (row == grid.length - 1 && column == grid[0].length - 1) {
      return grid[row][column];
    }

    // cache hit
    if (cache[row][column] != -1) {
      return cache[row][column];
    }

    // cache miss so calculate the minimum path sum
    cache[row][column] = Math.min(
            minPathSum2(row + 1, column, grid, cache), minPathSum2(row, column + 1, grid, cache))
        + grid[row][column];

    return cache[row][column];
  }
}
