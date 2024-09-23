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
      if (!map.containsKey(c)) {
        maxLen = Math.max(maxLen, i - start + 1);
      } else {
        start = map.get(c) + 1;
        map = new HashMap<>();
        for (int j = start; j <= i; j++) {
          map.put(s.charAt(j), j);
        }
      }
      map.put(c, i);
    }
    return maxLen;
  }
}
