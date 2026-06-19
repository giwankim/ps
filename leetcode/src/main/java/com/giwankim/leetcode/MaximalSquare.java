package com.giwankim.leetcode;

public class MaximalSquare {
  public int maximalSquare(char[][] matrix) {
    int result = 0;
    int n = matrix.length;
    int m = matrix[0].length;
    int[][] dp = new int[n][m];
    for (int i = 0; i < n; i++) {
      dp[i][0] = matrix[i][0] - '0';
      result = Math.max(result, dp[i][0]);
    }
    for (int j = 1; j < m; j++) {
      dp[0][j] = matrix[0][j] - '0';
      result = Math.max(result, dp[0][j]);
    }
    for (int i = 1; i < n; i++) {
      for (int j = 1; j < m; j++) {
        if (matrix[i][j] == '1') {
          dp[i][j] = Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1])) + 1;
          result = Math.max(result, dp[i][j]);
        }
      }
    }
    return result * result;
  }
}
