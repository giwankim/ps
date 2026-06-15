package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class DeleteTheMiddleNodeOfALinkedListTest {
  DeleteTheMiddleNodeOfALinkedList sut = new DeleteTheMiddleNodeOfALinkedList();

  // Step 1: the critical edge case — for n = 1 the only node is itself the middle (index 0), so
  // deleting it leaves an empty list. ListNode.of() returns null, and so must the result.
  @Test
  void singleNodeListBecomesEmpty() {
    assertThat(sut.deleteMiddle(ListNode.of(1))).isNull();
  }

  // Step 2: n = 2 deletes index 1, keeping only the head. Comparing against a one-node list also
  // verifies the survivor's next is nulled — a dangling pointer to the deleted node would fail
  // equality because @EqualsAndHashCode compares the whole chain.
  @Test
  void twoNodeListKeepsOnlyTheFirstAndNullsItsNext() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 2))).isEqualTo(ListNode.of(1));
  }

  // Step 3: LeetCode Example 3 — [2,1] with n = 2 removes node 1 (value 1), leaving [2].
  @Test
  void leetCodeExample3() {
    assertThat(sut.deleteMiddle(ListNode.of(2, 1))).isEqualTo(ListNode.of(2));
  }

  // Step 4: n = 3 has a single, unambiguous middle at index 1; the head relinks past it to the
  // tail.
  @Test
  void threeNodeListRemovesTheSingleMiddle() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 2, 3))).isEqualTo(ListNode.of(1, 3));
  }

  // Step 5: LeetCode Example 2 — n = 4, middle index 2 (value 3), giving [1,2,4].
  @Test
  void leetCodeExample2() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 2, 3, 4))).isEqualTo(ListNode.of(1, 2, 4));
  }

  // Step 6: odd length n = 5 has two central candidates (indices 2 and 3); floor(5/2) = 2 picks the
  // lower one (value 3), so the result is [1,2,4,5].
  @Test
  void oddLengthRemovesTheLowerCenter() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 2, 3, 4, 5))).isEqualTo(ListNode.of(1, 2, 4, 5));
  }

  // Step 7: even length n = 6 — floor(6/2) = 3 removes index 3 (value 4), the upper of the two
  // centers, giving [1,2,3,5,6]. Contrast with Step 6 to pin the floor-division convention.
  @Test
  void evenLengthRemovesTheUpperCenter() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 2, 3, 4, 5, 6)))
        .isEqualTo(ListNode.of(1, 2, 3, 5, 6));
  }

  // Step 8: LeetCode Example 1 — n = 7, middle index 3 (value 7). Removing it relinks an interior
  // node (value 4) to the node after the middle (value 1): [1,3,4,1,2,6].
  @Test
  void leetCodeExample1() {
    assertThat(sut.deleteMiddle(ListNode.of(1, 3, 4, 7, 1, 2, 6)))
        .isEqualTo(ListNode.of(1, 3, 4, 1, 2, 6));
  }

  // Step 9: deletion is positional, not value-based. With duplicates surrounding a distinct middle
  // (n = 5, index 2 holds value 1), only that one node is removed and all four 7s survive.
  @Test
  void deletionIsByPositionNotByValue() {
    assertThat(sut.deleteMiddle(ListNode.of(7, 7, 1, 7, 7))).isEqualTo(ListNode.of(7, 7, 7, 7));
  }

  // Step 10: values at the constraint upper bound (Node.val <= 10^5) are handled like any other —
  // here the middle (value 1) is removed, leaving the two boundary-valued nodes.
  @Test
  void valuesAtTheConstraintUpperBoundAreHandled() {
    assertThat(sut.deleteMiddle(ListNode.of(100000, 1, 100000)))
        .isEqualTo(ListNode.of(100000, 100000));
  }

  // Step 11: maximum length (n = 10^5). Values 1..n let us predict every surviving value by index,
  // and we walk the result iteratively — comparing two 10^5-deep lists via the recursive
  // @EqualsAndHashCode/toString would itself overflow the stack.
  @Test
  void maximumLengthListDeletesTheMiddleByIndex() {
    int n = 100000;
    int[] values = new int[n];
    for (int i = 0; i < n; i++) {
      values[i] = i + 1;
    }

    ListNode result = sut.deleteMiddle(ListNode.of(values));

    int middleIndex = n / 2;
    int count = 0;
    for (ListNode node = result; node != null; node = node.next, count++) {
      // positions before the removed middle keep their value (index + 1); positions at or after it
      // shift up by one original slot (index + 2).
      int expected = count < middleIndex ? count + 1 : count + 2;
      assertThat(node.val).isEqualTo(expected);
    }
    assertThat(count).isEqualTo(n - 1);
  }
}
