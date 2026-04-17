package com.giwankim.leetcode;

public class ProductOfArrayExceptSelf {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int[] productExceptSelf(int[] nums) {
    int[] products = new int[nums.length];
    int prefix = 1;
    for (int i = 0; i < nums.length; i++) {
      products[i] = prefix;
      prefix *= nums[i];
    }
    int suffix = 1;
    for (int i = nums.length - 1; i >= 0; i--) {
      products[i] *= suffix;
      suffix *= nums[i];
    }
    return products;
  }
}
