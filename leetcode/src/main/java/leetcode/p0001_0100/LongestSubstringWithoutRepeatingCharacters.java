package leetcode.p0001_0100;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/longest-substring-without-repeating-characters/">3.
 * Longest Substring Without Repeating Characters</a>
 */
public class LongestSubstringWithoutRepeatingCharacters {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public int lengthOfLongestSubstring(String s) {
    int result = 0;
    int i = 0;
    Set<Character> window = new HashSet<>();
    for (int j = 0; j < s.length(); j++) {
      while (window.contains(s.charAt(j))) {
        window.remove(s.charAt(i));
        i++;
      }
      result = Math.max(result, j - i + 1);
      window.add(s.charAt(j));
    }
    return result;
  }
}
