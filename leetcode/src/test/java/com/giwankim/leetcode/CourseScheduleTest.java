package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CourseScheduleTest {

  @ParameterizedTest
  @MethodSource
  void canFinish(int numCourses, int[][] prerequisites, boolean expected) {
    boolean actual = new CourseSchedule().canFinish(numCourses, prerequisites);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> canFinish() {
    return Stream.of(
        Arguments.of(2, new int[][] {{1, 0}}, true),
        Arguments.of(2, new int[][] {{1, 0}, {0, 1}}, false));
  }
}
