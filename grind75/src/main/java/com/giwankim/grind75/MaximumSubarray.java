package com.giwankim.grind75;

public class MaximumSubarray {

  public int maxSubArray(int[] nums) {
    // prefix sums
    int[] prefixSums = new int[nums.length + 1];
    for (int i = 0; i < nums.length; i++) {
      prefixSums[i + 1] = prefixSums[i] + nums[i];
    }

    // maximum subarray
    int result = Integer.MIN_VALUE;
    int minSum = 0;
    for (int i = 1; i < prefixSums.length; i++) {
      result = Math.max(result, prefixSums[i] - minSum);
      minSum = Math.min(minSum, prefixSums[i]);
    }
    return result;
  }
}
