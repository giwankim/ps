package com.giwankim.programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("NonAsciiCharacters")
class 거리두기_확인하기Test {
  @ParameterizedTest
  @MethodSource
  void solution(String[][] places, int[] expected) {
    int[] actual = new 거리두기_확인하기().solution(places);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> solution() {
    return Stream.of(
        Arguments.of(
            new String[][] {
              {"POOOP", "OXXOX", "OPXPX", "OOXOX", "POXXP"},
              {"POOPX", "OXPXP", "PXXXO", "OXXXO", "OOOPP"},
              {"PXOPX", "OXOXP", "OXPOX", "OXXOP", "PXPOX"},
              {"OOOXX", "XOOOX", "OOOXX", "OXOOX", "OOOOO"},
              {"PXPXP", "XPXPX", "PXPXP", "XPXPX", "PXPXP"}
            },
            new int[] {1, 0, 1, 1, 1}));
  }
}
