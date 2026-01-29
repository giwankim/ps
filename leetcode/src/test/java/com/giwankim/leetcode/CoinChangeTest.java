package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CoinChangeTest {

  @ParameterizedTest
  @MethodSource
  void coinChange(int[] coins, int amount, int expected) {
    int actual = new CoinChange().coinChange(coins, amount);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> coinChange() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 5}, 11, 3),
        Arguments.of(new int[] {2}, 3, -1),
        Arguments.of(new int[] {1}, 0, 0));
  }
}
