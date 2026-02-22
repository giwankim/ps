package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IsomorphicStringsTest {
  IsomorphicStrings sut = new IsomorphicStrings();

  @ParameterizedTest
  @CsvSource({"egg,add", "paper,title"})
  void isomorphic(String s, String t) {
    boolean actual = sut.isIsomorphic(s, t);
    assertThat(actual).isTrue();
  }

  @ParameterizedTest
  @CsvSource({"f11,b23", "badc,baba", "ab,aa"})
  void notIsomorphic(String s, String t) {
    boolean actual = sut.isIsomorphic(s, t);
    assertThat(actual).isFalse();
  }
}
