package com.giwankim.grind75;

import java.util.ArrayList;
import java.util.List;

public class InsertInterval {

  public int[][] insert(int[][] intervals, int[] newInterval) {
    int n = intervals.length;
    if (n == 0) {
      return new int[][] {newInterval};
    }

    List<int[]> result = new ArrayList<>();

    int i = 0;
    // add intervals ending before start of the newInterval
    while (i < n && intervals[i][1] < newInterval[0]) {
      result.add(intervals[i]);
      i += 1;
    }

    // merge overlapping intervals
    while (i < n && intervals[i][0] <= newInterval[1]) {
      newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
      newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
      i += 1;
    }
    result.add(newInterval);

    // add the rest
    while (i < n) {
      result.add(intervals[i]);
      i += 1;
    }

    return result.toArray(new int[0][]);
  }
}
