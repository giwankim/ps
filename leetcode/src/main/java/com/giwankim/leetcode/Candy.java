package com.giwankim.leetcode;

import java.util.Arrays;

public class Candy {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public int candy(int[] ratings) {
    int[] candies = new int[ratings.length];
    Arrays.fill(candies, 1);
    for (int i = 1; i < ratings.length; i++) {
      if (ratings[i - 1] < ratings[i]) {
        candies[i] = candies[i - 1] + 1;
      }
    }
    for (int i = ratings.length - 1; i > 0; i--) {
      if (ratings[i - 1] > ratings[i]) {
        candies[i - 1] = Math.max(candies[i - 1], candies[i] + 1);
      }
    }
    int result = 0;
    for (int candy : candies) {
      result += candy;
    }
    return result;
  }
}
