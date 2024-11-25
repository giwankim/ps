package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximalSquareTest {

  @ParameterizedTest
  @MethodSource
  void maximalSquare(char[][] matrix, int expected) {
    int actual = new MaximalSquare().maximalSquare(matrix);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maximalSquare() {
    return Stream.of(
        Arguments.of(
            new char[][] {
              {'1', '0', '1', '0', '0'},
              {'1', '0', '1', '1', '1'},
              {'1', '1', '1', '1', '1'},
              {'1', '0', '0', '1', '0'}
            },
            4),
        Arguments.of(new char[][] {{'0', '1'}, {'1', '0'}}, 1),
        Arguments.of(new char[][] {{'0'}}, 0));
  }
}
