package com.giwankim.leetcode;

public class MaxProfit {
  public int maxProfit(int[] prices) {
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;
    for (int price : prices) {
      maxProfit = Math.max(maxProfit, price - minPrice);
      minPrice = Math.min(price, minPrice);
    }
    return maxProfit;
  }
}
