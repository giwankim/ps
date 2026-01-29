package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CorporateFlightBookingsTest {
  private CorporateFlightBookings sut;

  @BeforeEach
  void setUp() {
    sut = new CorporateFlightBookings();
  }

  @ParameterizedTest
  @MethodSource
  void corpFlightBookings(int[][] bookings, int n, int[] expected) {
    int[] actual = sut.corpFlightBookings(bookings, n);
    assertThat(actual).containsExactly(expected);
  }

  static Stream<Arguments> corpFlightBookings() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 2, 10}, {2, 2, 15}}, 2, new int[] {10, 25}),
        Arguments.of(
            new int[][] {{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5, new int[] {10, 55, 45, 25, 25}));
  }
}
