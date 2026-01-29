package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RearrangeStringKDistanceApartTest {
  @ParameterizedTest
  @MethodSource
  void rearrangeString(String s, int k, String expected) {
    var sut = new RearrangeStringKDistanceApart();
    String actual = sut.rearrangeString(s, k);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> rearrangeString() {
    return Stream.of(
        Arguments.of("aabbcc", 3, "abcabc"),
        Arguments.of("aaabc", 3, ""),
        Arguments.of("aaadbbcc", 2, "abacabcd"));
  }
}
