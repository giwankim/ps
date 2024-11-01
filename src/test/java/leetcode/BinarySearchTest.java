package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinarySearchTest {

  @ParameterizedTest
  @MethodSource
  void search(int[] nums, int target, int expected) {
    int actual = new BinarySearch().search(nums, target);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> search() {
    return Stream.of(
        Arguments.of(new int[] {-1, 0, 3, 5, 9, 12}, 9, 4),
        Arguments.of(new int[] {-1, 0, 3, 5, 9, 12}, 2, -1));
  }
}
