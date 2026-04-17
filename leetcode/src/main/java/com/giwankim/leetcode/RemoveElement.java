package com.giwankim.leetcode;

public class RemoveElement {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int removeElement(int[] nums, int val) {
    int index = 0;
    for (int i = 0; i < nums.length; i++) {
      if (nums[i] != val) {
        nums[index] = nums[i];
        index += 1;
      }
    }
    return index;
  }
}
