package com.giwankim.leetcode;

import java.util.Arrays;

public class MaximumIceCreamBars {
  /**
   * @implNote Counting sort over the cost range. Time {@code O(n + C)}, auxiliary space
   *     {@code O(C)} for the {@code counts} array, where {@code n = costs.length} and {@code C} is
   *     the largest cost. Since {@code C <= 10^5} is bounded, this runs in effectively linear time.
   */
  public int maxIceCream(int[] costs, int coins) {
    int maxCost = Arrays.stream(costs).max().orElse(0);

    int[] counts = new int[maxCost + 1];
    for (int cost : costs) {
      counts[cost]++;
    }

    int result = 0;
    for (int cost = 1; cost <= maxCost; cost++) {
      if (cost > coins) {
        break;
      }
      int buyable = Math.min(counts[cost], coins / cost);
      coins -= buyable * cost;
      result += buyable;
    }
    return result;
  }
}
