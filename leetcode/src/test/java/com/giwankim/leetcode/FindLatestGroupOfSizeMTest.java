package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindLatestGroupOfSizeMTest {

  FindLatestGroupOfSizeM sut = new FindLatestGroupOfSizeM();

  @Test
  void findLatestStep() {
    int[] arr = {3, 5, 1, 2, 4};
    int m = 1;
    int expected = 4;

    int actual = sut.findLatestStep(arr, m);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void stepDoesNotExist() {
    int[] arr = {3, 1, 5, 4, 2};
    int m = 2;
    int expected = -1;

    int actual = sut.findLatestStep(arr, m);

    assertThat(actual).isEqualTo(expected);
  }
}
