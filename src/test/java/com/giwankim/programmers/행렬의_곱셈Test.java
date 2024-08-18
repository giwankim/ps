package com.giwankim.programmers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class 행렬의_곱셈Test {
  @ParameterizedTest
  @MethodSource
  void solution(int[][] arr1, int[][] arr2, int[][] expected) {
    int[][] actual = new 행렬의_곱셈().solution(arr1, arr2);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> solution() {
    return Stream.of(
        Arguments.of(
            new int[][]{{1, 4}, {3, 2}, {4, 1}},
            new int[][]{{3, 3}, {3, 3}},
            new int[][]{{15, 15}, {15, 15}, {15, 15}}),
        Arguments.of(
            new int[][]{{2, 3, 2}, {4, 2, 4}, {3, 1, 4}},
            new int[][]{{5, 4, 3}, {2, 4, 1}, {3, 1, 1}},
            new int[][]{{22, 22, 11}, {36, 28, 18}, {29, 20, 14}}));
  }
}
