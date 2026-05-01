package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FindKPairsWithSmallestSums {
  /**
   * @implNote Time {@code O(k log k)}, auxiliary space {@code O(k)} excluding the output.
   */
  public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>(k);
    PriorityQueue<int[]> minHeap =
        new PriorityQueue<>(Comparator.comparingInt(a -> nums1[a[0]] + nums2[a[1]]));
    for (int i = 0; i < nums1.length && i < k; i++) {
      minHeap.offer(new int[] {i, 0});
    }
    while (result.size() < k && !minHeap.isEmpty()) {
      int[] pair = minHeap.poll();
      int i = pair[0];
      int j = pair[1];
      result.add(List.of(nums1[i], nums2[j]));
      if (j + 1 < nums2.length) {
        minHeap.offer(new int[] {i, j + 1});
      }
    }
    return result;
  }
}
