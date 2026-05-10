package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class FindFirstAndLastPositionOfElementInSortedArrayTest {
  FindFirstAndLastPositionOfElementInSortedArray sut =
      new FindFirstAndLastPositionOfElementInSortedArray();

  // Step 1: LeetCode Example 3 — empty input cannot contain the target.
  @Test
  void leetCodeExampleThreeEmptyArrayReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {}, 0)).containsExactly(-1, -1);
  }

  // Step 2: single element equal to target returns the same start and end index.
  @Test
  void singleElementEqualToTargetReturnsOnlyIndex() {
    assertThat(sut.searchRange(new int[] {5}, 5)).containsExactly(0, 0);
  }

  // Step 3: single element different from target returns not found.
  @Test
  void singleElementDifferentFromTargetReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {5}, 6)).containsExactly(-1, -1);
  }

  // Step 4: two equal elements both matching target span the whole array.
  @Test
  void twoElementsBothMatchTargetReturnsWholeArray() {
    assertThat(sut.searchRange(new int[] {2, 2}, 2)).containsExactly(0, 1);
  }

  // Step 5: two elements with target only at the left edge return a one-index range.
  @Test
  void twoElementsTargetAtFirstOnlyReturnsFirstIndex() {
    assertThat(sut.searchRange(new int[] {2, 4}, 2)).containsExactly(0, 0);
  }

  // Step 6: two elements with target only at the right edge return a one-index range.
  @Test
  void twoElementsTargetAtLastOnlyReturnsLastIndex() {
    assertThat(sut.searchRange(new int[] {2, 4}, 4)).containsExactly(1, 1);
  }

  // Step 7: one interior occurrence still returns [index, index].
  @Test
  void targetAppearsOnceInMiddleReturnsSameStartAndEnd() {
    assertThat(sut.searchRange(new int[] {1, 3, 5, 7, 9}, 5)).containsExactly(2, 2);
  }

  // Step 8: duplicate target block at the start returns the prefix bounds.
  @Test
  void targetAppearsAsPrefixReturnsPrefixBounds() {
    assertThat(sut.searchRange(new int[] {2, 2, 2, 4, 6}, 2)).containsExactly(0, 2);
  }

  // Step 9: duplicate target block at the end returns the suffix bounds.
  @Test
  void targetAppearsAsSuffixReturnsSuffixBounds() {
    assertThat(sut.searchRange(new int[] {1, 3, 5, 5, 5}, 5)).containsExactly(2, 4);
  }

  // Step 10: all elements matching target returns the full range.
  @Test
  void everyElementIsTargetReturnsWholeArray() {
    assertThat(sut.searchRange(new int[] {8, 8, 8, 8}, 8)).containsExactly(0, 3);
  }

  // Step 11: interior duplicate block returns both boundaries, not just one hit.
  @Test
  void targetAppearsAsInteriorBlockReturnsInteriorBounds() {
    assertThat(sut.searchRange(new int[] {1, 2, 4, 4, 4, 7, 9}, 4)).containsExactly(2, 4);
  }

  // Step 12: LeetCode Example 1 — target appears twice in the middle.
  @Test
  void leetCodeExampleOneTargetRangeInMiddle() {
    assertThat(sut.searchRange(new int[] {5, 7, 7, 8, 8, 10}, 8)).containsExactly(3, 4);
  }

  // Step 13: LeetCode Example 2 — target is absent between existing values.
  @Test
  void leetCodeExampleTwoMissingTargetReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {5, 7, 7, 8, 8, 10}, 6)).containsExactly(-1, -1);
  }

  // Step 14: target smaller than every element returns not found.
  @Test
  void targetSmallerThanAllElementsReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {3, 5, 7}, 1)).containsExactly(-1, -1);
  }

  // Step 15: target larger than every element returns not found.
  @Test
  void targetLargerThanAllElementsReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {3, 5, 7}, 9)).containsExactly(-1, -1);
  }

  // Step 16: missing target in a gap between duplicate blocks returns not found.
  @Test
  void targetMissingBetweenDuplicateBlocksReturnsNotFound() {
    assertThat(sut.searchRange(new int[] {1, 1, 2, 2, 4, 4}, 3)).containsExactly(-1, -1);
  }

  // Step 17: negative sorted values follow the same boundary rules.
  @Test
  void negativeValuesReturnTargetRange() {
    assertThat(sut.searchRange(new int[] {-10, -5, -5, -5, -1}, -5)).containsExactly(1, 3);
  }

  // Step 18: lower constraint value can be found at the left boundary.
  @Test
  void minimumConstraintValueAtLeftBoundaryReturnsRange() {
    assertThat(sut.searchRange(new int[] {-1_000_000_000, -1_000_000_000, 0}, -1_000_000_000))
        .containsExactly(0, 1);
  }

  // Step 19: upper constraint value can be found at the right boundary.
  @Test
  void maximumConstraintValueAtRightBoundaryReturnsRange() {
    assertThat(sut.searchRange(new int[] {0, 1_000_000_000, 1_000_000_000}, 1_000_000_000))
        .containsExactly(1, 2);
  }

  // Step 20: maximum constrained length supports a target block in the middle of the array.
  @Test
  void maximumLengthArrayReturnsInteriorTargetBounds() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 0, 40_000, -1);
    Arrays.fill(nums, 40_000, 60_000, 0);
    Arrays.fill(nums, 60_000, n, 1);

    assertThat(sut.searchRange(nums, 0)).containsExactly(40_000, 59_999);
  }

  // Step 21: maximum constrained length with all elements equal returns both extreme indexes.
  @Test
  void maximumLengthArrayAllTargetsReturnsWholeArray() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 7);

    assertThat(sut.searchRange(nums, 7)).containsExactly(0, n - 1);
  }
}
