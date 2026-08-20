package leetcode.p0001_0100;

import java.util.ArrayDeque;
import java.util.Deque;

/** <a href="https://leetcode.com/problems/simplify-path/">71. Simplify Path</a> */
public class SimplifyPath {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
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
