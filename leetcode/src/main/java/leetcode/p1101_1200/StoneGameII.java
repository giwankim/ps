package leetcode.p1101_1200;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/stone-game-ii/">1140. Stone Game II</a> */
public class StoneGameII {
  private int n;
  private int[][] dp;
  private int[] psum;

  /**
   * @implNote Time {@code O(n^3)} since the memo behind {@code maxStones} settles each of the
   *     {@code O(n^2)} {@code (start, m)} states once and every state scans up to {@code 2m}
   *     opening moves, each {@code O(1)} because {@code psum} turns a suffix total into a single
   *     subtraction — auxiliary space {@code O(n^2)} for {@code dp} plus {@code O(n)} for
   *     {@code psum}, matched by a recursion depth of up to {@code n} frames, where {@code n =
   *     piles.length} and {@code m} is the current take cap, itself bounded by {@code n}.
   */
  public int stoneGameII(int[] piles) {
    n = piles.length;
    dp = new int[n][n + 1];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }
    psum = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      psum[i] = psum[i - 1] + piles[i - 1];
    }
    return maxStones(0, 1);
  }

  private int maxStones(int start, int m) {
    if (start == n) {
      return 0;
    }
    if (start > n) {
      return Integer.MIN_VALUE;
    }
    if (dp[start][m] != -1) {
      return dp[start][m];
    }
    int result = Integer.MIN_VALUE;
    int total = psum[n] - psum[start];
    for (int x = 1; x <= 2 * m && (start + x - 1) < n; x++) {
      int stones = total - maxStones(start + x, Math.max(m, x));
      result = Math.max(result, stones);
    }
    dp[start][m] = result;
    return result;
  }
}
