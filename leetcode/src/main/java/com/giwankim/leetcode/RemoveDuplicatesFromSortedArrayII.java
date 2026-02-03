package com.giwankim.leetcode;

public class RemoveDuplicatesFromSortedArrayII {
  public int removeDuplicates(int[] nums) {
    // Time complexity: O(n), Space complexity: O(1)
    int i = 1;
    int count = 1;
    for (int j = 1; j < nums.length; j++) {
      if (nums[j - 1] == nums[j]) {
        count += 1;
      } else {
        count = 1;
      }

      if (count <= 2) {
        nums[i] = nums[j];
        i += 1;
      }
    }
    return i;
  }
}
