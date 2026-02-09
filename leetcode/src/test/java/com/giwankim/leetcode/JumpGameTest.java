package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JumpGameTest {
  JumpGame sut = new JumpGame();

  @Test
  void can() {
    int[] nums = {2, 3, 1, 1, 4};
    assertThat(sut.canJump(nums)).isTrue();
  }

  @Test
  void cant() {
    int[] nums = {3, 2, 1, 0, 4};
    assertThat(sut.canJump(nums)).isFalse();
  }
}
