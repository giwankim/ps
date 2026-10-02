package leetcode.p0001_0100;

import java.util.ArrayList;
import java.util.List;

/** <a href="https://leetcode.com/problems/generate-parentheses/">22. Generate Parentheses</a> */
public class GenerateParentheses {
  private int n;
  private List<String> result;

  /**
   * @implNote Time {@code O(n * C_n) = O(4^n / √n)}, auxiliary space {@code O(n)} excluding the
   *     output, where {@code C_n = (2n)! / ((n + 1)! * n!)} is the {@code n}-th Catalan number.
   *     <p>{@link #generateParenthesis(int, int, StringBuilder)} only extends prefixes with
   *     {@code right <= left <= n}, and every such prefix can be completed, so the recursion tree
   *     has exactly {@code C_n} leaves and at most {@code 2n * C_n} internal nodes (each one a
   *     proper prefix of some leaf). Internal nodes do {@code O(1)} work, including the
   *     out-of-range child calls that return on entry, while each leaf copies the shared
   *     {@link StringBuilder} of length {@code 2n} into a new {@link String} for {@code O(n)} work.
   *     Auxiliary space is the recursion depth (at most {@code 2n + 1} frames) plus that buffer;
   *     the output list itself occupies {@code O(n * C_n)} characters.
   */
  public List<String> generateParenthesis(int n) {
    this.n = n;
    result = new ArrayList<>();
    generateParenthesis(0, 0, new StringBuilder());
    return result;
  }

  private void generateParenthesis(int left, int right, StringBuilder curr) {
    if (left > n || right > n || left < right) {
      return;
    }
    if (left == n && right == n) {
      result.add(curr.toString());
      return;
    }
    curr.append('(');
    generateParenthesis(left + 1, right, curr);
    curr.deleteCharAt(curr.length() - 1);
    curr.append(')');
    generateParenthesis(left, right + 1, curr);
    curr.deleteCharAt(curr.length() - 1);
  }
}
