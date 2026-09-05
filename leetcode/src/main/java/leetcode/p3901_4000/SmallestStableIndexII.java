package leetcode.p3901_4000;

/**
 * <a href="https://leetcode.com/problems/smallest-stable-index-ii/">3904. Smallest Stable Index
 * II</a>
 */
public class SmallestStableIndexII {
  /**
   * Index {@code i} is stable when the largest value in {@code nums[0..i]} exceeds the smallest
   * value in {@code nums[i..n-1]} by at most {@code k}. Rescanning both windows for every index is
   * quadratic, which the 10^5 length ceiling rules out, so a backward pass records each suffix
   * minimum and the forward pass folds each element into a running maximum, returning at the first
   * index whose gap is within {@code k}. Only the suffix side needs an array.
   *
   * @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = nums.length}.
   */
  public int firstStableIndex(int[] nums, int k) {
    int n = nums.length;
    int[] mins = new int[n];
    mins[n - 1] = nums[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      mins[i] = Math.min(mins[i + 1], nums[i]);
    }
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < n; i++) {
      max = Math.max(max, nums[i]);
      if (max - mins[i] <= k) {
        return i;
      }
    }
    return -1;
  }
}
