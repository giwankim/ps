package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RotateArrayTest {
  RotateArray sut = new RotateArray();

  @ParameterizedTest
  @MethodSource
  void rotate(int[] nums, int k, int[] expected) {
    sut.rotate(nums, k);
    assertThat(nums).isEqualTo(expected);
  }

  static Stream<Arguments> rotate() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 3, 4, 5, 6, 7}, 3, new int[] {5, 6, 7, 1, 2, 3, 4}),
        Arguments.of(new int[] {-1, -100, 3, 99}, 2, new int[] {3, 99, -1, -100}));
  }
}
