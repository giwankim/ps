package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MinimumWindowSubstringTest {
  MinimumWindowSubstring sut = new MinimumWindowSubstring();

  @ParameterizedTest
  @CsvSource({"abc,def", "a,aa"})
  void noSuchWindow(String s, String t) {
    assertThat(sut.minWindow(s, t)).isEmpty();
  }

  @ParameterizedTest
  @CsvSource({"a,a", "abcde,ebdac"})
  void entireStringIsMinimumWindow(String s, String t) {
    assertThat(sut.minWindow(s, t)).isEqualTo(s);
  }

  @ParameterizedTest
  @CsvSource({"ADOBECODEBANC,ABC,BANC", "bdab,ab,ab", "aaaaaaaaaaaabbbbbcdd, abcdd, abbbbbcdd"})
  void minWindow(String s, String t, String expected) {
    assertThat(sut.minWindow(s, t)).isEqualTo(expected);
  }
}
