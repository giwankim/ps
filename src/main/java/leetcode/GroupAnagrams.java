package leetcode;

import java.util.*;

public class GroupAnagrams {
  public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String str : strs) {
      char[] chars = str.toCharArray();
      Arrays.sort(chars);
      String key = String.valueOf(chars);

      groups.computeIfAbsent(key, k -> new ArrayList<>());

      groups.get(key).add(str);
    }
    return new ArrayList<>(groups.values());
  }
}
