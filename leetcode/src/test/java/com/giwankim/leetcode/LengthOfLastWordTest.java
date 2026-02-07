package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LengthOfLastWordTest {
  LengthOfLastWord sut = new LengthOfLastWord();

  @ParameterizedTest
  @CsvSource(
      value = {"Hello World:5", "luffy is still joyboy:6"},
      delimiter = ':')
  void lengthOfLastWord(String s, int expected) {
    int length = sut.lengthOfLastWord(s);
    assertThat(length).isEqualTo(expected);
  }

  @Test
  void leadingAndTrailingSpaces() {
    int length = sut.lengthOfLastWord("   fly me   to   the moon  ");
    assertThat(length).isEqualTo(4);
  }
}
