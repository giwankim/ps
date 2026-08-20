package leetcode.p0001_0100;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/** <a href="https://leetcode.com/problems/valid-parentheses/">20. Valid Parentheses</a> */
public class ValidParentheses {
  private static Map<Character, Character> MAP = Map.of(')', '(', ']', '[', '}', '{');

  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
      if (c == '(' || c == '[' || c == '{') {
        stack.push(c);
      } else {
        if (stack.isEmpty()) {
          return false;
        }
        char d = stack.pop();
        if (MAP.get(c) != d) {
          return false;
        }
      }
    }
    return stack.isEmpty();
  }
}
