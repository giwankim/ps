package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GenerateParenthesesTest {
  @ParameterizedTest
  @MethodSource
  void generateParentheses(int n, List<String> expected) {
    List<String> actual = new GenerateParentheses().generateParenthesis(n);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> generateParentheses() {
    return Stream.of(
        Arguments.of(3, List.of("((()))", "(()())", "(())()", "()(())", "()()()")),
        Arguments.of(1, List.of("()")));
  }
}
