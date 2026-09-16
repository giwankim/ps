package leetcode.p0601_0700;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/palindromic-substrings/">647. Palindromic Substrings</a>
 */
public class PalindromicSubstrings {
  private int n;
  private int[][] dp;

  /**
   * @implNote Time {@code O(n^2)}: filling {@code dp} and walking every start-end pair are both
   *     quadratic, and the memo behind {@link #isPalindrome} settles each of those pairs once at
   *     {@code O(1)}, every later lookup being a hit. Space {@code O(n^2)} for {@code dp}, which
   *     dominates the {@code O(n)} recursion stack spent stepping both ends inward, where {@code n
   *     = s.length()}.
   */
  public int countSubstrings(String s) {
    n = s.length();
    dp = new int[n][n];
    for (int[] r : dp) {
      Arrays.fill(r, -1);
    }
    int result = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        result += isPalindrome(s, i, j) ? 1 : 0;
      }
    }
    return result;
  }

  private boolean isPalindrome(String s, int i, int j) {
    if (i > j) {
      return true;
    }
    if (dp[i][j] != -1) {
      return dp[i][j] == 1;
    }
    if (s.charAt(i) != s.charAt(j)) {
      dp[i][j] = 0;
      return false;
    }
    dp[i][j] = isPalindrome(s, i + 1, j - 1) ? 1 : 0;
    return dp[i][j] == 1;
  }
}
