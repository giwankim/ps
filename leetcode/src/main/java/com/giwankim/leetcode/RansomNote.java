package com.giwankim.leetcode;

public class RansomNote {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public boolean canConstruct(String ransomNote, String magazine) {
    int[] counts = new int[256];
    for (char c : magazine.toCharArray()) {
      counts[c]++;
    }
    for (char c : ransomNote.toCharArray()) {
      counts[c]--;
      if (counts[c] < 0) {
        return false;
      }
    }
    return true;
  }
}
