package com.giwankim.leetcode;

public class LongestPalindrome {
  String maxStr = "";

  public String longestPalindrome(String s) {
    for (int i = 0; i < s.length(); i++) {
      extendPalindrome(i, i, s);
      extendPalindrome(i, i + 1, s);
    }
    return maxStr;
  }

  private void extendPalindrome(int j, int k, String s) {
    while (j >= 0 && k < s.length() && s.charAt(j) == s.charAt(k)) {
      j -= 1;
      k += 1;
    }
    if (k - j - 1 > maxStr.length()) {
      maxStr = s.substring(j + 1, k);
    }
  }
}
