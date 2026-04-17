package com.giwankim.grind75;

public class MajorityElement {

  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public int majorityElement(int[] nums) {
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
