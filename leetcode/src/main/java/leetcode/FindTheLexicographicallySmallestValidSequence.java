package leetcode;

import java.util.Arrays;

public class FindTheLexicographicallySmallestValidSequence {
  private int[][][] dp;
  private int n, m;
  private String word1, word2;

  /**
   * @implNote Time {@code O(n * m)}, space {@code O(n * m)}, where {@code n = word1.length()} and
   *     {@code m = word2.length()}.
   */
  public int[] validSequence(String word1, String word2) {
    this.word1 = word1;
    this.word2 = word2;
    n = word1.length();
    m = word2.length();
    dp = new int[n][m][2];
    for (int[][] plane : dp) {
      for (int[] row : plane) {
        Arrays.fill(row, -1);
      }
    }
    if (!can(0, 0, 1)) {
      return new int[0];
    }
    int[] ans = new int[m];
    int canChange = 1;
    for (int i = 0, j = 0; i < n && j < m; i++) {
      if (word1.charAt(i) == word2.charAt(j)) {
        ans[j++] = i;
      } else if (canChange == 1 && can(i + 1, j + 1, 0)) {
        ans[j++] = i;
        canChange = 0;
      }
    }
    return ans;
  }

  private boolean can(int i, int j, int canChange) {
    if (j == m) {
      return true;
    }
    if (i == n) {
      return false;
    }
    if (dp[i][j][canChange] != -1) {
      return dp[i][j][canChange] == 1;
    }
    boolean result = false;
    if (word1.charAt(i) == word2.charAt(j)) {
      result = can(i + 1, j + 1, canChange);
    }
    if (canChange == 1) {
      result = result || can(i + 1, j + 1, 0);
    }
    result = result || can(i + 1, j, canChange);
    dp[i][j][canChange] = result ? 1 : 0;
    return result;
  }
}
