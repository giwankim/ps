package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximumSubarrayTest {

  @ParameterizedTest
  @MethodSource
  void maxSubArray(int[] nums, int expected) {
    assertThat(new MaximumSubarray().maxSubArray(nums)).isEqualTo(expected);
  }

  static Stream<Arguments> maxSubArray() {
    return Stream.of(
        Arguments.of(new int[] {-1}, -1),
        Arguments.of(new int[] {5, 4, -1, 7, 8}, 23),
        Arguments.of(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6));
  }
}
