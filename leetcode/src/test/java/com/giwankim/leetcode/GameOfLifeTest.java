package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameOfLifeTest {
  GameOfLife sut = new GameOfLife();

  @Test
  void liveCellDiesFromUnderPopulation() {
    int[][] board = {{1, 0}, {0, 0}};
    int[][] expected = {{0, 0}, {0, 0}};
    sut.gameOfLife(board);
    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void liveCellLivesOnToTheNextGeneration() {
    int[][] board = {{1, 1}, {1, 0}};
    int[][] expected = {{1, 1}, {1, 1}};
    sut.gameOfLife(board);
    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void liveCellDiesFromOverPopulation() {
    int[][] board = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    int[][] expected = {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};
    sut.gameOfLife(board);
    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void deadCellWithExactlyThreeLiveNeighborsBecomesLiveCell() {
    int[][] board = {{0, 1}, {1, 1}};
    int[][] expected = {{1, 1}, {1, 1}};
    sut.gameOfLife(board);
    assertThat(board).isDeepEqualTo(expected);
  }

  @Test
  void gameOfLife() {
    int[][] board = {{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
    int[][] expected = {{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {0, 1, 0}};
    sut.gameOfLife(board);
    assertThat(board).isDeepEqualTo(expected);
  }
}
