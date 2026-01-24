package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximalSquareTest {

  MaximalSquare subject = new MaximalSquare();

  @ParameterizedTest
  @MethodSource
  void maximalSquare(char[][] matrix, int expected) {
    assertThat(subject.maximalSquare(matrix)).isEqualTo(expected);
  }

  static Stream<Arguments> maximalSquare() {
    return Stream.of(
        Arguments.of(new char[][] {{'0'}}, 0),
        Arguments.of(new char[][] {{'1'}}, 1),
        Arguments.of(new char[][] {{'0', '1'}, {'1', '0'}}, 1),
        Arguments.of(
            new char[][] {
              {'1', '0', '1', '0', '0'},
              {'1', '0', '1', '1', '1'},
              {'1', '1', '1', '1', '1'},
              {'1', '0', '0', '1', '0'}
            },
            4));
  }
}
