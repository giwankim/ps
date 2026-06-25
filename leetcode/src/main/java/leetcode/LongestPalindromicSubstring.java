package leetcode;

public class LongestPalindromicSubstring {
  public String longestPalindrome(String s) {
    int n = s.length();
    int[] result = {0, 1};
    boolean[][] dp = new boolean[n][n + 1];
    for (int i = 0; i < n; i++) {
      dp[i][0] = true;
    }
    for (int len = 1; len <= n; len++) {
      for (int i = 0; i < n - len + 1; i++) {
        if (s.charAt(i) == s.charAt(i + len - 1) && (len == 1 || dp[i + 1][len - 2])) {
          dp[i][len] = true;
          if (len > result[1]) {
            result[0] = i;
            result[1] = len;
          }
        }
      }
    }
    return s.substring(result[0], result[0] + result[1]);
  }
}
