package in.the.wild.apple;

import java.util.*;

public class LongestUniqueSubstringSet {
  public List<String> longestUniqueSubstringSet(String s) {
    Set<Character> set = new HashSet<>();
    List<String> result = new ArrayList<>();
    longestUniqueSubstringSet(s, 0, set, new ArrayList<>(), result);
    return result;
  }

  private void longestUniqueSubstringSet(String s, int i, Set<Character> set, List<String> current, List<String> result) {
    if (i == s.length()) {
      if (current.size() > result.size()) {
        result.clear();
        result.addAll(current);
        return;
      }
    }
    for (int j = i; j < s.length(); j++) {
      String t = s.substring(i, j + 1);
      if (containsAny(set, t)) {
        break;
      }
      current.add(t);
      for (char c : t.toCharArray()) {
        set.add(c);
      }
      longestUniqueSubstringSet(s, j + 1, set, current, result);
      current.removeLast();
      for (char c : t.toCharArray()) {
        set.remove(c);
      }
    }
  }

  private boolean containsAny(Set<Character> set, String t) {
    for (char c : t.toCharArray()) {
      if (set.contains(c)) {
        return true;
      }
    }
    return false;
  }
}
