package leetcode.p0601_0700;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/valid-parenthesis-string/">678. Valid Parenthesis
 * String</a>
 */
public class ValidParenthesisString {
  private int n;
  private int[][] dp;

  /**
   * @implNote Time {@code O(n^2)}: initializing {@code dp} is quadratic, and at most {@code n^2}
   *     position-balance states are evaluated once with up to three transitions each. Auxiliary
   *     space {@code O(n^2)} for {@code dp}, plus {@code O(n)} for the recursion stack, where
   *     {@code n = s.length()}.
   */
  public boolean checkValidString(String s) {
    n = s.length();
    dp = new int[n][n];
    for (int[] r : dp) {
      Arrays.fill(r, -1);
    }
    return checkValidString(s, 0, 0);
  }

  private boolean checkValidString(String s, int start, int balance) {
    if (start == n) {
      return balance == 0;
    }
    if (balance < 0) {
      return false;
    }
    if (dp[start][balance] != -1) {
      return dp[start][balance] == 1;
    }
    char c = s.charAt(start);
    int result = 0;
    if (c == '(') {
      result = checkValidString(s, start + 1, balance + 1) ? 1 : 0;
    } else if (c == ')') {
      result = checkValidString(s, start + 1, balance - 1) ? 1 : 0;
    } else {
      result = checkValidString(s, start + 1, balance + 1)
              || checkValidString(s, start + 1, balance - 1)
              || checkValidString(s, start + 1, balance)
          ? 1
          : 0;
    }
    dp[start][balance] = result;
    return result == 1;
  }
}
