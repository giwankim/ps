package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubstringWithConcatenationOfAllWords {
  public List<Integer> findSubstring(String s, String[] words) {
    // Time complexity: O(|s|), Space complexity: O(|s|)
    Map<String, Integer> wordCount = new HashMap<>();
    for (String word : words) {
      wordCount.merge(word, 1, Integer::sum);
    }
    List<Integer> result = new ArrayList<>();
    int w = words[0].length();
    int k = words.length;
    int windowSize = w * k;

    for (int offset = 0; offset < w; offset++) {
      Map<String, Integer> window = new HashMap<>();
      int left = offset;

      for (int right = offset; right + w <= s.length(); right += w) {
        window.merge(s.substring(right, right + w), 1, Integer::sum);
        if (right + w - left == windowSize) {
          if (wordCount.equals(window)) {
            result.add(left);
          }
          window.compute(s.substring(left, left + w), (_, v) -> v == 1 ? null : v - 1);
          left += w;
        }
      }
    }
    return result;
  }
}
