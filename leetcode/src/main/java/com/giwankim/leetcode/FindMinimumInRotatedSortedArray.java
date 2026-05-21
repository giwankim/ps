package com.giwankim.leetcode;

public class FindMinimumInRotatedSortedArray {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int findMin(int[] nums) {
    int result = -1;
    int lo = 0;
    int hi = nums.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (nums[mid] <= nums[nums.length - 1]) {
        result = nums[mid];
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }
}
