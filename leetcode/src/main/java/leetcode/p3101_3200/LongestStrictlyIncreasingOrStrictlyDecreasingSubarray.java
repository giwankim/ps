package leetcode.p3101_3200;

/**
 * <a
 * href="https://leetcode.com/problems/longest-strictly-increasing-or-strictly-decreasing-subarray/">3105.
 * Longest Strictly Increasing or Strictly Decreasing Subarray</a>
 */
public class LongestStrictlyIncreasingOrStrictlyDecreasingSubarray {
  /**
   * @implNote Time {@code O(n)} from two linear passes, one per direction, auxiliary space
   *     {@code O(1)}, where {@code n = nums.length}.
   */
  public int longestMonotonicSubarray(int[] nums) {
    int n = nums.length;
    int incr = 1;
    int curr = 1;
    for (int i = 1; i < n; i++) {
      if (nums[i] > nums[i - 1]) {
        curr++;
        incr = Math.max(incr, curr);
      } else {
        curr = 1;
      }
    }
    int decr = 1;
    curr = 1;
    for (int i = 1; i < n; i++) {
      if (nums[i] < nums[i - 1]) {
        curr++;
        decr = Math.max(decr, curr);
      } else {
        curr = 1;
      }
    }
    return Math.max(incr, decr);
  }
}
