package in.the.wild.apple;

import java.util.*;

public class LongestUniqueSubstringSet {
  public List<String> longestUniqueSubstringSet(String s) {
    Map<Character, Integer> map = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
      map.put(s.charAt(i), i);
    }

    List<Substring> intervals = new ArrayList<>();
    Set<Character> set = new HashSet<>();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (set.contains(c)) {
        continue;
      }
      set.add(c);
      intervals.add(new Substring(i, map.get(c)));
    }

    List<Substring> substrings = new ArrayList<>();
    Substring interval = intervals.get(0);
    for (int i = 1; i < intervals.size(); i++) {
      if (interval.end >= intervals.get(i).start) { // overlap
        interval.end = Math.max(interval.end, intervals.get(i).end);
      } else {
        substrings.add(interval);
        interval = intervals.get(i);
      }
    }
    substrings.add(interval);

    return substrings.stream().map(it -> s.substring(it.start, it.end + 1)).toList();
  }

  public static class Substring {
    public int start;
    public int end;

    public Substring(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public String toString() {
      return String.format("[%d, %d]", start, end);
    }
  }
}
