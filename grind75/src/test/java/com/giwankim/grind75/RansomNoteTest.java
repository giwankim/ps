package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RansomNoteTest {

  @ParameterizedTest
  @MethodSource("cases")
  void canConstruct(String ransomNote, String magazine, boolean expected) {
    boolean actual = new RansomNote().canConstruct(ransomNote, magazine);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of("a", "b", false),
        Arguments.of("aa", "ab", false),
        Arguments.of("aa", "aab", true));
  }
}
