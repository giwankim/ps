package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RotateImageTest {

  @ParameterizedTest
  @MethodSource
  void rotate(int[][] matrix, int[][] expected) {
    new RotateImage().rotate(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  private static Stream<Arguments> rotate() {
    return Stream.of(
        Arguments.of(
            new int[][] {
              {1, 2, 3},
              {4, 5, 6},
              {7, 8, 9}
            },
            new int[][] {{7, 4, 1}, {8, 5, 2}, {9, 6, 3}}),
        Arguments.of(
            new int[][] {
              {5, 1, 9, 11},
              {2, 4, 8, 10},
              {13, 3, 6, 7},
              {15, 14, 12, 16}
            },
            new int[][] {{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}}));
  }
}
