package leetcode;

import java.util.Set;
import java.util.stream.Collectors;

public class LengthOfLongestSubstring {
  public int lengthOfLongestSubstring(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    int maxLen = 0;
    for (int i = 0; i < s.length(); i++) {
      for (int j = i + 1; j <= s.length(); j++) {
        String substring = s.substring(i, j);
        if (isValid(substring)) {
          maxLen = Math.max(maxLen, j - i);
        }
      }
    }
    return maxLen;
  }

  private boolean isValid(String s) {
    Set<Character> set = s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    return set.size() == s.length();
  }
}
