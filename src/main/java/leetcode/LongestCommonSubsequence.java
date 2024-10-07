package leetcode;

public class LongestCommonSubsequence {
  public int longestCommonSubsequence(String text1, String text2) {
    int[][] a = new int[text1.length() + 1][text2.length() + 1];
    a[0][0] = 0;
    for (int i = 1; i <= text1.length(); i++) {
      for (int j = 1; j <= text2.length(); j++) {
        if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
          a[i][j] = 1 + a[i - 1][j - 1];
        } else {
          a[i][j] = Math.max(a[i - 1][j], a[i][j - 1]);
        }
      }
    }
    return a[text1.length()][text2.length()];
  }
}
