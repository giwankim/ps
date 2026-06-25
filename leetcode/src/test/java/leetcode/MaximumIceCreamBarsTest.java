package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumIceCreamBarsTest {
  MaximumIceCreamBars sut = new MaximumIceCreamBars();

  // Step 1: smallest meaningful input — a single bar within budget is bought
  @Test
  void singleAffordableBarBuysExactlyOne() {
    assertThat(sut.maxIceCream(new int[] {2}, 5)).isEqualTo(1);
  }

  // Step 2: the "cannot afford" branch — a single bar costing more than every coin buys nothing
  @Test
  void singleUnaffordableBarBuysNone() {
    assertThat(sut.maxIceCream(new int[] {5}, 2)).isEqualTo(0);
  }

  // Step 3: the coins == cost boundary — affordability must be inclusive (<=, not <)
  @Test
  void singleBarPricedExactlyAtBudgetIsBought() {
    assertThat(sut.maxIceCream(new int[] {3}, 3)).isEqualTo(1);
  }

  // Step 4: multiple bars whose total exactly drains the budget — every bar is bought
  @Test
  void buysEveryBarWhenTotalExactlyEqualsBudget() {
    assertThat(sut.maxIceCream(new int[] {1, 2, 3}, 6)).isEqualTo(3);
  }

  // Step 5: the stopping condition — once the next bar exceeds the remaining coins, buying halts
  //         even though a coin is left over (1 + 2 = 3 spent, can't add the 3-coin bar)
  @Test
  void stopsWhenNextBarExceedsRemainingCoinsLeavingChange() {
    assertThat(sut.maxIceCream(new int[] {1, 2, 3}, 4)).isEqualTo(2);
  }

  // Step 6: the core greedy insight — buy cheapest first regardless of input order. Given {3,1,2}
  //         the optimal buys 1 + 2 (2 bars); a naive "buy in given order" would spend 3 on the
  //         first bar and afford only 1. This test is what separates greedy from non-greedy.
  @Test
  void buysCheapestFirstRegardlessOfInputOrder() {
    assertThat(sut.maxIceCream(new int[] {3, 1, 2}, 3)).isEqualTo(2);
  }

  // Step 7: duplicate prices with the cutoff falling among equal-cost bars — three of the four
  //         1-coin bars are affordable
  @Test
  void handlesDuplicatePricesAtTheCutoff() {
    assertThat(sut.maxIceCream(new int[] {1, 1, 1, 1}, 3)).isEqualTo(3);
  }

  // Step 8: every bar exceeds the budget even after sorting — the cheapest (6) already costs more
  //         than the 5 coins, so nothing is bought
  @Test
  void buysNoneWhenEveryBarExceedsBudget() {
    assertThat(sut.maxIceCream(new int[] {10, 6, 8, 7}, 5)).isEqualTo(0);
  }

  // Step 9: surplus coins do not inflate the count — only three bars exist, so the answer caps at
  //         the array length no matter how much money is left over
  @Test
  void surplusCoinsDoNotExceedBarCount() {
    assertThat(sut.maxIceCream(new int[] {1, 2, 3}, 100)).isEqualTo(3);
  }

  // Step 10: LeetCode Example 1 — sorted [1,1,2,3,4], buy 1+1+2+3 = 7 for 4 bars
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxIceCream(new int[] {1, 3, 2, 4, 1}, 7)).isEqualTo(4);
  }

  // Step 11: LeetCode Example 2 — cheapest bar (6) already exceeds the 5 coins, so 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxIceCream(new int[] {10, 6, 8, 7, 7, 8}, 5)).isEqualTo(0);
  }

  // Step 12: LeetCode Example 3 — total cost is 18 <= 20 coins, so all 6 bars are bought
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxIceCream(new int[] {1, 6, 3, 1, 2, 5}, 20)).isEqualTo(6);
  }

  // Step 13: upper size bound (n = 1e5) with every bar affordable — the total (1e5) sits well
  //          under the budget (1e8), so all 100_000 bars are bought; the answer must still cap
  //          at the array length, and an O(n log n) / counting sort must scale to 1e5 elements
  @Test
  void maximumSizeWithCheapBarsBuysEveryBar() {
    int[] costs = new int[100_000];
    Arrays.fill(costs, 1);
    assertThat(sut.maxIceCream(costs, 100_000_000)).isEqualTo(100_000);
  }

  // Step 14: maximum array size at the maximum per-bar cost — only the budget limits the count
  //          (1e8 coins / 1e5 per bar = exactly 1000 bars). A solution that builds a full
  //          cumulative-cost array in int would overflow here (1e5 bars * 1e5 ~ 1e10 >
  //          Integer.MAX_VALUE); subtracting each cost from coins instead stays correct.
  @Test
  void maximumCostBarsAreBoundedByBudgetWithoutOverflow() {
    int[] costs = new int[100_000];
    Arrays.fill(costs, 100_000);
    assertThat(sut.maxIceCream(costs, 100_000_000)).isEqualTo(1000);
  }
}
