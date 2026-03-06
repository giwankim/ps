package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class LinkedListCycleTest {
  private final LinkedListCycle sut = new LinkedListCycle();

  @ParameterizedTest
  @MethodSource
  void noCycle(ListNode head) {
    assertThat(sut.hasCycle(head)).isFalse();
  }

  private static Stream<ListNode> noCycle() {
    return Stream.of(null, ListNode.of(1), ListNode.of(1, 2), ListNode.of(1, 2, 3, 4, 5));
  }

  @ParameterizedTest
  @MethodSource
  void hasCycle(ListNode head) {
    assertThat(sut.hasCycle(head)).isTrue();
  }

  private static Stream<ListNode> hasCycle() {
    return Stream.of(
        listWithTailConnectedTo(new int[] {1}, 0),
        listWithTailConnectedTo(new int[] {1, 2}, 0),
        listWithTailConnectedTo(new int[] {3, 2, 0, -4}, 1));
  }

  private static ListNode listWithTailConnectedTo(int[] values, int pos) {
    if (values.length == 0) {
      return null;
    }

    ListNode head = ListNode.of(values);
    if (pos < 0) {
      return head;
    }

    ListNode target = null;
    ListNode tail = head;
    int index = 0;
    for (ListNode current = head; current != null; current = current.next) {
      if (index == pos) {
        target = current;
      }
      tail = current;
      index++;
    }

    if (target == null) {
      throw new IllegalArgumentException("pos is out of range: " + pos);
    }

    tail.next = target;
    return head;
  }
}
