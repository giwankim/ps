package com.giwankim.programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class 두_큐_합_같게_만들기Test {

  @ParameterizedTest
  @MethodSource
  void 두_큐_합_같게_만들기(int[] queue1, int[] queue2, int expected) {
    int actual = new 두_큐_합_같게_만들기().solution(queue1, queue2);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> 두_큐_합_같게_만들기() {
    return Stream.of(
        Arguments.of(new int[] {3, 2, 7, 2}, new int[] {4, 6, 5, 1}, 2),
        Arguments.of(new int[] {1, 2, 1, 2}, new int[] {1, 10, 1, 2}, 7),
        Arguments.of(new int[] {1, 1}, new int[] {1, 5}, -1));
  }
}
