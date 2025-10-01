package com.giwankim.grind75;

import java.util.HashMap;
import java.util.Map;

public class MajorityElement {

  public int majorityElement(int[] nums) {
    int n = nums.length;
    Map<Integer, Integer> counts = new HashMap<>();
    for (int num : nums) {
      counts.put(num, counts.getOrDefault(num, 0) + 1);
      if (counts.get(num) > n / 2) {
        return num;
      }
    }
    return -1;
  }
}
