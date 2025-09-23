package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FloodFillTest {

  @ParameterizedTest
  @MethodSource
  void floodFill(int[][] image, int sr, int sc, int color, int[][] expected) {
    assertThat(new FloodFill().floodFill(image, sr, sc, color)).isEqualTo(expected);
  }

  static Stream<Arguments> floodFill() {
    return Stream.of(
        Arguments.of(
            new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}},
            1,
            1,
            2,
            new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}),
        Arguments.of(new int[][] {{0, 0, 0}}, 0, 0, 0, new int[][] {{0, 0, 0}}));
  }
}
