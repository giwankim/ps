package com.giwankim.leetcode;

public class ContainerWithMostWater {
  public int maxArea(int[] height) {
    // Time complexity: O(n), Space complexity: O(1)
    int result = 0;
    int lo = 0;
    int hi = height.length - 1;
    while (lo < hi) {
      int bottom = hi - lo;
      int side = Math.min(height[lo], height[hi]);
      result = Math.max(result, bottom * side);
      if (height[lo] < height[hi]) {
        lo += 1;
      } else {
        hi -= 1;
      }
    }
    return result;
  }
}
