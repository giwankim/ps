package com.giwankim.grind75;

public class ValidPalindrome {

  public boolean isPalindrome(String s) {
    int left = 0;
    int right = s.length() - 1;
    while (left <= right) {
      char c = s.charAt(left);
      char d = s.charAt(right);
      if (!Character.isLetterOrDigit(c)) {
        left += 1;
      } else if (!Character.isLetterOrDigit(d)) {
        right -= 1;
      } else {
        if (Character.toLowerCase(c) != Character.toLowerCase(d)) {
          return false;
        }
        left += 1;
        right -= 1;
      }
    }
    return true;
  }
}
