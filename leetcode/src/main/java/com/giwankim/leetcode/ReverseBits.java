package com.giwankim.leetcode;

public class ReverseBits {
  public int reverseBits(int n) {
    // Time complexity: O(1), Space complexity: O(1)
    int result = 0;
    for (int i = 0; i < 32; i++) {
      result <<= 1;
      result |= (n & 1);
      n >>= 1;
    }
    return result;
  }
}
