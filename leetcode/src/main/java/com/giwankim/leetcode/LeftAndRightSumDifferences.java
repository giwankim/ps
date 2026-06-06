package com.giwankim.leetcode;

public class LeftAndRightSumDifferences {
  public int[] leftRightDifference(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    int curr = 0;
    for (int i = 0; i < n; i++) {
      result[i] = curr;
      curr += nums[i];
    }
    curr = 0;
    for (int i = n - 1; i >= 0; i--) {
      result[i] = Math.abs(result[i] - curr);
      curr += nums[i];
    }
    return result;
  }
}
