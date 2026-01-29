package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SlidingWindowMaximumTest {

  @ParameterizedTest
  @MethodSource
  void maxSlidingWindow(int[] nums, int k, int[] expected) {
    int[] actual = new SlidingWindowMaximum().maxSlidingWindow(nums, k);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maxSlidingWindow() {
    return Stream.of(
        Arguments.of(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3, new int[] {3, 3, 5, 5, 6, 7}),
        Arguments.of(new int[] {1}, 1, new int[] {1}));
  }
}
