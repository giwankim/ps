package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TwoSumIITest {
  TwoSumII sut = new TwoSumII();

  @ParameterizedTest
  @MethodSource
  void twoSum(int[] numbers, int target, int[] expected) {
    int[] actual = sut.twoSum(numbers, target);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> twoSum() {
    return Stream.of(
        Arguments.of(new int[] {2, 7, 11, 15}, 9, new int[] {1, 2}),
        Arguments.of(new int[] {2, 3, 4}, 6, new int[] {1, 3}),
        Arguments.of(new int[] {-1, 0}, -1, new int[] {1, 2}));
  }
}
