package leetcode;

import java.util.Arrays;

public class CoinChange {
  public int coinChange(int[] coins, int amount) {
    int[] a = new int[amount + 1];
    Arrays.fill(a, Integer.MAX_VALUE);
    a[0] = 0;
    for (int i = 1; i <= amount; i++) {
      for (int coin : coins) {
        if (i - coin >= 0 && a[i - coin] != Integer.MAX_VALUE) {
          a[i] = Math.min(a[i], a[i - coin] + 1);
        }
      }
    }
    return a[amount] == Integer.MAX_VALUE ? -1 : a[amount];
  }
}
