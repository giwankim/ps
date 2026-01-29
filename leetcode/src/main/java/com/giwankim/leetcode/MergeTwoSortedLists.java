package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class MergeTwoSortedLists {
  public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
    ListNode dummy = new ListNode();
    ListNode it = dummy;
    while (list1 != null || list2 != null) {
      if (list1 == null) {
        it.next = list2;
        list2 = list2.next;
      } else if (list2 == null) {
        it.next = list1;
        list1 = list1.next;
      } else if (list1.val <= list2.val) {
        it.next = list1;
        list1 = list1.next;
      } else {
        it.next = list2;
        list2 = list2.next;
      }
      it = it.next;
    }
    return dummy.next;
  }
}
