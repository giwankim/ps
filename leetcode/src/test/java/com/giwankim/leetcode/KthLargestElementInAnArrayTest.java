package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KthLargestElementInAnArrayTest {
  KthLargestElementInAnArray sut = new KthLargestElementInAnArray();

  @Test
  void distinctElements() {
    int[] nums = {3, 2, 1, 5, 6, 4};
    int k = 2;

    int kthLargest = sut.findKthLargest(nums, k);

    assertThat(kthLargest).isEqualTo(5);
  }

  @Test
  void repeatedElements() {
    int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};
    int k = 4;

    int kthLargest = sut.findKthLargest(nums, k);

    assertThat(kthLargest).isEqualTo(4);
  }
}
