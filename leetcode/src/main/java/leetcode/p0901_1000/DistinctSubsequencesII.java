package leetcode.p0901_1000;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/distinct-subsequences-ii/">940. Distinct Subsequences
 * II</a>
 */
public class DistinctSubsequencesII {
  public static final int MOD = 1_000_000_007;

  /**
   * @implNote Time {@code O(n)} since each letter is appended once, doubling the running count and
   *     subtracting the count that stood just before that letter's previous occurrence, at
   *     {@code O(1)} each. Auxiliary space {@code O(n)} for {@code dp}, dominating the 26-slot
   *     {@code last} table, where {@code n = s.length()}.
   */
  public int distinctSubseqII(String s) {
    int n = s.length();
    int[] dp = new int[n + 1];
    dp[0] = 1;
    int[] last = new int[26];
    Arrays.fill(last, -1);
    for (int i = 1; i <= n; i++) {
      int idx = s.charAt(i - 1) - 'a';
      dp[i] = (2 * dp[i - 1]) % MOD;
      if (last[idx] != -1) {
        dp[i] = (dp[i] - dp[last[idx] - 1]) % MOD;
      }
      last[idx] = i;
    }
    dp[n]--;
    if (dp[n] < 0) {
      dp[n] += MOD;
    }
    return dp[n];
  }
}
