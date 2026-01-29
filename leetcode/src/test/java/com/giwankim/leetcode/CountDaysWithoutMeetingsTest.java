package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CountDaysWithoutMeetingsTest {

  @ParameterizedTest
  @MethodSource("cases")
  void countDays(int days, int[][] meetings, int expected) {
    int daysWithoutMeetings = new CountDaysWithoutMeetings().countDays(days, meetings);
    assertThat(daysWithoutMeetings).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(10, new int[][] {{5, 7}, {1, 3}, {9, 10}}, 2),
        Arguments.of(5, new int[][] {{2, 4}, {1, 3}}, 1),
        Arguments.of(6, new int[][] {{1, 6}}, 0),
        Arguments.of(8, new int[][] {{3, 4}, {4, 8}, {2, 5}, {3, 8}}, 1));
  }
}
