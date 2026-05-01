package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class SortList {
  /**
   * @implNote Time {@code O(n log n)}, space {@code O(log n)}, where {@code n} is the list
   *     length.
   *     <p><b>Time:</b> top-down merge sort halves the list with two-pointer traversal
   *     ({@link #split}, {@code O(n)}), recurses on each half, and merges the two sorted
   *     halves ({@link #merge}, {@code O(n)}), giving the recurrence
   *     {@code T(n) = 2·T(n/2) + O(n)}. Applying the master theorem with {@code a = 2},
   *     {@code b = 2}, {@code f(n) = O(n)}: {@code n^log_b(a) = n} matches {@code f(n)},
   *     so case 2 gives {@code T(n) = Θ(n log n)} — equivalently, every level does
   *     {@code O(n)} work across {@code log n} levels.
   *     <p><b>Space:</b> auxiliary space is the recursion stack of depth {@code O(log n)};
   *     {@link #merge} re-links existing nodes in place rather than allocating a buffer,
   *     so no per-level scratch array is needed.
   */
  public ListNode sortList(ListNode head) {
    if (head == null || head.next == null) {
      return head;
    }
    ListNode right = split(head);
    ListNode leftSorted = sortList(head);
    ListNode rightSorted = sortList(right);
    return merge(leftSorted, rightSorted);
  }

  /**
   * Splits the list of length {@code k >= 2} rooted at {@code head} into two halves and
   * returns the head of the second half. The first half is truncated in place by nulling
   * the {@code next} link of its last node, so {@code head} now references a complete
   * list on its own. When {@code k} is odd, the first half is one node longer (the slow
   * pointer starts one position behind {@code head} via the dummy header, so it stops one
   * short of the true midpoint).
   */
  private ListNode split(ListNode head) {
    ListNode dummy = new ListNode(-1, head);
    ListNode slow = dummy;
    ListNode fast = dummy;

    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }

    ListNode left = slow.next;
    slow.next = null;

    return left;
  }

  /**
   * Merges two sorted lists {@code a} and {@code b} into a single sorted list by re-linking
   * their existing nodes in place; runs in {@code O(|a| + |b|)} time and {@code O(1)}
   * auxiliary space (only the dummy header is allocated). Stable: when {@code a.val == b.val}
   * the node from {@code a} is linked first.
   */
  private ListNode merge(ListNode a, ListNode b) {
    ListNode dummy = new ListNode();
    ListNode tail = dummy;

    while (a != null && b != null) {
      if (a.val <= b.val) {
        tail.next = a;
        a = a.next;
      } else {
        tail.next = b;
        b = b.next;
      }
      tail = tail.next;
    }

    tail.next = a != null ? a : b;

    return dummy.next;
  }
}
