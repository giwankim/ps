package com.giwankim.grind75;

import com.giwankim.grind75.support.ListNode;

public class MergeTwoSortedLists {

  public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
    ListNode result = new ListNode(-1); // dummy head
    ListNode cur = result;                  // current node in result

    while (list1 != null && list2 != null) {
      if (list1.val < list2.val) {
        // add list1 to result
        cur.next = list1;
        list1 = list1.next;
      } else {
        // add list2 to result
        cur.next = list2;
        list2 = list2.next;
      }
      cur = cur.next;
    }

    // nodes left on list1
    while (list1 != null) {
      cur.next = list1;
      cur = cur.next;
      list1 = list1.next;
    }

    // nodes are left on list2
    while (list2 != null) {
      cur.next = list2;
      cur = cur.next;
      list2 = list2.next;
    }

    return result.next;
  }
}
