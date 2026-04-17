package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class SimplifyPath {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public String simplifyPath(String path) {
    String[] tokens = path.split("/");
    Deque<String> deque = new ArrayDeque<>();
    for (String token : tokens) {
      switch (token) {
        case "", "." -> {}
        case ".." -> deque.pollLast();
        default -> deque.addLast(token);
      }
    }
    return "/" + String.join("/", deque);
  }
}
