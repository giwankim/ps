package leetcode.p2901_3000;

import java.util.HashMap;
import java.util.Map;

/**
 * <a
 * href="https://leetcode.com/problems/length-of-longest-subarray-with-at-most-k-frequency/">2958.
 * Length of Longest Subarray With at Most K Frequency</a>
 */
public class LengthOfLongestSubarrayWithAtMostKFrequency {
  /**
   * @implNote Time {@code O(n)} since each index enters and leaves the sliding window at most once,
   *     auxiliary space {@code O(n)} for the frequency map, where {@code n = nums.length}.
   */
  public int maxSubarrayLength(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    int result = 0;
    int i = 0;
    for (int j = 0; j < nums.length; j++) {
      freq.merge(nums[j], 1, Integer::sum);
      while (freq.get(nums[j]) > k) {
        freq.merge(nums[i], -1, Integer::sum);
        i++;
      }
      result = Math.max(result, j - i + 1);
    }
    return result;
  }
}
