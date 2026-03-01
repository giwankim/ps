package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WordPatternTest {
  WordPattern sut = new WordPattern();

  @ParameterizedTest
  @CsvSource({"abcd, dog cat fox", "ab, dog cat fish"})
  void patternLengthAndNumberOfWordsAreNotTheSame(String pattern, String s) {
    assertThat(sut.wordPattern(pattern, s)).isFalse();
  }

  @Test
  void match() {
    String pattern = "abba";
    String s = "dog cat cat dog";
    assertThat(sut.wordPattern(pattern, s)).isTrue();
  }

  @ParameterizedTest
  @CsvSource({"aaaa, dog cat cat dog", "abba, dog cat cat fish"})
  void notEvenAFunction(String pattern, String s) {
    assertThat(sut.wordPattern(pattern, s)).isFalse();
  }

  @Test
  void notOneToOne() {
    String pattern = "abbc";
    String s = "dog cat cat dog";
    assertThat(sut.wordPattern(pattern, s)).isFalse();
  }
}
