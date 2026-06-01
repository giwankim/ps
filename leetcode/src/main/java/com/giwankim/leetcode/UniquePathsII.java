package com.giwankim.leetcode;

public class UniquePathsII {
  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the {@code dp} grid,
   *     where {@code n = obstacleGrid.length} is the number of rows and {@code m =
   *     obstacleGrid[0].length} the number of columns.
   */
  public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    int n = obstacleGrid.length;
    int m = obstacleGrid[0].length;
    int[][] dp = new int[n][m];
    dp[0][0] = obstacleGrid[0][0] == 1 ? 0 : 1;
    for (int i = 1; i < n; i++) {
      dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
    }
    for (int j = 1; j < m; j++) {
      dp[0][j] = obstacleGrid[0][j] == 1 ? 0 : dp[0][j - 1];
    }
    for (int i = 1; i < n; i++) {
      for (int j = 1; j < m; j++) {
        if (obstacleGrid[i][j] == 1) {
          dp[i][j] = 0;
        } else {
          dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
        }
      }
    }
    return dp[n - 1][m - 1];
  }

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(m)} for the rolling {@code dp} row,
   *     where {@code n = obstacleGrid.length} is the number of rows and {@code m =
   *     obstacleGrid[0].length} the number of columns.
   */
  public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
    int m = obstacleGrid[0].length;
    int[] dp = new int[m];
    dp[0] = 1;
    for (int[] row : obstacleGrid) {
      for (int j = 0; j < m; j++) {
        if (row[j] == 1) {
          dp[j] = 0;
          continue;
        }
        if (j > 0) {
          dp[j] += dp[j - 1];
        }
      }
    }
    return dp[m - 1];
  }
}
