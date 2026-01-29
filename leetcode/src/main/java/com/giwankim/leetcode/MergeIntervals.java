package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeIntervals {
  public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

    List<int[]> result = new ArrayList<>();
    int start = intervals[0][0];
    int end = intervals[0][1];

    for (int[] interval : intervals) {
      if (end < interval[0]) {
        result.add(new int[] {start, end});
        start = interval[0];
        end = interval[1];
      } else {
        end = Math.max(end, interval[1]);
      }
    }
    result.add(new int[] {start, end});

    return result.toArray(new int[0][]);
  }
}
