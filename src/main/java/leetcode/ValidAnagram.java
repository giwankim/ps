package leetcode;

import java.util.HashMap;
import java.util.Map;

public class ValidAnagram {
  public boolean isAnagram(String s, String t) {
    Map<Character, Integer> sMap = new HashMap<>();
    for (char c : s.toCharArray()) {
      sMap.put(c, sMap.getOrDefault(c, 0) + 1);
    }

    Map<Character, Integer> tMap = new HashMap<>();
    for (char c : t.toCharArray()) {
      tMap.put(c, tMap.getOrDefault(c, 0) + 1);
    }

    return sMap.equals(tMap);
  }
}
