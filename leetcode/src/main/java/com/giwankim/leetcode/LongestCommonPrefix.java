package com.giwankim.leetcode;

public class LongestCommonPrefix {
  public String longestCommonPrefix(String[] strs) {
    // Time complexity: O(n*m), Space complexity: O(1)
    String prefix = strs[0];
    for (int i = 1; i < strs.length; i++) {
      int j = 0;
      while (j < prefix.length() && j < strs[i].length() && prefix.charAt(j) == strs[i].charAt(j)) {
        j++;
      }
      prefix = prefix.substring(0, j);
    }
    return prefix;
  }
}
