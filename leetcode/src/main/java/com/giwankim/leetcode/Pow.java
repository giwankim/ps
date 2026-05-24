package com.giwankim.leetcode;

public class Pow {
  /**
   * @implNote Time {@code O(log |n|)}, space {@code O(1)}.
   */
  public double myPow(double x, int n) {
    if (n < 0) {
      return (1 / x) * 1 / myPow(x, -(n + 1));
    }
    if (n == 1) {
      return x;
    }
    double result = 1.0;
    while (n > 0) {
      if (n % 2 == 1) {
        result *= x;
      }
      x *= x;
      n /= 2;
    }
    return result;
  }

  /**
   * @implNote Time {@code O(log |n|)}, space {@code O(log |n|)} for the recursion stack.
   */
  public double myPow2(double x, int n) {
    if (n == 0) {
      return 1.0;
    }
    if (n < 0) {
      return (1 / x) * myPow2(1 / x, -(n + 1));
    }
    if ((n & 1) == 1) {
      return x * myPow2(x * x, n / 2);
    }
    return myPow2(x * x, n / 2);
  }
}
