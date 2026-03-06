package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidParenthesesTest {
  ValidParentheses sut = new ValidParentheses();

  @ParameterizedTest
  @CsvSource({"(),true", "()[]{},true", "(],false", "([]),true", "],false"})
  void isValid(String s, boolean expected) {
    boolean actual = sut.isValid(s);
    assertThat(actual).isEqualTo(expected);
  }
}
