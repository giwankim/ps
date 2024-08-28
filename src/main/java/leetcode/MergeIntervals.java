package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeIntervals {
  public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

    List<int[]> mergedIntervals = new ArrayList<>();
    mergedIntervals.add(intervals[0]);

    for (int i = 1; i < intervals.length; i++) {
      int[] interval = mergedIntervals.getLast();
      if (interval[1] < intervals[i][0]) {
        mergedIntervals.add(intervals[i]);
      } else {
        mergedIntervals.set(
            mergedIntervals.size() - 1,
            new int[] {interval[0], Math.max(interval[1], intervals[i][1])});
      }
    }

    return mergedIntervals.toArray(new int[0][]);
  }
}
