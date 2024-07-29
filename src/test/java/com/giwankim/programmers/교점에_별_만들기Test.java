package com.giwankim.programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class 교점에_별_만들기Test {
  @ParameterizedTest
  @MethodSource
  void intersect(int[][] line, String[] expected) {
    교점에_별_만들기 subject = new 교점에_별_만들기();
    assertThat(subject.solution(line)).containsExactly(expected);
  }

  private static Stream<Arguments> intersect() {
    return Stream.of(
        Arguments.of(
            new int[][] {{2, -1, 4}, {-2, -1, 4}, {0, -1, 1}, {5, -8, -12}, {5, 8, 12}},
            new String[] {
              "....*....",
              ".........",
              ".........",
              "*.......*",
              ".........",
              ".........",
              ".........",
              ".........",
              "*.......*"
            }),
        Arguments.of(new int[][] {{0, 1, -1}, {1, 0, -1}, {1, 0, 1}}, new String[] {"*.*"}),
        Arguments.of(new int[][] {{1, -1, 0}, {2, -1, 0}}, new String[] {"*"}),
        Arguments.of(new int[][] {{1, -1, 0}, {2, -1, 0}, {4, -1, 0}}, new String[] {"*"}));
  }
}
