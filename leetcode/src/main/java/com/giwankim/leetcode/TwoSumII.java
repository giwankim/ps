package com.giwankim.leetcode;

public class TwoSumII {
  public int[] twoSum(int[] numbers, int target) {
    // Time complexity: O(n), Space complexity: O(1)
    int i = 0;
    int j = numbers.length - 1;
    while (i < j) {
      int diff = numbers[i] + numbers[j] - target;
      if (diff == 0) {
        return new int[] {i + 1, j + 1};
      } else if (diff < 0) {
        i++;
      } else {
        j--;
      }
    }
    return new int[0];
  }
}
