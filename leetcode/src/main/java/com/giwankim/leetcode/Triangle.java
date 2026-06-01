package com.giwankim.leetcode;

import java.util.List;

public class Triangle {
  /**
   * @implNote Time {@code O(n²)}, auxiliary space {@code O(n)} for the {@code dp} grid, where
   *     {@code n = triangle.size()} is the number of rows.
   */
  public int minimumTotal(List<List<Integer>> triangle) {
    int[] dp = new int[triangle.size()];
    dp[0] = triangle.getFirst().getFirst();
    for (int i = 1; i < triangle.size(); i++) {
      List<Integer> row = triangle.get(i);
      dp[i] = dp[i - 1] + row.get(i);
      for (int j = i - 1; j >= 1; j--) {
        dp[j] = Math.min(dp[j], dp[j - 1]) + row.get(j);
      }
      dp[0] += row.getFirst();
    }

    int result = Integer.MAX_VALUE;
    for (int j = 0; j < triangle.size(); j++) {
      result = Math.min(result, dp[j]);
    }
    return result;
  }
}
