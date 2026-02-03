package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BestTimeToBuyAndSellStockTest {
  BestTimeToBuyAndSellStock sut = new BestTimeToBuyAndSellStock();

  @Test
  void maxProfit() {
    int[] prices = {7, 1, 5, 3, 6, 4};
    int profit = sut.maxProfit(prices);
    assertThat(profit).isEqualTo(5);
  }

  @Test
  void zeroProfit() {
    int[] prices = {7, 6, 4, 3, 1};
    int profit = sut.maxProfit(prices);
    assertThat(profit).isZero();
  }
}
