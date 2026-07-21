package leetcode;

import java.util.ArrayList;
import java.util.List;

public class MaximizeActiveSectionWithTradeI {
  /** @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = s.length()}. */
  public int maxActiveSectionsAfterTrade(String s) {
    int ones = (int) s.chars().filter(c -> c == '1').count();

    List<Integer> zeroes = new ArrayList<>();
    int n = s.length();
    int i = 0;
    for (int j = 0; j <= n; j++) {
      if (j < n && s.charAt(i) == s.charAt(j)) {
        continue;
      }
      if (s.charAt(i) == '0') {
        zeroes.add(j - i);
      }
      i = j;
    }

    int maxGain = 0;
    for (int j = 0; j < zeroes.size() - 1; j++) {
      maxGain = Math.max(maxGain, zeroes.get(j) + zeroes.get(j + 1));
    }

    return ones + maxGain;
  }
}
