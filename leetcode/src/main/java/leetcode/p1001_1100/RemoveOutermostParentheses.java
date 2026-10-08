package leetcode.p1001_1100;

/**
 * <a href="https://leetcode.com/problems/remove-outermost-parentheses/">1021. Remove Outermost
 * Parentheses</a>
 */
public class RemoveOutermostParentheses {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)}, where {@code n = s.length()}: one
   *     pass tracks the open-paren balance and copies every character except the opener that lifts
   *     it from 0 and the closer that returns it to 0, which are exactly the outermost pair of each
   *     primitive. The space is the array made by {@code s.toCharArray()} plus the result builder.
   */
  public String removeOuterParentheses(String s) {
    StringBuilder result = new StringBuilder();
    int balance = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        if (balance > 0) {
          result.append(c);
        }
        balance++;
      } else {
        balance--;
        if (balance > 0) {
          result.append(c);
        }
      }
    }
    return result.toString();
  }
}
