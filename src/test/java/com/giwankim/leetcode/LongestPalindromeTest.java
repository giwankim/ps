package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestPalindromeTest {
  @ParameterizedTest
  @MethodSource
  void longestPalindrome(String s, String expected) {
    String actual = new LongestPalindrome().longestPalindrome(s);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> longestPalindrome() {
    return Stream.of(
        Arguments.of("a", "a"),
        Arguments.of("babad", "bab"),
        Arguments.of("cbbd", "bb"));
  }
}
