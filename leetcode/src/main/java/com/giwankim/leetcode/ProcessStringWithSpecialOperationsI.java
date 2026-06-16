package com.giwankim.leetcode;

public class ProcessStringWithSpecialOperationsI {
  public String processStr(String s) {
    StringBuilder result = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (c == '#') {
        result.append(result);
      } else if (c == '*') {
        if (!result.isEmpty()) {
          result.deleteCharAt(result.length() - 1);
        }
      } else if (c == '%') {
        result.reverse();
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }
}
