package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseBitsTest {
  ReverseBits sut = new ReverseBits();

  @ParameterizedTest
  @MethodSource
  void reverseBits(int n, int expected) {
    int actual = sut.reverseBits(n);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> reverseBits() {
    return Stream.of(
        Arguments.of(0b00000010100101000001111010011100, 0b0111001011110000010100101000000),
        Arguments.of(0b1111111111111111111111111111100, 0b111111111111111111111111111110),
        Arguments.of(0b11111111111111111111111111111101, 0b10111111111111111111111111111111));
  }
}
