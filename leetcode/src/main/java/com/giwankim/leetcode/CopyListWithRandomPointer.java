package com.giwankim.leetcode;

import java.util.IdentityHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

public class CopyListWithRandomPointer {
  public Node copyRandomList(Node head) {
    // Time complexity: O(n), Space complexity: O(n)
    Map<Node, Node> toCopy = new IdentityHashMap<>();
    Node dummy = new Node(-1);
    Node curr = dummy;
    for (Node it = head; it != null; it = it.next) {
      curr.next = new Node(it.val);
      toCopy.put(it, curr.next);
      curr = curr.next;
    }

    curr = dummy.next;
    for (Node it = head; it != null; it = it.next) {
      curr.random = toCopy.get(it.random);
      curr = curr.next;
    }

    return dummy.next;
  }

  @AllArgsConstructor
  public static class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
      this(val, null, null);
    }
  }
}
