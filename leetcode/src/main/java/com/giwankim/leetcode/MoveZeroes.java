package com.giwankim.leetcode;

public class MoveZeroes {
  public void moveZeroes(int[] nums) {
    int index = 0; // write index

    // move non-zero elements to the front
    for (int i = 0; i < nums.length; i++) {
      if (nums[i] != 0) {
        nums[index] = nums[i];
        index += 1;
      }
    }

    // populate remaining positions with 0
    while (index < nums.length) {
      nums[index] = 0;
      index += 1;
    }
  }
}
