package com.giwankim.grind75;

public class ValidAnagram {
  public boolean isAnagram(String s, String t) {
    int[] counts = new int[26];

    for (char c : s.toCharArray()) {
      counts[c - 'a'] += 1;
    }

    for (char d : t.toCharArray()) {
      counts[d - 'a'] -= 1;
    }

    for (int count : counts) {
      if (count != 0) {
        return false;
      }
    }
    return true;
  }
}
