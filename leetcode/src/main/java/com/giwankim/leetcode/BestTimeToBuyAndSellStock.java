package com.giwankim.leetcode;

public class BestTimeToBuyAndSellStock {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int maxProfit(int[] prices) {
    int result = 0;
    int minPrice = prices[0];
    for (int price : prices) {
      if (price > minPrice) {
        result = Math.max(result, price - minPrice);
      }
      minPrice = Math.min(price, minPrice);
    }
    return result;
  }
}
