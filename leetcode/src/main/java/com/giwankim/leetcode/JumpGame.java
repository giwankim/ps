package com.giwankim.leetcode;

public class JumpGame {
  public boolean canJump(int[] nums) {
    int maxReach = 0;
    for (int i = 0; i < nums.length; i++) {
      if (i > maxReach) {
        return false;
      }
      maxReach = Math.max(maxReach, i + nums[i]);
      if (maxReach + 1 >= nums.length) {
        return true;
      }
    }
    return maxReach + 1 >= nums.length;
  }
}
