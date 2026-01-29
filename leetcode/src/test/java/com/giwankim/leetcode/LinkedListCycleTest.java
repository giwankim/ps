package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LinkedListCycleTest {

  @ParameterizedTest
  @MethodSource
  void hasCycle(ListNode head, boolean expected) {
    boolean actual = new LinkedListCycle().hasCycle(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> hasCycle() {
    ListNode head1 = ListNode.of(3, 2, 0, -4);
    head1.next.next.next.next = head1.next;

    ListNode head2 = ListNode.of(1, 2);
    head2.next.next = head2;

    ListNode head3 = ListNode.of(1);

    return Stream.of(
        Arguments.of(head1, true), Arguments.of(head2, true), Arguments.of(head3, false));
  }
}
