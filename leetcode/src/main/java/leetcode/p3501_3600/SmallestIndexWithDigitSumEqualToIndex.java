package leetcode.p3501_3600;

/**
 * <a href="https://leetcode.com/problems/smallest-index-with-digit-sum-equal-to-index/">3550.
 * Smallest Index With Digit Sum Equal to Index</a>
 */
public class SmallestIndexWithDigitSumEqualToIndex {
  /**
   * @implNote Time {@code O(n log M)} since each index peels its value's {@code floor(log10(M)) +
   *     1} decimal digits at most once before the early return — the constraint {@code nums[i] <=
   *     1000} caps that at 4 digits, so this is effectively {@code O(n)} — auxiliary space
   *     {@code O(1)}, where {@code n = nums.length} and {@code M = max(nums)}.
   */
  public int smallestIndex(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      int num = nums[i];
      int sum = 0;
      while (num != 0) {
        sum += num % 10;
        num /= 10;
      }
      if (sum == i) {
        return i;
      }
    }
    return -1;
  }
}
