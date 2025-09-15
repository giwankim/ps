package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class SumRootToLeafNumbers {

  public int sumNumbers(TreeNode root) {
    return sumNumbers(root, 0);
  }

  private int sumNumbers(TreeNode root, int currentSum) {
    if (root == null) {
      return 0;
    }

    currentSum = currentSum * 10 + root.val;

    if (root.left == null && root.right == null) {
      return currentSum;
    }
    return sumNumbers(root.left, currentSum) + sumNumbers(root.right, currentSum);
  }

  public int sumNumbersDfs(TreeNode root) {
    int result = 0;

    Deque<NodeSum> stack = new ArrayDeque<>();
    stack.push(new NodeSum(root, 0));

    while (!stack.isEmpty()) {
      NodeSum nodeSum = stack.pop();
      TreeNode node = nodeSum.node;

      int currentSum = nodeSum.accumulator * 10 + node.val;

      // leaf node
      if (node.left == null && node.right == null) {
        result += currentSum;
      }

      // preorder traversal
      if (node.left != null) {
        stack.push(new NodeSum(node.left, currentSum));
      }
      if (node.right != null) {
        stack.push(new NodeSum(node.right, currentSum));
      }
    }

    return result;
  }

  public int sumNumbersBfs(TreeNode root) {
    int result = 0;

    Queue<NodeSum> queue = new LinkedList<>();
    queue.offer(new NodeSum(root, 0));

    while (!queue.isEmpty()) {
      NodeSum nodeSum = queue.poll();
      TreeNode node = nodeSum.node;

      int currentSum = nodeSum.accumulator * 10 + node.val;

      if (node.left == null && node.right == null) {
        result += currentSum;
      }
      if (node.left != null) {
        queue.offer(new NodeSum(node.left, currentSum));
      }
      if (node.right != null) {
        queue.offer(new NodeSum(node.right, currentSum));
      }
    }

    return result;
  }

  record NodeSum(TreeNode node, int accumulator) {}
}
