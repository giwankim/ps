package com.giwankim.leetcode;

public class ContainerWithMostWater {
  public int maxArea(int[] height) {
    int result = 0;

    int left = 0;
    int right = height.length - 1;

    while (left < right) {
      int width = right - left;
      if (height[left] < height[right]) {
        result = Math.max(result, width * height[left]);
        left += 1;
      } else {
        result = Math.max(result, width * height[right]);
        right -= 1;
      }
    }

    return result;
  }
}
