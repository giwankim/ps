package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestPalindromeTest {

  @ParameterizedTest
  @MethodSource
  void longestPalindrome(String s, int expected) {
    int actual = new LongestPalindrome().longestPalindrome(s);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> longestPalindrome() {
    return Stream.of(Arguments.of("abccccdd", 7), Arguments.of("a", 1));
  }
}
