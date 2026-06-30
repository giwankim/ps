package leetcode;

public class NumberOfSubstringsContainingAllThreeCharacters {
  public int numberOfSubstrings(String s) {
    int result = 0;
    int a = 0, b = 0, c = 0;
    int l = 0, r = 0;
    while (l <= r && r < s.length()) {
      if (s.charAt(r) == 'a') {
        a++;
      } else if (s.charAt(r) == 'b') {
        b++;
      } else {
        c++;
      }
      r++;

      while (a > 0 && b > 0 && c > 0) {
        result += s.length() - r + 1;
        if (s.charAt(l) == 'a') {
          a--;
        } else if (s.charAt(l) == 'b') {
          b--;
        } else {
          c--;
        }
        l++;
      }
    }
    return result;
  }
}
