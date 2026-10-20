package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/sum-of-sortable-integers/">3886. Sum of Sortable
 * Integers</a>
 */
public class SumOfSortableIntegers {
  /**
   * @implNote Time {@code O(n·d(n))} since each divisor {@code k} of {@code n} gets one pass over
   *     the array, finding every block's min and max and counting its cyclic descents in
   *     {@code O(k)} per block, which dominates the {@code O(n)} scan for divisors; auxiliary space
   *     {@code O(1)}, where {@code n = nums.length} and {@code d(n)} is the number of divisors of
   *     {@code n}, at most 128 under the constraint {@code n <= 10^5}.
   */
  public int sortableIntegers(int[] nums) {
    int n = nums.length;
    int ans = 0;
    for (int k = 1; k <= n; k++) {
      if (n % k != 0) {
        continue;
      }
      if (check(nums, k)) {
        ans += k;
      }
    }
    return ans;
  }

  private boolean check(int[] nums, int k) {
    int n = nums.length;
    int prevMax = 0;
    for (int i = 0; i < n; i += k) {
      int min = Integer.MAX_VALUE;
      int max = Integer.MIN_VALUE;
      for (int j = i; j < i + k; j++) {
        min = Math.min(min, nums[j]);
        max = Math.max(max, nums[j]);
      }
      if (prevMax > min) {
        return false;
      }
      prevMax = max;

      int desc = 0;
      for (int j = i; j < i + k - 1; j++) {
        if (nums[j] > nums[j + 1]) {
          desc++;
        }
      }
      if (nums[i + k - 1] > nums[i]) {
        desc++;
      }
      if (desc > 1) {
        return false;
      }
    }
    return true;
  }
}
