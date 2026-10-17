package leetcode.p3501_3600;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumSwapsToSortByDigitSumTest {
  MinimumSwapsToSortByDigitSum sut = new MinimumSwapsToSortByDigitSum();

  // ===========================================================================================
  // The floor and the sort key (Steps 1-3).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= nums.length). One value is already in its sorted place, so
  //         no swap is needed
  @Test
  void singleValueIsAlreadySorted() {
    assertThat(sut.minSwaps(new int[] {7})).isZero();
  }

  // Step 2: the target order is by digit sum, not by value. [9, 10] is numerically sorted, but the
  //         digit sums are [9, 1], so 10 belongs first and one swap is needed. A solution that
  //         sorts by value answers 0, and so does one that sorts by digit sum descending
  @Test
  void numericallySortedPairIsOutOfDigitSumOrder() {
    assertThat(sut.minSwaps(new int[] {9, 10})).isEqualTo(1);
  }

  // Step 3: the mirror of Step 2. [10, 9] is numerically reversed, but its digit sums [1, 9]
  //         already rise. Sorting by value, or by digit sum descending, answers 1
  @Test
  void numericallyReversedPairIsInDigitSumOrder() {
    assertThat(sut.minSwaps(new int[] {10, 9})).isZero();
  }

  // ===========================================================================================
  // The digit sum (Steps 4-6).
  // ===========================================================================================

  // Step 4: the digit sum adds every digit, not just the last one. 91 -> 9 + 1 = 10 and 5 -> 5, so
  //         5 belongs first. A key of nums[i] % 10 compares 1 with 5, keeps the order, and
  //         answers 0
  @Test
  void digitSumAddsEveryDigitNotJustTheLast() {
    assertThat(sut.minSwaps(new int[] {91, 5})).isEqualTo(1);
  }

  // Step 5: the digits are summed once, not reduced to a single digit. 19 -> 10 and 9 -> 9, so 9
  //         belongs first. Repeating the sum until one digit remains turns 19 into 10 and then 1,
  //         keeps the order, and answers 0
  @Test
  void digitSumIsSummedOnceNotReducedToASingleDigit() {
    assertThat(sut.minSwaps(new int[] {19, 9})).isEqualTo(1);
  }

  // Step 6: the two largest legal values sort in reverse numeric order. 999,999,999 carries the
  //         largest digit sum any input can have, 81, while 1,000,000,000 has digit sum 1.
  //         Sorting by value answers 0
  @Test
  void twoLargestLegalValuesSortInReverseNumericOrder() {
    assertThat(sut.minSwaps(new int[] {999999999, 1000000000})).isEqualTo(1);
  }

  // ===========================================================================================
  // The tie rule (Steps 7-9). "If two numbers have the same digit sum, the smaller number appears
  // first." The values are distinct, so this makes the target order total.
  // ===========================================================================================

  // Step 7: 30 and 12 both have digit sum 3, so 12 belongs first. A stable sort on digit sum alone
  //         keeps the input order of tied values and answers 0, and so does one that puts the
  //         larger value first
  @Test
  void equalDigitSumsPutTheSmallerValueFirst() {
    assertThat(sut.minSwaps(new int[] {30, 12})).isEqualTo(1);
  }

  // Step 8: the mirror of Step 7. The tied values are already in increasing order, so no swap is
  //         needed. A solution that breaks ties toward the larger value answers 1
  @Test
  void equalDigitSumsAlreadyInValueOrderNeedNoSwap() {
    assertThat(sut.minSwaps(new int[] {12, 30})).isZero();
  }

  // Step 9: 1,000,000,000 has ten digits and ties with 1 at digit sum 1, so the tie rule puts it
  //         last. A digit loop capped at nine digits drops its leading 1, gives it digit sum 0,
  //         sorts it first, and answers 0
  @Test
  void tenDigitCeilingTiesWithOneAndSortsAfterIt() {
    assertThat(sut.minSwaps(new int[] {1000000000, 1})).isEqualTo(1);
  }

  // ===========================================================================================
  // Counting swaps (Steps 10-14). Single-digit values are their own digit sums, so in Steps 10-13
  // the target order is plain increasing order and only the counting is under test.
  // ===========================================================================================

  // Step 10: a swap exchanges any two positions, not just neighbors. [3, 2, 1] needs one swap of
  //          its ends. Counting adjacent swaps, as bubble sort or an inversion count would,
  //          answers 3
  @Test
  void swapExchangesAnyTwoPositionsNotJustNeighbors() {
    assertThat(sut.minSwaps(new int[] {3, 2, 1})).isEqualTo(1);
  }

  // Step 11: in [2, 3, 1] every value is misplaced, but the three form one cycle — 2 sits where 1
  //          belongs, 1 where 3 belongs, 3 where 2 belongs. The first swap places one value and
  //          the second places the last two at once, so 2. Counting misplaced positions answers 3
  @Test
  void cycleOfThreeNeedsTwoSwaps() {
    assertThat(sut.minSwaps(new int[] {2, 3, 1})).isEqualTo(2);
  }

  // Step 12: [2, 1, 4, 3] is two independent swapped pairs, one swap each. A solution that treats
  //          every misplaced value as one cycle, answering the misplaced count minus one,
  //          answers 3
  @Test
  void twoSwappedPairsNeedOneSwapEach() {
    assertThat(sut.minSwaps(new int[] {2, 1, 4, 3})).isEqualTo(2);
  }

  // Step 13: [2, 3, 4, 1] misplaces the same four positions as Step 12 but chains them into a
  //          single cycle, which needs 3 swaps. Together the two steps show that the answer
  //          depends on how the misplaced values form cycles, not on how many there are. Halving
  //          the misplaced count answers 2 here, and counting the cycles answers 1
  @Test
  void cycleOfFourNeedsThreeSwaps() {
    assertThat(sut.minSwaps(new int[] {2, 3, 4, 1})).isEqualTo(3);
  }

  // Step 14: digit sums, ties, and cycles together. The target is
  //          [1000, 2, 11, 20, 3, 12, 30, 111, 40] with digit sums 1 | 2 2 2 | 3 3 3 3 | 4.
  //          1000, 11, 12, and 30 are already placed, 2 and 20 trade places inside the tie at
  //          digit sum 2 (1 swap), and 111, 40, and 3 form a cycle across two digit sums
  //          (2 swaps), so 3. A stable sort on digit sum alone answers 1, counting misplaced
  //          positions answers 5, and sorting by value answers 7
  @Test
  void swapInsideATieAndCycleAcrossDigitSumsAddUp() {
    assertThat(sut.minSwaps(new int[] {1000, 20, 11, 2, 111, 12, 30, 40, 3})).isEqualTo(3);
  }

  // ===========================================================================================
  // The official examples (Steps 15-17).
  // ===========================================================================================

  // Step 15: digit sums [10, 1] put 100 ahead of 37. Sorting by value answers 0, and so does
  //          reducing digit sums to one digit, since 37 -> 10 -> 1 then ties with 100 and the tie
  //          keeps 37 first
  @Test
  void leetCodeExample1() {
    assertThat(sut.minSwaps(new int[] {37, 100})).isEqualTo(1);
  }

  // Step 16: digit sums [4, 5, 6, 7] already rise while the values do not, so nothing moves.
  //          Sorting by value answers 2
  @Test
  void leetCodeExample2() {
    assertThat(sut.minSwaps(new int[] {22, 14, 33, 7})).isZero();
  }

  // Step 17: a three-way tie at digit sum 7 must come out as 16, 34, 43, ahead of 18 at digit sum
  //          9 — two swaps, 18 with 16 and 43 with 34. A stable sort on digit sum alone answers
  //          3, counting misplaced positions answers 4, and counting adjacent swaps answers 6
  @Test
  void leetCodeExample3() {
    assertThat(sut.minSwaps(new int[] {18, 43, 34, 16})).isEqualTo(2);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 18-20). 10^5 distinct values up to 10^9. Placing each value by
  // scanning the array for it, as selection sort does, takes ~10^10 comparisons and cannot finish.
  // Sorting once and finding positions through an index or a hash map finishes in milliseconds.
  // The timeouts run on a separate thread because JUnit's default SAME_THREAD mode only checks
  // elapsed time after the method returns.
  // ===========================================================================================

  // Step 18: maximum length, the values 1 to 100,000 in increasing order. Digit-sum order scatters
  //          almost all of them: the values form only 12 cycles, so 100,000 - 12 = 99,988 swaps.
  //          Sorting by value answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthIncreasingRamp() {
    assertThat(sut.minSwaps(maximumLength(i -> i + 1))).isEqualTo(99_988);
  }

  // Step 19: Step 18 with four zeros appended to every value, so the largest is exactly the
  //          ceiling 1,000,000,000. Trailing zeros change neither a digit sum nor the order of the
  //          values, so the answer is still 99,988. A last-digit key sees only zeros and answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithTrailingZerosReachingTheCeiling() {
    assertThat(sut.minSwaps(maximumLength(i -> (i + 1) * 10_000))).isEqualTo(99_988);
  }

  // Step 20: maximum length counting down from the ceiling, 1,000,000,000 to 999,900,001. The
  //          values form 13 cycles, so 99,987 swaps. Sorting by value reverses the array in 50,000
  //          swaps, and a stable sort on digit sum alone answers 99,983
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthCountingDownFromTheCeiling() {
    assertThat(sut.minSwaps(maximumLength(i -> 1_000_000_000 - i))).isEqualTo(99_987);
  }

  // ===========================================================================================
  // Hygiene (Step 21).
  // ===========================================================================================

  // Step 21: several arrays answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that keeps a position map or a visited array on
  //          the instance without resetting it per call answers the later arrays from the earlier
  //          ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    assertThat(sut.minSwaps(new int[] {37, 100})).isEqualTo(1);
    assertThat(sut.minSwaps(maximumLength(i -> i + 1))).isEqualTo(99_988);
    assertThat(sut.minSwaps(new int[] {7})).isZero();
    assertThat(sut.minSwaps(new int[] {18, 43, 34, 16})).isEqualTo(2);
    assertThat(sut.minSwaps(new int[] {22, 14, 33, 7})).isZero();
  }

  /** The most values the constraints allow, 10^5, with {@code nums[i] = valueAt(i)}. */
  private static int[] maximumLength(IntUnaryOperator valueAt) {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, valueAt);
    return nums;
  }
}
