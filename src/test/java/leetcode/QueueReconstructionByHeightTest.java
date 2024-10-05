package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class QueueReconstructionByHeightTest {

  @ParameterizedTest
  @MethodSource
  void reconstructQueue(int[][] people, int[][] expected) {
    int[][] actual = new QueueReconstructionByHeight().reconstructQueue(people);
    assertThat(actual).isDeepEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("reconstructQueue")
  void reconstructQueue2(int[][] people, int[][] expected) {
    int[][] actual = new QueueReconstructionByHeight().reconstructQueue2(people);
    assertThat(actual).isDeepEqualTo(expected);
  }

  private static Stream<Arguments> reconstructQueue() {
    return Stream.of(
        Arguments.of(
            new int[][] {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}},
            new int[][] {{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}}),
        Arguments.of(
            new int[][] {{6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}},
            new int[][] {{4, 0}, {5, 0}, {2, 2}, {3, 2}, {1, 4}, {6, 0}}));
  }
}
