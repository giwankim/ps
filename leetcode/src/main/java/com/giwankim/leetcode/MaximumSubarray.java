package com.giwankim.leetcode;

public class MaximumSubarray {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)},
   *     where {@code n = nums.length}.
   *     <p>A first pass builds a prefix sum array so that the sum of any
   *     subarray {@code nums[l..r]} equals {@code prefix[r + 1] - prefix[l]}.
   *     A second pass scans {@code prefix} once, tracking the minimum prefix
   *     value seen so far and maximizing {@code prefix[i] - minSum} to find
   *     the largest subarray sum. Both passes are linear, and the auxiliary
   *     {@code prefix} array of length {@code n + 1} dominates space; the
   *     method returns a single {@code int}, so there is no output term to
   *     exclude.
   */
  public int maxSubArray(int[] nums) {
    int[] prefix = new int[nums.length + 1];
    for (int i = 0; i < nums.length; i++) {
      prefix[i + 1] = prefix[i] + nums[i];
    }

    int result = Integer.MIN_VALUE;
    int minSum = 0;
    for (int i = 1; i <= nums.length; i++) {
      result = Math.max(result, prefix[i] - minSum);
      minSum = Math.min(minSum, prefix[i]);
    }
    return result;
  }
}
