package leetcode.p2901_3000;

import java.util.HashSet;
import java.util.Set;

public class SmallestMissingIntegerGreaterThanSequentialPrefixSum {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(n)} for the {@code seen} set, where
   *     {@code n = nums.length}. The upward search is unbounded in form only: every step it takes
   *     is paid for by a distinct element of {@code nums} that it walks past, so it runs at most
   *     {@code n + 1} times.
   */
  public int missingInteger(int[] nums) {
    int sum = nums[0];
    for (int i = 1; i < nums.length; i++) {
      if (nums[i] != nums[i - 1] + 1) {
        break;
      }
      sum += nums[i];
    }

    Set<Integer> seen = new HashSet<>();
    for (int x : nums) {
      seen.add(x);
    }

    int result = sum;
    while (seen.contains(result)) {
      result++;
    }
    return result;
  }
}
