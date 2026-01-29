package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MinWindowSubstring {
  public String minWindow(String s, String t) {
    // character count of t
    Map<Character, Integer> tCount = new HashMap<>();
    for (char c : t.toCharArray()) {
      tCount.put(c, tCount.getOrDefault(c, 0) + 1);
    }

    int minLen = Integer.MAX_VALUE;
    int ansStart = 0;
    Map<Character, Integer> sCount = new HashMap<>(); // window
    int left = 0;
    int right = 0;
    int count = 0; // matched characters
    while (right < s.length()) {
      // add s[right] to window
      char c = s.charAt(right);
      if (tCount.containsKey(c)) {
        sCount.put(c, sCount.getOrDefault(c, 0) + 1);
        if (Objects.equals(tCount.get(c), sCount.get(c))) {
          count += 1;
        }
      }

      while (count == tCount.size()) {
        if (right - left + 1 < minLen) {
          ansStart = left;
          minLen = right - left + 1;
        }

        // shift beginning of window by one
        char d = s.charAt(left);
        if (sCount.containsKey(d) && Objects.equals(sCount.get(d), tCount.get(d))) {
          count -= 1;
        }
        sCount.computeIfPresent(d, (k, v) -> v == 1 ? null : v - 1);
        left += 1;
      }

      right += 1;
    }

    return minLen == Integer.MAX_VALUE ? "" : s.substring(ansStart, ansStart + minLen);
  }
}
