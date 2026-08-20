package leetcode.p0201_0300;

/**
 * <a href="https://leetcode.com/problems/minimum-size-subarray-sum/">209. Minimum Size Subarray
 * Sum</a>
 */
public class MinimumSizeSubarraySum {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public int minSubArrayLen(int target, int[] nums) {
    int result = Integer.MAX_VALUE;
    int currSum = 0;
    int j = 0;
    for (int i = 0; i < nums.length; i++) {
      currSum += nums[i];
      while (currSum >= target) {
        result = Math.min(result, i - j + 1);
        currSum -= nums[j];
        j++;
      }
    }
    return result == Integer.MAX_VALUE ? 0 : result;
  }
}
