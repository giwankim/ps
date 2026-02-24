package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SetMatrixZeroesTest {
  SetMatrixZeroes sut = new SetMatrixZeroes();

  @Test
  void neitherFirstRowNorFirstColumnContainsZero() {
    int[][] matrix = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
    int[][] expected = {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};
    sut.setZeroes(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void firstRowContainsZeroFirstColumnNoZero() {
    int[][] matrix = {{1, 2, 0}, {3, 4, 5}, {6, 7, 9}};
    int[][] expected = {{0, 0, 0}, {3, 4, 0}, {6, 7, 0}};
    sut.setZeroes(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void firstRowNoZeroFirstColumnContainsZero() {
    int[][] matrix = {{1, 2, 3}, {0, 4, 5}, {6, 7, 8}};
    int[][] expected = {{0, 2, 3}, {0, 0, 0}, {0, 7, 8}};
    sut.setZeroes(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void firstRowContainsZeroFirstColumnContainsZero() {
    int[][] matrix = {{1, 0, 2}, {0, 3, 4}, {5, 6, 7}};
    int[][] expected = {{0, 0, 0}, {0, 0, 0}, {0, 0, 7}};
    sut.setZeroes(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void leftRightCornerZero() {
    int[][] matrix = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    int[][] expected = {{0, 0, 0}, {0, 4, 5}, {0, 7, 8}};
    sut.setZeroes(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void setZeroes(int[][] matrix, int[][] expected) {
    sut.setZeroes(matrix);
    assertThat(matrix).isEqualTo(expected);
  }

  static Stream<Arguments> setZeroes() {
    return Stream.of(
        Arguments.of(
            new int[][] {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}},
            new int[][] {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}}),
        Arguments.of(
            new int[][] {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}},
            new int[][] {{0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0}}));
  }
}
