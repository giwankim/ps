package leetcode;

public class MaximumProductOfTwoElementsInAnArray {
  /**
   * @implNote Time {@code O(n)} — a single pass tracks the two largest values, demoting the
   *     incumbent maximum when a larger one arrives so that duplicates occupy both slots. The
   *     constraint {@code 1 <= nums[i]} leaves every factor {@code nums[i] - 1} non-negative, so
   *     that pair is the only candidate and the answer follows in {@code O(1)}. Auxiliary space
   *     {@code O(1)} for the two running maxima, and {@code nums} is left unmodified, where
   *     {@code n = nums.length}.
   */
  public int maxProduct(int[] nums) {
    int max1 = Integer.MIN_VALUE;
    int max2 = Integer.MIN_VALUE;
    for (int num : nums) {
      if (num > max1) {
        max2 = max1;
        max1 = num;
      } else if (num > max2) {
        max2 = num;
      }
    }
    return (max1 - 1) * (max2 - 1);
  }
}
