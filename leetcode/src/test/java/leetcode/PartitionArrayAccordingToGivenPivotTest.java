package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PartitionArrayAccordingToGivenPivotTest {
  PartitionArrayAccordingToGivenPivot sut = new PartitionArrayAccordingToGivenPivot();

  // Step 1: smallest valid input — pivot must equal an element, so the minimum
  // case is a length-1 array holding only the pivot itself (1 <= nums.length)
  @Test
  void singleElementEqualToPivotIsUnchanged() {
    assertThat(sut.pivotArray(new int[] {5}, 5)).containsExactly(5);
  }

  // Step 2: every element equals the pivot — the whole array is the "equal" block
  @Test
  void allElementsEqualToPivotStayTogether() {
    assertThat(sut.pivotArray(new int[] {7, 7, 7}, 7)).containsExactly(7, 7, 7);
  }

  // Step 3: only elements less than the pivot — they keep their original order
  // and the lone pivot lands at the end (no "greater" block exists)
  @Test
  void elementsLessThanPivotComeFirstInOriginalOrder() {
    assertThat(sut.pivotArray(new int[] {3, 1, 2, 5}, 5)).containsExactly(3, 1, 2, 5);
  }

  // Step 4: only elements greater than the pivot — they keep their original order
  // and the lone pivot lands at the front (no "less" block exists)
  @Test
  void elementsGreaterThanPivotComeLastInOriginalOrder() {
    assertThat(sut.pivotArray(new int[] {5, 9, 7, 8}, 5)).containsExactly(5, 9, 7, 8);
  }

  // Step 5: both sides present around a single pivot — less block, pivot, greater
  // block, each side independently preserving the order it had in the input
  @Test
  void lessAndGreaterArePartitionedAroundSinglePivot() {
    assertThat(sut.pivotArray(new int[] {4, 1, 6, 2, 3}, 3)).containsExactly(1, 2, 3, 4, 6);
  }

  // Step 6: LeetCode Example 1 — multiple pivot occurrences are grouped together
  // in the middle, between the less and greater blocks
  @Test
  void multiplePivotOccurrencesAreGroupedInTheMiddle() {
    assertThat(sut.pivotArray(new int[] {9, 12, 5, 10, 14, 3, 10}, 10))
        .containsExactly(9, 5, 3, 10, 10, 12, 14);
  }

  // Step 7: LeetCode Example 2 — negative values are handled like any other number
  // relative to the pivot (-10^6 <= nums[i] <= 10^6)
  @Test
  void handlesNegativeValues() {
    assertThat(sut.pivotArray(new int[] {-3, 4, 3, 2}, 2)).containsExactly(-3, 2, 4, 3);
  }

  // Step 8: the greater block keeps input order rather than being sorted — here a
  // sort would yield [1, 2, 4, 5, 6, 9], so this pins down "stable", not "sorted"
  @Test
  void preservesRelativeOrderRatherThanSorting() {
    assertThat(sut.pivotArray(new int[] {6, 1, 9, 2, 5, 4}, 4)).containsExactly(1, 2, 4, 6, 9, 5);
  }

  // Step 9: duplicates on the less and greater sides, plus several pivots — every
  // group preserves order and the equal block sits between the two outer blocks
  @Test
  void preservesOrderWithDuplicatesOnBothSides() {
    assertThat(sut.pivotArray(new int[] {2, 5, 2, 5, 1, 5, 9}, 5))
        .containsExactly(2, 2, 1, 5, 5, 5, 9);
  }

  // Step 10: the result is the same length as the input — nothing is dropped or
  // added during the rearrangement
  @Test
  void resultHasSameLengthAsInput() {
    assertThat(sut.pivotArray(new int[] {9, 12, 5, 10, 14, 3, 10}, 10)).hasSize(7);
  }
}
