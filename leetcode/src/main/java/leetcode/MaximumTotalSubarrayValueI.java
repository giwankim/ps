package leetcode;

public class MaximumTotalSubarrayValueI {
  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length} */
  public long maxTotalValue(int[] nums, int k) {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    for (int num : nums) {
      max = Math.max(max, num);
      min = Math.min(min, num);
    }
    return (long) (max - min) * k;
  }
}
