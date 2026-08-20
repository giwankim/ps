package leetcode.p0801_0900;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/stone-game/">877. Stone Game</a> */
public class StoneGame {
  private int[][] dp;
  private int[] psum;

  /**
   * @implNote Time {@code O(n^2)} since clearing {@code dp} touches {@code O(n^2)} cells and the
   *     memo behind {@code maxStones} then fills those same {@code O(n^2)} intervals at
   *     {@code O(1)} each, the {@code psum} build being linear — auxiliary space {@code O(n^2)} for
   *     {@code dp}, dominating the {@code O(n)} held by {@code psum} and the recursion depth, where
   *     {@code n = piles.length}.
   */
  public boolean stoneGame(int[] piles) {
    int n = piles.length;
    dp = new int[n][n];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    psum = new int[n + 1];
    int total = 0;
    for (int i = 0; i < n; i++) {
      total += piles[i];
      psum[i + 1] = total;
    }
    int stones = maxStones(piles, 0, n - 1);
    return stones > total - stones;
  }

  private int maxStones(int[] piles, int left, int right) {
    if (left > right) {
      return 0;
    }
    if (dp[left][right] != -1) {
      return dp[left][right];
    }
    int total = psum[right + 1] - psum[left];
    int result = total - maxStones(piles, left + 1, right);
    result = Math.max(result, total - maxStones(piles, left, right - 1));
    dp[left][right] = result;
    return result;
  }
}
