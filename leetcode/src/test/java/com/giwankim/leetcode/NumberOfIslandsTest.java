package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOfIslandsTest {
  NumberOfIslands sut = new NumberOfIslands();

  @Test
  void allWater() {
    char[][] grid = {
      {'0', '0'},
      {'0', '0'}
    };
    assertThat(sut.numIslands(grid)).isZero();
  }

  @Test
  void singleLandCell() {
    char[][] grid = {{'1'}};
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void singleWaterCell() {
    char[][] grid = {{'0'}};
    assertThat(sut.numIslands(grid)).isZero();
  }

  @Test
  void entireGridIsOneIsland() {
    char[][] grid = {
      {'1', '1'},
      {'1', '1'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void horizontallyConnected() {
    char[][] grid = {{'1', '1', '1'}};
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void verticallyConnected() {
    char[][] grid = {{'1'}, {'1'}, {'1'}};
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void diagonalCellsAreNotConnected() {
    char[][] grid = {
      {'1', '0'},
      {'0', '1'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(2);
  }

  @Test
  void fourIsolatedCorners() {
    char[][] grid = {
      {'1', '0', '1'},
      {'0', '0', '0'},
      {'1', '0', '1'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(4);
  }

  @Test
  void lShapedIsland() {
    char[][] grid = {
      {'1', '0'},
      {'1', '0'},
      {'1', '1'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void leetCodeExample1() {
    char[][] grid = {
      {'1', '1', '1', '1', '0'},
      {'1', '1', '0', '1', '0'},
      {'1', '1', '0', '0', '0'},
      {'0', '0', '0', '0', '0'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(1);
  }

  @Test
  void leetCodeExample2() {
    char[][] grid = {
      {'1', '1', '0', '0', '0'},
      {'1', '1', '0', '0', '0'},
      {'0', '0', '1', '0', '0'},
      {'0', '0', '0', '1', '1'}
    };
    assertThat(sut.numIslands(grid)).isEqualTo(3);
  }
}
