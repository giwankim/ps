package leetcode;

import java.util.Arrays;
import java.util.Comparator;

public class NonOverlapIntervals {
  public int eraseOverlapIntervals(int[][] intervals) {
    // sort by end time
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));

    int end = Integer.MIN_VALUE; // current end of scheduled intervals
    int result = 0;
    for (int[] interval : intervals) {
      if (end <= interval[0]) { // can schedule
        end = interval[1];
      } else {
        result += 1;
      }
    }
    return result;
  }
}
