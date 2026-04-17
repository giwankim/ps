package com.giwankim.leetcode;

public class LengthOfLastWord {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int lengthOfLastWord(String s) {
    int length = 0;
    int i = s.length() - 1;
    while (i >= 0 && s.charAt(i) == ' ') {
      i -= 1;
    }
    while (i >= 0 && s.charAt(i) != ' ') {
      length += 1;
      i -= 1;
    }
    return length;
  }
}
