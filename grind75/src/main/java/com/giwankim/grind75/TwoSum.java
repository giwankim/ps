package com.giwankim.grind75;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> numToIndex = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      int complement = target - nums[i];
      if (numToIndex.containsKey(complement)) {
        return new int[] {numToIndex.get(complement), i};
      }
      numToIndex.put(nums[i], i);
    }
    return new int[0];
  }
}
