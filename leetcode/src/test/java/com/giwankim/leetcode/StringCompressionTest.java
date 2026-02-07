package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StringCompressionTest {
  StringCompression sut = new StringCompression();

  @Test
  void groupLengthOne() {
    char[] chars = {'a'};
    int length = sut.compress(chars);
    assertThat(length).isEqualTo(1);
    assertThat(Arrays.copyOf(chars, length)).containsExactly('a');
  }

  @Test
  void groupLengthSingleDigit() {
    char[] chars = {'a', 'a', 'b', 'b', 'c', 'c', 'c'};
    int length = sut.compress(chars);
    assertThat(length).isEqualTo(6);
    assertThat(Arrays.copyOf(chars, length)).containsExactly('a', '2', 'b', '2', 'c', '3');
  }

  @Test
  void groupLengthMultipleDigits() {
    char[] chars = {'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'};
    int length = sut.compress(chars);
    assertThat(length).isEqualTo(4);
    assertThat(Arrays.copyOf(chars, length)).containsExactly('a', 'b', '1', '2');
  }
}
