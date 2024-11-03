package leetcode;

public class PalindromicSubstrings {
  public int countSubstrings(String s) {
    int cnt = 0;
    for (int i = 0; i < s.length(); i++) {
      for (int j = i; j < s.length(); j++) {
        String t = s.substring(i, j + 1);
        String reversed = new StringBuilder(t).reverse().toString();
        if (t.equals(reversed)) {
          cnt += 1;
        }
      }
    }
    return cnt;
  }
}
