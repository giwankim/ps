package com.giwankim.leetcode;

public class CountSubarraysWithMajorityElementI {
  /** @implNote Time {@code O(n²)}, auxiliary space {@code O(n)}, where {@code n = nums.length}. */
  public int countMajoritySubarrays(int[] nums, int target) {
    int n = nums.length;
    int[] prefix = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      prefix[i] = prefix[i - 1] + (nums[i - 1] == target ? 1 : 0);
    }
    int result = 0;
    for (int s = 0; s < n; s++) {
      for (int e = s; e < n; e++) {
        int len = e - s + 1;
        int count = prefix[e + 1] - prefix[s];
        if (2 * count > len) {
          result++;
        }
      }
    }
    return result;
  }
}
