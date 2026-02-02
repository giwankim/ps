package com.giwankim.leetcode;

public class RemoveElement {
  public int removeElement(int[] nums, int val) {
    // Time complexity: O(n), Space complexity: O(1)
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
