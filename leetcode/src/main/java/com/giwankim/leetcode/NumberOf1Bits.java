package com.giwankim.leetcode;

public class NumberOf1Bits {
  public int hammingWeight(int n) {
    int result = 0;
    while (n != 0) {
      result += (n & 1);
      n >>= 1;
    }
    return result;
  }
}
