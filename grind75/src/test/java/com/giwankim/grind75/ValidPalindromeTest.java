package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidPalindromeTest {

  @ParameterizedTest
  @MethodSource
  void isPalindrome(String s, boolean expected) {
    assertThat(new ValidPalindrome().isPalindrome(s)).isEqualTo(expected);
  }

  static Stream<Arguments> isPalindrome() {
    return Stream.of(
        Arguments.of("A man, a plan, a canal: Panama", true),
        Arguments.of("race a car", false),
        Arguments.of(" ", true));
  }
}
