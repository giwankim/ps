package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentElements {
  public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int x : nums) {
      map.put(x, map.getOrDefault(x, 0) + 1);
    }

    List<Integer>[] buckets = new List[nums.length + 1];

    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      int num = entry.getKey();
      int count = entry.getValue();
      if (buckets[count] == null) {
        buckets[count] = new ArrayList<>();
      }
      buckets[count].add(num);
    }

    List<Integer> result = new ArrayList<>();
    for (int i = buckets.length - 1; i >= 0; i--) {
      if (buckets[i] != null) {
        result.addAll(buckets[i]);
      }
      if (result.size() >= k) {
        break;
      }
    }

    return result.stream().mapToInt(Integer::intValue).toArray();
  }
}
