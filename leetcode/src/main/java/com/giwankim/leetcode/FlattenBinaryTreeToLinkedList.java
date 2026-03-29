package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class FlattenBinaryTreeToLinkedList {
  public void flatten(TreeNode root) {
    List<TreeNode> nodes = new ArrayList<>();
    preorder(root, nodes);
    if (nodes.isEmpty()) {
      return;
    }
    for (int i = 0; i + 1 < nodes.size(); i++) {
      nodes.get(i).left = null;
      nodes.get(i).right = nodes.get(i + 1);
    }
    nodes.getLast().left = null;
    nodes.getLast().right = null;
  }

  private void preorder(TreeNode root, List<TreeNode> nodes) {
    if (root == null) {
      return;
    }
    nodes.add(root);
    preorder(root.left, nodes);
    preorder(root.right, nodes);
  }
}
