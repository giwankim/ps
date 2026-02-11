package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IntegerToRomanTest {
  IntegerToRoman sut = new IntegerToRoman();

  @ParameterizedTest
  @CsvSource({"3, III", "12, XII", "112, CXII", "1321, MCCCXXI"})
  void noSubtraction(int num, String expected) {
    String actual = sut.intToRoman(num);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"6, VI", "17, XVII", "58, LVIII", "157, CLVII", "700, DCC"})
  void fives(int num, String expected) {
    String actual = sut.intToRoman(num);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
    "4, IV",
    "9, IX",
    "49, XLIX",
    "400, CD",
    "900, CM",
    "1994, MCMXCIV",
    "3749, MMMDCCXLIX"
  })
  void subtraction(int num, String expected) {
    String actual = sut.intToRoman(num);
    assertThat(actual).isEqualTo(expected);
  }
}
