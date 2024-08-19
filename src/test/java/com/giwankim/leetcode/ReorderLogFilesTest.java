package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReorderLogFilesTest {
  @ParameterizedTest
  @MethodSource
  void reorderLogFiles(String[] logs, String[] expected) {
    String[] actual = new ReorderLogFiles().reorderLogFiles(logs);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> reorderLogFiles() {
    return Stream.of(
        Arguments.of(
            new String[] {
              "dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"
            },
            new String[] {
              "let1 art can", "let3 art zero", "let2 own kit dig", "dig1 8 1 5 1", "dig2 3 6"
            }),
        Arguments.of(
            new String[] {"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"},
            new String[] {"g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"}));
  }
}
