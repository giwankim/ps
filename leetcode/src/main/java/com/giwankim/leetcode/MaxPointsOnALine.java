package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;

public class MaxPointsOnALine {
  /**
   * @implNote Time {@code O(n² log C)}, space {@code O(n)} excluding the input, where {@code n =
   *     points.length} and {@code C} is the maximum absolute coordinate value.
   */
  public int maxPoints(int[][] points) {
    if (points.length <= 2) {
      return points.length;
    }
    int result = 0;
    for (int i = 0; i < points.length; i++) {
      Map<Fraction, Integer> slopeCount = new HashMap<>();
      for (int j = i + 1; j < points.length; j++) {
        int dx = points[j][0] - points[i][0];
        int dy = points[j][1] - points[i][1];
        Fraction slope = new Fraction(dy, dx);
        int pointsOnLine = slopeCount.merge(slope, 1, Integer::sum) + 1;
        result = Math.max(result, pointsOnLine);
      }
    }
    return result;
  }

  /**
   * @implNote Time {@code O(log C)} for the Euclidean {@code gcd}, space {@code O(1)} where
   *     {@code C} is the larger of the two input magnitudes.
   */
  private record Fraction(int numerator, int denominator) {
    Fraction {
      int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
      numerator /= gcd;
      denominator /= gcd;
      if (denominator < 0 || (denominator == 0 && numerator < 0)) {
        numerator = -numerator;
        denominator = -denominator;
      }
    }

    private static int gcd(int a, int b) {
      while (b != 0) {
        int remainer = a % b;
        a = b;
        b = remainer;
      }
      return a;
    }
  }
}
