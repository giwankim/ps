package leetcode.p0201_0300;

import java.util.HashSet;
import java.util.Set;

/** <a href="https://leetcode.com/problems/contains-duplicate/">217. Contains Duplicate</a> */
public class ContainsDuplicate {
  public boolean containsDuplicate(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int x : nums) {
      if (set.contains(x)) {
        return true;
      }
      set.add(x);
    }
    return false;
  }
}
