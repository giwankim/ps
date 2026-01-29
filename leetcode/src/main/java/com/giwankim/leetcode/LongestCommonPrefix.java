package com.giwankim.leetcode;

public class LongestCommonPrefix {
  public String longestCommonPrefix(String[] strs) {
    String result = strs[0];
    for (int i = 1; i < strs.length; i++) {
      result = commonPrefix(result, strs[i]);
    }
    return result;
  }

  private String commonPrefix(String s, String t) {
    int i = 0;
    while (i < s.length() && i < t.length() && s.charAt(i) == t.charAt(i)) {
      i += 1;
    }
    return s.substring(0, i);
  }
}
