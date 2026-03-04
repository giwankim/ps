package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeIntervalsTest {
  MergeIntervals sut = new MergeIntervals();

  @Test
  void oneInterval() {
    int[][] intervals = {{1, 2}};
    var actual = sut.merge(intervals);
    assertThat(actual).isDeepEqualTo(intervals);
  }

  @Test
  void mergeNonOverlapping() {
    int[][] intervals = {{4, 7}, {1, 2}};
    int[][] expected = {{1, 2}, {4, 7}};
    var actual = sut.merge(intervals);
    assertThat(actual).isDeepEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void mergeOverlapping(int[][] intervals, int[][] expected) {
    var actual = sut.merge(intervals);
    assertThat(actual).isDeepEqualTo(expected);
  }

  static Stream<Arguments> mergeOverlapping() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 4}, {4, 5}}, new int[][] {{1, 5}}),
        Arguments.of(new int[][] {{4, 7}, {1, 4}}, new int[][] {{1, 7}}));
  }

  @Test
  void mergeIntervals() {
    var intervals = new int[][] {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
    var expected = new int[][] {{1, 6}, {8, 10}, {15, 18}};
    var actual = sut.merge(intervals);
    assertThat(actual).isDeepEqualTo(expected);
  }
}
