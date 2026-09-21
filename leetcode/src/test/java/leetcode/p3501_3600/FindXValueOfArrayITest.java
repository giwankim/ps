package leetcode.p3501_3600;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindXValueOfArrayITest {
  FindXValueOfArrayI sut = new FindXValueOfArrayI();

  // Step 1: both constraint floors. Removing two empty ends is legal and leaves one element,
  //         so a solver that requires an actual deletion misses the only operation.
  @Test
  void k1SingleElementCountsRemovingNeitherEnd() {
    assertThat(sut.resultArray(new int[] {1}, 1)).containsExactly(1L);
  }

  // Step 2: one element belongs only to its remainder bucket. The result must include all k
  //         buckets, including unreachable ones and the final index k - 1.
  @Test
  void k5SingleElementCanReachTheLastBucket() {
    assertThat(sut.resultArray(new int[] {9}, 5)).containsExactly(0L, 0L, 0L, 0L, 1L);
  }

  // Step 3: all input values are positive, but the product remainder can be zero. Treating
  //         remainder zero as an empty or invalid state loses this singleton.
  @Test
  void divisibleSingletonBelongsToRemainderZero() {
    assertThat(sut.resultArray(new int[] {5}, 5)).containsExactly(1L, 0L, 0L, 0L, 0L);
  }

  // Step 4: [2], [3], and [2,3] require an empty prefix, an empty suffix, and both empty,
  //         respectively. Requiring either removed end to be nonempty drops a distinct bucket.
  @Test
  void eitherRemovedEndMayBeEmpty() {
    assertThat(sut.resultArray(new int[] {2, 3}, 5)).containsExactly(0L, 1L, 1L, 1L, 0L);
  }

  // Step 5: retaining the middle [1] requires removing both ends. Counting only retained
  //         prefixes and suffixes misses this interval and undercounts remainder one.
  @Test
  void removingBothEndsCanLeaveAnInteriorSingleton() {
    assertThat(sut.resultArray(new int[] {2, 1, 3}, 5)).containsExactly(0L, 2L, 2L, 2L, 0L);
  }

  // ===========================================================================================
  // Contiguity, multiplicity, and modular products (Steps 6-10).
  // ===========================================================================================

  // Step 6: [2,3] cannot remain after deleting the middle 5. Counting subsequences invents a
  //         remainder-one result, while each valid interval crossing the middle has remainder zero.
  @Test
  void retainedElementsMustBeContiguous() {
    assertThat(sut.resultArray(new int[] {2, 5, 3}, 5)).containsExactly(4L, 0L, 1L, 1L, 0L);
  }

  // Step 7: three singleton intervals and the whole interval yield remainder two, while both
  //         pairs yield remainder one. Equal values or products do not make operations identical.
  @Test
  void equalValuesAtDifferentPositionsCountSeparately() {
    assertThat(sut.resultArray(new int[] {2, 2, 2}, 3)).containsExactly(0L, 2L, 4L);
  }

  // Step 8: the pair has product 6 and remainder two. Using subarray sums instead sends that
  //         interval to remainder one, even though the singleton counts look correct.
  @Test
  void remainingElementsAreMultipliedRatherThanAdded() {
    assertThat(sut.resultArray(new int[] {2, 3}, 4)).containsExactly(0L, 0L, 2L, 1L);
  }

  // Step 9: neither 2 is divisible by 4, but their product is. Composite k rules out treating
  //         zero products as just intervals containing a multiple of k, or assuming inverses exist.
  @Test
  void nonzeroFactorsCanCombineIntoRemainderZero() {
    assertThat(sut.resultArray(new int[] {2, 2}, 4)).containsExactly(1L, 0L, 2L, 0L);
  }

  // Step 10: at the last 2, intervals previously at remainders one and three both move to two.
  //          Colliding contributions must be added, not overwritten or reduced to a boolean flag.
  @Test
  void differentRemaindersCanMergeIntoTheSameBucket() {
    assertThat(sut.resultArray(new int[] {3, 3, 2}, 4)).containsExactly(0L, 1L, 3L, 2L);
  }

  // ===========================================================================================
  // Starting, extending, and counting each interval exactly once (Steps 11-15).
  // ===========================================================================================

  // Step 11: intervals containing the initial 5 have remainder zero, but [2], [3], and [2,3]
  //          start after it. A zero prefix product must not suppress new intervals.
  @Test
  void newIntervalsCanStartAfterAZeroRemainder() {
    assertThat(sut.resultArray(new int[] {5, 2, 3}, 5)).containsExactly(3L, 1L, 1L, 1L, 0L);
  }

  // Step 12: the mirror of Step 11. Appending 5 makes all intervals ending there zero, but must
  //          not erase counts for [2], [3], and [2,3], which ended earlier.
  @Test
  void laterZeroRemaindersDoNotEraseEarlierCounts() {
    assertThat(sut.resultArray(new int[] {2, 3, 5}, 5)).containsExactly(3L, 1L, 1L, 1L, 0L);
  }

  // Step 13: products of lengths 1, 2, 3, and 4 cycle through remainders 2, 4, 3, and 1.
  //          Each element extends an interval only once, so cascading in-place updates overcount.
  @Test
  void remainderCyclesDoNotReuseTheCurrentElement() {
    assertThat(sut.resultArray(new int[] {2, 2, 2, 2}, 5)).containsExactly(0L, 1L, 4L, 2L, 3L);
  }

  // Step 14: all six nonempty intervals have remainder zero for k = 1. Allowing the removed
  //          ends to cover the whole array introduces invalid empty intervals and extra operations.
  @Test
  void k1CountsEveryNonemptyIntervalExactlyOnce() {
    assertThat(sut.resultArray(new int[] {2, 3, 7}, 1)).containsExactly(6L);
  }

  // Step 15: the same multiset has different products for adjacent pairs after reordering.
  //          Sorting, even on a private copy, changes the answer and violates contiguity.
  @Test
  void inputOrderDeterminesWhichProductsArePossible() {
    assertThat(sut.resultArray(new int[] {2, 3, 2}, 4)).containsExactly(1L, 0L, 4L, 1L);
    assertThat(sut.resultArray(new int[] {2, 2, 3}, 4)).containsExactly(2L, 0L, 3L, 1L);
  }

  // ===========================================================================================
  // Arithmetic and the official examples (Steps 16-19).
  // ===========================================================================================

  // Step 16: each value is -1 modulo 5, so even-length products yield one and odd lengths four.
  //          The exact product overflows long, and multiplying a remainder by a raw value can
  //          overflow int. Merely widening the final result array does not fix either problem.
  @Test
  void largeFactorsNeedSafeModularMultiplication() {
    assertThat(sut.resultArray(new int[] {999_999_999, 999_999_999, 999_999_999, 999_999_999}, 5))
        .containsExactly(0L, 4L, 0L, 0L, 6L);
  }

  // Step 17: the nine intervals containing 3 yield zero. The other buckets include both retained
  //          boundary intervals and the interior [4], ruling out prefix-only counting.
  @Test
  void leetCodeExample1() {
    assertThat(sut.resultArray(new int[] {1, 2, 3, 4, 5}, 3)).containsExactly(9L, 2L, 4L);
  }

  // Step 18: [1], [2], and [1,2] are the only nonzero-remainder intervals. The unreachable
  //          remainder-three bucket still belongs in the output, even when most products are zero.
  @Test
  void leetCodeExample2() {
    assertThat(sut.resultArray(new int[] {1, 2, 4, 8, 16, 32}, 4)).containsExactly(18L, 1L, 2L, 0L);
  }

  // Step 19: nine intervals cross the middle 2, while six lie within the two runs of ones.
  //          Deduplicating equal retained values loses operations at distinct positions.
  @Test
  void leetCodeExample3() {
    assertThat(sut.resultArray(new int[] {1, 1, 2, 1, 1}, 2)).containsExactly(9L, 6L);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 20-23).
  // At n = 100,000 there are 5,000,050,000 intervals: quadratic enumeration is impractical,
  // while O(n * k) needs at most 500,000 remainder transitions. Counts must remain exact longs.
  // ===========================================================================================

  // Step 20: n * (n + 1) / 2 belongs entirely to bucket zero for k = 1. Computing the total
  //          in int or reducing the count modulo an unrelated constant corrupts the answer.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k1MaximumLengthNeedsLongCounts() {
    assertThat(sut.resultArray(filled(100_000, 1), 1)).containsExactly(5_000_050_000L);
  }

  // Step 21: the same interval count belongs to bucket one at maximum k. Special-casing k = 1
  //          while retaining int counters for the general case still overflows here.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k5MaximumLengthAllOnesNeedsLongCountsOutsideBucketZero() {
    assertThat(sut.resultArray(filled(100_000, 1), 5))
        .containsExactly(0L, 5_000_050_000L, 0L, 0L, 0L);
  }

  // Step 22: even lengths contribute 2,500,000,000 intervals and odd lengths 2,500,050,000.
  //          Two different buckets exceed int while large factors exercise modular multiplication.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlternatingProductRemaindersBothExceedInt() {
    assertThat(sut.resultArray(filled(100_000, 999_999_999), 5))
        .containsExactly(0L, 2_500_000_000L, 0L, 0L, 2_500_050_000L);
  }

  // Step 23: repeat residues [0,4,3,2,1] using values near the upper limit. Each of the 20,000
  //          nonzero blocks contributes ten intervals, split as [3,3,1,3] across buckets 1..4.
  //          All remaining intervals cross a multiple of 5, exercising every bucket at full size.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthMixedValuesExerciseEveryRemainder() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> 1_000_000_000 - i % 5);

    assertThat(sut.resultArray(nums, 5))
        .containsExactly(4_999_850_000L, 60_000L, 60_000L, 20_000L, 60_000L);
  }

  // ===========================================================================================
  // Caller input and per-call state (Steps 24-25).
  // ===========================================================================================

  // Step 24: reducing caller values modulo k in place destroys the original input even when
  //          the counts are correct. Preserve both the values and their order.
  @Test
  void inputArrayIsNotModified() {
    int[] nums = {999_999_999, 2, 1_000_000_000, 3};
    int[] original = nums.clone();

    assertThat(sut.resultArray(nums, 5)).containsExactly(6L, 0L, 1L, 2L, 1L);
    assertThat(nums).containsExactly(original);
  }

  // Step 25: grow and shrink both n and k on one instance, with the largest input in the middle.
  //          Retained counts, stale remainder buffers, and reused result arrays must not leak.
  @Test
  void oneInstanceAnswersDifferentLengthsAndModuliIndependently() {
    long[] first = sut.resultArray(new int[] {2, 3}, 4);
    assertThat(first).containsExactly(0L, 0L, 2L, 1L);
    assertThat(sut.resultArray(new int[] {1, 2, 3, 4, 5, 2, 1, 3}, 5))
        .containsExactly(20L, 5L, 5L, 3L, 3L);
    assertThat(sut.resultArray(new int[] {7}, 1)).containsExactly(1L);
    assertThat(sut.resultArray(new int[] {4, 2, 3}, 3)).containsExactly(3L, 1L, 2L);
    assertThat(sut.resultArray(new int[] {3, 3, 2}, 4)).containsExactly(0L, 1L, 3L, 2L);
    assertThat(first).containsExactly(0L, 0L, 2L, 1L);
  }

  private static int[] filled(int size, int value) {
    int[] nums = new int[size];
    Arrays.fill(nums, value);
    return nums;
  }
}
