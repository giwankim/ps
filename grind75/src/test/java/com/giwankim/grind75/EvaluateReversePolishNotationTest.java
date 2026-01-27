package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EvaluateReversePolishNotationTest {

  EvaluateReversePolishNotation sut = new EvaluateReversePolishNotation();

  @ParameterizedTest
  @MethodSource
  void binaryOperations(String[] tokens, int expected) {
    assertThat(sut.evalRPN(tokens)).isEqualTo(expected);
  }

  static Stream<Arguments> binaryOperations() {
    return Stream.of(
        Arguments.of(new String[] {"1", "2", "+"}, 3),
        Arguments.of(new String[] {"3", "1", "-"}, 2),
        Arguments.of(new String[] {"2", "3", "*"}, 6),
        Arguments.of(new String[] {"4", "2", "/"}, 2),
        Arguments.of(new String[] {"5", "2", "/"}, 2));
  }

  @ParameterizedTest
  @MethodSource
  void evalRPN(String[] tokens, int expected) {
    assertThat(sut.evalRPN(tokens)).isEqualTo(expected);
  }

  static Stream<Arguments> evalRPN() {
    return Stream.of(
        Arguments.of(new String[] {"2", "1", "+", "3", "*"}, 9),
        Arguments.of(new String[] {"4", "13", "5", "/", "+"}, 6),
        Arguments.of(
            new String[] {"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"},
            22));
  }
}
