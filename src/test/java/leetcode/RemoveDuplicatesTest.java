package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveDuplicatesTest {
  @ParameterizedTest
  @MethodSource
  void removeDuplicates(int[] nums, int expectedNums[], int expectedK) {
    int k = new RemoveDuplicates().removeDuplicates(nums);
    assertThat(k).isEqualTo(expectedK);
    assertThat(nums).hasSizeGreaterThanOrEqualTo(k);
    assertThat(expectedNums).hasSizeGreaterThanOrEqualTo(k);
    for (int i = 0; i < k; i++) {
      assertThat(nums[i]).isEqualTo(expectedNums[i]);
    }
  }

  private static Stream<Arguments> removeDuplicates() {
    return Stream.of(
        Arguments.of(new int[] {1, 1, 2}, new int[] {1, 2, 1}, 2),
        Arguments.of(
            new int[] {0, 0, 1, 1, 1, 2, 2, 3, 3, 4}, new int[] {0, 1, 2, 3, 4, 0, 1, 1, 2, 3}, 5));
  }
}
