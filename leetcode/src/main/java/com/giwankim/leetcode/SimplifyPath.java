package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class SimplifyPath {
  public String simplifyPath(String path) {
    // Time complexity: O(n), Space complexity: O(n)
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
