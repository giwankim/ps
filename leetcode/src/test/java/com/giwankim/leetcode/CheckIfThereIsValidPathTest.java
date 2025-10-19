package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CheckIfThereIsValidPathTest {

  CheckIfThereIsValidPath solution = new CheckIfThereIsValidPath();

  @Test
  void validPath() {
    int[][] grid = {{2, 4, 3}, {6, 5, 2}};
    assertThat(solution.hasValidPath(grid)).isTrue();
  }

  @ParameterizedTest
  @MethodSource
  void noPath(int[][] grid) {
    assertThat(solution.hasValidPath(grid)).isFalse();
  }

  static Stream<Arguments> noPath() {
    return Stream.of(
        Arguments.of((Object) new int[][] {{1, 2, 1}, {1, 2, 1}}),
        Arguments.of((Object) new int[][] {{1, 1, 2}}));
  }
}
