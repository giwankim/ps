package com.giwankim.leetcode;

public class ClimbingStairs {
  /**
   * @implNote Bottom-up tabulation of {@code f(n) = f(n-1) + f(n-2)}. Time {@code O(n)}, space
   *     {@code O(1)}.
   */
  public int climbStairs(int n) {
    if (n <= 2) {
      return n;
    }
    int prev = 1;
    int curr = 2;
    for (int i = 3; i <= n; i++) {
      int temp = curr;
      curr += prev;
      prev = temp;
    }
    return curr;
  }
}
