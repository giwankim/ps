package com.giwankim.leetcode;

public class AddBinary {
  /** @implNote Time {@code O(|a| + |b|)}, space {@code O(1)}. */
  public String addBinary(String a, String b) {
    StringBuilder sb = new StringBuilder();
    int i = a.length() - 1;
    int j = b.length() - 1;
    int carry = 0;
    while (i >= 0 || j >= 0) {
      if (i >= 0) {
        carry += a.charAt(i) - '0';
        i--;
      }
      if (j >= 0) {
        carry += b.charAt(j) - '0';
        j--;
      }
      sb.append(carry % 2);
      carry /= 2;
    }
    if (carry > 0) {
      sb.append('1');
    }
    return sb.reverse().toString();
  }
}
