package com.giwankim.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
  public int longestConsecutive(int[] nums) {
    // Time complexity: O(n), Space complexity: O(n)
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
}
