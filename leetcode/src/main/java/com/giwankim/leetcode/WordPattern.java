package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordPattern {
  public boolean wordPattern(String pattern, String s) {
    // Time complexity: O(|pattern| + |s|), Space complexity: O(|pattern| + |s|)
    String[] words = s.split(" ");
    if (pattern.length() != words.length) {
      return false;
    }
    Map<Character, String> charToWord = new HashMap<>();
    Set<String> range = new HashSet<>();
    for (int i = 0; i < words.length; i++) {
      char c = pattern.charAt(i);
      String word = words[i];
      if (charToWord.containsKey(c)) {
        if (!charToWord.get(c).equals(word)) {
          return false;
        }
      } else {
        if (range.contains(word)) {
          return false;
        }
        charToWord.put(c, word);
        range.add(word);
      }
    }
    return true;
  }
}
