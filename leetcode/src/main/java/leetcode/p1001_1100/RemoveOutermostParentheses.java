package leetcode.p1001_1100;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/remove-outermost-parentheses/">1021. Remove Outermost
 * Parentheses</a>
 */
public class RemoveOutermostParentheses {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)}, where {@code n = s.length()}: one
   *     pass tracks the open-paren balance, and each character is copied a constant number of
   *     times, into {@code s.toCharArray()}, a primitive's builder, its stripped piece in
   *     {@code parens}, and the joined result. The array and the pieces account for the space.
   */
  public String removeOuterParentheses(String s) {
    List<String> parens = new ArrayList<>();
    StringBuilder curr = new StringBuilder();
    int balance = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        if (balance > 0) {
          curr.append(c);
        }
        balance++;
      } else if (c == ')') {
        balance--;
        if (balance == 0) {
          parens.add(curr.toString());
          curr = new StringBuilder();
        } else {
          curr.append(c);
        }
      }
    }
    return String.join("", parens);
  }
}
