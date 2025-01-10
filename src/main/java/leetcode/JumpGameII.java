package leetcode;

import java.util.Arrays;

public class JumpGameII {
  public int jump(int[] nums) {
    int[] a = new int[nums.length];
    Arrays.fill(a, Integer.MAX_VALUE);
    a[0] = 0;
    for (int i = 0; i < a.length; i++) {
      for (int j = 1; j <= nums[i]; j++) {
        if (i + j >= nums.length) {
          continue;
        }
        a[i + j] = Math.min(a[i + j], a[i] + 1);
      }
    }
    return a[nums.length - 1];
  }
}
