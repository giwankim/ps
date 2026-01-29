package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PossibleBipartitionTest {
  @ParameterizedTest
  @MethodSource
  void possibleBipartition(int n, int[][] dislikes, boolean expected) {
    PossibleBipartition sut = new PossibleBipartition();
    boolean actual = sut.possibleBipartition(n, dislikes);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> possibleBipartition() {
    return Stream.of(
        Arguments.of(4, new int[][] {{1, 2}, {1, 3}, {2, 4}}, true),
        Arguments.of(3, new int[][] {{1, 2}, {1, 3}, {2, 3}}, false));
  }
}
