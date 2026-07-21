package leetcode;

public class MaximizeActiveSectionWithTradeI {
  /** @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = s.length()}. */
  public int maxActiveSectionsAfterTrade(String s) {
    int ones = (int) s.chars().filter(c -> c == '1').count();

    int curr = 0;
    int prev = -1;
    int maxGain = 0;

    int n = s.length();
    int i = 0;
    for (int j = 0; j <= n; j++) {
      if (j < n && s.charAt(i) == s.charAt(j)) {
        continue;
      }
      if (s.charAt(i) == '0') {
        curr = j - i;
        if (prev != -1) {
          maxGain = Math.max(maxGain, prev + curr);
        }
        prev = curr;
      }
      i = j;
    }
    return ones + maxGain;
  }
}
