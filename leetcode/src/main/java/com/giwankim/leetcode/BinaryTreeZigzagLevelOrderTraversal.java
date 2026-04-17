package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeZigzagLevelOrderTraversal {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }
    List<List<Integer>> result = new ArrayList<>();
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    boolean reverse = false;
    while (!queue.isEmpty()) {
      int size = queue.size();
      List<Integer> level = new ArrayList<>();
      while (size-- > 0) {
        TreeNode node = queue.poll();
        level.add(node.val);
        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
      }
      if (reverse) {
        Collections.reverse(level);
      }
      result.add(level);
      reverse = !reverse;
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }
    List<List<Integer>> result = new ArrayList<>();
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    boolean leftToRight = true;
    while (!queue.isEmpty()) {
      int size = queue.size();
      List<Integer> level = new LinkedList<>();
      while (size-- > 0) {
        TreeNode node = queue.poll();
        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
        if (leftToRight) {
          level.addLast(node.val);
        } else {
          level.addFirst(node.val);
        }
      }
      result.add(level);
      leftToRight = !leftToRight;
    }
    return result;
  }
}
