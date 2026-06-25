package leetcode;

public class CountBinarySubstrings {
  public int countBinarySubstrings(String s) {
    int result = 0;
    int current = 1;
    int prev = 0;
    for (int i = 1; i < s.length(); i++) {
      if (s.charAt(i) == s.charAt(i - 1)) {
        current += 1;
        continue;
      }
      result += Math.min(prev, current);
      prev = current;
      current = 1;
    }
    result += Math.min(prev, current);
    return result;
  }
}
