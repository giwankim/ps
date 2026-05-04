package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumSumCircularSubarrayTest {
  MaximumSumCircularSubarray sut = new MaximumSumCircularSubarray();

  // Smallest input per the LC constraint nums.length >= 1, with a positive value.
  @Test
  void singlePositiveElement() {
    assertThat(sut.maxSubarraySumCircular(new int[] {1})).isEqualTo(1);
  }

  // Single negative — guards against an empty-subarray fallback that would return 0.
  @Test
  void singleNegativeElement() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-1})).isEqualTo(-1);
  }

  // Smallest "extend" case — combining beats picking either alone (basic Kadane).
  @Test
  void twoPositiveElementsSum() {
    assertThat(sut.maxSubarraySumCircular(new int[] {1, 2})).isEqualTo(3);
  }

  // Smallest "stop early" case — extending past a positive into a negative reduces the sum.
  @Test
  void twoElementsTrailingNegativeIsExcluded() {
    assertThat(sut.maxSubarraySumCircular(new int[] {1, -1})).isEqualTo(1);
  }

  // All-positive input — the whole array is optimal; a wrap cannot improve on it.
  @Test
  void allPositiveElementsSumToWhole() {
    assertThat(sut.maxSubarraySumCircular(new int[] {1, 2, 3, 4})).isEqualTo(10);
  }

  // Optimal at the start — algorithm must not extend past the peak.
  @Test
  void bestSubarrayAtStart() {
    assertThat(sut.maxSubarraySumCircular(new int[] {5, -1, -2})).isEqualTo(5);
  }

  // Optimal at the end — algorithm must consider trailing positions (mirror of
  // bestSubarrayAtStart).
  @Test
  void bestSubarrayAtEnd() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-2, -1, 5})).isEqualTo(5);
  }

  // Optimal in the middle — guards against off-by-one boundary bugs.
  @Test
  void bestSubarrayInMiddle() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-1, 5, -1})).isEqualTo(5);
  }

  // LC Example 1 — [1, -2, 3, -2] → 3. Optimal is the single element [3]; wrap does not help.
  @Test
  void leetCodeExampleOneNoWrapNeeded() {
    assertThat(sut.maxSubarraySumCircular(new int[] {1, -2, 3, -2})).isEqualTo(3);
  }

  // LC Example 2 — [5, -3, 5] → 10. Wrapping subarray [5, 5] (indices 2 → 0) beats every linear
  // subarray. The smallest input that fails for a plain Kadane implementation; forces the
  // "total − minSubarraySum" branch.
  @Test
  void wrapAroundBeatsLinear() {
    assertThat(sut.maxSubarraySumCircular(new int[] {5, -3, 5})).isEqualTo(10);
  }

  // A single deep interior negative — wrap [4, 3] (indices 2 → 0) sums to 7, while the best
  // linear subarray is just [4]. Confirms the wrap branch handles a large-magnitude middle gap.
  @Test
  void wrapSkipsLargeInteriorNegative() {
    assertThat(sut.maxSubarraySumCircular(new int[] {3, -100, 4})).isEqualTo(7);
  }

  // Two equal peaks separated by a negative run — wrap [3, 3] (indices 3 → 0) sums to 6. Linear
  // Kadane returns only 4 (the trailing 3 plus −1 plus 3). Forces minimum-subarray to span
  // multiple elements.
  @Test
  void wrapJoinsTwoPeaksAcrossNegativeRun() {
    assertThat(sut.maxSubarraySumCircular(new int[] {3, -1, -1, 3})).isEqualTo(6);
  }

  // All-negative with identical values — total − minSubarraySum = 0 here, which corresponds to an
  // empty subarray and is forbidden. The result must come from the linear branch.
  @Test
  void allNegativeSameValueReturnsSingleElement() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-2, -2, -2})).isEqualTo(-2);
  }

  // LC Example 3 — [-3, -2, -3] → -2. All-negative mixed values: must return the least negative,
  // not 0. Catches an implementation that takes max(linear, total − minSubarraySum) without the
  // empty-subarray guard.
  @Test
  void leetCodeExampleThreeAllNegative() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-3, -2, -3})).isEqualTo(-2);
  }

  // Maximum length per the LC constraint nums.length <= 3 * 10^4 — exercises linear-time scaling.
  @Test
  void maxLengthArrayPerLeetCodeConstraint() {
    int[] nums = new int[30_000];
    Arrays.fill(nums, 1);
    assertThat(sut.maxSubarraySumCircular(nums)).isEqualTo(30_000);
  }

  // n=2 with leading negative — [-1, 1] → 1. Completes the n=2 sign-permutation grid alongside
  // twoPositiveElementsSum ([+,+]) and twoElementsTrailingNegativeIsExcluded ([+,−]); guards
  // against asymmetric handling of element order.
  @Test
  void twoElementsLeadingNegativeIsExcluded() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-1, 1})).isEqualTo(1);
  }

  // n=2, both negative — [-1, -2] → -1. Smallest input where total − minSubarraySum = 0 (empty
  // subarray, forbidden), so the linear branch must win. Exercises the empty-subarray guard at the
  // minimum-length scale (a sharper version of the n=3 guard in
  // allNegativeSameValueReturnsSingleElement).
  @Test
  void twoNegativeElementsReturnLeastNegative() {
    assertThat(sut.maxSubarraySumCircular(new int[] {-1, -2})).isEqualTo(-1);
  }

  // Boundary values per the LC constraint -3*10^4 <= nums[i] <= 3*10^4. Wrap [30000, 30000]
  // (indices 2 → 0) sums to 60000 and beats every linear subarray. Guards against implementations
  // that use a narrower accumulator (e.g. short) or clamp at smaller magnitudes.
  @Test
  void boundaryMagnitudeValuesAtConstraintEdge() {
    assertThat(sut.maxSubarraySumCircular(new int[] {30_000, -30_000, 30_000})).isEqualTo(60_000);
  }

  // Maximum length combined with maximum value — n * max(nums[i]) = 9 * 10^8 fits in a 32-bit int
  // (Integer.MAX_VALUE ≈ 2.147 * 10^9), guarding against overflow with a smaller accumulator type
  // or premature narrowing. maxLengthArrayPerLeetCodeConstraint only stresses length; this
  // stresses the worst-case sum magnitude the LC constraints permit.
  @Test
  void maxLengthAtMaxValueDoesNotOverflow() {
    int[] nums = new int[30_000];
    Arrays.fill(nums, 30_000);
    assertThat(sut.maxSubarraySumCircular(nums)).isEqualTo(900_000_000);
  }
}
