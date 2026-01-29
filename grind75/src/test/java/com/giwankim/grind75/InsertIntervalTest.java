package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InsertIntervalTest {

  InsertInterval sut = new InsertInterval();

  @Test
  void insertIntoEmpty() {
    int[][] intervals = {};
    int[] newInterval = {1, 2};
    int[][] expected = {{1, 2}};

    int[][] actual = sut.insert(intervals, newInterval);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void insertBeginning() {
    int[][] intervals = {{3, 4}};
    int[] newInterval = {1, 2};
    int[][] expected = {{1, 2}, {3, 4}};

    int[][] actual = sut.insert(intervals, newInterval);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void insertMiddle() {
    int[][] intervals = {{1, 2}, {5, 6}};
    int[] newInterval = {3, 4};
    int[][] expected = {{1, 2}, {3, 4}, {5, 6}};

    int[][] actual = sut.insert(intervals, newInterval);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void insertEnd() {
    int[][] intervals = {{1, 2}};
    int[] newInterval = {3, 4};
    int[][] expected = {{1, 2}, {3, 4}};

    int[][] actual = sut.insert(intervals, newInterval);

    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void insertMiddleOverlap(int[][] intervals, int[] newInterval, int[][] expected) {
    int[][] actual = sut.insert(intervals, newInterval);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> insertMiddleOverlap() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 3}, {6, 9}}, new int[] {2, 5}, new int[][] {{1, 5}, {6, 9}}),
        Arguments.of(
            new int[][] {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}},
            new int[] {4, 8},
            new int[][] {{1, 2}, {3, 10}, {12, 16}}));
  }
}
