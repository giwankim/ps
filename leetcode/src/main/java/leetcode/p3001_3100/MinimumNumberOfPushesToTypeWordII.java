package leetcode.p3001_3100;

import java.util.Arrays;

public class MinimumNumberOfPushesToTypeWordII {
  /**
   * @implNote Time {@code O(n)} since the counting pass touches every character once while the sort
   *     and the accumulation loop both run over the fixed 26-element {@code cnt} array and so
   *     contribute a constant no matter how long the word is — auxiliary space {@code O(1)} because
   *     {@code cnt} holds one slot per letter of the alphabet, where {@code n = word.length()}.
   */
  public int minimumPushes(String word) {
    int[] cnt = new int[26];
    for (char c : word.toCharArray()) {
      cnt[c - 'a']++;
    }
    Arrays.sort(cnt);
    int ans = 0;
    for (int i = 25; i >= 0; i--) {
      // push: (25 - i) / 8 + 1
      // freq: cnt[i]
      int pushes = (25 - i) / 8 + 1;
      ans += pushes * cnt[i];
    }
    return ans;
  }
}
