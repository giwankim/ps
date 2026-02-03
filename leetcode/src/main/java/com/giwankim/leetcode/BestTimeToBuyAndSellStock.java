package com.giwankim.leetcode;

public class BestTimeToBuyAndSellStock {
  public int maxProfit(int[] prices) {
    // Time complexity: O(n), Space complexity: O(1)
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
