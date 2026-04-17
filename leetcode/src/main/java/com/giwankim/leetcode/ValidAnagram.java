package com.giwankim.leetcode;

public class ValidAnagram {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public boolean isAnagram(String s, String t) {
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
