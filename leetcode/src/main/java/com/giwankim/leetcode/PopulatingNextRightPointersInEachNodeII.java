package com.giwankim.leetcode;

import java.util.LinkedList;
import java.util.Queue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class PopulatingNextRightPointersInEachNodeII {
  public Node connect(Node root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    if (root == null) {
      return null;
    }
    Queue<Node> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
      Node prev = null;
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        Node node = queue.poll();

        if (prev != null) {
          prev.next = node;
        }
        prev = node;

        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
      }
    }
    return root;
  }

  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node(int val) {
      this(val, null, null, null);
    }

    public static Node of(Integer... vals) {
      if (vals.length == 0) {
        return null;
      }
      return Node.builder().val(vals[0]).left(buildNode(vals, 1)).right(buildNode(vals, 2)).build();
    }

    private static Node buildNode(Integer[] vals, int i) {
      if (i < 0 || i >= vals.length || vals[i] == null) {
        return null;
      }
      return Node.builder()
          .val(vals[i])
          .left(buildNode(vals, 2 * i + 1))
          .right(buildNode(vals, 2 * i + 2))
          .build();
    }
  }
}
