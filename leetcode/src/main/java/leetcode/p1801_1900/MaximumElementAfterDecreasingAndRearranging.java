package leetcode.p1801_1900;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/">1846.
 * Maximum Element After Decreasing and Rearranging</a>
 */
public class MaximumElementAfterDecreasingAndRearranging {
  public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
    Arrays.sort(arr);
    arr[0] = 1;
    int result = 1;
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] - arr[i - 1] > 1) {
        arr[i] = arr[i - 1] + 1;
      }
      result = Math.max(result, arr[i]);
    }
    return result;
  }
}
