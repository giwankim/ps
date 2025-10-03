package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClimbingStairsTest {

  @ParameterizedTest
  @MethodSource
  void climbStairs(int n, int expected) {
    int actual = new ClimbingStairs().climbStairs(n);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> climbStairs() {
    return Stream.of(Arguments.of(2, 2), Arguments.of(3, 3));
  }
}
