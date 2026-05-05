package com.giwankim.leetcode;

public class PlusOne {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)} excluding the returned array, where
   *     {@code n = digits.length}.
   */
  public int[] plusOne(int[] digits) {
    for (int i = digits.length - 1; i >= 0; i--) {
      if (digits[i] == 9) {
        digits[i] = 0;
        continue;
      }
      digits[i] += 1;
      return digits;
    }
    int[] result = new int[digits.length + 1];
    result[0] = 1;
    return result;
  }
}
