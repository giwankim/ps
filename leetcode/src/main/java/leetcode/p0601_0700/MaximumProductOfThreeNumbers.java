package leetcode.p0601_0700;

public class MaximumProductOfThreeNumbers {
  /**
   * @implNote Time {@code O(n)} — a single pass tracks the three largest and two smallest values,
   *     leaving the only two triples that can win, the three largest and the two most negative
   *     paired with the largest, to be formed in {@code O(1)}. Auxiliary space {@code O(1)} for the
   *     five running extremes, and {@code nums} is left unmodified, where {@code n = nums.length}.
   */
  public int maximumProduct(int[] nums) {
    // in sorted array [min1, min2, max3, max2, max1]
    int max1 = Integer.MIN_VALUE;
    int max2 = Integer.MIN_VALUE;
    int max3 = Integer.MIN_VALUE;
    int min1 = Integer.MAX_VALUE;
    int min2 = Integer.MAX_VALUE;
    for (int num : nums) {
      if (num > max1) {
        max3 = max2;
        max2 = max1;
        max1 = num;
      } else if (num > max2) {
        max3 = max2;
        max2 = num;
      } else if (num > max3) {
        max3 = num;
      }
      if (num < min1) {
        min2 = min1;
        min1 = num;
      } else if (num < min2) {
        min2 = num;
      }
    }
    return Math.max(max1 * max2 * max3, min1 * min2 * max1);
  }
}
