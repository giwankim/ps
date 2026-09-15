package leetcode.p2401_2500;

import java.util.Arrays;

/**
 * <a
 * href="https://leetcode.com/problems/maximum-number-of-non-overlapping-palindrome-substrings/">2472.
 * Maximum Number of Non-overlapping Palindrome Substrings</a>
 */
public class MaximumNumberOfNonOverlappingPalindromeSubstrings {
  private int n;
  private int[] dp;

  /**
   * @implNote Time {@code O(n * k)}: the {@code dp} fill is linear, then the memo behind
   *     {@link #maxPalindromes(String, int, int)} computes {@code n} states, each testing at
   *     {@code O(k)} apiece only the two {@link #isPalindrome} candidates of length {@code k} and
   *     {@code k + 1} that a shortest cut can use, with every memo hit {@code O(1)}. Space
   *     {@code O(n)} for {@code dp} plus a recursion stack reaching depth {@code n} along the skip
   *     chain, where {@code n = s.length()}.
   */
  public int maxPalindromes(String s, int k) {
    n = s.length();
    dp = new int[n];
    Arrays.fill(dp, -1);
    return maxPalindromes(s, 0, k);
  }

  private int maxPalindromes(String s, int i, int k) {
    if (i == s.length()) {
      return 0;
    }
    if (dp[i] != -1) {
      return dp[i];
    }
    int result = maxPalindromes(s, i + 1, k);
    for (int j = i + k - 1; j <= i + k && j < n; j++) {
      if (isPalindrome(s, i, j)) {
        result = Math.max(result, 1 + maxPalindromes(s, j + 1, k));
      }
    }
    dp[i] = result;
    return result;
  }

  private boolean isPalindrome(String s, int i, int j) {
    while (i < j) {
      if (s.charAt(i) != s.charAt(j)) {
        return false;
      }
      i++;
      j--;
    }
    return true;
  }
}
