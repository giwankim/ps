package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;

public class PerfectRectangle {

  public boolean isRectangleCover(int[][] rectangles) {
    // find maximum rectangular area
    long x = Integer.MAX_VALUE;
    long y = Integer.MAX_VALUE;
    long a = Integer.MIN_VALUE;
    long b = Integer.MIN_VALUE;

    long area = 0;

    for (int[] rectangle : rectangles) {
      x = Math.min(x, rectangle[0]);
      y = Math.min(y, rectangle[1]);
      a = Math.max(a, rectangle[2]);
      b = Math.max(b, rectangle[3]);

      area += ((long) (rectangle[2] - rectangle[0])) * ((long) (rectangle[3] - rectangle[1]));
    }

    // count corners
    Map<String, Integer> counts = new HashMap<>();

    for (int[] rectangle : rectangles) {
      String lb = rectangle[0] + "," + rectangle[1];
      String lu = rectangle[0] + "," + rectangle[3];
      String rt = rectangle[2] + "," + rectangle[3];
      String rb = rectangle[2] + "," + rectangle[1];

      counts.put(lb, counts.getOrDefault(lb, 0) + 1);
      counts.put(lu, counts.getOrDefault(lu, 0) + 1);
      counts.put(rt, counts.getOrDefault(rt, 0) + 1);
      counts.put(rb, counts.getOrDefault(rb, 0) + 1);
    }

    // outer corners should be 1
    if (counts.getOrDefault(x + "," + y, 0) != 1
        || counts.getOrDefault(x + "," + b, 0) != 1
        || counts.getOrDefault(a + "," + b, 0) != 1
        || counts.getOrDefault(a + "," + y, 0) != 1) {
      return false;
    }
    counts.remove(x + "," + y);
    counts.remove(x + "," + b);
    counts.remove(a + "," + b);
    counts.remove(a + "," + y);

    // all other corners should be even
    for (int count : counts.values()) {
      if (count % 2 != 0) {
        return false;
      }
    }

    // area
    return area == (a - x) * (b - y);
  }
}
