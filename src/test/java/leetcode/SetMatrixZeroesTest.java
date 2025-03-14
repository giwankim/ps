package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SetMatrixZeroesTest {
  SetMatrixZeroes sut;

  @BeforeEach
  void setUp() {
    sut = new SetMatrixZeroes();
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
