package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IsSubsequenceTest {
  IsSubsequence sut = new IsSubsequence();

  @ParameterizedTest
  @CsvSource({"abc,ahbgdc,true", "axc,ahbgdc,false"})
  void isSubsequence(String s, String t, boolean expected) {
    boolean actual = sut.isSubsequence(s, t);
    assertThat(actual).isEqualTo(expected);
  }
}
