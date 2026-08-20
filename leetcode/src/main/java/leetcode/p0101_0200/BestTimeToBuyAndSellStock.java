package leetcode.p0101_0200;

/**
 * <a href="https://leetcode.com/problems/best-time-to-buy-and-sell-stock/">121. Best Time to Buy
 * and Sell Stock</a>
 */
public class BestTimeToBuyAndSellStock {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
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
