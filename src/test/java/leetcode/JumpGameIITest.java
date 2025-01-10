package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JumpGameIITest {

  @ParameterizedTest
  @MethodSource
  void jump(int[] nums, int expected) {
    var sut = new JumpGameII();
    int actual = sut.jump(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> jump() {
    return Stream.of(
        Arguments.of(new int[] {2, 3, 1, 1, 4}, 2), Arguments.of(new int[] {2, 3, 0, 1, 4}, 2));
  }
}
