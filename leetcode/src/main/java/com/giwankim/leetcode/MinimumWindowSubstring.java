package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {
  public String minWindow(String s, String t) {
    // Time complexity: O(n + m), Space complexity: O(n + m)
    String result = s;

    Map<Character, Integer> need = new HashMap<>();
    for (char c : t.toCharArray()) {
      need.merge(c, 1, Integer::sum);
    }

    Map<Character, Integer> have = new HashMap<>();
    int formed = 0;
    int required = need.size();
    int i = 0;
    for (int j = 0; j < s.length(); j++) {
      have.merge(s.charAt(j), 1, Integer::sum);
      if (have.get(s.charAt(j)).equals(need.get(s.charAt(j)))) {
        formed++;
      }
      if (formed == required) {
        while (i <= j
            && (!need.containsKey(s.charAt(i)) || have.get(s.charAt(i)) > need.get(s.charAt(i)))) {
          have.merge(s.charAt(i), -1, Integer::sum);
          i++;
        }
        if (result.length() > j - i + 1) {
          result = s.substring(i, j + 1);
        }
      }
    }
    return formed == required ? result : "";
  }
}
