package leetcode.p0401_0500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumMovesToEqualArrayElementsIITest {
  MinimumMovesToEqualArrayElementsII sut = new MinimumMovesToEqualArrayElementsII();

  // ===========================================================================================
  // The floor (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= nums.length). One element already equals itself. Reading the
  //         goal as "bring everything to 0" answers 5
  @Test
  void singleElementNeedsNoMoves() {
    assertThat(sut.minMoves2(new int[] {5})).isZero();
  }

  // Step 2: a move changes one element by 1, so closing a gap of 3 takes 3 moves wherever the two
  //         elements meet. Halving the gap, as if one move closed it from both sides, answers 1
  @Test
  void twoElementsCostTheirGap() {
    assertThat(sut.minMoves2(new int[] {1, 4})).isEqualTo(3);
  }

  // Step 3: the mirror of Step 2. Nothing says the array is sorted, so the larger element can come
  //         first. A solution that reports nums[1] - nums[0] answers -3
  @Test
  void largerElementCanComeFirst() {
    assertThat(sut.minMoves2(new int[] {4, 1})).isEqualTo(3);
  }

  // Step 4: already equal, with a value above zero. Summing distance from 0 instead of distance
  //         from a target among the elements answers 21
  @Test
  void equalElementsNeedNoMoves() {
    assertThat(sut.minMoves2(new int[] {7, 7, 7})).isZero();
  }

  // ===========================================================================================
  // A move goes either way (Steps 5-7).
  // ===========================================================================================

  // Step 5: raising the single 1 twice is the whole answer. A solution that only ever lowers
  //         elements has to bring both 3s down to 1 and answers 4
  @Test
  void raisingAloneCanBeCheapest() {
    assertThat(sut.minMoves2(new int[] {1, 3, 3})).isEqualTo(2);
  }

  // Step 6: the mirror of Step 5. Lowering the single 3 twice is the whole answer. A solution that
  //         only ever raises elements has to lift both 1s to 3 and answers 4
  @Test
  void loweringAloneCanBeCheapest() {
    assertThat(sut.minMoves2(new int[] {1, 1, 3})).isEqualTo(2);
  }

  // Step 7: meeting at 5 raises the 1 by 4 and lowers the 9 by 4. Either single direction answers
  //         12, and so does the formula from problem 453, sum - n * min, whose moves differ
  @Test
  void raisingAndLoweringTogetherBeatEitherAlone() {
    assertThat(sut.minMoves2(new int[] {1, 5, 9})).isEqualTo(8);
  }

  // ===========================================================================================
  // Which value everything meets at (Steps 8-14).
  //
  // Both official examples accept the mean, so these steps have to rule it out themselves. The cost
  // is a sum of absolute distances, which the middle of the sorted order minimizes: moving the
  // target away from it gets more elements farther than it gets elements closer.
  // ===========================================================================================

  // Step 8: an outlier drags the mean and the midpoint but not the middle element.
  //         target 0 -> 10, mean 2 -> 16, midpoint 5 -> 25
  @Test
  void highOutlierDoesNotPullTheTarget() {
    assertThat(sut.minMoves2(new int[] {0, 0, 0, 0, 10})).isEqualTo(10);
  }

  // Step 9: the mirror of Step 8, for solutions that handle only an upward skew.
  //         target 0 -> 10, mean -2 -> 16, midpoint -5 -> 25
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void lowOutlierDoesNotPullTheTarget() {
    assertThat(sut.minMoves2(new int[] {-10, 0, 0, 0, 0})).isEqualTo(10);
  }

  // Step 10: the most frequent value saves no moves by itself. Steps 8 and 9 cannot tell the mode
  //          from the median, because there the two coincide.
  //          target 5 -> 13, mode 0 -> 18, rounded mean 4 -> 14
  @Test
  void mostFrequentValueIsNotTheTarget() {
    assertThat(sut.minMoves2(new int[] {0, 0, 5, 6, 7})).isEqualTo(13);
  }

  // Step 11: the even-length version of the outlier trap.
  //          target 2 or 3 -> 100, mean 26 -> 146, midpoint 50 -> 194
  @Test
  void meanIsNotTheTargetOnEvenLength() {
    assertThat(sut.minMoves2(new int[] {1, 2, 3, 100})).isEqualTo(100);
  }

  // Step 12: "the middle" means the middle of the sorted order. Taking nums[n / 2] from the
  //          unsorted array aims at 1 and answers 3
  @Test
  void middleIndexOfUnsortedInputIsNotTheMedian() {
    assertThat(sut.minMoves2(new int[] {3, 1, 2})).isEqualTo(2);
  }

  // Step 13: an even length has two middle elements, and any target from 2 to 3 costs 4. Stepping
  //          one index past them aims at 4 and answers 6
  @Test
  void evenLengthAcceptsEitherMiddleElement() {
    assertThat(sut.minMoves2(new int[] {1, 2, 3, 4})).isEqualTo(4);
  }

  // Step 14: pairing the smallest with the largest gives two gaps of 10. Each pair counts once, so
  //          a loop over every index instead of stopping at the middle answers 40
  @Test
  void eachPairAroundTheMiddleCountsOnce() {
    assertThat(sut.minMoves2(new int[] {0, 0, 10, 10})).isEqualTo(20);
  }

  // ===========================================================================================
  // Negative values (Steps 15-16).
  // ===========================================================================================

  // Step 15: every magnitude is 5, but the values are 10 apart. Comparing Math.abs of the values
  //          answers 0, and taking 0 as the target because it sits between them answers 15
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void valuesAcrossZeroAreNotComparedByMagnitude() {
    assertThat(sut.minMoves2(new int[] {-5, -5, 5})).isEqualTo(10);
  }

  // Step 16: a binary search on the target value has lo = -1 and hi = 0 here, and (lo + hi) / 2
  //          truncates toward zero to give 0, not -1. With hi = mid the range never shrinks and the
  //          search spins forever, which no input with non-negative values can reveal. Steps 9, 15,
  //          and 20 hit the same loop, so they carry a timeout too rather than hang the build
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void negativeValuesOneApartStillTerminate() {
    assertThat(sut.minMoves2(new int[] {-1, -1, 0})).isEqualTo(1);
  }

  // ===========================================================================================
  // The official examples (Steps 17-18).
  // ===========================================================================================

  // Step 17: LeetCode's Example 1. The Explanation's reminder targets the reading from problem 453,
  //          where a move changes n - 1 elements. That reading, or a target of 1 or 3, answers 3
  @Test
  void leetCodeExample1() {
    assertThat(sut.minMoves2(new int[] {1, 2, 3})).isEqualTo(2);
  }

  // Step 18: LeetCode's Example 2. Unsorted and even-length, with any target from 2 to 9 costing
  //          16. Pairing from both ends without sorting first, 9 - 1 and then 2 - 10, answers 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.minMoves2(new int[] {1, 10, 2, 9})).isEqualTo(16);
  }

  // ===========================================================================================
  // The constraint bounds (Steps 19-26).
  //
  // Two limits interact. Values reach +/-10^9, but the statement promises the answer fits in an
  // int. Even consecutive values cost about k^2 / 4, so an all-distinct input cannot exceed
  // k = 92,681, and n = 10^5 has to repeat values.
  //
  // O(n log n) finishes these in milliseconds. O(n^2) needs ~10^10 simple operations, but its inner
  // loop is a tight sum of absolute differences, which took 2.8 to 4.3 seconds when measured. A 5
  // second timeout would let it pass, hence the 1 second limit.
  // ===========================================================================================

  // Step 19: the widest gap the values allow, and 2 * 10^9 still fits in an int. Walking every
  //          integer target from min to max visits 2 * 10^9 candidates, however short the array is
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void widestValueGap() {
    assertThat(sut.minMoves2(new int[] {-1_000_000_000, 1_000_000_000})).isEqualTo(2_000_000_000);
  }

  // Step 20: the answer is exactly Integer.MAX_VALUE, while the wrong targets cost more than an int
  //          holds. Trying each element with an int total wraps target 10^9 to -442450943, which
  //          then wins the minimum and comes back as the answer
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void answerCanBeExactlyIntegerMax() {
    assertThat(sut.minMoves2(new int[] {-1_000_000_000, 0, 147_483_647, 1_000_000_000}))
        .isEqualTo(Integer.MAX_VALUE);
  }

  // Step 21: n = 10^5 at the value ceiling. The moves are 0, but the sum is 10^14, so an int-sum
  //          mean wraps and aims at 2764. A quickselect that partitions equal values to one side
  //          also degrades to quadratic time on this input
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAtValueCeilingNeedsNoMoves() {
    assertThat(sut.minMoves2(filled(100_000, 1_000_000_000))).isZero();
  }

  // Step 22: the mirror of Step 21 at the value floor. An int-sum mean wraps and aims at -2764
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAtValueFloorNeedsNoMoves() {
    assertThat(sut.minMoves2(filled(100_000, -1_000_000_000))).isZero();
  }

  // Step 23: n = 10^5 with the answer still at Integer.MAX_VALUE, carried by three outliers around
  //          a crowd of zeros. A counting sort over the value range needs 2 * 10^9 buckets, and the
  //          mean 1474 costs 147394104 moves too many
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAnswerCanBeExactlyIntegerMax() {
    int[] nums = filled(100_000, 0);
    nums[0] = -1_000_000_000;
    nums[1] = 147_483_647;
    nums[2] = 1_000_000_000;

    assertThat(sut.minMoves2(nums)).isEqualTo(Integer.MAX_VALUE);
  }

  // Step 24: n = 10^5 with 100 scrambled copies of 0..999, each copy costing 250,000 moves. Trying
  //          every element as the target is ~10^10 operations
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthWithRepeatedValues() {
    assertThat(sut.minMoves2(scrambled(100_000, 1_000, 0))).isEqualTo(25_000_000);
  }

  // Step 25: 92,000 distinct values near the value floor, scrambled, costing 92000^2 / 4 moves.
  //          Trying only the distinct values as targets survives Step 24 but not this
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largestDistinctInputScrambled() {
    assertThat(sut.minMoves2(scrambled(92_000, 92_000, -1_000_000_000))).isEqualTo(2_116_000_000);
  }

  // Step 26: sorted descending, near the value ceiling. A quickselect that pivots on either end
  //          element removes few elements per partition here and goes quadratic
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largestDistinctInputDescending() {
    int[] nums = new int[92_000];
    Arrays.setAll(nums, i -> 1_000_000_000 - i);

    assertThat(sut.minMoves2(nums)).isEqualTo(2_116_000_000);
  }

  // ===========================================================================================
  // Hygiene (Step 27).
  // ===========================================================================================

  // Step 27: one instance answers inputs of different sizes, the largest in the middle. A sorted
  //          copy or running total cached on the instance leaks from one call into the next
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.minMoves2(new int[] {1, 2, 3})).isEqualTo(2);
    assertThat(sut.minMoves2(scrambled(100_000, 1_000, 0))).isEqualTo(25_000_000);
    assertThat(sut.minMoves2(new int[] {7})).isZero();
    assertThat(sut.minMoves2(new int[] {-5, -5, 5})).isEqualTo(10);
    assertThat(sut.minMoves2(new int[] {1, 10, 2, 9})).isEqualTo(16);
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }

  /** Spreads offset..offset + modulus - 1 evenly over n slots, in a reproducible shuffled order. */
  private static int[] scrambled(int n, int modulus, int offset) {
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> offset + (int) (i * 7919L % modulus));
    return nums;
  }
}
