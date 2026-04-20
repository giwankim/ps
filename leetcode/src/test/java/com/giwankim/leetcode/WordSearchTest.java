package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WordSearchTest {
  WordSearch sut = new WordSearch();

  @Test
  void returnsFalseWhenStartingLetterIsAbsent() {
    assertThat(sut.exist(new char[][] {{'A'}}, "B")).isFalse();
  }

  @Test
  void returnsTrueForEmptyWordOnNonEmptyBoard() {
    assertThat(sut.exist(new char[][] {{'A'}}, "")).isTrue();
  }

  @Test
  void findsSingleLetterWordOnSingleCellBoard() {
    assertThat(sut.exist(new char[][] {{'A'}}, "A")).isTrue();
  }

  @Test
  void findsWordFromHorizontalNeighbors() {
    assertThat(sut.exist(new char[][] {{'A', 'B'}}, "AB")).isTrue();
  }

  @Test
  void findsWordThatRequiresTraversingLeftward() {
    assertThat(sut.exist(new char[][] {{'A', 'B', 'C'}}, "CBA")).isTrue();
  }

  @Test
  void findsWordFromVerticalNeighbors() {
    assertThat(sut.exist(new char[][] {{'A'}, {'B'}}, "AB")).isTrue();
  }

  @Test
  void findsWordThatRequiresTraversingUpward() {
    assertThat(sut.exist(new char[][] {{'A'}, {'B'}, {'C'}}, "CBA")).isTrue();
  }

  @Test
  void findsWordWhosePathTurnsAtACorner() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B'},
              {'D', 'C'}
            },
            "ABCD"))
        .isTrue();
  }

  @Test
  void doesNotTreatDiagonalCellsAsAdjacent() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B'},
              {'C', 'D'}
            },
            "AD"))
        .isFalse();
  }

  @Test
  void doesNotReuseASingleCellToMatchRepeatedLetters() {
    assertThat(sut.exist(new char[][] {{'A'}}, "AA")).isFalse();
  }

  @Test
  void rejectsWordLongerThanTotalNumberOfCells() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B'},
              {'C', 'D'}
            },
            "ABCDE"))
        .isFalse();
  }

  @Test
  void findsWordSpanningEveryCellOfASingleRowBoard() {
    assertThat(sut.exist(new char[][] {{'H', 'E', 'L', 'L', 'O'}}, "HELLO")).isTrue();
  }

  @Test
  void findsWordSpanningEveryCellOfASingleColumnBoard() {
    assertThat(sut.exist(new char[][] {{'H'}, {'E'}, {'L'}, {'L'}, {'O'}}, "HELLO"))
        .isTrue();
  }

  @Test
  void retriesFromAlternateStartingCellWhenFirstCandidateDeadEnds() {
    assertThat(sut.exist(new char[][] {{'A', 'X', 'A', 'B'}}, "AB")).isTrue();
  }

  @Test
  void findsRepeatedLetterWordByZigzaggingBetweenCells() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B'},
              {'B', 'A'}
            },
            "ABAB"))
        .isTrue();
  }

  @Test
  void backtracksWhenAnEarlyBranchDeadEnds() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'E', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "ABCESEEEFS"))
        .isTrue();
  }

  @Test
  void doesNotMutateBoardDuringSearch() {
    char[][] board = {
      {'A', 'B', 'C', 'E'},
      {'S', 'F', 'C', 'S'},
      {'A', 'D', 'E', 'E'}
    };
    sut.exist(board, "ABCCEF");
    assertThat(board).isDeepEqualTo(new char[][] {
      {'A', 'B', 'C', 'E'},
      {'S', 'F', 'C', 'S'},
      {'A', 'D', 'E', 'E'}
    });
  }

  @Test
  void findsCanonicalLeetCodeExample() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "ABCCED"))
        .isTrue();
  }

  @Test
  void findsCanonicalLeetCodeAlternativePath() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "SEE"))
        .isTrue();
  }

  @Test
  void rejectsCanonicalLeetCodeNegativeCase() {
    assertThat(sut.exist(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "ABCB"))
        .isFalse();
  }
}
