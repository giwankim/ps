package leetcode.p0101_0200;

import static leetcode.support.ListNodeAssertions.assertListEquals;
import static org.assertj.core.api.Assertions.assertThat;

import leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class SortListTest {
  SortList sut = new SortList();

  // Step 1 — null head, the empty list
  @Test
  void returnsNullForNullHead() {
    assertThat(sut.sortList(null)).isNull();
  }

  // Step 2 — n=1 base case: recursion bottoms out
  @Test
  void returnsSingleNodeListUnchanged() {
    assertListEquals(sut.sortList(ListNode.of(5)), ListNode.of(5));
  }

  // Step 3a — n=2 ascending: comparator picks left on `<`
  @Test
  void leavesTwoAscendingNodesUnchanged() {
    assertListEquals(sut.sortList(ListNode.of(1, 2)), ListNode.of(1, 2));
  }

  // Step 3b — n=2 descending: comparator picks right on `>`
  @Test
  void sortsTwoDescendingNodes() {
    assertListEquals(sut.sortList(ListNode.of(2, 1)), ListNode.of(1, 2));
  }

  // Step 3c — n=2 equal values: comparator must terminate on `==`
  @Test
  void leavesTwoEqualValuesUnchanged() {
    assertListEquals(sut.sortList(ListNode.of(1, 1)), ListNode.of(1, 1));
  }

  // Step 4 — n=3 odd-length split exercises the uneven midpoint
  @Test
  void sortsThreeDescendingNodes() {
    assertListEquals(sut.sortList(ListNode.of(3, 2, 1)), ListNode.of(1, 2, 3));
  }

  // Step 5a — n=5 sorted: multi-level recursion makes no swaps
  @Test
  void leavesAlreadySortedListUnchanged() {
    assertListEquals(sut.sortList(ListNode.of(1, 2, 3, 4, 5)), ListNode.of(1, 2, 3, 4, 5));
  }

  // Step 5b — n=5 reversed: every comparison crosses halves
  @Test
  void sortsFiveReverseSortedNodes() {
    assertListEquals(sut.sortList(ListNode.of(5, 4, 3, 2, 1)), ListNode.of(1, 2, 3, 4, 5));
  }

  // Step 6 — mixed duplicates form a sorted multiset
  @Test
  void preservesDuplicateValuesInSortedOrder() {
    assertListEquals(sut.sortList(ListNode.of(3, 1, 2, 3, 1)), ListNode.of(1, 1, 2, 3, 3));
  }

  // Step 7 — all-identical values stress the equality branch at scale
  @Test
  void handlesListOfIdenticalValues() {
    assertListEquals(sut.sortList(ListNode.of(7, 7, 7)), ListNode.of(7, 7, 7));
  }

  // Step 8 — negatives and zero span the signed value range
  @Test
  void sortsListContainingNegativeValues() {
    assertListEquals(sut.sortList(ListNode.of(3, -1, 2, -5, 0)), ListNode.of(-5, -1, 0, 2, 3));
  }

  // Step 9 — canonical LeetCode 148 regression anchor
  @Test
  void sortsCanonicalLeetCodeExample() {
    assertListEquals(sut.sortList(ListNode.of(4, 2, 1, 3)), ListNode.of(1, 2, 3, 4));
  }
}
