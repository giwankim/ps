package com.giwankim.leetcode;

import java.util.Arrays;
import java.util.List;

public class ReverseWordsInAString {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public String reverseWords(String s) {
    String[] words = s.trim().split("\\s+");
    List<String> reversedWords = Arrays.asList(words).reversed();
    return String.join(" ", reversedWords);
  }
}
