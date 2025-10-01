package com.giwankim.grind75;

public class MajorityElement {

  public int majorityElement(int[] nums) {
    int result = -1;
    int count = 0;

    for (int num : nums) {
      if (count == 0) {
        result = num;
        count = 1;
      } else if (result == num) {
        count += 1;
      } else if (result != num) {
        count -= 1;
      }
    }
    return result;
  }
}
