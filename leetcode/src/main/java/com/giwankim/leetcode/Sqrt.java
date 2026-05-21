package com.giwankim.leetcode;

public class Sqrt {
  /**
   * @implNote Time {@code O(log x)}, space {@code O(1)}, where {@code x} is the input value.
   */
  public int mySqrt(int x) {
    long lo = 0;
    long hi = x + 1L;
    while (lo < hi) {
      long mid = lo + (hi - lo) / 2;
      if (mid * mid > x) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    return Math.toIntExact(lo - 1);
  }

  /**
   * @implNote Time {@code O(log x)}, space {@code O(1)}, where {@code x} is the input value.
   */
  public int mySqrt2(int x) {
    int result = 0;
    int lo = 0;
    int hi = x;
    while (lo <= hi)  {
      int mid = lo + (hi - lo) / 2;
      if ((long) mid * mid <= x) {
        result = mid;
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }
    return result;
  }
}
