package leetcode.p3701_3800;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/smallest-missing-multiple-of-k/">3718. Smallest Missing
 * Multiple of K</a>
 */
public class SmallestMissingMultipleOfK {
  /**
   * @implNote Time {@code O(n)} expected — one pass hashes every value, then the probe walks
   *     multiples of {@code k} until one is absent, which takes at most {@code n + 1} steps because
   *     the set holds at most {@code n} of them — auxiliary space {@code O(n)} for the set, where
   *     {@code n = nums.length}.
   */
  public int missingMultiple(int[] nums, int k) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) {
      set.add(num);
    }
    int result = k;
    while (set.contains(result)) {
      result += k;
    }
    return result;
  }
}
