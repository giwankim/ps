package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BestTimeToBuyAndSellStockIITest {
  BestTimeToBuyAndSellStockII sut = new BestTimeToBuyAndSellStockII();

  @Test
  void noProfit() {
    int[] prices = {7, 6, 4, 3, 1};
    int profit = sut.maxProfit(prices);
    assertThat(profit).isZero();
  }

  @Test
  void buyAndSellOnce() {
    int[] prices = {1, 2, 3, 4, 5};
    int profit = sut.maxProfit(prices);
    assertThat(profit).isEqualTo(4);
  }

  @Test
  void buyAndSellMultipleTimes() {
    int[] prices = {7, 1, 5, 3, 6, 4};
    int profit = sut.maxProfit(prices);
    assertThat(profit).isEqualTo(7);
  }
}
