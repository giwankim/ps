package leetcode;

import java.util.Arrays;

public class PredictTheWinner {
  private static int n;
  private static int[][] dp;
  private static int[] psum;

  /**
   * @implNote Time {@code O(n^2)} since the {@code psum} build is linear and the memo behind
   *     {@code score} then fills {@code O(n^2)} intervals at {@code O(1)} each — auxiliary space
   *     {@code O(n^2)} for {@code dp}, dominating the {@code O(n)} held by {@code psum} and the
   *     recursion depth, where {@code n = nums.length}.
   */
  public boolean predictTheWinner(int[] nums) {
    n = nums.length;
    dp = new int[n][n];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    psum = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      psum[i] = psum[i - 1] + nums[i - 1];
    }
    int total = psum[n];
    int score = score(0, n - 1);
    return score >= (total - score);
  }

  private static int score(int left, int right) {
    if (left > right) {
      return 0;
    }
    if (dp[left][right] != -1) {
      return dp[left][right];
    }
    int total = psum[right + 1] - psum[left];
    int result = Integer.MIN_VALUE;
    result = Math.max(result, total - score(left + 1, right)); // choose left
    result = Math.max(result, total - score(left, right - 1)); // choose right
    dp[left][right] = result;
    return result;
  }
}
