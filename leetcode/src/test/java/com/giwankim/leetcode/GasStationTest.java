package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GasStationTest {
  GasStation sut = new GasStation();

  @ParameterizedTest
  @MethodSource
  void can(int[] gas, int[] cost, int expected) {
    int actual = sut.canCompleteCircuit(gas, cost);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> can() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 3, 4, 5}, new int[] {3, 4, 5, 1, 2}, 3),
        Arguments.of(new int[] {3, 1, 1}, new int[] {1, 2, 2}, 0));
  }

  @Test
  void cannot() {
    int[] gas = {2, 3, 4};
    int[] cost = {3, 4, 3};
    int actual = sut.canCompleteCircuit(gas, cost);
    assertThat(actual).isEqualTo(-1);
  }
}
