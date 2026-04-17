package com.giwankim.leetcode;

public class RemoveDuplicatesFromSortedArray {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int removeDuplicates(int[] nums) {
    int i = 1;
    for (int j = 1; j < nums.length; j++) {
      if (nums[j - 1] == nums[j]) {
        continue;
      }
      nums[i] = nums[j];
      i += 1;
    }
    return i;
  }
}
