package grind75;

import java.util.ArrayDeque;
import java.util.Deque;

public class ValidParentheses {
  /**
   * @implNote Time and auxiliary space {@code O(n)}, where {@code n = s.length()}; the stack holds
   *     one expected closer per unmatched opener, so each closing bracket costs a single equality
   *     check against the popped top.
   */
  public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
      if (c == '(') {
        stack.push(')');
      } else if (c == '[') {
        stack.push(']');
      } else if (c == '{') {
        stack.push('}');
      } else if (stack.isEmpty()) {
        return false;
      } else if (stack.pop() != c) {
        return false;
      }
    }
    return stack.isEmpty();
  }
}
