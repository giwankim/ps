package leetcode.p1101_1200;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <a
 * href="https://leetcode.com/problems/reverse-substrings-between-each-pair-of-parentheses/">1190.
 * Reverse Substrings Between Each Pair of Parentheses</a>
 */
public class ReverseSubstringsBetweenEachPairOfParentheses {
  /**
   * @implNote Time {@code O(n * d)}, space {@code O(n)}, where {@code n = s.length()} and {@code d}
   *     is the maximum nesting depth ({@code d <= n / 2}, so {@code O(n^2)} in the worst case):
   *     each {@code ')'} pops, reverses, and re-pushes every character inside its pair, so a
   *     character is copied once per enclosing pair. The stack never holds more than {@code n}
   *     characters across its chunks.
   */
  public String reverseParentheses(String s) {
    Deque<String> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
      if (c == ')') {
        StringBuilder curr = new StringBuilder();
        while (!stack.isEmpty() && stack.peek().charAt(0) != '(') {
          curr.append(new StringBuilder(stack.pop()).reverse());
        }
        stack.pop();
        if (!curr.isEmpty()) {
          stack.push(curr.toString());
        }
      } else {
        stack.push(String.valueOf(c));
      }
    }
    return String.join("", stack.reversed());
  }
}
