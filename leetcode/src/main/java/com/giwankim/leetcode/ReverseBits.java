package com.giwankim.leetcode;

public class ReverseBits {
  /**
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public int reverseBits(int n) {
    int result = 0;
    for (int i = 0; i < 32; i++) {
      result <<= 1;
      result |= (n & 1);
      n >>= 1;
    }
    return result;
  }

  /**
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public int reverseBits2(int n) {
    return Integer.reverse(n);
  }

  private static final int[] REVERSED_BYTE = new int[1 << 8];

  static {
    for (int i = 0; i < REVERSED_BYTE.length; i++) {
      int reversed = 0;
      int n = i;
      for (int j = 0; j < 8; j++) {
        reversed <<= 1;
        reversed |= (n & 1);
        n >>= 1;
      }
      REVERSED_BYTE[i] = reversed;
    }
  }

  /**
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public int reverseBits3(int n) {
    return REVERSED_BYTE[n & 0xFF] << 24
        | REVERSED_BYTE[(n >> 8) & 0xFF] << 16
        | REVERSED_BYTE[(n >> 16) & 0xFF] << 8
        | REVERSED_BYTE[(n >> 24) & 0xFF];
  }
}
