package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RottingOrangesTest {

  @ParameterizedTest
  @MethodSource
  void orangesRotting(int[][] grid, int expected) {
    int actual = new RottingOranges().orangesRotting(grid);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> orangesRotting() {
    return Stream.of(
        Arguments.of(new int[][] {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}, 4),
        Arguments.of(new int[][] {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}, -1),
        Arguments.of(new int[][] {{0, 2}}, 0));
  }
}
