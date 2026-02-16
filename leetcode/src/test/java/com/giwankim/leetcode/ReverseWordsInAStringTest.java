package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReverseWordsInAStringTest {
  ReverseWordsInAString sut = new ReverseWordsInAString();

  @Test
  void regularlySpaced() {
    String s = "the sky is blue";
    String expected = "blue is sky the";
    String actual = sut.reverseWords(s);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void leadingAndTrailingSpaces() {
    String s = "  hello world  ";
    String expected = "world hello";
    String actual = sut.reverseWords(s);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void multipleSpacesBetweenWords() {
    String s = "a good   example";
    String expected = "example good a";
    String actual = sut.reverseWords(s);
    assertThat(actual).isEqualTo(expected);
  }
}
