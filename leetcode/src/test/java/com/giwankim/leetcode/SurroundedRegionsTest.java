package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SurroundedRegionsTest {
  SurroundedRegions sut = new SurroundedRegions();

  @ParameterizedTest
  @MethodSource
  void shouldNotCaptureRegionsOnBorder(char[][] board, char[][] expected) {
    sut.solve(board);
    assertThat(board).isDeepEqualTo(expected);
  }

  static Stream<Arguments> shouldNotCaptureRegionsOnBorder() {
    return Stream.of(
        Arguments.of(new char[][] {{'X'}}, new char[][] {{'X'}}),
        Arguments.of(new char[][] {{'O'}}, new char[][] {{'O'}}),
        Arguments.of(new char[][] {{'X', 'O'}, {'O', 'X'}}, new char[][] {{'X', 'O'}, {'O', 'X'}}),
        Arguments.of(
            new char[][] {{'O', 'X', 'X'}, {'X', 'O', 'O'}},
            new char[][] {{'O', 'X', 'X'}, {'X', 'O', 'O'}}));
  }

  @Test
  void shouldCaptureSingleSurroundedRegion() {
    char[][] board = {
      {'X', 'X', 'X'},
      {'X', 'O', 'X'},
      {'X', 'X', 'X'}
    };
    char[][] expected = {
      {'X', 'X', 'X'},
      {'X', 'X', 'X'},
      {'X', 'X', 'X'}
    };

    sut.solve(board);

    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void shouldCaptureSurroundedButNotBorderConnected() {
    char[][] board = {
      {'X', 'X', 'X', 'X'},
      {'X', 'O', 'O', 'X'},
      {'X', 'X', 'O', 'X'},
      {'X', 'O', 'X', 'X'}
    };
    char[][] expected = {
      {'X', 'X', 'X', 'X'},
      {'X', 'X', 'X', 'X'},
      {'X', 'X', 'X', 'X'},
      {'X', 'O', 'X', 'X'}
    };

    sut.solve(board);

    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void shouldNotCaptureBorderConnectedChain() {
    char[][] board = {
      {'X', 'O', 'X', 'X'},
      {'X', 'O', 'X', 'X'},
      {'X', 'O', 'O', 'X'},
      {'X', 'X', 'X', 'X'}
    };
    char[][] expected = {
      {'X', 'O', 'X', 'X'},
      {'X', 'O', 'X', 'X'},
      {'X', 'O', 'O', 'X'},
      {'X', 'X', 'X', 'X'}
    };

    sut.solve(board);

    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void shouldHandleDisjointRegions() {
    char[][] board = {
      {'O', 'X', 'X', 'O', 'X'},
      {'X', 'O', 'X', 'O', 'X'},
      {'X', 'X', 'X', 'X', 'X'}
    };
    char[][] expected = {
      {'O', 'X', 'X', 'O', 'X'},
      {'X', 'X', 'X', 'O', 'X'},
      {'X', 'X', 'X', 'X', 'X'}
    };

    sut.solve(board);

    assertThat(board).isDeepEqualTo(expected);
  }
}
