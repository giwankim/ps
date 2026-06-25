package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ContainsDuplicateTest {

  @ParameterizedTest
  @MethodSource
  void containsDuplicate(int[] nums, boolean expected) {
    boolean actual = new ContainsDuplicate().containsDuplicate(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> containsDuplicate() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 3, 1}, true),
        Arguments.of(new int[] {1, 2, 3, 4}, false),
        Arguments.of(new int[] {1, 1, 1, 3, 3, 4, 3, 2, 4, 2}, true));
  }
}
