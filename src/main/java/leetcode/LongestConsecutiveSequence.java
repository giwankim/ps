package leetcode;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
  public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) {
      set.add(num);
    }
    int result = 0;
    for (int x : nums) {
      if (set.contains(x - 1)) {
        continue;
      }
      int y = x;
      while (set.contains(y + 1)) {
        y += 1;
      }
      result = Math.max(result, y - x + 1);
    }
    return result;
  }
}
