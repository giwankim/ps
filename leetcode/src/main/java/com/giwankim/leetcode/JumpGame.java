package com.giwankim.leetcode;

public class JumpGame {
  public boolean canJump(int[] nums) {
    // Time complexity: O(n), Space complexity: O(1)
    int farthest = 0;
    for (int i = 0; i < nums.length; i++) {
      if (i > farthest) {
        return false;
      }
      int jumpLocation = Math.min(i + nums[i], nums.length - 1);
      farthest = Math.max(farthest, jumpLocation);
    }
    return farthest == nums.length - 1;
  }
}
