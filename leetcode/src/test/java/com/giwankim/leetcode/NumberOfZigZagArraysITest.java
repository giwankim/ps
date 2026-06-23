package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOfZigZagArraysITest {
  NumberOfZigZagArraysI sut = new NumberOfZigZagArraysI();

  // Step 1: LeetCode Example 1 — the narrowest range (just two values) at the shortest length;
  // the only zigzags are [4,5,4] and [5,4,5], so both starting directions are counted
  @Test
  void lengthThreeOnTwoValuesCountsBothDirections() {
    assertThat(sut.zigZagArrays(3, 4, 5)).isEqualTo(2);
  }

  // Step 2: with exactly two distinct values a zigzag must strictly alternate, so there are still
  // only two arrays no matter how long it gets — guards against counting runs like [1,2,2,1]
  @Test
  void twoValuesAllowOnlyStrictAlternationRegardlessOfLength() {
    assertThat(sut.zigZagArrays(4, 1, 2)).isEqualTo(2);
  }

  // Step 3: LeetCode Example 2 — three values give 10 triples; this is the first case that
  // exercises
  // the "no three consecutive monotonic" rule, which excludes [1,2,3] and [3,2,1]
  @Test
  void lengthThreeOnThreeValuesExcludesMonotoneTriples() {
    assertThat(sut.zigZagArrays(3, 1, 3)).isEqualTo(10);
  }

  // Step 4: the conditions are purely relative comparisons, so shifting every value up by 3 leaves
  // the count unchanged — [4,6] spans the same three values as Example 2 and still gives 10
  @Test
  void countDependsOnRangeWidthNotItsOffset() {
    assertThat(sut.zigZagArrays(3, 4, 6)).isEqualTo(10);
  }

  // Step 5: the same offset-invariance pressed against the value ceiling — l=1998 is far from 1,
  // yet
  // the three-value range still yields 10; an implementation sized by r rather than (r-l+1) fails
  // here
  @Test
  void offsetInvarianceHoldsNearTheValueCeiling() {
    assertThat(sut.zigZagArrays(3, 1998, 2000)).isEqualTo(10);
  }

  // Step 6: widening the range to four values grows the length-three count to 28
  // (2 * (0^2 + 1^2 + 2^2 + 3^2)), pinning the dependence on the number of available values
  @Test
  void widerRangeAddsMoreLengthThreeArrays() {
    assertThat(sut.zigZagArrays(3, 1, 4)).isEqualTo(28);
  }

  // Step 7: holding the range at three values but extending the length to four gives 16 — the count
  // also depends on n, so this complements Step 3 by moving the other dimension
  @Test
  void extraLengthOnThreeValuesGrowsTheCount() {
    assertThat(sut.zigZagArrays(4, 1, 3)).isEqualTo(16);
  }

  // Step 8: a medium case away from any boundary — length five over five values gives 492, a value
  // a
  // closed-form-only or off-by-one transition would be unlikely to reproduce by accident
  @Test
  void mediumCaseFiveValuesLengthFive() {
    assertThat(sut.zigZagArrays(5, 1, 5)).isEqualTo(492);
  }

  // Step 9: with l=1, r=2000 the raw length-three count is 2 * sum(k^2, k=0..1999) = 5,329,334,000,
  // which exceeds 1e9+7 about fivefold; the reported answer must be that total mod 1e9+7
  @Test
  void largeCountIsReducedModuloOneBillionSeven() {
    assertThat(sut.zigZagArrays(3, 1, 2000)).isEqualTo(329_333_965);
  }

  // Step 10: the longest array (n = 2000) over just two values — still exactly 2 — confirms the
  // length loop runs to the constraint ceiling without overflowing or drifting off strict
  // alternation
  @Test
  void maxLengthOnTwoValuesStaysTwo() {
    assertThat(sut.zigZagArrays(2000, 1, 2)).isEqualTo(2);
  }

  // Step 11: both dimensions at their maxima (n = 2000, range = 2000 values) — the answer
  // 594,850,306 combines a constraint-ceiling workload with modular reduction in a single case
  @Test
  void constraintCeilingCombinesLengthRangeAndModulo() {
    assertThat(sut.zigZagArrays(2000, 1, 2000)).isEqualTo(594_850_306);
  }
}
