package leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class MaxOccurrencesOfASubstring {
  public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
    Map<String, Integer> frequencies = new HashMap<>();

    for (int i = 0; i < s.length(); i++) {
      if (i + minSize > s.length()) {
        continue;
      }

      // substring t satisfying minSize <= t.length() <= maxSize
      String t = s.substring(i, i + minSize);

      // unique characters
      Set<Character> characters = t.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

      if (characters.size() <= maxLetters) {
        frequencies.put(t, frequencies.getOrDefault(t, 0) + 1);
      }
    }

    // max frequency
    return frequencies.values().stream().max(Integer::compareTo).orElse(0);
  }
}
