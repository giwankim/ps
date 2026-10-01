package leetcode.p0001_0100;

import java.util.ArrayDeque;
import java.util.Deque;

/** <a href="https://leetcode.com/problems/valid-parentheses/">20. Valid Parentheses</a> */
public class ValidParentheses {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = s.length()}: each opener
   *     pushes its expected closer, so a closing bracket needs only one equality check against the
   *     popped top. The stack peaks at {@code n} entries on an all-opener string, alongside the
   *     {@code toCharArray()} copy.
   */
  public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
      if (c == '(') {
        stack.push(')');
      } else if (c == '{') {
        stack.push('}');
      } else if (c == '[') {
        stack.push(']');
      } else if (stack.isEmpty() || c != stack.pop()) {
        return false;
      }
    }
    return stack.isEmpty();
  }
}
