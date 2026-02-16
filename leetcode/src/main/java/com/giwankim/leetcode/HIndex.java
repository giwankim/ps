package com.giwankim.leetcode;

public class HIndex {
  public int hIndex(int[] citations) {
    // Time complexity: O(n), Space complexity: O(1)
    int n = citations.length;
    int[] counts = new int[n + 1];
    for (int citation : citations) {
      if (citation >= n) {
        counts[n]++;
      } else {
        counts[citation]++;
      }
    }
    int count = 0;
    for (int i = n; i >= 0; i--) {
      count += counts[i];
      if (count >= i) {
        return i;
      }
    }
    return 0;
  }
}
