package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MissingNumberTest {

  @ParameterizedTest
  @MethodSource
  void missingNumber(int[] nums, int expected) {
    int actual = new MissingNumber().missingNumber(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> missingNumber() {
    return Stream.of(
        Arguments.of(new int[] {3, 0, 1}, 2),
        Arguments.of(new int[] {0, 1}, 2),
        Arguments.of(new int[] {9, 6, 4, 2, 3, 5, 7, 0, 1}, 8),
        Arguments.of(new int[] {1}, 0));
  }
}
