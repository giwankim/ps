package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CombinationsTest {
  private Combinations sut;

  @BeforeEach
  void setUp() {
    sut = new Combinations();
  }

  @Test
  void singleElement() {
    assertThat(sut.combine(1, 1)).containsExactlyInAnyOrder(List.of(1));
  }

  @Test
  void allElements() {
    assertThat(sut.combine(4, 4)).containsExactlyInAnyOrder(List.of(1, 2, 3, 4));
  }

  @Test
  void singletons() {
    assertThat(sut.combine(4, 1))
        .containsExactlyInAnyOrder(List.of(1), List.of(2), List.of(3), List.of(4));
  }

  @ParameterizedTest
  @MethodSource
  void combine(int n, int k, List<List<Integer>> expected) {
    assertThat(sut.combine(n, k)).containsExactlyInAnyOrderElementsOf(expected);
  }

  static Stream<Arguments> combine() {
    return Stream.of(
        Arguments.of(
            4,
            2,
            List.of(
                List.of(1, 2),
                List.of(1, 3),
                List.of(1, 4),
                List.of(2, 3),
                List.of(2, 4),
                List.of(3, 4))),
        Arguments.of(3, 2, List.of(List.of(1, 2), List.of(1, 3), List.of(2, 3))),
        Arguments.of(
            5,
            3,
            List.of(
                List.of(1, 2, 3),
                List.of(1, 2, 4),
                List.of(1, 2, 5),
                List.of(1, 3, 4),
                List.of(1, 3, 5),
                List.of(1, 4, 5),
                List.of(2, 3, 4),
                List.of(2, 3, 5),
                List.of(2, 4, 5),
                List.of(3, 4, 5))));
  }
}
