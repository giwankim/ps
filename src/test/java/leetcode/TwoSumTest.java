package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TwoSumTest {
  @ParameterizedTest(name = "indices {2} sums to {1} in {0}")
  @MethodSource("baseTestCases")
  void should_return_indices(int[] nums, int target, int[] expected) {
    int[] actual = new TwoSum().twoSum(nums, target);
    assertThat(actual).containsExactlyInAnyOrder(expected);
  }

  private static Stream<Arguments> baseTestCases() {
    return Stream.of(
        Arguments.of(new int[] {2, 7, 11, 15}, 9, new int[] {0, 1}),
        Arguments.of(new int[] {3, 2, 4}, 6, new int[] {1, 2}),
        Arguments.of(new int[] {3, 3}, 6, new int[] {0, 1}));
  }
}
