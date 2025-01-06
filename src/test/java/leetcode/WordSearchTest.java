package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WordSearchTest {
  @ParameterizedTest
  @MethodSource
  void exist(char[][] board, String word, boolean expected) {
    WordSearch sut = new WordSearch();
    boolean actual = sut.exist(board, word);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> exist() {
    return Stream.of(
        Arguments.of(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "ABCCED",
            true),
        Arguments.of(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "SEE",
            true),
        Arguments.of(
            new char[][] {
              {'A', 'B', 'C', 'E'},
              {'S', 'F', 'C', 'S'},
              {'A', 'D', 'E', 'E'}
            },
            "ABCB",
            false));
  }
}
