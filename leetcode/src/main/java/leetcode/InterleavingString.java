package leetcode;

import java.util.Arrays;

public class InterleavingString {
  private int n, m;
  private int[][] dp;

  /**
   * @implNote Time {@code O(n * m)} since the memo behind the recursive overload settles each of
   *     the {@code (n + 1) * (m + 1)} prefix pairs once, branching over the two candidate
   *     characters at {@code O(1)} each, and a length mismatch short-circuits before any of that —
   *     auxiliary space {@code O(n * m)} for {@code dp}, dominating the recursion depth of up to
   *     {@code n + m} frames, where {@code n = s1.length()} and {@code m = s2.length()}.
   */
  public boolean isInterleave(String s1, String s2, String s3) {
    n = s1.length();
    m = s2.length();
    if (n + m != s3.length()) {
      return false;
    }
    dp = new int[n + 1][m + 1];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    return isInterleave(s1, 0, s2, 0, s3);
  }

  private boolean isInterleave(String s1, int i, String s2, int j, String s3) {
    if (i == n && j == m) {
      return true;
    }
    if (i > n || j > m) {
      return false;
    }
    if (dp[i][j] != -1) {
      return dp[i][j] == 1;
    }
    boolean result = false;
    if (i < n && s1.charAt(i) == s3.charAt(i + j)) {
      result |= isInterleave(s1, i + 1, s2, j, s3);
    }
    if (j < m && s2.charAt(j) == s3.charAt(i + j)) {
      result |= isInterleave(s1, i, s2, j + 1, s3);
    }
    dp[i][j] = result ? 1 : 0;
    return result;
  }
}
