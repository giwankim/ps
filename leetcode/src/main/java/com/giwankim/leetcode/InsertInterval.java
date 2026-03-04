package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertInterval {
  public int[][] insert(int[][] intervals, int[] newInterval) {
    // Time complexity: O(n), Space complexity: O(n)
    List<int[]> rawIntervals = new ArrayList<>(Arrays.asList(intervals));
    int idx = Arrays.binarySearch(intervals, newInterval, (a, b) -> a[0] - b[0]);
    if (idx < 0) {
      idx = -(idx + 1);
    }
    rawIntervals.add(idx, newInterval);
    return merge(rawIntervals).toArray(new int[0][]);
  }

  private List<int[]> merge(List<int[]> intervals) {
    List<int[]> result = new ArrayList<>();
    for (int[] interval : intervals) {
      if (result.isEmpty() || result.getLast()[1] < interval[0]) {
        result.add(interval);
        continue;
      }
      result.getLast()[1] = Math.max(result.getLast()[1], interval[1]);
    }
    return result;
  }
}
