package leetcode;

import java.util.Arrays;

public class StoneGameIII {
  private int[] dp;
  private int[] psum;

  /**
   * @implNote Time {@code O(n)} since the {@code psum} build is linear and the memo behind
   *     {@code score} then settles each of the {@code n} suffixes once, trying at most three moves
   *     at {@code O(1)} each — auxiliary space {@code O(n)} for {@code dp} and {@code psum},
   *     matched by a recursion depth of up to {@code n} frames, where {@code n =
   *     stoneValue.length}.
   */
  public String stoneGameIII(int[] stoneValue) {
    int n = stoneValue.length;
    psum = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      psum[i] = psum[i - 1] + stoneValue[i - 1];
    }
    dp = new int[n];
    Arrays.fill(dp, Integer.MIN_VALUE);
    int alice = score(stoneValue, 0);
    int bob = psum[n] - alice;
    if (alice > bob) {
      return "Alice";
    } else if (alice < bob) {
      return "Bob";
    }
    return "Tie";
  }

  private int score(int[] stoneValue, int start) {
    if (start == stoneValue.length) {
      return 0;
    }
    if (dp[start] != Integer.MIN_VALUE) {
      return dp[start];
    }
    int total = psum[stoneValue.length] - psum[start];
    int result = Integer.MIN_VALUE;
    for (int i = 1; i <= 3; i++) {
      if (start + i > stoneValue.length) {
        break;
      }
      result = Math.max(result, total - score(stoneValue, start + i));
    }
    dp[start] = result;
    return result;
  }
}
