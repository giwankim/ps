package com.giwankim.leetcode;

public class RansomNote {
  public boolean canConstruct(String ransomNote, String magazine) {
    // Time complexity: O(n), Space complexity: O(1)
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
