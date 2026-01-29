package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JumpGameTest {
  @ParameterizedTest
  @MethodSource
  void canJump(int[] nums, boolean expected) {
    JumpGame sut = new JumpGame();
    boolean actual = sut.canJump(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> canJump() {
    return Stream.of(
        Arguments.of(new int[] {2, 3, 1, 1, 4}, true),
        Arguments.of(new int[] {3, 2, 1, 0, 4}, false));
  }
}
