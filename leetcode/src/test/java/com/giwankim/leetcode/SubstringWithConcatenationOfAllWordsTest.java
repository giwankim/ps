package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SubstringWithConcatenationOfAllWordsTest {
  SubstringWithConcatenationOfAllWords sut = new SubstringWithConcatenationOfAllWords();

  @Test
  void noConcatenatedSubstring() {
    String s = "wordgoodgoodgoodbestword";
    String[] words = {"word", "good", "best", "word"};
    var actual = sut.findSubstring(s, words);
    assertThat(actual).isEmpty();
  }

  @ParameterizedTest
  @MethodSource
  void findSubstrings(String s, String[] words, List<Integer> expected) {
    var actual = sut.findSubstring(s, words);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  static Stream<Arguments> findSubstrings() {
    return Stream.of(
        Arguments.of("barfoothefoobarman", new String[] {"foo", "bar"}, List.of(0, 9)),
        Arguments.of(
            "barfoofoobarthefoobarman", new String[] {"bar", "foo", "the"}, List.of(6, 9, 12)),
        Arguments.of(
            "lingmindraboofooowingdingbarrwingmonkeypoundcake",
            new String[] {"fooo", "barr", "wing", "ding", "wing"},
            List.of(13)));
  }
}
