package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WordSearchIITest {
  WordSearchII sut = new WordSearchII();

  @Test
  void returnsEmptyListWhenWordListIsEmpty() {
    char[][] board = {{'a'}};

    assertThat(sut.findWords(board, new String[] {})).isEmpty();
  }

  @Test
  void findsSingleLetterWordOnSingleCellBoard() {
    char[][] board = {{'a'}};

    assertThat(sut.findWords(board, new String[] {"a"})).containsExactly("a");
  }

  @Test
  void returnsEmptyWhenStartingLetterIsAbsentFromBoard() {
    char[][] board = {{'a'}};

    assertThat(sut.findWords(board, new String[] {"b"})).isEmpty();
  }

  @Test
  void doesNotRevisitASingleCellToSatisfyRepeatedLetter() {
    char[][] board = {{'a'}};

    assertThat(sut.findWords(board, new String[] {"aa"})).isEmpty();
  }

  @Test
  void findsWordFromHorizontalNeighbors() {
    char[][] board = {
      {'a', 'b'},
      {'c', 'd'}
    };

    assertThat(sut.findWords(board, new String[] {"ab"})).containsExactly("ab");
  }

  @Test
  void findsWordFromVerticalNeighbors() {
    char[][] board = {
      {'a', 'b'},
      {'c', 'd'}
    };

    assertThat(sut.findWords(board, new String[] {"ac"})).containsExactly("ac");
  }

  @Test
  void findsWordRequiringLeftwardTraversal() {
    char[][] board = {{'a', 'b'}};

    assertThat(sut.findWords(board, new String[] {"ba"})).containsExactly("ba");
  }

  @Test
  void findsWordOnNonSquareBoard() {
    char[][] board = {{'a', 'b', 'c'}};

    assertThat(sut.findWords(board, new String[] {"abc"})).containsExactly("abc");
  }

  @Test
  void doesNotConsiderDiagonalCellsAdjacent() {
    char[][] board = {
      {'a', 'b'},
      {'c', 'd'}
    };

    assertThat(sut.findWords(board, new String[] {"ad"})).isEmpty();
  }

  @Test
  void returnsEmptyWhenWordIsLongerThanBoardCellCount() {
    char[][] board = {{'a', 'b'}};

    assertThat(sut.findWords(board, new String[] {"abcde"})).isEmpty();
  }

  @Test
  void findsMultipleWordsThatSharePrefixes() {
    char[][] board = {
      {'o', 'a'},
      {'h', 't'}
    };

    assertThat(sut.findWords(board, new String[] {"oa", "oat", "oath", "hat"}))
        .containsExactlyInAnyOrder("oa", "oat", "oath");
  }

  @Test
  void findsBothSiblingWordsWhenTheyShareAPrefix() {
    char[][] board = {
      {'a', 'b'},
      {'c', 'd'}
    };

    assertThat(sut.findWords(board, new String[] {"ab", "ac"}))
        .containsExactlyInAnyOrder("ab", "ac");
  }

  @Test
  void findsBothWordsEvenWhenCellStateMustResetBetweenSearches() {
    char[][] board = {
      {'a', 'b'},
      {'a', 'b'}
    };

    assertThat(sut.findWords(board, new String[] {"ab", "ba"}))
        .containsExactlyInAnyOrder("ab", "ba");
  }

  @Test
  void doesNotReuseASingleCellWithinOneWord() {
    char[][] board = {
      {'a', 'b'},
      {'c', 'd'}
    };

    assertThat(sut.findWords(board, new String[] {"aba"})).isEmpty();
  }

  @Test
  void findsWordWhosePathZigzagsAcrossRows() {
    char[][] board = {
      {'a', 'b'},
      {'d', 'c'}
    };

    assertThat(sut.findWords(board, new String[] {"abcd"})).containsExactly("abcd");
  }

  @Test
  void findsCanonicalLeetCodeExampleMatches() {
    char[][] board = {
      {'o', 'a', 'a', 'n'},
      {'e', 't', 'a', 'e'},
      {'i', 'h', 'k', 'r'},
      {'i', 'f', 'l', 'v'}
    };

    assertThat(sut.findWords(board, new String[] {"oath", "pea", "eat", "rain"}))
        .containsExactlyInAnyOrder("oath", "eat");
  }

  @Test
  void returnsEachFoundWordOnlyOnceEvenWhenMultiplePathsExist() {
    char[][] board = {
      {'a', 'a'},
      {'a', 'a'}
    };

    assertThat(sut.findWords(board, new String[] {"aa"})).containsExactly("aa");
  }
}
