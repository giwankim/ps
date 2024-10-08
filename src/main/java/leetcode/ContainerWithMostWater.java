package leetcode;

public class ContainerWithMostWater {
  public int maxArea(int[] height) {
    int maxArea = 0;
    int left = 0;
    int right = height.length - 1;
    while (left < right) {
      int area = Math.min(height[left], height[right]) * (right - left);
      maxArea = Math.max(maxArea, area);
      if (height[left] < height[right]) {
        left += 1;
      } else {
        right -= 1;
      }
    }
    return maxArea;
  }
}
