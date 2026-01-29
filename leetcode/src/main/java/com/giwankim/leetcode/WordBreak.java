package com.giwankim.leetcode;

import java.util.List;

public class WordBreak {
  public boolean wordBreak(String s, List<String> wordDict) {
    boolean[] a = new boolean[s.length() + 1];
    a[0] = true;
    for (int i = 1; i <= s.length(); i++) {
      for (String word : wordDict) {
        if (word.length() > i) {
          continue;
        }
        if (!a[i - word.length()]) {
          continue;
        }
        if (word.equals(s.substring(i - word.length(), i))) {
          a[i] = true;
          break;
        }
      }
    }
    return a[s.length()];
  }
}
