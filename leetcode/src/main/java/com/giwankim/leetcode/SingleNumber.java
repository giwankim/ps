package com.giwankim.leetcode;

public class SingleNumber {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)},
   *     where {@code n = nums.length}.
   */
  public int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
      result ^= num;
    }
    return result;
  }
}
