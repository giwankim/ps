package leetcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MaxProfitTest {
  @ParameterizedTest
  @MethodSource
  void maxProfit(int[] prices, int expected) {
    int actual = new MaxProfit().maxProfit(prices);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maxProfit() {
    return Stream.of(
        Arguments.of(new int[]{7, 1, 5, 3, 6, 4}, 5),
        Arguments.of(new int[]{7, 6, 4, 3, 1}, 0)
    );
  }
}