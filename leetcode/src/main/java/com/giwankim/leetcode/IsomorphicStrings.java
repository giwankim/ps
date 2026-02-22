package com.giwankim.leetcode;

import java.util.Arrays;

public class IsomorphicStrings {
  public boolean isIsomorphic(String s, String t) {
    // Time complexity: O(n), Space complexity: O(1)
    int[] st = new int[256];
    int[] ts = new int[256];
    Arrays.fill(st, -1);
    Arrays.fill(ts, -1);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      char d = t.charAt(i);
      if (st[c] == -1 && ts[d] == -1) {
        st[c] = d;
        ts[d] = c;
      } else if (st[c] != d || ts[d] != c) {
        return false;
      }
    }
    return true;
  }
}
