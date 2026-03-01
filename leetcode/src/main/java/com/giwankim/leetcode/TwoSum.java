package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
  public int[] twoSum(int[] nums, int target) {
    // Time complexity: O(n), Space complexity: O(n)
    Map<Integer, Integer> valueToIndex = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      if (valueToIndex.containsKey(target - nums[i])) {
        return new int[] {valueToIndex.get(target - nums[i]), i};
      }
      valueToIndex.put(nums[i], i);
    }
    return new int[0];
  }
}
