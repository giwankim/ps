package leetcode;

import java.util.HashMap;
import java.util.Map;

public class LengthOfLongestSubstring {
  public int lengthOfLongestSubstring(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    int maxLen = 1;
    Map<Character, Integer> map = new HashMap<>();
    map.put(s.charAt(0), 0);
    int start = 0;
    for (int i = 1; i < s.length(); i++) {
      char c = s.charAt(i);
      if (map.containsKey(c)) {
        start = Math.max(start, map.get(c) + 1);
      }
      maxLen = Math.max(maxLen, i - start + 1);
      map.put(c, i);
    }
    return maxLen;
  }
}
