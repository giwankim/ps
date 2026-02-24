package com.giwankim.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstringWithoutRepeatingCharacters {
  public int lengthOfLongestSubstring(String s) {
    // Time complexity: O(n), Space complexity: O(1)
    int result = 0;
    int i = 0;
    Set<Character> window = new HashSet<>();
    for (int j = 0; j < s.length(); j++) {
      while (window.contains(s.charAt(j))) {
        window.remove(s.charAt(i));
        i++;
      }
      result = Math.max(result, j - i + 1);
      window.add(s.charAt(j));
    }
    return result;
  }
}
