package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MinimumPathSumTest {

  @ParameterizedTest
  @MethodSource("cases")
  void minPathSum(int[][] grid, int expected) {
    int actual = new MinimumPathSum().minPathSum(grid);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("cases")
  void minPathSumRecursive(int[][] grid, int expected) {
    int actual = new MinimumPathSum().minPathSumRecursive(grid);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}, 7),
        Arguments.of(new int[][] {{1, 2, 3}, {4, 5, 6}}, 12));
  }
}
