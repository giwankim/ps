package leetcode.p2001_2100;

import java.util.HashMap;
import java.util.Map;
import leetcode.support.ListNode;

/**
 * <a
 * href="https://leetcode.com/problems/find-the-minimum-and-maximum-number-of-nodes-between-critical-points/">2058.
 * Find the Minimum and Maximum Number of Nodes Between Critical Points</a>
 */
public class FindTheMinimumAndMaximumNumberOfNodesBetweenCriticalPoints {
  /**
   * @implNote Expected time {@code O(n)} because the position-building pass and the consecutive
   *     critical-point scans are linear in the number of nodes, and auxiliary space {@code O(n)}
   *     for {@code pos}.
   */
  public int[] nodesBetweenCriticalPoints(ListNode head) {
    Map<ListNode, Integer> pos = new HashMap<>();
    ListNode iter = head;
    for (int i = 0; iter != null; i++, iter = iter.next) {
      pos.put(iter, i);
    }

    int minDist = Integer.MAX_VALUE;
    ListNode first = null;
    ListNode prev = null;
    ListNode curr = head;
    while (curr != null) {
      curr = findNextCriticalPoint(curr);
      if (curr != null) {
        if (first == null) {
          first = curr;
        }
        if (prev != null) {
          minDist = Math.min(minDist, pos.get(curr) - pos.get(prev));
        }
        prev = curr;
      }
    }
    minDist = minDist == Integer.MAX_VALUE ? -1 : minDist;
    int maxDist = -1;
    if (prev != null && first != null && prev != first) {
      maxDist = pos.get(prev) - pos.get(first);
    }
    return new int[] {minDist, maxDist};
  }

  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n} is the number of
   *     nodes reachable from {@code head}.
   */
  private ListNode findNextCriticalPoint(ListNode head) {
    ListNode prev = head;
    ListNode curr = head;
    while (curr != null) {
      if (curr.next != null) {
        if ((curr.val > prev.val && curr.val > curr.next.val)
            || (curr.val < prev.val && curr.val < curr.next.val)) {
          return curr;
        }
      }
      prev = curr;
      curr = curr.next;
    }
    return null;
  }
}
