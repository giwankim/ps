package com.giwankim.leetcode;

public class HappyNumber {
  /**
   * @implNote Time {@code O(log n)}, space {@code O(1)}.
   */
  public boolean isHappy(int n) {
    int fast = n;
    int slow = n;
    do {
      slow = sumOfSquares(slow);
      fast = sumOfSquares(sumOfSquares(fast));
    } while (fast != 1 && slow != fast);
    return fast == 1;
  }

  private int sumOfSquares(int n) {
    int result = 0;
    while (n != 0) {
      int digit = n % 10;
      result += digit * digit;
      n /= 10;
    }
    return result;
  }
}
