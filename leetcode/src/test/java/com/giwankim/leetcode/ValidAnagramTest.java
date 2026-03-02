package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidAnagramTest {
  ValidAnagram sut = new ValidAnagram();

  @ParameterizedTest
  @CsvSource({"anagram, nagaram, true", "rat, car, false"})
  void isAnagram(String s, String t, boolean expected) {
    boolean actual = sut.isAnagram(s, t);
    assertThat(actual).isEqualTo(expected);
  }
}
