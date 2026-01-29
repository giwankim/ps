package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestConsecutiveSequenceTest {

  @ParameterizedTest
  @MethodSource
  void longestConsecutive(int[] nums, int expected) {
    int actual = new LongestConsecutiveSequence().longestConsecutive(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> longestConsecutive() {
    return Stream.of(
        Arguments.of(new int[] {100, 4, 200, 1, 3, 2}, 4),
        Arguments.of(new int[] {0, 3, 7, 2, 5, 8, 4, 6, 0, 1}, 9));
  }
}
