package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MostCommonWordTest {
  @ParameterizedTest
  @MethodSource
  void mostCommonWord(String paragraph, String[] banned, String expected) {
    String actual = new MostCommonWord().mostCommonWord(paragraph, banned);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> mostCommonWord() {
    return Stream.of(
        Arguments.of("a.", new String[] {}, "a"),
        Arguments.of(
            "Bob hit a ball, the hit BALL flew far after it was hit.",
            new String[] {"hit"},
            "ball"));
  }
}
