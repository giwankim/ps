package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WordPattern {
  public boolean wordPattern(String pattern, String s) {
    // Time complexity: O(|pattern| + |s|), Space complexity: O(|s|)
    String[] words = s.split(" ");
    if (pattern.length() != words.length) {
      return false;
    }

    Map<String, Integer> map = new HashMap<>();
    for (int i = 0; i < words.length; i++) {
      String c = String.valueOf(pattern.charAt(i));
      String word = words[i];
      map.putIfAbsent(c, i);
      map.putIfAbsent(word, i);
      if (!Objects.equals(map.get(c), map.get(word))) {
        return false;
      }
    }
    return true;
  }
}
