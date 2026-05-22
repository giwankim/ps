package com.giwankim.leetcode;

import java.util.Arrays;

public class CoinChange {
  /**
   * @implNote Time {@code O(amount * n)}, auxiliary space {@code O(amount)} for the {@code dp}
   *     array, where {@code n = coins.length}.
   */
  public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;
    for (int i = 1; i < dp.length; i++) {
      for (int coin : coins) {
        if (i - coin < 0 || dp[i - coin] == Integer.MAX_VALUE) {
          continue;
        }
        dp[i] = Math.min(dp[i], dp[i - coin] + 1);
      }
    }
    if (dp[amount] == Integer.MAX_VALUE) {
      return -1;
    }
    return dp[amount];
  }
}
