package com.giwankim.leetcode;

public class FindTheIndexOfTheFirstOccurrenceInAString {
  public int strStr(String haystack, String needle) {
    int n = haystack.length();
    int m = needle.length();
    for (int i = 0; i <= n - m; i++) {
      for (int j = 0; j < m; j++) {
        if (haystack.charAt(i + j) != needle.charAt(j)) {
          break;
        }
        if (j == m - 1) {
          return i;
        }
      }
    }
    return -1;
  }
}
