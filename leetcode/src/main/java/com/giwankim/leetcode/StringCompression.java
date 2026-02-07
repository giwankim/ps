package com.giwankim.leetcode;

public class StringCompression {
  public int compress(char[] chars) {
    int i = 0;
    int j = 0;
    while (j < chars.length) {
      char c = chars[j];
      int count = 0;
      while (j < chars.length && chars[j] == c) {
        count++;
        j++;
      }
      chars[i++] = c;
      if (count > 1) {
        for (char d : String.valueOf(count).toCharArray()) {
          chars[i++] = d;
        }
      }
    }
    return i;
  }
}
