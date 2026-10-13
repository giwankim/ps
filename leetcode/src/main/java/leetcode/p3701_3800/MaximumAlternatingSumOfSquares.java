package leetcode.p3701_3800;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/maximum-alternating-sum-of-squares/">3727. Maximum
 * Alternating Sum of Squares</a>
 */
public class MaximumAlternatingSumOfSquares {
  /**
   * @implNote Time {@code O(n log n)} from sorting the squares, auxiliary space {@code O(log n)}
   *     for the sort's recursion stack since squaring and sorting reuse {@code nums} in place,
   *     where {@code n = nums.length}.
   */
  public long maxAlternatingSum(int[] nums) {
    int n = nums.length;
    for (int i = 0; i < n; i++) {
      nums[i] *= nums[i];
    }
    Arrays.sort(nums);
    long ans = 0L;
    for (int i = n - 1; i >= n / 2; i--) {
      ans += nums[i];
    }
    for (int i = n / 2 - 1; i >= 0; i--) {
      ans -= nums[i];
    }
    return ans;
  }
}
