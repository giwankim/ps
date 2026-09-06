package leetcode.p0101_0200;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/distinct-subsequences/">115. Distinct Subsequences</a> */
public class DistinctSubsequences {
  private int n, m;
  private int[][] dp;

  /**
   * @implNote Time {@code O(n * m)} since the memo behind the recursive overload settles each of
   *     the {@code n * m} index pairs once, branching over skipping {@code s[i]} and, when it
   *     matches {@code t[j]}, also consuming it, at {@code O(1)} each. Auxiliary space {@code O(n *
   *     m)} for {@code dp}, dominating the recursion depth of up to {@code n} frames since every
   *     call advances {@code i}, where {@code n = s.length()} and {@code m = t.length()}.
   */
  public int numDistinct(String s, String t) {
    n = s.length();
    m = t.length();
    dp = new int[n][m];
    for (int[] a : dp) {
      Arrays.fill(a, -1);
    }
    return numDistinct(s, 0, t, 0);
  }

  private int numDistinct(String s, int i, String t, int j) {
    if (j == m) {
      return 1;
    }
    if (i == n) {
      return 0;
    }
    if (dp[i][j] != -1) {
      return dp[i][j];
    }
    int result = numDistinct(s, i + 1, t, j);
    if (s.charAt(i) == t.charAt(j)) {
      result += numDistinct(s, i + 1, t, j + 1);
    }
    dp[i][j] = result;
    return result;
  }
}
