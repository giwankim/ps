package leetcode;

import java.util.Arrays;

public class ValidAnagram {
  public boolean isAnagram(String s, String t) {
    char[] sChars = s.toCharArray();
    Arrays.sort(sChars);
    char[] tChars = t.toCharArray();
    Arrays.sort(tChars);
    return Arrays.equals(sChars, tChars);
  }
}
