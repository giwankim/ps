package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LeastNumberOfUniqueIntegersTest {

  @ParameterizedTest
  @MethodSource
  void findLeastNumOfUniqueInts(int[] arr, int k, int expected) {
    var sut = new LeastNumberOfUniqueIntegers();
    int actual = sut.findLeastNumOfUniqueInts(arr, k);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> findLeastNumOfUniqueInts() {
    return Stream.of(
        Arguments.of(new int[] {5, 5, 4}, 1, 1),
        Arguments.of(new int[] {4, 3, 1, 1, 3, 3, 2}, 3, 2));
  }
}
