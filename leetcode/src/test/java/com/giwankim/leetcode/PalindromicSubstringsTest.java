package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PalindromicSubstringsTest {

  @ParameterizedTest
  @CsvSource({"abc, 3", "aaa, 6"})
  void countSubstrings(String s, int expected) {
    int actual = new PalindromicSubstrings().countSubstrings(s);
    assertThat(actual).isEqualTo(expected);
  }
}
