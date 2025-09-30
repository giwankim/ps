package com.giwankim.leetcode;

public class PerfectRectangle {

  public boolean isRectangleCover(int[][] rectangles) {
    // find maximum rectangular area
    int x = Integer.MAX_VALUE;
    int y = Integer.MAX_VALUE;
    int a = Integer.MIN_VALUE;
    int b = Integer.MIN_VALUE;

    for (int[] rectangle : rectangles) {
      x = Math.min(x, rectangle[0]);
      y = Math.min(y, rectangle[1]);
      a = Math.max(a, rectangle[2]);
      b = Math.max(b, rectangle[3]);
    }

    // check that each of the grid in [x, y, a, b] is contained in exactly one rectangle
    for (int i = x; i < a; i++) {
      for (int j = y; j < b; j++) {
        // check for [i, j, i + 1, j + 1]
        int count = 0; // number of overlaps with rectangles
        for (int[] rectangle : rectangles) {
          if (rectangle[0] <= i
              && rectangle[1] <= j
              && i + 1 <= rectangle[2]
              && j + 1 <= rectangle[3]) {
            count += 1;
          }
        }
        if (count != 1) {
          return false;
        }
      }
    }

    return true;
  }
}
