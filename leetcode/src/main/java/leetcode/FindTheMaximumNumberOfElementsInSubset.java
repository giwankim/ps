package leetcode;

import java.util.HashMap;
import java.util.Map;

public class FindTheMaximumNumberOfElementsInSubset {
  /**
   * @implNote Time {@code O(n log log V)}, auxiliary space {@code O(n)} for the {@code freq} map,
   *     where {@code n = nums.length} and {@code V} is the largest value in {@code nums}.
   *     <p>Each distinct base starts a chain that squares its value every step, so the chain passes
   *     {@code V} after {@code O(log log V)} squarings — for the smallest base {@code 2},
   *     {@code 2^(2^k) > V} once {@code k > log2 log2 V} — and larger bases terminate even sooner.
   */
  public int maximumLength(int[] nums) {
    Map<Long, Integer> freq = new HashMap<>();
    for (int num : nums) {
      freq.merge((long) num, 1, Integer::sum);
    }

    int ones = freq.getOrDefault(1L, 0);
    int result = (ones & 1) == 1 ? ones : ones - 1;
    freq.remove(1L);

    for (long x : freq.keySet()) {
      int cnt = 0;
      while (freq.getOrDefault(x, 0) >= 2) {
        cnt += 2;
        x *= x;
      }
      cnt += freq.containsKey(x) ? 1 : -1;
      result = Math.max(result, cnt);
    }
    return result;
  }
}
