package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximalSquareTest {
  MaximalSquare sut = new MaximalSquare();

  @Test
  void leetCodeExample3SingleZeroCellHasNoSquare() {
    char[][] matrix = {{'0'}};
    assertThat(sut.maximalSquare(matrix)).isZero();
  }

  @Test
  void singleOneCellIsUnitSquare() {
    char[][] matrix = {{'1'}};
    assertThat(sut.maximalSquare(matrix)).isEqualTo(1);
  }

  @Test
  void leetCodeExample2NoAdjacentOnesGivesUnitSquare() {
    char[][] matrix = {
      {'0', '1'},
      {'1', '0'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(1);
  }

  @Test
  void leetCodeExample1FindsTwoByTwoSquare() {
    char[][] matrix = {
      {'1', '0', '1', '0', '0'},
      {'1', '0', '1', '1', '1'},
      {'1', '1', '1', '1', '1'},
      {'1', '0', '0', '1', '0'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(4);
  }

  @Test
  void allZerosHasNoSquare() {
    char[][] matrix = {
      {'0', '0', '0'},
      {'0', '0', '0'},
      {'0', '0', '0'}
    };
    assertThat(sut.maximalSquare(matrix)).isZero();
  }

  @Test
  void fullGridOfOnesIsOneBigSquare() {
    char[][] matrix = {
      {'1', '1', '1'},
      {'1', '1', '1'},
      {'1', '1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(9);
  }

  @Test
  void twoByTwoBlockOfOnes() {
    char[][] matrix = {
      {'1', '1'},
      {'1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(4);
  }

  @Test
  void singleRowOfOnesCapsSideAtOne() {
    char[][] matrix = {{'1', '1', '1', '1'}};
    assertThat(sut.maximalSquare(matrix)).isEqualTo(1);
  }

  @Test
  void singleColumnOfOnesCapsSideAtOne() {
    char[][] matrix = {{'1'}, {'1'}, {'1'}};
    assertThat(sut.maximalSquare(matrix)).isEqualTo(1);
  }

  @Test
  void wideRectangleOfOnesLimitedByHeight() {
    char[][] matrix = {
      {'1', '1', '1', '1'},
      {'1', '1', '1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(4);
  }

  @Test
  void tallRectangleOfOnesLimitedByWidth() {
    char[][] matrix = {
      {'1', '1'},
      {'1', '1'},
      {'1', '1'},
      {'1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(4);
  }

  @Test
  void holeInCenterShrinksSquareToOne() {
    char[][] matrix = {
      {'1', '1', '1'},
      {'1', '0', '1'},
      {'1', '1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(1);
  }

  @Test
  void picksLargerOfTwoSeparateSquares() {
    char[][] matrix = {
      {'1', '1', '0', '0', '0'},
      {'1', '1', '0', '1', '1'},
      {'0', '0', '1', '1', '1'},
      {'0', '0', '1', '1', '1'},
      {'0', '0', '1', '1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(9);
  }

  @Test
  void squareAnchoredInBottomRightCorner() {
    char[][] matrix = {
      {'0', '0', '0', '0'},
      {'0', '0', '0', '0'},
      {'0', '0', '1', '1'},
      {'0', '0', '1', '1'}
    };
    assertThat(sut.maximalSquare(matrix)).isEqualTo(4);
  }
}
