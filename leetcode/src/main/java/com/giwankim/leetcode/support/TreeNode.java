package com.giwankim.leetcode.support;

import java.util.Objects;

public class TreeNode {

  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  public TreeNode(int val) {
    this(val, null, null);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TreeNode treeNode)) {
      return false;
    }
    return val == treeNode.val
        && Objects.equals(left, treeNode.left)
        && Objects.equals(right, treeNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val, left, right);
  }

  @Override
  public String toString() {
    return "TreeNode{val=%d, left=%s, right=%s}".formatted(val, left, right);
  }
}
