package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class MergeKSortedListTest {
  MergeKSortedList sut = new MergeKSortedList();

  // Step 1: empty input array — nothing to merge, returns null
  @Test
  void emptyArrayReturnsNull() {
    assertThat(sut.mergeKLists(new ListNode[0])).isNull();
  }

  // Step 2: single null entry — null entries are skipped, returns null
  @Test
  void singleNullEntryReturnsNull() {
    assertThat(sut.mergeKLists(new ListNode[] {null})).isNull();
  }

  // Step 3: every entry is null — still returns null
  @Test
  void allNullEntriesReturnNull() {
    assertThat(sut.mergeKLists(new ListNode[] {null, null, null})).isNull();
  }

  // Step 4: single one-element list — returned unchanged
  @Test
  void singleOneElementListReturnedUnchanged() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1)})).isEqualTo(ListNode.of(1));
  }

  // Step 5: single already-sorted list — returned unchanged
  @Test
  void singleSortedListReturnedUnchanged() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1, 2, 3)}))
        .isEqualTo(ListNode.of(1, 2, 3));
  }

  // Step 6: a null entry alongside a real list — null is skipped, list returned
  @Test
  void nullEntryAlongsideListIsSkipped() {
    assertThat(sut.mergeKLists(new ListNode[] {null, ListNode.of(1, 2)}))
        .isEqualTo(ListNode.of(1, 2));
  }

  // Step 7: two single-element lists in ascending order
  @Test
  void twoSingleElementListsInOrder() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1), ListNode.of(2)}))
        .isEqualTo(ListNode.of(1, 2));
  }

  // Step 8: two single-element lists supplied in reverse order — locks min-first comparison
  @Test
  void twoSingleElementListsInReverseOrder() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(2), ListNode.of(1)}))
        .isEqualTo(ListNode.of(1, 2));
  }

  // Step 9: two equal-length sorted lists interleaved
  @Test
  void twoEqualLengthListsInterleaved() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1, 3, 5), ListNode.of(2, 4, 6)}))
        .isEqualTo(ListNode.of(1, 2, 3, 4, 5, 6));
  }

  // Step 10: two lists of different lengths — shorter exhausts first, remainder appended
  @Test
  void twoListsOfDifferentLengthsMerged() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1, 5), ListNode.of(2, 3, 4)}))
        .isEqualTo(ListNode.of(1, 2, 3, 4, 5));
  }

  // Step 11: duplicate values across lists are all preserved
  @Test
  void duplicateValuesAcrossListsArePreserved() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(1, 1), ListNode.of(1)}))
        .isEqualTo(ListNode.of(1, 1, 1));
  }

  // Step 12: LeetCode Example 1 — three lists with overlapping values
  @Test
  void leetCodeExampleOneThreeListsWithOverlaps() {
    assertThat(sut.mergeKLists(
            new ListNode[] {ListNode.of(1, 4, 5), ListNode.of(1, 3, 4), ListNode.of(2, 6)}))
        .isEqualTo(ListNode.of(1, 1, 2, 3, 4, 4, 5, 6));
  }

  // Step 13: negative values are ordered correctly
  @Test
  void negativeValuesAreOrderedCorrectly() {
    assertThat(sut.mergeKLists(new ListNode[] {ListNode.of(-3, -1), ListNode.of(-2, 0)}))
        .isEqualTo(ListNode.of(-3, -2, -1, 0));
  }

  // Step 14: mix of null entries and lists — nulls skipped, lists merged
  @Test
  void mixOfNullEntriesAndListsAreMerged() {
    assertThat(sut.mergeKLists(new ListNode[] {null, ListNode.of(1, 4), null, ListNode.of(2, 3)}))
        .isEqualTo(ListNode.of(1, 2, 3, 4));
  }

  // Step 15: property — output is non-decreasing
  @Test
  void mergedOutputIsNonDecreasing() {
    ListNode merged = sut.mergeKLists(
        new ListNode[] {ListNode.of(1, 4, 5), ListNode.of(1, 3, 4), ListNode.of(2, 6)});
    for (ListNode it = merged; it != null && it.next != null; it = it.next) {
      assertThat(it.val).isLessThanOrEqualTo(it.next.val);
    }
  }

  // Step 16: property — merged length equals the sum of input lengths
  @Test
  void mergedLengthEqualsSumOfInputLengths() {
    ListNode merged = sut.mergeKLists(
        new ListNode[] {ListNode.of(1, 4, 5), ListNode.of(1, 3, 4), ListNode.of(2, 6)});
    int count = 0;
    for (ListNode it = merged; it != null; it = it.next) {
      count++;
    }
    assertThat(count).isEqualTo(8);
  }
}
