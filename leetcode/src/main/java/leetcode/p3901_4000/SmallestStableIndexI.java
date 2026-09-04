package leetcode.p3901_4000;

/**
 * <a href="https://leetcode.com/problems/smallest-stable-index-i/">3903. Smallest Stable Index
 * I</a>
 */
public class SmallestStableIndexI {
  /**
   * Index {@code i} is stable when the largest value in {@code nums[0..i]} exceeds the smallest
   * value in {@code nums[i..n-1]} by at most {@code k}. A forward pass records each prefix maximum
   * and a backward pass each suffix minimum, so every index is then checked in constant time.
   *
   * @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = nums.length}.
   */
  public int firstStableIndex(int[] nums, int k) {
    int n = nums.length;
    int[] maxes = new int[n];
    maxes[0] = nums[0];
    for (int i = 1; i < n; i++) {
      maxes[i] = Math.max(maxes[i - 1], nums[i]);
    }
    int[] mins = new int[n];
    mins[n - 1] = nums[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      mins[i] = Math.min(mins[i + 1], nums[i]);
    }
    for (int i = 0; i < n; i++) {
      if (maxes[i] - mins[i] <= k) {
        return i;
      }
    }
    return -1;
  }
}
