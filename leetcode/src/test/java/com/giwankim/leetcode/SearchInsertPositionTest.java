package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SearchInsertPositionTest {
  SearchInsertPosition sut = new SearchInsertPosition();

  // Step 1: single-element array — target equals the only element, return its index
  @Test
  void singleElementEqualToTargetReturnsZero() {
    assertThat(sut.searchInsert(new int[] {5}, 5)).isZero();
  }

  // Step 2: single-element array — target smaller than the only element, insert at left edge
  @Test
  void singleElementGreaterThanTargetReturnsZero() {
    assertThat(sut.searchInsert(new int[] {5}, 3)).isZero();
  }

  // Step 3: single-element array — target larger, insert past the end (locks return == length)
  @Test
  void singleElementLessThanTargetReturnsLength() {
    assertThat(sut.searchInsert(new int[] {5}, 7)).isEqualTo(1);
  }

  // Step 4: two-element array — target equals the first element
  @Test
  void twoElementsTargetMatchesFirst() {
    assertThat(sut.searchInsert(new int[] {3, 7}, 3)).isZero();
  }

  // Step 5: two-element array — target equals the last element
  @Test
  void twoElementsTargetMatchesLast() {
    assertThat(sut.searchInsert(new int[] {3, 7}, 7)).isEqualTo(1);
  }

  // Step 6: two-element array — target falls between the two elements
  @Test
  void twoElementsTargetBetweenInsertsAtMiddle() {
    assertThat(sut.searchInsert(new int[] {3, 7}, 5)).isEqualTo(1);
  }

  // Step 7: target smaller than every element in a multi-element array — insert at zero
  @Test
  void targetSmallerThanAllReturnsZero() {
    assertThat(sut.searchInsert(new int[] {2, 4, 6, 8}, 1)).isZero();
  }

  // Step 8: target larger than every element — insert at array length (right-edge regression)
  @Test
  void targetLargerThanAllReturnsLength() {
    assertThat(sut.searchInsert(new int[] {2, 4, 6, 8}, 10)).isEqualTo(4);
  }

  // Step 9: odd-sized array — target equals the exact middle element
  @Test
  void targetMatchesMiddleOfOddSizedArray() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 7, 9}, 5)).isEqualTo(2);
  }

  // Step 10: odd-sized array — target falls into a gap between two interior elements
  @Test
  void targetInGapBetweenInteriorElementsInsertsAtGap() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 7, 9}, 6)).isEqualTo(3);
  }

  // Step 11: LeetCode Example 1 — target found in the middle of the array
  @Test
  void leetCodeExampleOneTargetFound() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 6}, 5)).isEqualTo(2);
  }

  // Step 12: LeetCode Example 2 — target inserts at the gap after the first element
  @Test
  void leetCodeExampleTwoTargetInsertsInGap() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 6}, 2)).isEqualTo(1);
  }

  // Step 13: LeetCode Example 3 — target larger than every element
  @Test
  void leetCodeExampleThreeTargetBeyondAll() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 6}, 7)).isEqualTo(4);
  }

  // Step 14: LeetCode Example 4 — target smaller than the first element
  @Test
  void leetCodeExampleFourTargetBeforeFirst() {
    assertThat(sut.searchInsert(new int[] {1, 3, 5, 6}, 0)).isZero();
  }

  // Step 15: negative values are ordered correctly — insert in an interior gap
  @Test
  void negativeValuesInsertedInCorrectGap() {
    assertThat(sut.searchInsert(new int[] {-5, -2, 0, 3}, -3)).isEqualTo(1);
  }
}
