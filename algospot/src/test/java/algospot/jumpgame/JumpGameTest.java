package algospot.jumpgame;

import static algospot.jumpgame.Main.jump;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JumpGameTest {

  @ParameterizedTest
  @MethodSource("cases")
  void jumpGame(int n, int[][] board, int expected) {
    int[][] cache = new int[n][n];
    for (int[] row : cache) {
      Arrays.fill(row, -1);
    }

    int actual = jump(n, board, 0, 0, cache);

    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(
            7,
            new int[][] {
              {2, 5, 1, 6, 1, 4, 1},
              {6, 1, 1, 2, 2, 9, 3},
              {7, 2, 3, 2, 1, 3, 1},
              {1, 1, 3, 1, 7, 1, 2},
              {4, 1, 2, 3, 4, 1, 2},
              {3, 3, 1, 2, 3, 4, 1},
              {1, 5, 2, 9, 4, 7, 0}
            },
            1),
        Arguments.of(
            7,
            new int[][] {
              {2, 5, 1, 6, 1, 4, 1},
              {6, 1, 1, 2, 2, 9, 3},
              {7, 2, 3, 2, 1, 3, 1},
              {1, 1, 3, 1, 7, 1, 2},
              {4, 1, 2, 3, 4, 1, 3},
              {3, 3, 1, 2, 3, 4, 1},
              {1, 5, 2, 9, 4, 7, 0}
            },
            0));
  }
}
