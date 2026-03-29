package com.giwankim.leetcode.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class TreeNode {
  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode(int val) {
    this(val, null, null);
  }

  public static TreeNode of(Integer... vals) {
    if (vals.length == 0) {
      return null;
    }
    return new TreeNode(vals[0], constructFromArray(vals, 1), constructFromArray(vals, 2));
  }

  private static TreeNode constructFromArray(Integer[] vals, int k) {
    if (k < 0 || k >= vals.length) {
      return null;
    }
    if (vals[k] == null) {
      return null;
    }
    TreeNode root = new TreeNode(vals[k]);
    root.left = constructFromArray(vals, 2 * k + 1);
    root.right = constructFromArray(vals, 2 * k + 2);
    return root;
  }

  @Override
  public String toString() {
    return String.valueOf(val);
  }

  private void printRecursive(TreeNode node, int level, StringBuilder sb) {
    if (node != null) {
      printRecursive(node.right, level + 1, sb);
      sb.append("\t".repeat(Math.max(0, level)));
      sb.append(node.val);
      sb.append("\n");
      printRecursive(node.left, level + 1, sb);
    }
  }

  public String prettyPrint() {
    StringBuilder sb = new StringBuilder();
    printRecursive(this, 0, sb);
    return sb.toString();
  }
}
