package leetcode.p2901_3000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SubarraysDistinctElementSumOfSquaresITest {
  SubarraysDistinctElementSumOfSquaresI sut = new SubarraysDistinctElementSumOfSquaresI();

  // Step 1: smallest valid input (1 <= nums.length). One element is one subarray with one distinct
  //         value, so the answer is 1 squared whatever the value is. A solution that squares the
  //         element itself answers 49
  @Test
  void singleElementScoresOne() {
    assertThat(sut.sumCounts(List.of(7))).isEqualTo(1);
  }

  // ===========================================================================================
  // What gets counted, and what gets squared (Steps 2-5).
  // ===========================================================================================

  // Step 2: each subarray's count is squared on its own, then the squares are summed.
  //         counts = [1], [2], [1, 2] -> 1, 1, 2 -> 1 + 1 + 4 = 6. Summing the counts without
  //         squaring answers 4, squaring their total answers 16, and counting only the prefixes
  //         (every subarray starting at index 0) answers 5
  @Test
  void eachSubarrayCountIsSquaredBeforeSumming() {
    assertThat(sut.sumCounts(List.of(1, 2))).isEqualTo(6);
  }

  // Step 3: the count is of distinct values, not of elements. A uniform array has one distinct
  //         value in every subarray, so the answer is just the number of subarrays, 3 * 4 / 2.
  //         Squaring subarray lengths answers 20, and counting only the values that occur exactly
  //         once (which makes [4, 4] score 0) answers 3
  @Test
  void uniformArrayScoresTheNumberOfSubarrays() {
    assertThat(sut.sumCounts(List.of(4, 4, 4))).isEqualTo(6);
  }

  // Step 4: with no repeats the distinct count equals the length, so the answer sums squared
  //         lengths over every start: lengths 1, 2, 3 occur 3, 2, 1 times -> 3 + 8 + 9 = 20. A
  //         seen-set declared outside the loop over starts still holds 1, 2, 3 when the second
  //         start begins, scores every later subarray 9, and answers 41
  @Test
  void allDistinctArrayScoresSquaredLengthsFromEveryStart() {
    assertThat(sut.sumCounts(List.of(1, 2, 3))).isEqualTo(20);
  }

  // Step 5: distinct is by value across the whole subarray, not between neighbors. The two 1s are
  //         three apart, so [1, 2, 3, 1] holds 3 distinct values, not 4. Counting value changes
  //         plus one (runs) treats the second 1 as new and answers 50
  @Test
  void nonAdjacentRepeatCountsOnce() {
    assertThat(sut.sumCounts(List.of(1, 2, 3, 1))).isEqualTo(43);
  }

  // ===========================================================================================
  // Interactions between repeats and growth (Steps 6-8).
  // ===========================================================================================

  // Step 6: a repeat does not end a subarray. The count stalls at the second 1 and climbs again at
  //         the 3, so [1, 2, 1, 3] scores 9. Breaking out of the inner loop at the first repeated
  //         value, the habit carried over from "longest substring without repeating characters",
  //         skips every subarray that contains a repeat and answers 25
  @Test
  void countResumesGrowingAfterARepeat() {
    assertThat(sut.sumCounts(List.of(1, 2, 1, 3))).isEqualTo(38);
  }

  // Step 7: a subarray that drops one copy of a value may still hold another. Sliding a window of
  //         fixed length with a plain set turns [1, 1] into [1, 2] by removing the outgoing 1,
  //         which erases 1 from the set though the second 1 is still inside, scores [1, 2] as 1
  //         instead of 4, and answers 9. Copies have to be counted, not merely marked present
  @Test
  void droppingOneCopyKeepsTheValueWhileAnotherRemains() {
    assertThat(sut.sumCounts(List.of(1, 1, 2))).isEqualTo(12);
  }

  // Step 8: values reach 100 (1 <= nums[i] <= 100). Example 1's shape with 100 in place of each
  //         1. A seen-array of size 100 indexed by value throws ArrayIndexOutOfBoundsException at
  //         the first element; it needs 101 slots, or the value shifted down by one
  @Test
  void valueAtTheCeilingIsTracked() {
    assertThat(sut.sumCounts(List.of(100, 1, 100))).isEqualTo(15);
  }

  // ===========================================================================================
  // The official examples (Steps 9-10).
  // ===========================================================================================

  // Step 9: LeetCode Example 1. The two 1s are not adjacent, so picking indices 0 and 2 is not a
  //         subarray. Summing over subsequences instead adds [1, 1] and answers 16, counting runs
  //         scores [1, 2, 1] as 9 and answers 20, and breaking at the first repeat answers 11
  @Test
  void leetCodeExample1() {
    assertThat(sut.sumCounts(List.of(1, 2, 1))).isEqualTo(15);
  }

  // Step 10: LeetCode Example 2. [1, 1] has one distinct value even though it has two elements,
  //          and that value occurs twice. Squaring lengths answers 6, and counting only the values
  //          that occur exactly once answers 2
  @Test
  void leetCodeExample2() {
    assertThat(sut.sumCounts(List.of(1, 1))).isEqualTo(3);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= nums.length <= 100, 1 <= nums[i] <= 100).
  //
  // At n = 100 there are 5,050 subarrays with 171,700 elements between them, so rebuilding each
  // subarray's set from scratch (cubic) finishes in milliseconds, as does extending one set per
  // start (quadratic). The timeouts do not separate those. They separate polynomial from
  // exponential: reading "subarray" as "subsequence" enumerates 2^100 - 1 subsets and never
  // finishes. The largest possible answer is Step 11's 8,670,850, so an int cannot overflow.
  //
  // They run on a separate thread because JUnit's default SAME_THREAD mode only compares elapsed
  // time after the method returns: against a solution slow enough to matter, a same-thread timeout
  // hangs the build instead of reporting the failure it exists to report.
  // ===========================================================================================

  // Step 11: 1 through 100, every value distinct. Step 4's sum of squared lengths at full size,
  //          which is the largest answer the constraints allow
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllDistinctGivesTheLargestAnswer() {
    assertThat(sut.sumCounts(generated(100, i -> i + 1))).isEqualTo(8_670_850);
  }

  // Step 12: 100 copies of the ceiling value. Step 3 at full size: the answer is the number of
  //          subarrays, 100 * 101 / 2, and squaring lengths answers Step 11's 8,670,850
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthUniformAtTheValueCeiling() {
    assertThat(sut.sumCounts(Collections.nCopies(100, 100))).isEqualTo(5_050);
  }

  // Step 13: 1 through 10 repeated ten times. No two neighbors are equal, yet every subarray of
  //          length 10 or more has exactly 10 distinct values. Counting runs or squaring lengths
  //          both answer 8,670,850
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthDistinctCountSaturatesAtTen() {
    assertThat(sut.sumCounts(generated(100, i -> i % 10 + 1))).isEqualTo(445_360);
  }

  // ===========================================================================================
  // Hygiene (Step 14).
  // ===========================================================================================

  // Step 14: several arrays answered by one instance, the largest in the middle and the sizes
  //          deliberately out of order. A solution that keeps its seen-array, counts, or running
  //          total on the instance instead of resetting them per call answers the later arrays
  //          with the earlier ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnySizeInAnyOrder() {
    assertThat(sut.sumCounts(List.of(1, 2, 1))).isEqualTo(15);
    assertThat(sut.sumCounts(generated(100, i -> i + 1))).isEqualTo(8_670_850);
    assertThat(sut.sumCounts(List.of(7))).isEqualTo(1);
    assertThat(sut.sumCounts(List.of(4, 4, 4))).isEqualTo(6);
    assertThat(sut.sumCounts(List.of(1, 2))).isEqualTo(6);
  }

  private static List<Integer> generated(int n, IntUnaryOperator valueAt) {
    return IntStream.range(0, n).map(valueAt).boxed().toList();
  }
}
