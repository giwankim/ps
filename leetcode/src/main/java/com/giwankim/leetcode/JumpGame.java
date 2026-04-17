package com.giwankim.leetcode;

public class JumpGame {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public boolean canJump(int[] nums) {
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
