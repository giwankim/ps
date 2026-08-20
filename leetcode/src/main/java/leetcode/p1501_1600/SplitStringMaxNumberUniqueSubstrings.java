package leetcode.p1501_1600;

import java.util.HashSet;
import java.util.Set;

/**
 * <a
 * href="https://leetcode.com/problems/split-a-string-into-the-max-number-of-unique-substrings/">1593.
 * Split a String Into the Max Number of Unique Substrings</a>
 */
public class SplitStringMaxNumberUniqueSubstrings {
  public int maxUniqueSplit(String s) {
    Set<String> set = new HashSet<>();
    return maxUniqueSplit(s, 0, set);
  }

  private int maxUniqueSplit(String s, int i, Set<String> set) {
    if (i == s.length()) {
      return set.size();
    }
    int result = 0;
    for (int j = i; j < s.length(); j++) {
      String t = s.substring(i, j + 1);
      if (!set.add(t)) {
        continue;
      }
      result = Math.max(result, maxUniqueSplit(s, j + 1, set));
      set.remove(t);
    }
    return result;
  }
}
