package com.giwankim.leetcode;

import java.util.HashSet;
import java.util.Set;

public class HappyNumber {
  public boolean isHappy(int n) {
    Set<Integer> set = new HashSet<>();
    while (n != 1) {
      set.add(n);
      n = sumOfSquares(n);
      if (set.contains(n)) {
        return false;
      }
    }
    return true;
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
