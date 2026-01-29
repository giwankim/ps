package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WordBreakTest {

  @ParameterizedTest
  @MethodSource
  void wordBreak(String s, List<String> wordDict, boolean expected) {
    boolean actual = new WordBreak().wordBreak(s, wordDict);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> wordBreak() {
    return Stream.of(
        Arguments.of("leetcode", List.of("leet", "code"), true),
        Arguments.of("applepenapple", List.of("apple", "pen"), true),
        Arguments.of("catsandog", List.of("cats", "dog", "sand", "and", "cat"), false));
  }
}
