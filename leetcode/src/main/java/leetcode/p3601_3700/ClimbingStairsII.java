package leetcode.p3601_3700;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/climbing-stairs-ii/">3693. Climbing Stairs II</a> */
public class ClimbingStairsII {
  private int n;
  private int[] dp;

  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)} for the {@code dp} memo and the
   *     recursion stack. Each of the {@code n} states is solved once and tries at most three jumps
   *     of constant cost, and the call chain along jumps of length 1 is {@code n} frames deep.
   */
  public int climbStairs(int n, int[] costs) {
    this.n = n;
    dp = new int[n + 1];
    Arrays.fill(dp, -1);
    return climbStairs(costs, 0);
  }

  private int climbStairs(int[] costs, int start) {
    if (start > n) {
      return Integer.MAX_VALUE;
    }
    if (start == n) {
      return 0;
    }
    if (dp[start] != -1) {
      return dp[start];
    }
    int res = costs[start] + 1 + climbStairs(costs, start + 1);
    if (start + 1 < n) {
      res = Math.min(res, costs[start + 1] + 4 + climbStairs(costs, start + 2));
    }
    if (start + 2 < n) {
      res = Math.min(res, costs[start + 2] + 9 + climbStairs(costs, start + 3));
    }
    dp[start] = res;
    return res;
  }
}
