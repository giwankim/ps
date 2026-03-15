package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BasicCalculatorTest {
  BasicCalculator sut = new BasicCalculator();

  @ParameterizedTest
  @CsvSource({"42, 42", "0, 0"})
  void singleNumber(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }

  @Test
  void leadingAndTrailingSpaces() {
    assertThat(sut.calculate(" 3 ")).isEqualTo(3);
  }

  @Test
  void irregularSpacing() {
    assertThat(sut.calculate("2-1 + 2 ")).isEqualTo(3);
  }

  @ParameterizedTest
  @CsvSource({"1 + 1, 2", "2 - 1, 1", "123 + 45, 168"})
  void binaryOperations(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"1 + 2 + 3, 6", "5 - 3 - 1, 1", "5 - 5, 0"})
  void chainedOperations(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"-1, -1", "-(2 + 3), -5"})
  void unaryOperation(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"1 + (3 - 2), 2", "3 - (2 - 1), 2", "(3 - 1) - 2, 0", "(1+(4+5+2)-3)+(6+8), 23"})
  void parentheses(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({"(1 + 2), 3", "((1 + 2)), 3", "1 - (-2), 3", "-(3 + (4 - 1)), -6"})
  void parenthesesEdgeCases(String s, int expected) {
    assertThat(sut.calculate(s)).isEqualTo(expected);
  }
}
