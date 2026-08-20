package leetcode.p1501_1600;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/stone-game-v/">1563. Stone Game V</a> */
public class StoneGameV {
  private int[][] dp;
  private int[] psum;

  /**
   * @implNote Time {@code O(n^3)} since the memo behind {@code maxValue} settles each of the
   *     {@code O(n^2)} {@code (start, end)} intervals once and every interval scans its up-to-n cut
   *     points, each {@code O(1)} because {@code psum} turns a half's total into a single
   *     subtraction — auxiliary space {@code O(n^2)} for {@code dp} plus {@code O(n)} for
   *     {@code psum}, matched by a recursion depth of up to {@code n} frames, where {@code n =
   *     stoneValue.length}.
   */
  public int stoneGameV(int[] stoneValue) {
    int n = stoneValue.length;
    dp = new int[n][n];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    psum = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      psum[i] = psum[i - 1] + stoneValue[i - 1];
    }
    return maxValue(stoneValue, 0, n - 1);
  }

  private int maxValue(int[] stoneValue, int start, int end) {
    if (start > end) {
      return Integer.MIN_VALUE;
    }
    if (start == end) {
      return 0;
    }
    if (dp[start][end] != -1) {
      return dp[start][end];
    }
    int result = Integer.MIN_VALUE;
    for (int i = start; i < end; i++) {
      // stoneValue[start:i]
      int left = psum[i + 1] - psum[start];
      // stoneValue[i+1:end]
      int right = psum[end + 1] - psum[i + 1];
      if (left < right) {
        result = Math.max(result, left + maxValue(stoneValue, start, i));
      } else if (left > right) {
        result = Math.max(result, right + maxValue(stoneValue, i + 1, end));
      } else {
        result = Math.max(
            result,
            Math.max(
                left + maxValue(stoneValue, start, i), right + maxValue(stoneValue, i + 1, end)));
      }
    }
    dp[start][end] = result;
    return result;
  }
}
