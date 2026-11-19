package leetcode.p2501_2600;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/minimum-score-by-changing-two-elements/">2567. Minimum
 * Score by Changing Two Elements</a>
 */
public class MinimumScoreByChangingTwoElements {
  /**
   * @implNote Time {@code O(n log n)} for the in-place sort, then {@code O(1)} to compare the three
   *     ways to spend both changes on the ends. Auxiliary space {@code O(log n)} for the dual-pivot
   *     quicksort's recursion stack, where {@code n = nums.length}.
   */
  public int minimizeSum(int[] nums) {
    Arrays.sort(nums);
    int n = nums.length;
    int ans = nums[n - 2] - nums[1];
    ans = Math.min(ans, nums[n - 1] - nums[2]);
    ans = Math.min(ans, nums[n - 3] - nums[0]);
    return ans;
  }
}
