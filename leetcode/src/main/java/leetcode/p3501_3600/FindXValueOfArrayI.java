package leetcode.p3501_3600;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/find-x-value-of-array-i/">3524. Find X Value of Array
 * I</a>
 */
public class FindXValueOfArrayI {
  private int n;
  private int k;
  private int[] nums;
  private long[][] dp;

  /**
   * @implNote Time {@code O(n * k^2)}, space {@code O(n * k)}, where {@code n = nums.length}: the
   *     memo behind {@link #count(int, int)} holds {@code n * k} states, each scanning all
   *     {@code k} remainders of row {@code i + 1} because multiplication mod {@code k} has no
   *     inverse to solve for the source remainder directly. Filling from row {@code n - 1} down
   *     means row {@code i + 1} is already complete, so the recursion never goes deeper than one
   *     memo hit.
   */
  public long[] resultArray(int[] nums, int k) {
    this.n = nums.length;
    this.k = k;
    this.nums = nums;

    dp = new long[n][k];
    for (var row : dp) {
      Arrays.fill(row, -1L);
    }

    long[] result = new long[k];
    for (int i = n - 1; i >= 0; i--) {
      for (int x = 0; x < k; x++) {
        result[x] += count(i, x);
      }
    }
    return result;
  }

  private long count(int i, int r) {
    if (i == n) {
      return 0;
    }
    if (dp[i][r] != -1) {
      return dp[i][r];
    }
    long result = 0;
    if (nums[i] % k == r) {
      result += 1;
    }
    for (int s = 0; s < k; s++) {
      if (((nums[i] % k) * s) % k == r) {
        result += count(i + 1, s);
      }
    }
    dp[i][r] = result;
    return result;
  }
}
