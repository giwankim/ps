package com.giwankim.leetcode;

import java.util.IdentityHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public class CopyListWithRandomPointer {
  public Node copyRandomList(Node head) {
    // Time complexity: O(n), Space complexity: O(n)
    Map<Node, Node> map = new IdentityHashMap<>();
    for (Node it = head; it != null; it = it.next) {
      map.put(it, new Node(it.val));
    }
    for (Node it = head; it != null; it = it.next) {
      Node copy = map.get(it);
      copy.next = map.get(it.next);
      copy.random = map.get(it.random);
    }
    return map.get(head);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
      this(val, null, null);
    }
  }
}
