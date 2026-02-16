package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HIndexTest {
  HIndex sut = new HIndex();

  @ParameterizedTest
  @MethodSource
  void hIndex(int[] citations, int expected) {
    int actual = sut.hIndex(citations);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> hIndex() {
    return Stream.of(
        Arguments.of(new int[] {3, 0, 6, 1, 5}, 3),
        Arguments.of(new int[] {1, 3, 1}, 1),
        Arguments.of(new int[] {100}, 1));
  }
}
