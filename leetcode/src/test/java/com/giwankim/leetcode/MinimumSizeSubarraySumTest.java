package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinimumSizeSubarraySumTest {
  MinimumSizeSubarraySum sut = new MinimumSizeSubarraySum();

  @Test
  void noSuchSubArray() {
    int[] nums = {1, 1, 1, 1, 1, 1, 1, 1};
    int actual = sut.minSubArrayLen(11, nums);
    assertThat(actual).isZero();
  }

  @Test
  void singleElementSubArray() {
    int[] nums = {1, 4, 4};
    int actual = sut.minSubArrayLen(4, nums);
    assertThat(actual).isOne();
  }

  @Test
  void multipleElementSubArray() {
    int[] nums = {2, 3, 1, 2, 4, 3};
    int actual = sut.minSubArrayLen(7, nums);
    assertThat(actual).isEqualTo(2);
  }
}
