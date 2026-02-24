package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RotateImageTest {
  RotateImage sut = new RotateImage();

  @Test
  void oneByOne() {
    var matrix = new int[][] {{1}};
    var expected = new int[][] {{1}};
    sut.rotate(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void twoByTwo() {
    var matrix = new int[][] {{1, 2}, {3, 4}};
    var expected = new int[][] {{3, 1}, {4, 2}};
    sut.rotate(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void threeByThree() {
    var matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    var expected = new int[][] {{7, 4, 1}, {8, 5, 2}, {9, 6, 3}};
    sut.rotate(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }

  @Test
  void fourByFour() {
    var matrix = new int[][] {{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
    var expected = new int[][] {{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}};
    sut.rotate(matrix);
    assertThat(matrix).isDeepEqualTo(expected);
  }
}
