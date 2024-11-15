package leetcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak {
  public boolean wordBreak(String s, List<String> wordDict) {
    Set<String> wordSet = new HashSet<>(wordDict);

    boolean[] a = new boolean[s.length() + 1];
    a[0] = true;

    for (int i = 1; i <= s.length(); i++) {
      for (int j = 0; j < i; j++) {
        String t = s.substring(j, i);
        if (a[j] && wordSet.contains(t)) {
          a[i] = true;
          break;
        }
      }
    }

    return a[s.length()];
  }
}
