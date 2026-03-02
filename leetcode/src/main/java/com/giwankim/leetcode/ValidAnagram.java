package com.giwankim.leetcode;

public class ValidAnagram {
  public boolean isAnagram(String s, String t) {
    // Time complexity: O(n), Space complexity: O(1)
    if (s.length() != t.length()) {
      return false;
    }
    int[] counts = new int[126];
    for (char c : s.toCharArray()) {
      counts[c]++;
    }
    for (char c : t.toCharArray()) {
      counts[c]--;
    }
    for (int count : counts) {
      if (count != 0) {
        return false;
      }
    }
    return true;
  }
}
