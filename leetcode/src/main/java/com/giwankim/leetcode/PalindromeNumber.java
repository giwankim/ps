package com.giwankim.leetcode;

public class PalindromeNumber {
  /**
   * @implNote Time {@code O(log_10(x))}, space {@code O(1)}.
   */
  public boolean isPalindrome(int x) {
    if (x < 0) {
      return false;
    }
    return isPalindrome(String.valueOf(x));
  }

  private boolean isPalindrome(String s) {
    int left = 0;
    int right = s.length() - 1;
    while (left < right) {
      if (s.charAt(left) != s.charAt(right)) {
        return false;
      }
      left++;
      right--;
    }
    return true;
  }
}
