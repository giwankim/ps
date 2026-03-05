package com.giwankim.leetcode;

public class AddBinary {
  public String addBinary(String a, String b) {
    // Time complexity: O(|a| + |b|), Space complexity: O(1)
    StringBuilder result = new StringBuilder();
    int i = a.length() - 1;
    int j = b.length() - 1;
    int carry = 0;
    while (i >= 0 || j >= 0 || carry != 0) {
      int sum = carry;
      if (i >= 0) {
        sum += a.charAt(i) - '0';
      }
      if (j >= 0) {
        sum += b.charAt(j) - '0';
      }
      result.append(sum % 2);
      carry = sum / 2;
      i--;
      j--;
    }
    return result.reverse().toString();
  }
}
