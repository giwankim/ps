package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class SpiralMatrixTest {
  SpiralMatrix sut = new SpiralMatrix();

  @Test
  void oneByOne() {
    int[][] matrix = {{1}};
    List<Integer> actual = sut.spiralOrder(matrix);
    assertThat(actual).isEqualTo(List.of(1));
  }

  @Test
  void row() {
    int[][] matrix = {{1, 2, 3}};
    List<Integer> actual = sut.spiralOrder(matrix);
    assertThat(actual).isEqualTo(List.of(1, 2, 3));
  }

  @Test
  void column() {
    int[][] matrix = {{1}, {2}, {3}};
    List<Integer> actual = sut.spiralOrder(matrix);
    assertThat(actual).isEqualTo(List.of(1, 2, 3));
  }

  @Test
  void threeByThreeSquare() {
    int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    List<Integer> actual = sut.spiralOrder(matrix);
    assertThat(actual).isEqualTo(List.of(1, 2, 3, 6, 9, 8, 7, 4, 5));
  }

  @Test
  void threeByFourRectangle() {
    int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
    List<Integer> actual = sut.spiralOrder(matrix);
    assertThat(actual).isEqualTo(List.of(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7));
  }
}
