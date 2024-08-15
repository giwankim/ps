package com.giwankim.programmers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class 삼각_달팽이Test {
  @ParameterizedTest
  @MethodSource("testCases")
  void 삼각_달팽이(int n, int[] expected) {
    삼각_달팽이 subject = new 삼각_달팽이();

    assertThat(subject.solution(n)).isEqualTo(expected);
  }

  static Stream<Arguments> testCases() {
    return Stream.of(
        Arguments.of(1, new int[]{1}),
        Arguments.of(2, new int[]{1, 2, 3}),
        Arguments.of(3, new int[]{1, 2, 6, 3, 4, 5}),
        Arguments.of(4, new int[]{1, 2, 9, 3, 10, 8, 4, 5, 6, 7}),
        Arguments.of(5, new int[]{1, 2, 12, 3, 13, 11, 4, 14, 15, 10, 5, 6, 7, 8, 9}),
        Arguments.of(
            6,
            new int[]{1, 2, 15, 3, 16, 14, 4, 17, 21, 13, 5, 18, 19, 20, 12, 6, 7, 8, 9, 10, 11}));
  }
}
