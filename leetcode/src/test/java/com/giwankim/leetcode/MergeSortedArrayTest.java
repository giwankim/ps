package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeSortedArrayTest {
  MergeSortedArray sut = new MergeSortedArray();

  @ParameterizedTest
  @MethodSource
  void oneOfTheArraysIsEmpty(int[] num1, int m, int[] num2, int n, int[] expected) {
    sut.merge(num1, m, num2, n);
    assertThat(num1).isEqualTo(expected);
  }

  static Stream<Arguments> oneOfTheArraysIsEmpty() {
    return Stream.of(
        Arguments.of(new int[] {1}, 1, new int[] {}, 0, new int[] {1}),
        Arguments.of(new int[] {0}, 0, new int[] {1}, 1, new int[] {1}));
  }

  @Test
  void merge() {
    int[] num1 = new int[] {1, 2, 3, 0, 0, 0};
    int m = 3;
    int[] num2 = new int[] {2, 5, 6};
    int n = 3;
    int[] expected = new int[] {1, 2, 2, 3, 5, 6};

    sut.merge(num1, m, num2, n);

    assertThat(num1).isEqualTo(expected);
  }
}
