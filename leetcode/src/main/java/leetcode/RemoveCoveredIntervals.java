package leetcode;

public class RemoveCoveredIntervals {
  public int removeCoveredIntervals(int[][] intervals) {
    int n = intervals.length;
    boolean[] isCovered = new boolean[n];
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (intervals[i][0] <= intervals[j][0] && intervals[i][1] >= intervals[j][1]) {
          isCovered[j] = true;
        } else if (intervals[j][0] <= intervals[i][0] && intervals[j][1] >= intervals[i][1]) {
          isCovered[i] = true;
        }
      }
    }
    int result = 0;
    for (int i = 0; i < n; i++) {
      if (!isCovered[i]) {
        result += 1;
      }
    }
    return result;
  }
}
