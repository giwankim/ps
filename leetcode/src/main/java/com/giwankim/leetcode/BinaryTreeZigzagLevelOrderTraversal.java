package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeZigzagLevelOrderTraversal {
  public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
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
}
