package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AverageOfLevelsInBinaryTree {
  public List<Double> averageOfLevels(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    List<Double> result = new ArrayList<>();
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
      int n = queue.size();
      double sum = 0;
      for (int i = 0; i < n; i++) {
        TreeNode node = queue.poll();
        sum += node.val;
        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
      }
      result.add(sum / n);
    }
    return result;
  }
}
