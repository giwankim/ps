package leetcode.p1601_1700;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/">1658. Minimum
 * Operations to Reduce X to Zero</a>
 */
public class MinimumOperationsToReduceXToZero {
  /**
   * @implNote Time {@code O(n)} expected, space {@code O(n)}, where {@code n = nums.length}: one
   *     pass fills {@code psum} with every prefix sum mapped to its length (distinct, since
   *     {@code nums} is positive), and one backward pass pairs each suffix with its complementary
   *     prefix in an {@code O(1)} expected lookup.
   */
  public int minOperations(int[] nums, int x) {
    int n = nums.length;
    Map<Integer, Integer> psum = new HashMap<>();
    int sum = 0;
    psum.put(0, sum);
    for (int i = 0; i < n; i++) {
      sum += nums[i];
      psum.put(sum, i + 1);
    }

    int result = Integer.MAX_VALUE;
    if (psum.containsKey(x)) {
      result = psum.get(x);
    }

    sum = 0;
    for (int i = n - 1; i >= 0; i--) {
      sum += nums[i];
      if (psum.containsKey(x - sum) && psum.get(x - sum) + (n - i) <= n) {
        int cnt = psum.get(x - sum) + (n - i);
        result = Math.min(result, cnt);
      }
    }
    return result == Integer.MAX_VALUE ? -1 : result;
  }
}
