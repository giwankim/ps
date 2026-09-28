package leetcode.p1601_1700;

/**
 * <a href="https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/">1614. Maximum
 * Nesting Depth of the Parentheses</a>
 */
public class MaximumNestingDepthOfTheParentheses {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = s.length()}: one pass moves a
   *     running depth counter up on {@code '('} and down on {@code ')'}, recording its peak. The
   *     space is only the {@code toCharArray()} copy; the scan itself keeps two counters.
   */
  public int maxDepth(String s) {
    int result = 0;
    int depth = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        depth++;
        result = Math.max(result, depth);
      } else if (c == ')') {
        depth--;
      }
    }
    return result;
  }
}
