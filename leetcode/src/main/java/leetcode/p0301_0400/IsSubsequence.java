package leetcode.p0301_0400;

/** <a href="https://leetcode.com/problems/is-subsequence/">392. Is Subsequence</a> */
public class IsSubsequence {
  /** @implNote Time {@code O(m)}, space {@code O(1)}. */
  public boolean isSubsequence(String s, String t) {
    int i = 0;
    for (int j = 0; i < s.length() && j < t.length(); j++) {
      if (s.charAt(i) == t.charAt(j)) {
        i++;
      }
    }
    return i == s.length();
  }
}
