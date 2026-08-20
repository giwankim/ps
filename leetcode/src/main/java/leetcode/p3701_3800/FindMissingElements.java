package leetcode.p3701_3800;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/find-missing-elements/">3731. Find Missing Elements</a>
 */
public class FindMissingElements {
  /**
   * @implNote Time {@code O(n + r)} expected — one pass hashes every value while tracking the
   *     extremes, then a scan over the interior of the span emits each integer the set lacks —
   *     auxiliary space {@code O(n)} for that set, beyond the {@code O(k)} result, where {@code n =
   *     nums.length}, {@code r = max - min} is the span the input covers, and {@code k} is the
   *     number of missing integers.
   */
  public List<Integer> findMissingElements(int[] nums) {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    Set<Integer> seen = new HashSet<>();
    for (int num : nums) {
      max = Math.max(max, num);
      min = Math.min(min, num);
      seen.add(num);
    }
    List<Integer> ans = new ArrayList<>();
    for (int i = min + 1; i <= max - 1; i++) {
      if (!seen.contains(i)) {
        ans.add(i);
      }
    }
    return ans;
  }
}
