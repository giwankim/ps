package leetcode.p1101_1200;

/**
 * <a href="https://leetcode.com/problems/maximum-nesting-depth-of-two-valid-parentheses-strings/">
 * 1111. Maximum Nesting Depth of Two Valid Parentheses Strings</a>
 */
public class MaximumNestingDepthOfTwoValidParenthesesStrings {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = seq.length()}: one pass
   *     assigns each parenthesis by the parity of its nesting depth. The returned array holds
   *     {@code n} assignments; auxiliary space excluding the output is {@code O(1)} for the running
   *     depth counter and loop variables.
   */
  public int[] maxDepthAfterSplit(String seq) {
    int n = seq.length();
    int[] result = new int[n];
    int depth = 0;
    for (int i = 0; i < n; i++) {
      char c = seq.charAt(i);
      if (c == '(') {
        depth++;
        result[i] = depth & 1;
      } else if (c == ')') {
        result[i] = depth & 1;
        depth--;
      }
    }
    return result;
  }
}
