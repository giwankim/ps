package leetcode.p3001_3100;

public class MaximumLengthSubstringWithTwoOccurrences {
  /**
   * @implNote Time {@code O(n)} since each index enters and leaves the sliding window at most once,
   *     auxiliary space {@code O(1)} for the fixed 26-letter counts, where {@code n = s.length()}.
   */
  public int maximumLengthSubstring(String s) {
    int[] cnts = new int[26];
    int result = 0;
    int i = 0;
    for (int j = 0; j < s.length(); j++) {
      char c = s.charAt(j);
      if (++cnts[c - 'a'] > 2) {
        while (i < j && cnts[c - 'a'] > 2) {
          cnts[s.charAt(i++) - 'a']--;
        }
      }
      result = Math.max(result, j - i + 1);
    }
    return result;
  }
}
