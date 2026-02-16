package com.giwankim.leetcode;

public class IsSubsequence {
  public boolean isSubsequence(String s, String t) {
    // Time complexity: O(m), Space complexity: O(1)
    int i = 0;
    for (int j = 0; i < s.length() && j < t.length(); j++) {
      if (s.charAt(i) == t.charAt(j)) {
        i++;
      }
    }
    return i == s.length();
  }
}
