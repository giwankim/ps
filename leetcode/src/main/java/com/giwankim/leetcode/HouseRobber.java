package com.giwankim.leetcode;

public class HouseRobber {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)} for the {@code dp} array, where
   *     {@code n = nums.length}.
   */
  public int rob(int[] nums) {
    int[] dp = new int[nums.length + 1];
    dp[1] = nums[0];
    for (int i = 2; i <= nums.length; i++) {
      dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i - 1]);
    }
    return dp[nums.length];
  }

  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length}. */
  public int rob2(int[] nums) {
    int prev = 0;
    int curr = nums[0];
    for (int i = 1; i < nums.length; i++) {
      int temp = curr;
      curr = Math.max(curr, prev + nums[i]);
      prev = temp;
    }
    return curr;
  }
}
