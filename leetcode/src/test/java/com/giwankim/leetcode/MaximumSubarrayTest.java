package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumSubarrayTest {
  MaximumSubarray sut = new MaximumSubarray();

  // Step 1: LC Example 2 — [1] → 1. Smallest input per the constraint nums.length >= 1.
  @Test
  void singlePositiveElement() {
    assertThat(sut.maxSubArray(new int[] {1})).isEqualTo(1);
    assertThat(sut.maxSubArray2(new int[] {1})).isEqualTo(1);
  }

  // Step 2: single negative element — guards against an empty-subarray fallback that would return
  // 0.
  @Test
  void singleNegativeElement() {
    assertThat(sut.maxSubArray(new int[] {-3})).isEqualTo(-3);
    assertThat(sut.maxSubArray2(new int[] {-3})).isEqualTo(-3);
  }

  // Step 3: smallest "extend" case — combining beats picking either alone.
  @Test
  void twoPositiveElementsSum() {
    assertThat(sut.maxSubArray(new int[] {1, 2})).isEqualTo(3);
    assertThat(sut.maxSubArray2(new int[] {1, 2})).isEqualTo(3);
  }

  // Step 4: smallest "stop early" case — extending past the peak reduces the sum.
  @Test
  void twoElementsTrailingNegativeIsExcluded() {
    assertThat(sut.maxSubArray(new int[] {1, -1})).isEqualTo(1);
    assertThat(sut.maxSubArray2(new int[] {1, -1})).isEqualTo(1);
  }

  // Step 5: all-positive input — guards against accidental truncation.
  @Test
  void allPositiveElementsSumToWhole() {
    assertThat(sut.maxSubArray(new int[] {1, 2, 3, 4})).isEqualTo(10);
    assertThat(sut.maxSubArray2(new int[] {1, 2, 3, 4})).isEqualTo(10);
  }

  // Step 6: LC Example 3 — [5, 4, -1, 7, 8] → 23. Interior negative does not break the running sum,
  // so the whole array remains optimal.
  @Test
  void wholeArrayOptimalDespiteInteriorNegative() {
    assertThat(sut.maxSubArray(new int[] {5, 4, -1, 7, 8})).isEqualTo(23);
    assertThat(sut.maxSubArray2(new int[] {5, 4, -1, 7, 8})).isEqualTo(23);
  }

  // Step 7: all negative — guards against a maxSum = 0 initialization bug.
  @Test
  void allNegativeElementsReturnLeastNegative() {
    assertThat(sut.maxSubArray(new int[] {-3, -1, -4, -1, -5})).isEqualTo(-1);
    assertThat(sut.maxSubArray2(new int[] {-3, -1, -4, -1, -5})).isEqualTo(-1);
  }

  // Step 8: optimal at the start — algorithm must not extend past the peak.
  @Test
  void bestSubarrayAtStart() {
    assertThat(sut.maxSubArray(new int[] {5, -1, -2})).isEqualTo(5);
    assertThat(sut.maxSubArray2(new int[] {5, -1, -2})).isEqualTo(5);
  }

  // Step 9: optimal at the end — algorithm must consider trailing positions (mirror of Step 8).
  @Test
  void bestSubarrayAtEnd() {
    assertThat(sut.maxSubArray(new int[] {-1, -2, 5})).isEqualTo(5);
    assertThat(sut.maxSubArray2(new int[] {-1, -2, 5})).isEqualTo(5);
  }

  // Step 10: optimal in the middle — guards against off-by-one boundary bugs.
  @Test
  void bestSubarrayInMiddle() {
    assertThat(sut.maxSubArray(new int[] {-1, 5, -1})).isEqualTo(5);
    assertThat(sut.maxSubArray2(new int[] {-1, 5, -1})).isEqualTo(5);
  }

  // Step 11: a large intermediate negative breaks the running sum so the algorithm must drop the
  // prefix and restart.
  @Test
  void largeNegativeForcesReset() {
    assertThat(sut.maxSubArray(new int[] {3, -100, 4})).isEqualTo(4);
    assertThat(sut.maxSubArray2(new int[] {3, -100, 4})).isEqualTo(4);
  }

  // Step 12: LC Example 1 — subarray [4, -1, 2, 1] sums to 6.
  @Test
  void classicLeetCodeExample() {
    assertThat(sut.maxSubArray(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4})).isEqualTo(6);
    assertThat(sut.maxSubArray2(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4})).isEqualTo(6);
  }

  // Step 13: maximum length per the LC constraint nums.length <= 10^5 — exercises linear-time
  // scaling at the upper bound.
  @Test
  void maxLengthArrayPerLeetCodeConstraint() {
    int[] nums = new int[100_000];
    Arrays.fill(nums, 1);
    assertThat(sut.maxSubArray(nums)).isEqualTo(100_000);
    assertThat(sut.maxSubArray2(nums)).isEqualTo(100_000);
  }
}
