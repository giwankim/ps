package com.giwankim.leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) {
      set.add(num);
    }
    int result = 0;
    for (int num : set) {
      if (set.contains(num - 1)) {
        continue;
      }
      int length = 1;
      while (set.contains(num + length)) {
        length += 1;
      }
      result = Math.max(result, length);
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n log n)}, space {@code O(1)}.
   */
  public int longestConsecutive2(int[] nums) {
    if (nums.length == 0) {
      return 0;
    }
    int result = 1;
    Arrays.sort(nums);
    int length = 1;
    for (int i = 0; i + 1 < nums.length; i++) {
      if (nums[i] == nums[i + 1]) {
        continue;
      }
      if (nums[i + 1] == nums[i] + 1) {
        length += 1;
        result = Math.max(result, length);
      } else {
        length = 1;
      }
    }
    return result;
  }
}
