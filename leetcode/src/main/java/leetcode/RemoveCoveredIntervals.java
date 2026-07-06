package leetcode;

import java.util.Arrays;

public class RemoveCoveredIntervals {
  /** @implNote Time {@code O(n log n)}, space {@code O(1)}, where {@code n = intervals.length}. */
  public int removeCoveredIntervals(int[][] intervals) {
    Arrays.sort(
        intervals,
        (a, b) -> a[0] == b[0] ? Integer.compare(b[1], a[1]) : Integer.compare(a[0], b[0]));
    int result = 1;
    int end = intervals[0][1];
    for (int i = 1; i < intervals.length; i++) {
      if (end < intervals[i][1]) {
        result++;
        end = intervals[i][1];
      }
    }
    return result;
  }
}
