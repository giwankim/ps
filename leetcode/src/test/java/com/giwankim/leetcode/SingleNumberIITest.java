package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SingleNumberIITest {
  SingleNumberII sut = new SingleNumberII();

  // Step 1: smallest valid input per nums.length >= 1 — the lone value is already the answer.
  @Test
  void singleElementReturnsItself() {
    assertThat(sut.singleNumber(new int[] {7})).isEqualTo(7);
    assertThat(sut.singleNumber2(new int[] {7})).isEqualTo(7);
  }

  // Step 2: LeetCode Example 1 — one triplet plus a single positive value.
  @Test
  void leetCodeExample1() {
    assertThat(sut.singleNumber(new int[] {2, 2, 3, 2})).isEqualTo(3);
    assertThat(sut.singleNumber2(new int[] {2, 2, 3, 2})).isEqualTo(3);
  }

  // Step 3: same smallest triplet shape with the unique value at the head.
  @Test
  void uniqueAtStart() {
    assertThat(sut.singleNumber(new int[] {3, 2, 2, 2})).isEqualTo(3);
    assertThat(sut.singleNumber2(new int[] {3, 2, 2, 2})).isEqualTo(3);
  }

  // Step 4: unique value in the middle — rules out shortcuts that only inspect the edges.
  @Test
  void uniqueInMiddle() {
    assertThat(sut.singleNumber(new int[] {2, 3, 2, 2})).isEqualTo(3);
    assertThat(sut.singleNumber2(new int[] {2, 3, 2, 2})).isEqualTo(3);
  }

  // Step 5: LeetCode Example 2 — multiple interleaved triplets including zero.
  @Test
  void leetCodeExample2() {
    assertThat(sut.singleNumber(new int[] {0, 1, 0, 1, 0, 1, 99})).isEqualTo(99);
    assertThat(sut.singleNumber2(new int[] {0, 1, 0, 1, 0, 1, 99})).isEqualTo(99);
  }

  // Step 6: the unique element is 0 — guards against treating 0 as an uninitialized answer.
  @Test
  void zeroAsUnique() {
    assertThat(sut.singleNumber(new int[] {5, -1, 5, -1, 5, -1, 0})).isZero();
    assertThat(sut.singleNumber2(new int[] {5, -1, 5, -1, 5, -1, 0})).isZero();
  }

  // Step 7: the unique element is negative — sign bits must be preserved.
  @Test
  void negativeUnique() {
    assertThat(sut.singleNumber(new int[] {-2, -2, 1, 1, -3, 1, -2})).isEqualTo(-3);
    assertThat(sut.singleNumber2(new int[] {-2, -2, 1, 1, -3, 1, -2})).isEqualTo(-3);
  }

  // Step 8: negative triplets with a positive unique value — repeated negatives must cancel by
  // count, not by arithmetic sign assumptions.
  @Test
  void negativeTripletsWithPositiveUnique() {
    assertThat(sut.singleNumber(new int[] {-4, 6, -4, -4, -7, -7, -7})).isEqualTo(6);
    assertThat(sut.singleNumber2(new int[] {-4, 6, -4, -4, -7, -7, -7})).isEqualTo(6);
  }

  // Step 9: Integer.MIN_VALUE as the unique value — exercises the highest two's-complement bit.
  @Test
  void integerMinValueAsUnique() {
    int[] nums = {
      Integer.MAX_VALUE, Integer.MIN_VALUE, 12, 12, 12, Integer.MAX_VALUE, Integer.MAX_VALUE
    };

    assertThat(sut.singleNumber(nums)).isEqualTo(Integer.MIN_VALUE);
    assertThat(sut.singleNumber2(nums)).isEqualTo(Integer.MIN_VALUE);
  }

  // Step 10: Integer.MAX_VALUE as the unique value while MIN_VALUE appears three times.
  @Test
  void integerMaxValueAsUnique() {
    int[] nums = {
      Integer.MIN_VALUE, Integer.MAX_VALUE, -9, Integer.MIN_VALUE, -9, -9, Integer.MIN_VALUE
    };

    assertThat(sut.singleNumber(nums)).isEqualTo(Integer.MAX_VALUE);
    assertThat(sut.singleNumber2(nums)).isEqualTo(Integer.MAX_VALUE);
  }

  // Step 11: many distinct triplets in mixed order — confirms the count is global, not adjacent.
  @Test
  void manyInterleavedTriplets() {
    assertThat(sut.singleNumber(new int[] {10, -2, 10, 4, -2, 4, 99, 10, 4, -2, 8, 8, 8}))
        .isEqualTo(99);
    assertThat(sut.singleNumber2(new int[] {10, -2, 10, 4, -2, 4, 99, 10, 4, -2, 8, 8, 8}))
        .isEqualTo(99);
  }

  // Step 12: largest valid length under nums.length <= 3 * 10^4 and the triplet-plus-one shape.
  // 9 999 triplets plus one unique value -> length 29 998.
  @Test
  void largeInputAtConstraintBound() {
    int[] nums = new int[29_998];
    for (int i = 0; i < nums.length - 1; i++) {
      nums[i] = i / 3;
    }
    nums[nums.length - 1] = -1;

    assertThat(sut.singleNumber(nums)).isEqualTo(-1);
    assertThat(sut.singleNumber2(nums)).isEqualTo(-1);
  }
}
