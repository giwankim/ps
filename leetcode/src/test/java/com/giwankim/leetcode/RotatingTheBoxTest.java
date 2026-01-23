package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RotatingTheBoxTest {

  RotatingTheBox rotatingTheBox = new RotatingTheBox();

  @ParameterizedTest
  @MethodSource
  void row(char[][] boxGrid, char[][] expected) {
    assertThat(rotatingTheBox.rotateTheBox(boxGrid)).isDeepEqualTo(expected);
  }

  static Stream<Arguments> row() {
    return Stream.of(
        Arguments.of(new char[][] {{'#', '.', '#'}}, new char[][] {{'.'}, {'#'}, {'#'}}),
        Arguments.of(new char[][] {{'#', '#', '.'}}, new char[][] {{'.'}, {'#'}, {'#'}}),
        Arguments.of(new char[][] {{'#', '.', '.'}}, new char[][] {{'.'}, {'.'}, {'#'}}),
        Arguments.of(
            new char[][] {{'#', '.', '#', '.'}}, new char[][] {{'.'}, {'.'}, {'#'}, {'#'}}));
  }

  @Test
  void twoRowsAndObstacle() {
    char[][] boxGrid = {{'#', '.', '*', '.'}, {'#', '#', '*', '.'}};
    char[][] expected = {{'#', '.'}, {'#', '#'}, {'*', '*'}, {'.', '.'}};

    char[][] actual = rotatingTheBox.rotateTheBox(boxGrid);

    assertThat(actual).isDeepEqualTo(expected);
  }

  @Test
  void rotateBox() {
    char[][] boxGrid = {
      {'#', '#', '*', '.', '*', '.'}, {'#', '#', '#', '*', '.', '.'}, {'#', '#', '#', '.', '#', '.'}
    };

    char[][] expected = {
      {'.', '#', '#'},
      {'.', '#', '#'},
      {'#', '#', '*'},
      {'#', '*', '.'},
      {'#', '.', '*'},
      {'#', '.', '.'}
    };

    char[][] actual = rotatingTheBox.rotateTheBox(boxGrid);

    assertThat(actual).isDeepEqualTo(expected);
  }
}
