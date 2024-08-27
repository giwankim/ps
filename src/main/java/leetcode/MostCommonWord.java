package leetcode;

import java.util.*;

public class MostCommonWord {
  public String mostCommonWord(String paragraph, String[] banned) {
    HashSet<String> blacklist = new HashSet<>(Arrays.asList(banned));
    Map<String, Integer> counts = new HashMap<>();
    String[] words = paragraph.replaceAll("\\W+", " ").toLowerCase().split(" ");

    for (String word : words) {
      if (!blacklist.contains(word)) {
        counts.put(word, counts.getOrDefault(word, 0) + 1);
      }
    }

    return Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey();
  }
}
