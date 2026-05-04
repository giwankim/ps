package com.giwankim.leetcode;

public class MaximumSumCircularSubarray {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)},
   *     where {@code n = nums.length}.
   */
  public int maxSubarraySumCircular(int[] nums) {
    int maxSum = nums[0];
    int minSum = nums[0];
    int maxEndingAt = 0;
    int minEndingAt = 0;
    int totalSum = 0;
    for (int num : nums) {
      maxEndingAt = Math.max(maxEndingAt + num, num);
      minEndingAt = Math.min(minEndingAt + num, num);
      maxSum = Math.max(maxSum, maxEndingAt);
      minSum = Math.min(minSum, minEndingAt);
      totalSum += num;
    }
    if (totalSum == minSum) {
      return maxSum;
    }
    return Math.max(maxSum, totalSum - minSum);
  }
}
