package com.giwankim.leetcode;

public class JumpGameII {
  public int jump(int[] nums) {
    // Time complexity: O(n), Space complexity: O(1)
    int jumps = 0;
    int right = 0;
    int farthest = 0;
    for (int i = 0; i + 1 < nums.length; i++) {
      farthest = Math.max(farthest, i + nums[i]);
      if (i == right) {
        jumps += 1;
        right = farthest;
      }
    }
    return jumps;
  }
}
