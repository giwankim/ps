package leetcode.p0001_0100;

/** <a href="https://leetcode.com/problems/longest-common-prefix/">14. Longest Common Prefix</a> */
public class LongestCommonPrefix {
  /** @implNote Time {@code O(n*m)}, space {@code O(1)}. */
  public String longestCommonPrefix(String[] strs) {
    String prefix = strs[0];
    for (int i = 1; i < strs.length; i++) {
      int j = 0;
      while (j < prefix.length() && j < strs[i].length() && prefix.charAt(j) == strs[i].charAt(j)) {
        j++;
      }
      prefix = prefix.substring(0, j);
    }
    return prefix;
  }
}
