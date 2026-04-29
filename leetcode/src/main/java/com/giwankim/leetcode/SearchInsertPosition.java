package com.giwankim.leetcode;

import java.util.Arrays;

public class SearchInsertPosition {
  /**
   * @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}.
   */
  public int searchInsert(int[] nums, int target) {
    int idx = Arrays.binarySearch(nums, target);
    return idx >= 0 ? idx : -(idx + 1);
  }
}
