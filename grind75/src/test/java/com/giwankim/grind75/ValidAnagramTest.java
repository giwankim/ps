package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidAnagramTest {

  @ParameterizedTest
  @MethodSource
  void isAnagram(String s, String t, boolean expected) {
    assertThat(new ValidAnagram().isAnagram(s, t)).isEqualTo(expected);
  }

  static Stream<Arguments> isAnagram() {
    return Stream.of(Arguments.of("anagram", "nagaram", true), Arguments.of("rat", "car", false));
  }
}
