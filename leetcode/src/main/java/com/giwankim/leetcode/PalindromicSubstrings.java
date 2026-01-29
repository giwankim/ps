package com.giwankim.leetcode;

public class PalindromicSubstrings {
  public int countSubstrings(String s) {
    int cnt = 0;
    int n = s.length();
    boolean[][] isPalindrome = new boolean[n][n];

    for (int i = 0; i < n; i++) {
      isPalindrome[i][i] = true;
      cnt += 1;
    }

    for (int i = 0; i + 1 < n; i++) {
      if (s.charAt(i) == s.charAt(i + 1)) {
        isPalindrome[i][i + 1] = true;
        cnt += 1;
      } else {
        isPalindrome[i][i + 1] = false;
      }
    }

    for (int len = 3; len <= n; len++) {
      for (int i = 0; i < n; i++) {
        int j = i + len - 1;
        if (j >= n) {
          continue;
        }
        if (s.charAt(i) == s.charAt(j) && isPalindrome[i + 1][j - 1]) {
          isPalindrome[i][j] = true;
          cnt += 1;
        } else {
          isPalindrome[i][j] = false;
        }
      }
    }
    return cnt;
  }
}
