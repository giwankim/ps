package leetcode.p3301_3400;

public class FindTheLexicographicallySmallestValidSequence {
  /**
   * @implNote Time {@code O(n + m)}, space {@code O(n + m)}, where {@code n = word1.length()} and
   *     {@code m = word2.length()}.
   */
  public int[] validSequence(String word1, String word2) {
    int n = word1.length();
    int m = word2.length();

    int[] suf = new int[n + 1];
    int j = m - 1;
    for (int i = n - 1; i >= 0; i--) {
      if (j >= 0 && word1.charAt(i) == word2.charAt(j)) {
        j--;
      }
      suf[i] = m - 1 - j;
    }

    int[] ans = new int[m];
    j = 0;
    boolean canChange = true;
    for (int i = 0; i < n && j < m; i++) {
      if (word1.charAt(i) == word2.charAt(j)) {
        ans[j++] = i;
      } else if (canChange && suf[i + 1] >= m - j - 1) {
        ans[j++] = i;
        canChange = false;
      }
    }
    return j == m ? ans : new int[0];
  }
}
