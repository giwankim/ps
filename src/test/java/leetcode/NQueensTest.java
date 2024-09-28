package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NQueensTest {
  @ParameterizedTest
  @MethodSource
  void solveNQueens(int n, List<List<String>> expected) {
    List<List<String>> actual = new NQueens().solveNQueens(n);
    assertThat(actual).hasSize(expected.size()).containsAll(expected);
  }

  private static Stream<Arguments> solveNQueens() {
    return Stream.of(
        Arguments.of(
            4,
            List.of(
                List.of(".Q..", "...Q", "Q...", "..Q."), List.of("..Q.", "Q...", "...Q", ".Q.."))),
        Arguments.of(1, List.of(List.of("Q"))));
  }
}
