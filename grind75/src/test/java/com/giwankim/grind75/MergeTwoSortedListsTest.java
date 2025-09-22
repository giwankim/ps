package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.ListNode;
import org.junit.jupiter.api.Test;

class MergeTwoSortedListsTest {

  @Test
  void mergeTwoLists() {
    ListNode list1 = new ListNode(1);
    list1.next = new ListNode(2);
    list1.next.next = new ListNode(4);

    ListNode list2 = new ListNode(1);
    list2.next = new ListNode(3);
    list2.next.next = new ListNode(4);

    ListNode expected = new ListNode(1);
    expected.next = new ListNode(1);
    expected.next.next = new ListNode(2);
    expected.next.next.next = new ListNode(3);
    expected.next.next.next.next = new ListNode(4);
    expected.next.next.next.next.next = new ListNode(4);

    ListNode actual = new MergeTwoSortedLists().mergeTwoLists(list1, list2);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void listsAreEmpty() {
    assertThat(new MergeTwoSortedLists().mergeTwoLists(null, null)).isEqualTo(null);
  }

  @Test
  void firstListIsNull() {
    ListNode actual = new MergeTwoSortedLists().mergeTwoLists(null, new ListNode(0));
    assertThat(actual).isEqualTo(new ListNode(0));
  }

  @Test
  void secondListIsNull() {
    ListNode list1 = new ListNode(-10);
    list1.next = new ListNode(-6);
    list1.next.next = new ListNode(-6);
    list1.next.next.next = new ListNode(-6);
    list1.next.next.next.next = new ListNode(-3);
    list1.next.next.next.next.next = new ListNode(5);

    ListNode list2 = null;

    ListNode expected = new ListNode(-10);
    expected.next = new ListNode(-6);
    expected.next.next = new ListNode(-6);
    expected.next.next.next = new ListNode(-6);
    expected.next.next.next.next = new ListNode(-3);
    expected.next.next.next.next.next = new ListNode(5);

    assertThat(new MergeTwoSortedLists().mergeTwoLists(list1, list2)).isEqualTo(expected);
  }
}
