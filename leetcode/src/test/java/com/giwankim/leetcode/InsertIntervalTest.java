package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InsertIntervalTest {
  InsertInterval sut = new InsertInterval();

  @Test
  void intervalsEmpty() {
    int[][] intervals = {};
    int[] newInterval = {5, 7};
    int[][] expected = {{5, 7}};
    var actual = sut.insert(intervals, newInterval);
    assertThat(actual).isDeepEqualTo(expected);
  }

  @Test
  void insertNonOverlappingNewInterval() {
    int[][] intervals = {{1, 3}, {6, 9}};
    int[] newInterval = {4, 5};
    int[][] expected = {{1, 3}, {4, 5}, {6, 9}};
    var actual = sut.insert(intervals, newInterval);
    assertThat(actual).isDeepEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void insertOverlappingNewInterval(int[][] intervals, int[] newInterval, int[][] expected) {
    var actual = sut.insert(intervals, newInterval);
    assertThat(actual).isDeepEqualTo(expected);
  }

  static Stream<Arguments> insertOverlappingNewInterval() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 3}, {6, 9}}, new int[] {2, 5}, new int[][] {{1, 5}, {6, 9}}),
        Arguments.of(new int[][] {{1, 5}}, new int[] {2, 7}, new int[][] {{1, 7}}));
  }

  @Test
  void multipleOverlaps() {
    int[][] intervals = {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}};
    int[] newInterval = {4, 8};
    int[][] expected = {{1, 2}, {3, 10}, {12, 16}};
    var actual = sut.insert(intervals, newInterval);
    assertThat(actual).isDeepEqualTo(expected);
  }
}
