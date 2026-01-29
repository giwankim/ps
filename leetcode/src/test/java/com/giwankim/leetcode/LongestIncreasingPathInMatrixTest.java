package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestIncreasingPathInMatrixTest {

  @ParameterizedTest
  @MethodSource
  void longestIncreasingPath(int[][] matrix, int expected) {
    int actual = new LongestIncreasingPathInMatrix().longestIncreasingPath(matrix);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> longestIncreasingPath() {
    return Stream.of(
        Arguments.of(new int[][] {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}, 4),
        Arguments.of(new int[][] {{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}, 4),
        Arguments.of(new int[][] {{1}}, 1));
  }
}
