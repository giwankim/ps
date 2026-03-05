package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class InsertInterval {
  public int[][] insert(int[][] intervals, int[] newInterval) {
    // Time complexity: O(n), Space complexity: O(n)
    List<int[]> result = new ArrayList<>();
    int i = 0;
    while (i < intervals.length && intervals[i][1] < newInterval[0]) {
      result.add(intervals[i]);
      i++;
    }

    while (i < intervals.length && newInterval[1] >= intervals[i][0]) {
      newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
      newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
      i++;
    }
    result.add(newInterval);

    while (i < intervals.length) {
      result.add(intervals[i]);
      i++;
    }
    return result.toArray(new int[0][]);
  }
}
