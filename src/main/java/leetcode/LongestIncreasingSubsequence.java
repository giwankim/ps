package leetcode;

import java.util.Arrays;

public class LongestIncreasingSubsequence {
  public int lengthOfLIS(int[] nums) {
    int result = 0;

    int[] a = new int[nums.length];
    Arrays.fill(a, 1);

    for (int i = 0; i < nums.length; i++) {
      for (int j = 0; j < i; j++) {
        if (nums[j] < nums[i]) {
          a[i] = Math.max(a[i], 1 + a[j]);
        }
      }
      result = Math.max(result, a[i]);
    }
    return result;
  }
}
