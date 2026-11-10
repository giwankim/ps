package leetcode.p3601_3700;

/** <a href="https://leetcode.com/problems/climbing-stairs-ii/">3693. Climbing Stairs II</a> */
public class ClimbingStairsII {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)} for the {@code dp} table. Each step
   *     is filled once from at most three predecessors, and the iterative order keeps the call
   *     stack constant.
   */
  public int climbStairs(int n, int[] costs) {
    int[] dp = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      dp[i] = dp[i - 1] + costs[i - 1] + 1;
      if (i >= 2) {
        dp[i] = Math.min(dp[i], dp[i - 2] + costs[i - 1] + 4);
      }
      if (i >= 3) {
        dp[i] = Math.min(dp[i], dp[i - 3] + costs[i - 1] + 9);
      }
    }
    return dp[n];
  }
}
