package com.giwankim.leetcode;

import java.util.Arrays;
import java.util.List;

public class ReverseWordsInAString {
  public String reverseWords(String s) {
    // Time complexity: O(n), Space complexity: O(n)
    String[] words = s.trim().split("\\s+");
    List<String> reversedWords = Arrays.asList(words).reversed();
    return String.join(" ", reversedWords);
  }
}
