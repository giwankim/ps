package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
  public int[][] merge(int[][] intervals) {
    Arrays.sort(
        intervals,
        (a, b) -> {
          if (a[0] != b[0]) {
            return Integer.compare(a[0], b[0]);
          }
          return Integer.compare(a[1], b[1]);
        });

    List<int[]> mergedIntervals = new ArrayList<>();
    mergedIntervals.add(intervals[0]);

    for (int i = 1; i < intervals.length; i++) {
      int[] interval = mergedIntervals.get(mergedIntervals.size() - 1);
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
