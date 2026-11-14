package leetcode.p3301_3400;

import java.util.List;

/**
 * <a href="https://leetcode.com/problems/minimum-positive-sum-subarray/">3364. Minimum Positive Sum
 * Subarray</a>
 */
public class MinimumPositiveSumSubarray {
  /**
   * @implNote Time {@code O(n (r - l + 1))} from one prefix-sum lookup per window of each allowed
   *     size, space {@code O(n)} for the prefix sums, where {@code n = nums.size()}.
   */
  public int minimumSumSubarray(List<Integer> nums, int l, int r) {
    int n = nums.size();
    int[] psum = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      psum[i] = psum[i - 1] + nums.get(i - 1);
    }

    int ans = Integer.MAX_VALUE;
    for (int size = l; size <= r; size++) {
      for (int start = 0; start + size <= n; start++) {
        int sum = psum[start + size] - psum[start];
        if (sum > 0) {
          ans = Math.min(ans, sum);
        }
      }
    }
    return ans == Integer.MAX_VALUE ? -1 : ans;
  }
}
