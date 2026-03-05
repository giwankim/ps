package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AddBinaryTest {
  AddBinary sut = new AddBinary();

  @ParameterizedTest
  @CsvSource({"11,1,100", "1010,1011,10101"})
  void addBinary(String a, String b, String expected) {
    String actual = sut.addBinary(a, b);
    assertThat(actual).isEqualTo(expected);
  }
}
