package com.giwankim.leetcode;

public class RotateArray {
  public void rotate(int[] nums, int k) {
    // Time complexity: O(n), Space complexity: O(1)
    k = k % nums.length;
    reverse(nums, 0, nums.length - k - 1);
    reverse(nums, nums.length - k, nums.length - 1);
    reverse(nums, 0, nums.length - 1);
  }

  private void reverse(int[] nums, int left, int right) {
    while (left < right) {
      int tmp = nums[left];
      nums[left] = nums[right];
      nums[right] = tmp;
      left += 1;
      right -= 1;
    }
  }
}
