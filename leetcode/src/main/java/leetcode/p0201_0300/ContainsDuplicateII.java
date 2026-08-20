package leetcode.p0201_0300;

import java.util.HashSet;
import java.util.Set;

/** <a href="https://leetcode.com/problems/contains-duplicate-ii/">219. Contains Duplicate II</a> */
public class ContainsDuplicateII {
  /** @implNote Time {@code O(n)}, space {@code O(k)}. */
  public boolean containsNearbyDuplicate(int[] nums, int k) {
    Set<Integer> set = new HashSet<>();
    for (int i = 0; i < nums.length; i++) {
      if (set.contains(nums[i])) {
        return true;
      }
      set.add(nums[i]);
      if (set.size() > k) {
        set.remove(nums[i - k]);
      }
    }
    return false;
  }
}
