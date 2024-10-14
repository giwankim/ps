package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SearchInRotatedSortedArrayTest {

  @ParameterizedTest
  @MethodSource
  void search(int[] nums, int target, int expected) {
    int actual = new SearchInRotatedSortedArray().search(nums, target);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> search() {
    return Stream.of(
        Arguments.of(new int[] {4, 5, 6, 7, 0, 1, 2}, 0, 4),
        Arguments.of(new int[] {4, 5, 6, 7, 0, 1, 2}, 3, -1),
        Arguments.of(new int[] {1}, 0, -1),
        Arguments.of(new int[] {5, 1, 3}, 5, 0),
        Arguments.of(new int[] {4, 5, 6, 7, 8, 1, 2, 3}, 8, 4));
  }
}
