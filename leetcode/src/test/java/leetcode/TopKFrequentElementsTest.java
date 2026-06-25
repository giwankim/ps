package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TopKFrequentElementsTest {

  @ParameterizedTest
  @MethodSource
  void topKFrequent(int[] nums, int k, int[] expected) {
    int[] actual = new TopKFrequentElements().topKFrequent(nums, k);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> topKFrequent() {
    return Stream.of(
        Arguments.of(new int[] {1, 1, 1, 2, 2, 3}, 2, new int[] {1, 2}),
        Arguments.of(new int[] {1}, 1, new int[] {1}));
  }
}
