package com.giwankim.leetcode;

public class JumpGameII {
  public int jump(int[] nums) {
    int result = 0;
    int farthest = 0;
    int end = 0;
    for (int i = 0; i + 1 < nums.length; i++) {
      farthest = Math.max(farthest, i + nums[i]);
      if (i == end) {
        result += 1;
        end = farthest;
      }
    }
    return result;
  }
}
