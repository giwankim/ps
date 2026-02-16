package com.giwankim.leetcode;

public class TrappingRainWater {
  public int trap(int[] height) {
    // Time complexity: O(n), Space complexity: O(1)
    int result = 0;
    int left = 0;
    int right = height.length - 1;
    int maxLeft = 0;
    int maxRight = 0;
    while (left < right) {
      if (height[left] < height[right]) {
        maxLeft = Math.max(maxLeft, height[left]);
        result += maxLeft - height[left];
        left++;
      } else {
        maxRight = Math.max(maxRight, height[right]);
        result += maxRight - height[right];
        right--;
      }
    }
    return result;
  }
}
