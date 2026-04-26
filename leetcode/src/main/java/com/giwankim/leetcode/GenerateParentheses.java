package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
  /**
   * @implNote Time {@code O(4^n / √n)}, space {@code O(n)} excluding the output.
   *     <p>The count of well-formed strings is the n-th Catalan number
   *     {@code C_n = (2n choose n) / (n + 1) ~ 4^n / (n^{3/2} √π)}, and each of
   *     length {@code 2n} costs {@code O(n)} to copy from the shared
   *     {@link StringBuilder}. Auxiliary space is the recursion depth
   *     ({@code 2n}) plus that buffer; the output list itself occupies
   *     {@code O(n * C_n)}.
   */
  public List<String> generateParenthesis(int n) {
    List<String> result = new ArrayList<>();
    generateParenthesis(n, 0, 0, new StringBuilder(), result);
    return result;
  }

  private void generateParenthesis(
      int n, int left, int right, StringBuilder current, List<String> result) {
    if (left + right == 2 * n) {
      result.add(current.toString());
      return;
    }
    if (left < n) {
      current.append('(');
      generateParenthesis(n, left + 1, right, current, result);
      current.deleteCharAt(current.length() - 1);
    }
    if (left > right) {
      current.append(')');
      generateParenthesis(n, left, right + 1, current, result);
      current.deleteCharAt(current.length() - 1);
    }
  }
}
