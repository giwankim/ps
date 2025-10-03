package com.giwankim.grind75;

import java.util.HashMap;
import java.util.Map;

public class LongestPalindrome {
  public int longestPalindrome(String s) {
    // count each character
    Map<Character, Integer> counts = new HashMap<>();
    for (char c : s.toCharArray()) {
      counts.put(c, counts.getOrDefault(c, 0) + 1);
    }

    // keeps track whether center character should be counted
    boolean hasCenter = false;

    int result = 0;
    for (int count : counts.values()) {
      if (count % 2  == 0) {
        // add even count chracters
        result += count;
      } else {
        // add even count
        result += count - 1;
        // record that center character should be counted
        hasCenter = true;
      }
    }

    if (hasCenter) {
      result += 1;
    }

    return result;
  }
}
