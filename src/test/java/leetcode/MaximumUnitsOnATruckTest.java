package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximumUnitsOnATruckTest {
  private MaximumUnitsOnATruck sut = new MaximumUnitsOnATruck();

  @ParameterizedTest
  @MethodSource
  void maximumUnits(int[][] boxTypes, int truckSize, int expected) {
    int actual = sut.maximumUnits(boxTypes, truckSize);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maximumUnits() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 3}, {2, 2}, {3, 1}}, 4, 8),
        Arguments.of(new int[][] {{5, 10}, {2, 5}, {4, 7}, {3, 9}}, 10, 91));
  }
}
