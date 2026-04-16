package com.giwankim.leetcode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class WordLadder {
  /**
   * Time complexity: {@code O(n * m^2)}. Space complexity: {@code O(n * m)}.
   *
   * <p>Where {@code n = wordList.size()} and {@code m = beginWord.length()}.
   */
  public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> wordSet = Set.copyOf(wordList);
    Queue<String> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();

    queue.offer(beginWord);
    visited.add(beginWord);

    int result = 1;
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        String word = queue.poll();
        if (Objects.equals(word, endWord)) {
          return result;
        }
        StringBuilder sb = new StringBuilder(word);
        for (int i = 0; i < sb.length(); i++) {
          for (char c = 'a'; c <= 'z'; c++) {
            sb.setCharAt(i, c);
            String s = sb.toString();
            if (wordSet.contains(s) && !visited.contains(s)) {
              queue.offer(s);
              visited.add(s);
            }
            sb.setCharAt(i, word.charAt(i));
          }
        }
      }
      result += 1;
    }
    return 0;
  }
}
