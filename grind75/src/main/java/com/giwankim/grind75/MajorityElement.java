package com.giwankim.grind75;

public class MajorityElement {

  public int majorityElement(int[] nums) {
    // Time complexity: O(n), Space complexity: O(1)
    int result = Integer.MIN_VALUE;
    int count = 0;
    for (int num : nums) {
      if (count == 0) {
        result = num;
        count = 1;
      } else if (result == num) {
        count += 1;
      } else {
        count -= 1;
      }
    }
    return result;
  }
}
