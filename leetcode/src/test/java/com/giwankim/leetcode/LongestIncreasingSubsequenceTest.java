package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestIncreasingSubsequenceTest {
  @ParameterizedTest
  @MethodSource
  void lengthOfLIS(int[] nums, int expected) {
    var sut = new LongestIncreasingSubsequence();
    int actual = sut.lengthOfLIS(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> lengthOfLIS() {
    return Stream.of(
        Arguments.of(new int[] {10, 9, 2, 5, 3, 7, 101, 18}, 4),
        Arguments.of(new int[] {0, 1, 0, 3, 2, 3}, 4),
        Arguments.of(new int[] {7, 7, 7, 7, 7, 7, 7}, 1));
  }
}
