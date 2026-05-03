package com.giwankim.leetcode;

public class MaximumSubarray {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)},
   *     where {@code n = nums.length}.
   *     Recurrence: {@code T(n) = T(n - 1) + Θ(1) = Θ(n)} (telescoping).
   */
  public int maxSubArray(int[] nums) {
    int result = nums[0];
    int bestEndingAt = nums[0];
    for (int i = 1; i < nums.length; i++) {
      bestEndingAt = Math.max(nums[i], bestEndingAt + nums[i]);
      result = Math.max(result, bestEndingAt);
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n log n)} via divide-and-conquer with a linear cross-boundary scan;
   *     recursion depth {@code O(log n)}, where {@code n = nums.length}.
   *     Recurrence: {@code T(n) = 2·T(n/2) + Θ(n) = Θ(n log n)} by the Master Theorem (case 2,
   *     since {@code f(n) = Θ(n) = Θ(n^(log_2 2))}).
   */
  public int maxSubArray2(int[] nums) {
    return maxSubArray2(nums, 0, nums.length - 1);
  }

  private int maxSubArray2(int[] nums, int lo, int hi) {
    if (lo == hi) {
      return nums[lo];
    }
    int mid = lo + (hi - lo) / 2;
    int leftBest = maxSubArray2(nums, lo, mid);
    int rightBest = maxSubArray2(nums, mid + 1, hi);
    int crossBest = maxCrossing(nums, lo, mid, hi);
    return Math.max(leftBest, Math.max(rightBest, crossBest));
  }

  private int maxCrossing(int[] nums, int lo, int mid, int hi) {
    int leftBest = Integer.MIN_VALUE;
    int sum = 0;
    for (int i = mid; i >= lo; i--) {
      sum += nums[i];
      leftBest = Math.max(leftBest, sum);
    }
    sum = 0;
    int rightBest = Integer.MIN_VALUE;
    for (int i = mid + 1; i <= hi; i++) {
      sum += nums[i];
      rightBest = Math.max(rightBest, sum);
    }
    return leftBest + rightBest;
  }
}
