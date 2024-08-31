package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThreeSumTest {
  @ParameterizedTest
  @MethodSource
  void threeSum(int[] nums, List<List<Integer>> expected) {
    List<List<Integer>> actual = new ThreeSum().threeSum(nums);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  private static Stream<Arguments> threeSum() {
    return Stream.of(
        Arguments.of(new int[] {-1, 0, 1}, List.of(List.of(-1, 0, 1))),
        Arguments.of(new int[] {-1, -1, 0, 1, 2}, List.of(List.of(-1, -1, 2), List.of(-1, 0, 1))),
        Arguments.of(
            new int[] {-1, 0, 1, 2, -1, 4}, List.of(List.of(-1, -1, 2), List.of(-1, 0, 1))),
        Arguments.of(new int[] {0, 1, 1}, List.of()),
        Arguments.of(new int[] {0, 0, 0}, List.of(List.of(0, 0, 0))));
  }
}
