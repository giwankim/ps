package com.giwankim.leetcode;

public class NumberOfZigZagArraysI {
  private static final int MOD = 1_000_000_007;

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the {@code dp} table,
   *     where {@code m = r - l + 1} is the number of admissible values.
   */
  public int zigZagArrays(int n, int l, int r) {
    int m = r - l + 1;
    int[][][] dp = new int[n + 1][m][2];
    for (int j = 0; j < m; j++) {
      dp[1][j][0] = 1;
      dp[1][j][1] = 1;
    }

    int[] suffix = new int[m + 1];
    int[] prefix = new int[m + 1];
    for (int len = 2; len <= n; len++) {
      for (int w = m - 1; w >= 0; w--) {
        suffix[w] = suffix[w + 1] + dp[len - 1][w][1];
        suffix[w] %= MOD;
      }

      for (int w = 1; w <= m; w++) {
        prefix[w] = prefix[w - 1] + dp[len - 1][w - 1][0];
        prefix[w] %= MOD;
      }

      for (int v = 0; v < m; v++) {
        dp[len][v][0] = suffix[v + 1];
        dp[len][v][1] = prefix[v];
      }
    }

    int result = 0;
    for (int v = 0; v < m; v++) {
      result += dp[n][v][0];
      result %= MOD;
      result += dp[n][v][1];
      result %= MOD;
    }
    return result;
  }
}
