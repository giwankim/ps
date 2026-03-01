package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummaryRanges {
  public List<String> summaryRanges(int[] nums) {
    // Time complexity: O(n), Space complexity: O(1)
    if (nums.length == 0) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<>();
    int lo = nums[0];
    int hi = nums[0];
    for (int i = 1; i < nums.length; i++) {
      if (nums[i] == hi + 1) {
        hi = nums[i];
        continue;
      }
      addRange(lo, hi, result);
      lo = nums[i];
      hi = nums[i];
    }
    addRange(lo, hi, result);
    return result;
  }

  private void addRange(int lo, int hi, List<String> result) {
    if (lo == hi) {
      result.add(String.valueOf(lo));
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(String.valueOf(lo));
    sb.append("->");
    sb.append(String.valueOf(hi));
    result.add(sb.toString());
  }
}
