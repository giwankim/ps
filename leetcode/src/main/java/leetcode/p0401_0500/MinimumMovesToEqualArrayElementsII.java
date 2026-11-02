package leetcode.p0401_0500;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/">462. Minimum
 * Moves to Equal Array Elements II</a>
 */
public class MinimumMovesToEqualArrayElementsII {
  /**
   * @implNote Time {@code O(n log n)}, auxiliary space {@code O(n)} worst case for the merge buffer
   *     {@code Arrays.sort} may allocate, where {@code n = nums.length}.
   */
  public int minMoves2(int[] nums) {
    Arrays.sort(nums);
    int ans = 0;
    int target = nums[nums.length / 2];
    for (int num : nums) {
      ans += Math.abs(num - target);
    }
    return ans;
  }
}
