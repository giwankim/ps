package com.giwankim.leetcode;

import java.util.PriorityQueue;

public class MaximumTotalSubarrayValueII {
  public long maxTotalValue(int[] nums, int k) {
    int n = nums.length;
    int logN = 32 - Integer.numberOfLeadingZeros(n);
    int[][] stMin = new int[n][logN];
    int[][] stMax = new int[n][logN];
    for (int i = 0; i < n; i++) {
      stMin[i][0] = nums[i];
      stMax[i][0] = nums[i];
    }
    for (int j = 1; j < logN; j++) {
      for (int i = 0; i + (1 << j) <= n; i++) {
        stMin[i][j] = Math.min(stMin[i][j - 1], stMin[i + (1 << (j - 1))][j - 1]);
        stMax[i][j] = Math.max(stMax[i][j - 1], stMax[i + (1 << (j - 1))][j - 1]);
      }
    }


    PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));
    for (int i = 0; i < n; i++) {
      int j = 31 - Integer.numberOfLeadingZeros(n - i);
      int max = Math.max(stMax[i][j], stMax[n - (1 << j)][j]);
      int min = Math.min(stMin[i][j], stMin[n - (1 << j)][j]);
      maxHeap.offer(new int[] {max - min, i, n - 1});
    }

    long result = 0;
    while (k-- > 0 && !maxHeap.isEmpty()) {
      int[] subarray = maxHeap.poll();
      result += subarray[0];
      int l = subarray[1];
      int r = subarray[2];
      if (r - 1 >= l) {
        int j = 31 - Integer.numberOfLeadingZeros(r - l);
        int max = Math.max(stMax[l][j], stMax[r - (1 << j)][j]);
        int min = Math.min(stMin[l][j], stMin[r - (1 << j)][j]);
        maxHeap.offer(new int[] {max - min, l, r - 1});
      }
    }
    return result;
  }
}
