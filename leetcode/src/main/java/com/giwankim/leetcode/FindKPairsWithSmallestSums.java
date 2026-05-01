package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class FindKPairsWithSmallestSums {
  /**
   * @implNote Time {@code O(r log r)}, auxiliary space {@code O(r)} excluding the output,
   *     where {@code r = min(k, nums1.length * nums2.length)} is the number of pairs returned.
   */
  public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>();
    PriorityQueue<List<Integer>> minHeap = new PriorityQueue<>(
        (a, b) -> nums1[a.getFirst()] + nums2[a.getLast()] - nums1[b.getFirst()] - nums2[b.getLast()]);
    Set<List<Integer>> visited = new HashSet<>();
    minHeap.offer(List.of(0, 0));
    visited.add(List.of(0, 0));
    while (result.size() < k) {
      List<Integer> pair = minHeap.poll();
      int x = pair.getFirst();
      int y = pair.getLast();
      result.add(List.of(nums1[x], nums2[y]));
      if (x + 1 < nums1.length && !visited.contains(List.of(x + 1, y))) {
        minHeap.offer(List.of(x + 1, y));
        visited.add(List.of(x + 1, y));
      }
      if (y + 1 < nums2.length && !visited.contains(List.of(x, y + 1))) {
        minHeap.offer(List.of(x, y + 1));
        visited.add(List.of(x, y + 1));
      }
    }
    return result;
  }
}
