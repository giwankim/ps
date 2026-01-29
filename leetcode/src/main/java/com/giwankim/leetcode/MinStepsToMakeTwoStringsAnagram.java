package com.giwankim.leetcode;

public class MinStepsToMakeTwoStringsAnagram {
  public int minSteps(String s, String t) {
    int result = 0;

    int[] counts = new int[26];
    for (char c : t.toCharArray()) {
      counts[c - 'a'] += 1;
    }
    for (char c : s.toCharArray()) {
      counts[c - 'a'] -= 1;
    }

    for (int count : counts) {
      if (count > 0) {
        result += count;
      }
    }

    return result;
  }
}
