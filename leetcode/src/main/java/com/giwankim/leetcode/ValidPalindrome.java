package com.giwankim.leetcode;

public class ValidPalindrome {

  public boolean isPalindrome(String s) {
    int lo = 0;
    int hi = s.length() - 1;
    while (lo < hi) {
      char c = s.charAt(lo);
      char d = s.charAt(hi);
      if (!Character.isLetterOrDigit(c)) {
        lo += 1;
      } else if (!Character.isLetterOrDigit(d)) {
        hi -= 1;
      } else if (Character.toLowerCase(c) != Character.toLowerCase(d)) {
        return false;
      } else {
        lo += 1;
        hi -= 1;
      }
    }
    return true;
  }

  public boolean isPalindrome2(String s) {
    String filtered = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    String reversed = new StringBuilder(filtered).reverse().toString();
    return filtered.equals(reversed);
  }
}
