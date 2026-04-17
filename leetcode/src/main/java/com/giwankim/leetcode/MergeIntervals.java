package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
  /**
   * @implNote Time {@code O(n log n)}, space {@code O(n)}.
   */
  public int[][] merge(int[][] intervals) {
    List<int[]> result = new ArrayList<>();
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    for (int i = 0; i < intervals.length; i++) {
      if (result.isEmpty() || result.getLast()[1] < intervals[i][0]) {
        result.add(intervals[i]);
        continue;
      }
      result.getLast()[1] = Math.max(result.getLast()[1], intervals[i][1]);
    }
    return result.toArray(new int[0][]);
  }
}
