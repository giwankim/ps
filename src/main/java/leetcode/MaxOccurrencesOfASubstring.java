package leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class MaxOccurrencesOfASubstring {
  public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
    int result = 0;

    Map<String, Integer> frequencies = new HashMap<>();

    for (int i = 0; i < s.length(); i++) {
      if (i + minSize > s.length()) {
        continue;
      }
      String t = s.substring(i, i + minSize);
      frequencies.put(t, frequencies.getOrDefault(t, 0) + 1);
    }

    for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
      Set<Character> characters = entry.getKey().chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
      if (characters.size() <= maxLetters) {
        result = Math.max(result, entry.getValue());
      }
    }

    return result;
  }
}
