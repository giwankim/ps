package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeSortedArrayTest {
  @ParameterizedTest
  @MethodSource
  void merge(int[] num1, int m, int[] num2, int n, int[] expected) {
    new MergeSortedArray().merge(num1, m, num2, n);
    assertThat(num1).isEqualTo(expected);
  }

  private static Stream<Arguments> merge() {
    return Stream.of(
        Arguments.of(
            new int[] {1, 2, 3, 0, 0, 0}, 3, new int[] {2, 5, 6}, 3, new int[] {1, 2, 2, 3, 5, 6}),
        Arguments.of(new int[] {1}, 1, new int[] {}, 0, new int[] {1}),
        Arguments.of(new int[] {0}, 0, new int[] {1}, 1, new int[] {1}));
  }
}
