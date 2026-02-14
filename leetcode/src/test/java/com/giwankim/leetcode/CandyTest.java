package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CandyTest {
  Candy sut = new Candy();

  @Test
  void constant() {
    int[] ratings = {1, 1, 1, 1, 1};
    int expected = 5;
    int actual = sut.candy(ratings);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void increasing() {
    int[] ratings = {1, 2, 3, 4, 5};
    int expected = 15;
    int actual = sut.candy(ratings);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void decreasing() {
    int[] ratings = {5, 4, 3, 2, 1};
    int expected = 15;
    int actual = sut.candy(ratings);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void increasingThenDecreasing() {
    int[] ratings = {1, 2, 3, 2, 1};
    int expected = 9;
    int actual = sut.candy(ratings);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void candy(int[] ratings, int expected) {
    int actual = sut.candy(ratings);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> candy() {
    return Stream.of(
        Arguments.of(new int[] {1, 0, 2}, 5),
        Arguments.of(new int[] {1, 2, 2}, 4),
        Arguments.of(new int[] {1, 3, 4, 5, 2}, 11));
  }
}
