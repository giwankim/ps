package com.giwankim.leetcode;

import java.util.Map;

public class RomanToInteger {
  private static final Map<Character, Integer> map =
      Map.of(
          'I', 1,
          'V', 5,
          'X', 10,
          'L', 50,
          'C', 100,
          'D', 500,
          'M', 1000);

  public int romanToInt(String s) {
    // Time complexity: O(1) since there is a finite set of Roman numerals, Space complexity: O(1)
    int result = 0;
    for (int i = 0; i < s.length(); i++) {
      if (i + 1 < s.length() && map.get(s.charAt(i)) < map.get(s.charAt(i + 1))) {
        result += -map.get(s.charAt(i));
      } else {
        result += map.get(s.charAt(i));
      }
    }
    return result;
  }
}
