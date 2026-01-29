package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongestIncreasingSubsequence {
  public int lengthOfLIS(int[] nums) {
    List<Integer> piles = new ArrayList<>();
    for (int num : nums) {
      int index = Collections.binarySearch(piles, num);

      if (index < 0) {
        index = -index - 1;
      }

      if (index == piles.size()) {
        piles.add(num);
      } else {
        piles.set(index, num);
      }
    }
    return piles.size();
  }
}
