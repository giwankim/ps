package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThreeSumTest {
  ThreeSum sut = new ThreeSum();

  @Test
  void emptyTriplet() {
    int[] nums = {0, 1, 1};
    List<List<Integer>> actual = sut.threeSum(nums);
    assertThat(actual).isEmpty();
  }

  @ParameterizedTest
  @MethodSource
  void onlySolution(int[] nums, List<List<Integer>> expected) {
    List<List<Integer>> actual = sut.threeSum(nums);
    assertThat(canonicalize(actual)).containsExactlyInAnyOrderElementsOf(canonicalize(expected));
  }

  static Stream<Arguments> onlySolution() {
    return Stream.of(
        Arguments.of(new int[] {-1, 0, 1}, List.of(List.of(-1, 0, 1))),
        Arguments.of(new int[] {0, 0, 0}, List.of(List.of(0, 0, 0))));
  }

  @ParameterizedTest
  @MethodSource
  void multipleSolutions(int[] nums, List<List<Integer>> expected) {
    List<List<Integer>> actual = sut.threeSum(nums);
    assertThat(canonicalize(actual)).containsExactlyInAnyOrderElementsOf(canonicalize(expected));
  }

  static Stream<Arguments> multipleSolutions() {
    return Stream.of(
        Arguments.of(new int[] {-2, -1, 0, 1, 2}, List.of(List.of(-2, 0, 2), List.of(-1, 0, 1))),
        Arguments.of(
            new int[] {-2, -1, 0, 1, 2, 4}, List.of(List.of(-2, 0, 2), List.of(-1, 0, 1))));
  }

  @ParameterizedTest
  @MethodSource
  void duplicateElements(int[] nums, List<List<Integer>> expected) {
    List<List<Integer>> actual = sut.threeSum(nums);
    assertThat(canonicalize(actual)).containsExactlyInAnyOrderElementsOf(canonicalize(expected));
  }

  static Stream<Arguments> duplicateElements() {
    return Stream.of(
        Arguments.of(new int[] {-1, -1, 0, 1, 2}, List.of(List.of(-1, -1, 2), List.of(-1, 0, 1))),
        Arguments.of(
            new int[] {-1, 0, 1, 2, -1, 4}, List.of(List.of(-1, -1, 2), List.of(-1, 0, 1))),
        Arguments.of(new int[] {-2, 0, 1, 1, 2}, List.of(List.of(-2, 0, 2), List.of(-2, 1, 1))));
  }

  private static List<List<Integer>> canonicalize(List<List<Integer>> triplets) {
    return triplets.stream().map(t -> t.stream().sorted().toList()).toList();
  }
}
