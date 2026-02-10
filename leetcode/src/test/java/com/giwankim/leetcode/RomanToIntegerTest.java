package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RomanToIntegerTest {
  RomanToInteger sut = new RomanToInteger();

  @ParameterizedTest
  @CsvSource({"III, 3", "LVIII, 58"})
  void leftToRight(String s, int expected) {
    int actual = sut.romanToInt(s);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"IV, 4", "IX, 9", "XL, 40", "XC, 90", "CD, 400", "CM, 900", "MCMXCIV, 1994"})
  void useSubtraction(String s, int expected) {
    int actual = sut.romanToInt(s);
    assertThat(actual).isEqualTo(expected);
  }
}
