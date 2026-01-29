package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MeetingRoomsTest {

  @ParameterizedTest
  @MethodSource
  void canAttendMeetings(int[][] intervals, boolean expected) {
    boolean actual = new MeetingRooms().canAttendMeetings(intervals);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> canAttendMeetings() {
    return Stream.of(
        Arguments.of(new int[][] {{0, 30}, {5, 10}, {15, 20}}, false),
        Arguments.of(new int[][] {{7, 10}, {2, 4}}, true));
  }
}
