package com.giwankim.leetcode;

public class HouseRobber {
  /**
   * @implNote Bottom-up tabulation of {@code dp[i] = max(dp[i-1], dp[i-2] + nums[i-1])}, where
   *     {@code dp[i]} is the best take from the first {@code i} houses. The 1-indexed table with
   *     {@code dp[0] = 0} absorbs the empty-prefix base case, so the {@code n == 1} input needs no
   *     special branch. Time {@code O(n)}, auxiliary space {@code O(n)} for the {@code dp} array,
   *     where {@code n = nums.length}.
   */
  public int rob(int[] nums) {
    int[] dp = new int[nums.length + 1];
    dp[1] = nums[0];
    for (int i = 2; i <= nums.length; i++) {
      dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i - 1]);
    }
    return dp[nums.length];
  }

  /**
   * @implNote Two-variable rolling form of {@code dp[i] = max(dp[i-1], dp[i-2] + nums[i])}:
   *     {@code curr} holds the best take through the previous house and {@code prev} holds the one
   *     before that, so only two scalars need persisting across iterations. The {@code n == 1}
   *     input falls through the loop with {@code curr} still seeded to {@code nums[0]}. Time
   *     {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length}.
   */
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
