package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ArrayPartitionTest {
  @ParameterizedTest
  @MethodSource
  void arrayPairSum(int[] nums, int expected) {
    int actual = new ArrayPartition().arrayPairSum(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> arrayPairSum() {
    return Stream.of(
        Arguments.of(new int[] {1, 4, 3, 2}, 4), Arguments.of(new int[] {6, 2, 6, 5, 1, 2}, 9));
  }
}
