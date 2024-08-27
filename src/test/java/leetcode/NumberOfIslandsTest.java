package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NumberOfIslandsTest {
  @ParameterizedTest
  @MethodSource
  void numIslands(char[][] grid, int expected) {
    int actual = new NumberOfIslands().numIslands(grid);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> numIslands() {
    return Stream.of(
        Arguments.of(
            new char[][] {
              {'1', '1', '1', '1', '0'},
              {'1', '1', '0', '1', '0'},
              {'1', '1', '0', '0', '0'},
              {'0', '0', '0', '0', '0'}
            },
            1),
        Arguments.of(
            new char[][] {
              {'1', '1', '0', '0', '0'},
              {'1', '1', '0', '0', '0'},
              {'0', '0', '1', '0', '0'},
              {'0', '0', '0', '1', '1'}
            },
            3));
  }
}
