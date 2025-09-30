package com.giwankim.grind75;

public class RansomNote {
  public boolean canConstruct(String ransomNote, String magazine) {
    int[] counts = new int[26];

    // count letters in magazine
    for (char c : magazine.toCharArray()) {
      counts[c - 'a'] += 1;
    }

    // check if ransomNote can be constructed from magazine
    for (char c : ransomNote.toCharArray()) {
      counts[c - 'a'] -= 1;
      if (counts[c - 'a'] < 0) {
        return false;
      }
    }
    return true;
  }
}
