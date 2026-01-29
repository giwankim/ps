package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NumberOfProvincesTest {

  @ParameterizedTest
  @MethodSource
  void findCircleNum(int[][] isConnected, int expected) {
    int actual = new NumberOfProvinces().findCircleNum(isConnected);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> findCircleNum() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}, 2),
        Arguments.of(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}, 3));
  }
}
