package com.giwankim.grind75;

public class ClimbingStairs {

  public int climbStairs(int n) {
    int prev = 1;
    int curr = 1;
    for (int i = 0; i + 1 < n; i++) { // loop n - 1 times
      int temp = curr;
      curr += prev;
      prev = temp;
    }
    return curr;
  }
}
