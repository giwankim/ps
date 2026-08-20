package leetcode.p0001_0100;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/edit-distance/">72. Edit Distance</a> */
public class EditDistance {
  private int n, m;
  private int[][] dp;

  /**
   * @implNote Time {@code O(n * m)} since the memo behind the recursive overload settles each of
   *     the {@code n * m} suffix pairs once, branching over the free match and the three edits at
   *     {@code O(1)} each — auxiliary space {@code O(n * m)} for {@code dp}, dominating the
   *     recursion depth of up to {@code n + m} frames, where {@code n = word1.length()} and
   *     {@code m = word2.length()}.
   */
  public int minDistance(String word1, String word2) {
    n = word1.length();
    m = word2.length();
    dp = new int[n][m];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    return minDistance(word1, 0, word2, 0);
  }

  private int minDistance(String word1, int i, String word2, int j) {
    if (i == n && j == m) {
      return 0;
    }
    if (i == n) {
      return m - j;
    }
    if (j == m) {
      return n - i;
    }
    if (dp[i][j] != -1) {
      return dp[i][j];
    }
    int result = Integer.MAX_VALUE;
    if (word1.charAt(i) == word2.charAt(j)) {
      result = minDistance(word1, i + 1, word2, j + 1);
    }
    result = Math.min(result, 1 + minDistance(word1, i + 1, word2, j + 1));
    result = Math.min(result, 1 + minDistance(word1, i, word2, j + 1));
    result = Math.min(result, 1 + minDistance(word1, i + 1, word2, j));
    dp[i][j] = result;
    return result;
  }
}
