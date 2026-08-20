package leetcode.p0201_0300;

import java.util.ArrayDeque;
import java.util.Deque;

/** <a href="https://leetcode.com/problems/basic-calculator/">224. Basic Calculator</a> */
public class BasicCalculator {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public int calculate(String s) {
    int result = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    int val = 0;
    int sgn = 1;
    for (char c : s.toCharArray()) {
      if (Character.isDigit(c)) {
        val = val * 10 + (c - '0');
      } else if (c == '+') {
        result += sgn * val;
        val = 0;
        sgn = 1;
      } else if (c == '-') {
        result += sgn * val;
        val = 0;
        sgn = -1;
      } else if (c == '(') {
        stack.push(result);
        stack.push(sgn);
        result = 0;
        sgn = 1;
      } else if (c == ')') {
        result += sgn * val;
        val = 0;
        result *= stack.pop();
        result += stack.pop();
      }
    }
    return result + sgn * val;
  }
}
