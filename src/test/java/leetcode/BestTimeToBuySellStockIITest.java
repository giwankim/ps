package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class BestTimeToBuySellStockIITest {

  @ParameterizedTest
  @MethodSource
  void maxProfit(int[] prices, int expected) {
    int actual = new BestTimeToBuySellStockII().maxProfit(prices);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> maxProfit() {
    return Stream.of(
        Arguments.of(new int[] {7, 1, 5, 3, 6, 4}, 7),
        Arguments.of(new int[] {1, 2, 3, 4, 5}, 4),
        Arguments.of(new int[] {7, 6, 4, 3, 1}, 0));
  }
}
