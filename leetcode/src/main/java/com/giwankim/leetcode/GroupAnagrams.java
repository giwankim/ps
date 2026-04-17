package com.giwankim.leetcode;

import java.util.*;

public class GroupAnagrams {
  /**
   * @implNote Time {@code O(n log m)}, space {@code O(nm)}.
   */
  public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();
    for (String str : strs) {
      char[] chars = str.toCharArray();
      Arrays.sort(chars);
      String key = String.valueOf(chars);
      map.computeIfAbsent(key, _ -> new ArrayList<>()).add(str);
    }
    return new ArrayList<>(map.values());
  }
}
