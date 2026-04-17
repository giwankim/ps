package com.giwankim.leetcode;

import java.util.Arrays;

public class MinimumNumberOfArrowsToBurstBalloons {
  /**
   * @implNote Time {@code O(n log n)}, space {@code O(1)}.
   */
  public int findMinArrowShots(int[][] points) {
    Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
    int result = 0;
    int i = 0;
    while (i < points.length) {
      int end = points[i][1];
      while (i < points.length && points[i][0] <= end) {
        i += 1;
      }
      result += 1;
    }
    return result;
  }
}
