package com.giwankim.leetcode;

public class LengthOfLastWord {
  public int lengthOfLastWord(String s) {
    // Time complexity: O(n), Space complexity: O(1)
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
