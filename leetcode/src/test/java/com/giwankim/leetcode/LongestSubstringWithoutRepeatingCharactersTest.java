package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class LongestSubstringWithoutRepeatingCharactersTest {
  LongestSubstringWithoutRepeatingCharacters sut = new LongestSubstringWithoutRepeatingCharacters();

  @Test
  void blankStrings() {
    int actual = sut.lengthOfLongestSubstring("");
    assertThat(actual).isZero();
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "bbbbb", " "})
  void lengthOne(String s) {
    int actual = sut.lengthOfLongestSubstring(s);
    assertThat(actual).isOne();
  }

  @ParameterizedTest
  @MethodSource
  void lengthOfLongestSubstring(String s, int expected) {
    int actual = sut.lengthOfLongestSubstring(s);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> lengthOfLongestSubstring() {
    return Stream.of(
        Arguments.of("abcabcbb", 3), Arguments.of("pwwkew", 3), Arguments.of("tmmzuxt", 5));
  }
}
