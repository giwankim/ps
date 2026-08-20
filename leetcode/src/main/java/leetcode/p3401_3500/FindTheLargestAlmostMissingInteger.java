package leetcode.p3401_3500;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/find-the-largest-almost-missing-integer/">3471. Find the
 * Largest Almost Missing Integer</a>
 */
public class FindTheLargestAlmostMissingInteger {
  /**
   * @implNote Time {@code O(n + V)}, auxiliary space {@code O(V)} for {@code cnt} (which the
   *     {@code k == n} branch returns before allocating), where {@code n = nums.length} and
   *     {@code V = 51} bounds the value range {@code 0..50}. When {@code 1 < k < n} every interior
   *     index is covered by at least two windows, so only {@code nums[0]} and {@code nums[n - 1]}
   *     can qualify, and each only if its value occurs nowhere else — hence the two candidates are
   *     tested larger first. That covering argument breaks down at both ends of the {@code k}
   *     range: {@code k == n} leaves a single window holding everything, and {@code k == 1} makes
   *     every index its own window, so the answer is the largest value with frequency one.
   */
  public int largestInteger(int[] nums, int k) {
    int n = nums.length;
    if (k == n) {
      return Arrays.stream(nums).max().orElse(-1);
    }

    int[] cnt = new int[51];
    for (int x : nums) {
      cnt[x]++;
    }

    if (k == 1) {
      for (int i = 50; i >= 0; i--) {
        if (cnt[i] == 1) {
          return i;
        }
      }
      return -1;
    }

    for (int x : new int[] {Math.max(nums[0], nums[n - 1]), Math.min(nums[0], nums[n - 1])}) {
      if (cnt[x] == 1) {
        return x;
      }
    }
    return -1;
  }
}
