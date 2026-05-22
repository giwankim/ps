package com.giwankim.leetcode;

import java.util.List;
import java.util.Set;

public class WordBreak {
  /**
   * @implNote Bottom-up DP testing each {@code O(n^2)} substring {@code s[j, i)} against a hashed
   *     copy of the dictionary. Time {@code O(n^3 + m * L)}, auxiliary space {@code O(n + m * L)}
   *     for the {@code dp} array and {@code wordSet}, where {@code n = s.length()}, {@code m =
   *     wordDict.size()}, and {@code L} is the longest word length in {@code wordDict}.
   */
  public boolean wordBreak(String s, List<String> wordDict) {
    Set<String> wordSet = Set.copyOf(wordDict);
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;
    for (int i = 1; i < s.length() + 1; i++) {
      for (int j = 0; j < i; j++) {
        if (dp[j] && wordSet.contains(s.substring(j, i))) {
          dp[i] = true;
          break;
        }
      }
    }
    return dp[s.length()];
  }

  /**
   * @implNote Time {@code O(n * m * L)}, auxiliary space {@code O(n)} for the {@code dp} array,
   *     where {@code n = s.length()}, {@code m = wordDict.size()}, and {@code L} is the longest
   *     word length in {@code wordDict}.
   */
  public boolean wordBreak2(String s, List<String> wordDict) {
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;
    for (int i = 1; i <= s.length(); i++) {
      for (String word : wordDict) {
        if ((i - word.length() < 0)
            || !dp[i - word.length()]
            || !s.startsWith(word, i - word.length())) {
          continue;
        }
        dp[i] = true;
        break;
      }
    }
    return dp[s.length()];
  }
}
