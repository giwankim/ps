package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidPalindromeTest {
  ValidPalindrome sut = new ValidPalindrome();

  @ParameterizedTest
  @MethodSource
  void isPalindrome(String s, boolean expected) {
    assertThat(sut.isPalindrome(s)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("isPalindrome")
  void isPalindrome2(String s, boolean expected) {
    assertThat(sut.isPalindrome2(s)).isEqualTo(expected);
  }

  public static Stream<Arguments> isPalindrome() {
    return Stream.of(
        Arguments.of("A man, a plan, a canal: Panama", true),
        Arguments.of("race a car", false),
        Arguments.of(" ", true),
        Arguments.of("Do geese see God?", true),
        Arguments.of("Hannah", true),
        Arguments.of("Hang up!", false));
  }
}
