package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LeastNumberOfUniqueIntegers {
  public int findLeastNumOfUniqueInts(int[] arr, int k) {
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : arr) {
      freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    PriorityQueue<Integer> pq = new PriorityQueue<>(freqMap.values());

    while (k > 0 && !pq.isEmpty()) {
      int freq = pq.poll();
      if (freq <= k) {
        k -= freq;
      } else {
        pq.add(freq - k);
        k = 0;
      }
    }

    return pq.size();
  }
}
