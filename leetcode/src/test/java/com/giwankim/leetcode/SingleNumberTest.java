package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SingleNumberTest {
  SingleNumber sut = new SingleNumber();

  // Step 1: smallest valid input per the constraint nums.length >= 1 — forces a base case that
  // returns the lone element rather than a hardcoded constant.
  @Test
  void singleElementReturnsItself() {
    assertThat(sut.singleNumber(new int[] {7})).isEqualTo(7);
  }

  // Step 2: smallest pair-plus-unique with the unique at the tail.
  @Test
  void uniqueAtEnd() {
    assertThat(sut.singleNumber(new int[] {1, 1, 2})).isEqualTo(2);
  }

  // Step 3: same shape with the unique at the head — guards a "skip first" or "trust last" bug.
  @Test
  void uniqueAtStart() {
    assertThat(sut.singleNumber(new int[] {2, 1, 1})).isEqualTo(2);
  }

  // Step 4: unique flanked by its pair — forces full traversal across all positions.
  @Test
  void uniqueInMiddle() {
    assertThat(sut.singleNumber(new int[] {1, 2, 1})).isEqualTo(2);
  }

  // Step 5: LeetCode Example 1 — [2, 2, 1] → 1.
  @Test
  void leetCodeExample1() {
    assertThat(sut.singleNumber(new int[] {2, 2, 1})).isEqualTo(1);
  }

  // Step 6: LeetCode Example 2 — [4, 1, 2, 1, 2] → 4. Multiple interleaved pairs rule out naive
  // "compare adjacent elements" shortcuts.
  @Test
  void leetCodeExample2() {
    assertThat(sut.singleNumber(new int[] {4, 1, 2, 1, 2})).isEqualTo(4);
  }

  // Step 7: negative two's-complement values still cancel correctly — guards against a sum-based
  // shortcut that relies on positive magnitudes or risks overflow.
  @Test
  void negativeNumbers() {
    assertThat(sut.singleNumber(new int[] {-1, -1, -2})).isEqualTo(-2);
  }

  // Step 8: the unique element is 0 — guards against an "init result to 0 means no answer found"
  // bug.
  @Test
  void zeroAsUnique() {
    assertThat(sut.singleNumber(new int[] {1, 0, 1})).isZero();
  }

  // Step 9: a zero pair appears alongside the unique — confirms zero pairs do not perturb the
  // running answer (0 XOR x = x).
  @Test
  void zeroPairWithUnique() {
    assertThat(sut.singleNumber(new int[] {0, 0, 5})).isEqualTo(5);
  }

  // Step 10: maximum-sized input near the LC constraint nums.length <= 3 * 10^4 — exercises
  // linear-time scaling at the upper bound. 14 999 pairs of 1 plus a single 7 → length 29 999.
  @Test
  void largeInputAtConstraintBound() {
    int[] nums = new int[29_999];
    for (int i = 0; i < 29_998; i++) {
      nums[i] = 1;
    }
    nums[29_998] = 7;
    assertThat(sut.singleNumber(nums)).isEqualTo(7);
  }
}
