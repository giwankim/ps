package com.giwankim.leetcode;

public class GasStation {
  public int canCompleteCircuit(int[] gas, int[] cost) {
    // Time complexity: O(n), Space complexity: O(1)
    int result = 0;
    int totalGain = 0;
    int currGain = 0;
    for (int i = 0; i < gas.length; i++) {
      int diff = gas[i] - cost[i];
      currGain += diff;
      totalGain += diff;
      if (currGain < 0) {
        result = i + 1;
        currGain = 0;
      }
    }
    if (totalGain < 0) {
      result = -1;
    }
    return result;
  }
}
