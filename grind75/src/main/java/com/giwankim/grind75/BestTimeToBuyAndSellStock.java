package com.giwankim.grind75;

public class BestTimeToBuyAndSellStock {

  public int maxProfit(int[] prices) {
    int result = 0;
    int minPrice = Integer.MAX_VALUE;
    for (int price : prices) {
      result = Math.max(result, price - minPrice);
      minPrice = Math.min(price, minPrice);
    }
    return result;
  }
}
