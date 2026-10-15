package leetcode.p2901_3000;

import java.util.List;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/subarrays-distinct-element-sum-of-squares-i/">2913.
 * Subarrays Distinct Element Sum of Squares I</a>
 */
public class SubarraysDistinctElementSumOfSquaresI {
  /**
   * @implNote Time {@code O(n^3)}: each of the {@code n(n+1)/2} subarrays is copied into a fresh
   *     set in time proportional to its length, {@code n(n+1)(n+2)/6} element copies in all.
   *     Auxiliary space {@code O(n)} for the one live subarray set, where {@code n = nums.size()}.
   */
  public int sumCounts(List<Integer> nums) {
    int ans = 0;
    int n = nums.size();
    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        int cnt = Set.copyOf(nums.subList(i, j + 1)).size();
        ans += cnt * cnt;
      }
    }
    return ans;
  }
}
