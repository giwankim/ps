package leetcode.p2301_2400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumSumOfSquaredDifferenceTest {
  MinimumSumOfSquaredDifference sut = new MinimumSumOfSquaredDifference();

  // ===========================================================================================
  // The definition, with no budget to spend (Steps 1-3).
  // ===========================================================================================

  // Step 1: the floor. n >= 1, and although the statement calls the arrays "positive", the
  //         constraints allow 0 for every value and for both budgets. Identical arrays score 0
  @Test
  void zerosWithNoBudgetScoreZero() {
    assertThat(sut.minSumSquareDiff(new int[] {0}, new int[] {0}, 0, 0)).isZero();
  }

  // Step 2: one pair 3 apart scores 3 squared. A solution that sums absolute differences
  //         answers 3
  @Test
  void singlePairWithNoBudgetScoresTheSquaredDifference() {
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {4}, 0, 0)).isEqualTo(9);
  }

  // Step 3: differences pair nums1[i] with nums2[i], index by index. d = [9, 9] -> 81 + 81.
  //         A solution that sorts both arrays before pairing answers 0, and so does one that
  //         squares the signed total of nums1[i] - nums2[i], where +9 and -9 cancel
  @Test
  void differencesPairByIndexNotBySortedOrder() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 10}, new int[] {10, 1}, 0, 0)).isEqualTo(162);
  }

  // ===========================================================================================
  // The two budgets (Steps 4-9).
  //
  // Each unit of either budget moves one element by 1, which moves one difference by 1. Adding 1
  // to nums1[i] is the same as subtracting 1 from nums2[i], so k1 and k2 are one pooled budget
  // spent against d[i] = |nums1[i] - nums2[i]|.
  // ===========================================================================================

  // Step 4: k1 raises nums1 toward nums2. d = 3 -> 2
  @Test
  void k1AloneMovesNums1TowardNums2() {
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {4}, 1, 0)).isEqualTo(4);
  }

  // Step 5: the mirror of Step 4. k2 lowers nums2 toward nums1 just as well. A solution that
  //         spends only k1 answers 9
  @Test
  void k2AloneMovesNums2TowardNums1() {
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {4}, 0, 1)).isEqualTo(4);
  }

  // Step 6: when nums1 is the larger side, k1 has to decrement it. d = 3 -> 1. A solution that
  //         only ever increments nums1 drives the gap to 5 and answers 25
  @Test
  void k1DecrementsNums1WhenItIsTheLargerSide() {
    assertThat(sut.minSumSquareDiff(new int[] {4}, new int[] {1}, 2, 0)).isEqualTo(1);
  }

  // Step 7: neither budget closes the gap of 4 alone, but together they do: nums1 rises by 2
  //         and nums2 falls by 2 to meet at 3. A solution that spends only k1, or only
  //         max(k1, k2), answers 4
  @Test
  void k1AndK2PoolIntoOneBudget() {
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {5}, 2, 2)).isEqualTo(0);
  }

  // Step 8: "at most k1 times" makes the budget a ceiling, not a quota. 3 of the 4 units close
  //         the gap and the last one is simply not spent. A solution that must spend all 4
  //         answers 1, and so does one that computes d - k without clamping at 0. The Note that
  //         elements may go negative matters only when simulating the arrays, since working on
  //         the differences never needs to push one below 0
  @Test
  void budgetIsAnUpperBoundNotAQuota() {
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {4}, 4, 0)).isZero();
  }

  // Step 9: k1 counts modifications across the whole array, not per element. d = [3, 3] with 3
  //         units -> [2, 2] -> [1, 2] -> 1 + 4. Reading k1 as a per-index allowance zeroes both
  //         gaps and answers 0
  @Test
  void budgetIsSharedAcrossIndicesNotGrantedPerIndex() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 1}, new int[] {4, 4}, 3, 0)).isEqualTo(5);
  }

  // ===========================================================================================
  // Where to spend the budget: level the largest differences down (Steps 10-15).
  //
  // Shrinking a difference d by 1 saves d^2 - (d - 1)^2 = 2d - 1, so the next unit always pays
  // most on the current maximum. Spending greedily on the maximum flattens the tallest
  // differences into one shared level, and whatever budget cannot lower that whole level once
  // more lowers part of it by 1.
  // ===========================================================================================

  // Step 10: d = [1, 4] with 1 unit. Lowering the 4 saves 7 -> 1 + 9, while lowering the 1
  //          saves only 1. A solution that spends on the smallest difference first, or on the
  //          first index, answers 16
  @Test
  void unitGoesToTheLargestDifference() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 1}, new int[] {2, 5}, 1, 0)).isEqualTo(10);
  }

  // Step 11: the maximum moves as the budget is spent. d = [5, 4] with 3 units -> [4, 4] ->
  //          [3, 4] -> [3, 3] -> 9 + 9. A solution that sends the whole budget to the initial
  //          maximum ends at [2, 4] and answers 20
  @Test
  void budgetIsSpreadAcrossTheMaximumsNotDumpedIntoTheFirstOne() {
    assertThat(sut.minSumSquareDiff(new int[] {0, 0}, new int[] {5, 4}, 1, 2)).isEqualTo(18);
  }

  // Step 12: a remainder smaller than the level it sits on. d = [4, 4, 4] with 4 units: 3 units
  //          take the level to [3, 3, 3], and the fourth lowers just one of them -> [2, 3, 3] ->
  //          4 + 9 + 9. A solution that drops the leftover unit answers 27, and one that rounds
  //          it up into a whole level, which would cost 6 units, answers 12
  @Test
  void remainderBelowAFullLevelLowersPartOfIt() {
    assertThat(sut.minSumSquareDiff(new int[] {4, 4, 4}, new int[] {0, 0, 0}, 2, 2))
        .isEqualTo(22);
  }

  // Step 13: the level absorbs every difference it reaches on the way down. d = [6, 3, 3, 1]
  //          with 7 units: 3 units -> [3, 3, 3, 1], 3 more -> [2, 2, 2, 1], and the last unit
  //          -> [1, 2, 2, 1] -> 1 + 4 + 4 + 1. Dropping the remainder answers 13, and dumping
  //          the whole budget into the 6 answers 19
  @Test
  void levelCascadesThroughSeveralDistinctDifferences() {
    assertThat(sut.minSumSquareDiff(new int[] {6, 0, 3, 1}, new int[] {0, 3, 0, 0}, 4, 3))
        .isEqualTo(10);
  }

  // Step 14: the early-exit boundary. d = [3, 1, 2] sums to 6, exactly the pooled budget, so
  //          every gap closes. A solution that only short-circuits when the budget strictly
  //          exceeds the total still has to reach 0 the long way
  @Test
  void budgetEqualToTheTotalDifferenceClearsEveryGap() {
    assertThat(sut.minSumSquareDiff(new int[] {3, 0, 2}, new int[] {0, 1, 0}, 3, 3))
        .isZero();
  }

  // Step 15: both budgets at their 10^9 ceiling against d = [7, 9, 0], a total of 16. The level
  //          bottoms out at 0 and the spare 1,999,999,984 units go unspent. A solution that
  //          computes the final level as max(d) - k without clamping, or that keeps lowering
  //          past 0 and squares the negative gaps, answers something huge
  @Test
  void maximumBudgetsStopAtZeroInsteadOfOvershooting() {
    assertThat(sut.minSumSquareDiff(
            new int[] {7, 0, 3}, new int[] {0, 9, 3}, 1_000_000_000, 1_000_000_000))
        .isZero();
  }

  // ===========================================================================================
  // The official examples (Steps 16-17).
  // ===========================================================================================

  // Step 16: no budget at all, so this is the definition at scale. d = [1, 8, 17, 15] ->
  //          1 + 64 + 289 + 225. Summing absolute differences answers 41
  @Test
  void leetCodeExample1() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 2, 3, 4}, new int[] {2, 10, 20, 19}, 0, 0))
        .isEqualTo(579);
  }

  // Step 17: d = [4, 4, 4, 3] with 2 units. Two different 4s each drop to 3 -> 9 + 9 + 16 + 9.
  //          The Explanation spends k1 on index 0 and k2 on index 2, so the two budgets need not
  //          target the same index. Spending both units on one element answers 45, and
  //          spending them on the smallest difference answers 49
  @Test
  void leetCodeExample2() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 4, 10, 12}, new int[] {5, 8, 6, 9}, 1, 1))
        .isEqualTo(43);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 18-22).
  //
  // n reaches 10^5, a single difference reaches 10^5, and the pooled budget reaches 2 * 10^9.
  // Spending one unit at a time through a heap is O(k log n), about 3 * 10^10 operations, and
  // cannot finish. Rescanning the array once per level walked is O(n) per level, and Step 21
  // walks 63,245 levels, about 6 * 10^9 element visits. Sorting the differences, binary
  // searching the final level, or counting differences into buckets indexed by value all
  // finish in milliseconds.
  //
  // The arithmetic needs long in three places: one squared difference reaches 10^10, the total
  // of the differences reaches 10^10, and the answer reaches 10^15. The pooled budget,
  // 2,000,000,000, still fits in an int.
  // ===========================================================================================

  // Step 18: one pair at opposite ends of the value range. 100,000 squared is 10^10, and
  //          squaring in int wraps to 1,410,065,408
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void singlePairAtTheValueExtremesSquaresPastIntRange() {
    assertThat(sut.minSumSquareDiff(new int[] {0}, new int[] {100_000}, 0, 0))
        .isEqualTo(10_000_000_000L);
  }

  // Step 19: 10^5 pairs, each 10^5 apart, with the larger side alternating between the arrays
  //          -> 10^5 * 10^10. An int accumulator, or an int per-element square, cannot hold it
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAtMaxDifferenceWithNoBudgetNeedsALongAnswer() {
    int[] nums1 = alternating(100_000, 0, 100_000);
    int[] nums2 = alternating(100_000, 100_000, 0);

    assertThat(sut.minSumSquareDiff(nums1, nums2, 0, 0)).isEqualTo(1_000_000_000_000_000L);
  }

  // Step 20: Step 19 with both budgets maxed. The differences total 10^10, which an int sum
  //          wraps to 1,410,065,408, below the 2 * 10^9 budget, so a "budget covers everything"
  //          early exit fired off an int total answers 0. The budget really lowers every
  //          difference by exactly 20,000 -> 10^5 * 80,000^2
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxBudgetsAgainstMaxDifferencesNeedALongTotal() {
    int[] nums1 = alternating(100_000, 0, 100_000);
    int[] nums2 = alternating(100_000, 100_000, 0);

    assertThat(sut.minSumSquareDiff(nums1, nums2, 1_000_000_000, 1_000_000_000))
        .isEqualTo(640_000_000_000_000L);
  }

  // Step 21: every difference from 1 to 10^5 appears exactly once, sides alternating, against
  //          the full 2 * 10^9 budget. The level settles at 36,755 after walking down 63,245
  //          distinct values, and 3,365 units are left over to lower 3,365 of the 63,246
  //          differences sitting on that level by 1. This is the step that separates per-unit
  //          and per-level-rescan solutions from the fast ones
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void distinctDifferencesForceTensOfThousandsOfLevels() {
    int[] nums1 = new int[100_000];
    int[] nums2 = new int[100_000];
    Arrays.setAll(nums1, i -> i % 2 == 0 ? i + 1 : 0);
    Arrays.setAll(nums2, i -> i % 2 == 0 ? 0 : i + 1);

    assertThat(sut.minSumSquareDiff(nums1, nums2, 1_000_000_000, 1_000_000_000))
        .isEqualTo(101_991_141_900_770L);
  }

  // Step 22: 10^5 differences of 20,000 total exactly 2 * 10^9, and the budget is one unit short
  //          of that. Every difference reaches the level 1 and 99,999 of them reach 0, leaving
  //          a single difference of 1. A solution that drops the remainder at the last level
  //          answers 100,000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void budgetOneUnitShortOfTheTotalLeavesASingleUnitGap() {
    int[] nums1 = filled(100_000, 20_000);
    int[] nums2 = filled(100_000, 0);

    assertThat(sut.minSumSquareDiff(nums1, nums2, 1_000_000_000, 999_999_999)).isEqualTo(1);
  }

  // ===========================================================================================
  // Hygiene (Steps 23-24).
  // ===========================================================================================

  // Step 23: the caller's arrays come back untouched. Writing the differences back into nums1,
  //          or sorting either array in place, is a tempting shortcut
  @Test
  void inputArraysAreNotModified() {
    int[] nums1 = {1, 4, 10, 12};
    int[] nums2 = {5, 8, 6, 9};
    int[] nums1Before = nums1.clone();
    int[] nums2Before = nums2.clone();

    sut.minSumSquareDiff(nums1, nums2, 1, 1);

    assertThat(nums1).containsExactly(nums1Before);
    assertThat(nums2).containsExactly(nums2Before);
  }

  // Step 24: one instance answers inputs of different sizes in a deliberately jumbled order, the
  //          largest in the middle. A bucket array or level cached on the instance from an
  //          earlier call corrupts a later one
  @Test
  void oneInstanceAnswersSeveralInputsIndependently() {
    assertThat(sut.minSumSquareDiff(new int[] {1, 2, 3, 4}, new int[] {2, 10, 20, 19}, 0, 0))
        .isEqualTo(579);
    assertThat(sut.minSumSquareDiff(
            alternating(100_000, 0, 100_000), alternating(100_000, 100_000, 0), 0, 0))
        .isEqualTo(1_000_000_000_000_000L);
    assertThat(sut.minSumSquareDiff(new int[] {1}, new int[] {4}, 4, 0)).isZero();
    assertThat(sut.minSumSquareDiff(new int[] {1, 4, 10, 12}, new int[] {5, 8, 6, 9}, 1, 1))
        .isEqualTo(43);
    assertThat(sut.minSumSquareDiff(new int[] {6, 0, 3, 1}, new int[] {0, 3, 0, 0}, 4, 3))
        .isEqualTo(10);
  }

  private static int[] filled(int n, int value) {
    int[] arr = new int[n];
    Arrays.fill(arr, value);
    return arr;
  }

  private static int[] alternating(int n, int even, int odd) {
    int[] arr = new int[n];
    Arrays.setAll(arr, i -> i % 2 == 0 ? even : odd);
    return arr;
  }
}
