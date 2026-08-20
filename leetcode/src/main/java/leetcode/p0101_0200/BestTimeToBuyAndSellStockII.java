package leetcode.p0101_0200;

/**
 * <a href="https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/">122. Best Time to Buy
 * and Sell Stock II</a>
 */
public class BestTimeToBuyAndSellStockII {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public int maxProfit(int[] prices) {
    int result = 0;
    for (int i = 1; i < prices.length; i++) {
      if (prices[i] - prices[i - 1] > 0) {
        result += prices[i] - prices[i - 1];
      }
    }
    return result;
  }
}
