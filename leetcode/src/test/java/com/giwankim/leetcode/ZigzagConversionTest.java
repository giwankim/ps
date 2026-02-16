package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ZigzagConversionTest {
  ZigzagConversion sut = new ZigzagConversion();

  @Test
  void singleRow() {
    String actual = sut.convert("A", 1);
    assertThat(actual).isEqualTo("A");
  }

  @ParameterizedTest
  @CsvSource(
      value = {"PAYPALISHIRING:3:PAHNAPLSIIGYIR", "PAYPALISHIRING:4:PINALSIGYAHRPI"},
      delimiter = ':')
  void convert(String s, int numRows, String expected) {
    String actual = sut.convert(s, numRows);
    assertThat(actual).isEqualTo(expected);
  }
}
