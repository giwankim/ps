package leetcode;

import java.util.*;

public class TopKFrequentElements {
  public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int x : nums) {
      map.put(x, map.getOrDefault(x, 0) + 1);
    }

    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> map.get(b) - map.get(a));
    pq.addAll(map.keySet());

    int[] result = new int[k];
    for (int i = 0; i < k; i++) {
      result[i] = pq.poll();
    }
    return result;
  }
}
