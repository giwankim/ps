package com.giwankim.leetcode;

import java.util.*;
import com.giwankim.leetcode.support.TreeNode;

public class BinaryTreeInorderTraversal {
  public List<Integer> inorderTraversal(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }

    List<Integer> result = new ArrayList<>();

    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode iter = root;

    while (iter != null || !stack.isEmpty()) {
      // push left-most nodes onto stack
      while (iter != null) {
        stack.push(iter);
        iter = iter.left;
      }

      iter = stack.pop();
      result.add(iter.val); // visit current node
      iter = iter.right; // visit right subtree
    }
    return result;
  }
}
