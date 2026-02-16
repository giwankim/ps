package com.giwankim.leetcode;

public class ZigzagConversion {
  public String convert(String s, int numRows) {
    StringBuilder[] sbs = new StringBuilder[numRows];
    for (int i = 0; i < sbs.length; i++) {
      sbs[i] = new StringBuilder();
    }

    int i = 0;
    while (i < s.length()) {
      for (int j = 0; j < numRows && i < s.length(); j++) {
        sbs[j].append(s.charAt(i));
        i++;
      }
      for (int j = numRows - 2; j > 0 && i < s.length(); j--) {
        sbs[j].append(s.charAt(i));
        i++;
      }
    }

    StringBuilder result = new StringBuilder();
    for (StringBuilder sb : sbs) {
      result.append(sb);
    }
    return result.toString();
  }
}
