package leetcode.p2301_2400;

/**
 * <a href="https://leetcode.com/problems/minimum-sum-of-squared-difference/">2333. Minimum Sum of
 * Squared Difference</a>
 */
public class MinimumSumOfSquaredDifference {
  /**
   * @implNote Time {@code O(n + M)}: one pass buckets the differences into {@code cnt}, the
   *     top-down sweep lowers up to {@code cnt[v]} differences from {@code v} to {@code v - 1} in
   *     {@code O(1)} per value while spending the {@code k1 + k2} budget, and a final pass sums the
   *     squares. Space {@code O(M)} for {@code cnt}, where {@code n = nums1.length} and {@code M =
   *     10^5} bounds {@code |nums1[i] - nums2[i]|}.
   */
  public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
    int[] cnt = new int[100_001];
    int n = nums1.length;
    for (int i = 0; i < n; i++) {
      cnt[Math.abs(nums1[i] - nums2[i])]++;
    }

    int k = k1 + k2;

    for (int v = 100_000; v > 0 && k > 0; v--) {
      if (cnt[v] == 0) {
        continue;
      }
      int use = Math.min(cnt[v], k);
      cnt[v] -= use;
      cnt[v - 1] += use;
      k -= use;
    }

    long ans = 0L;
    for (int v = 1; v <= 100_000; v++) {
      ans += (long) cnt[v] * v * v;
    }
    return ans;
  }
}
