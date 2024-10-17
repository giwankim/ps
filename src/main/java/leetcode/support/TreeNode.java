package leetcode.support;

import java.util.Objects;

public class TreeNode {
  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode(int val) {
    this(val, null, null);
  }

  public TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  public static TreeNode from(int... vals) {
    if (vals.length == 0) {
      return null;
    }
    return new TreeNode(vals[0], constructFromArray(vals, 1), constructFromArray(vals, 2));
  }

  private static TreeNode constructFromArray(int[] vals, int k) {
    if (k < 0 || k >= vals.length) {
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

  void print_r(TreeNode node, int level, StringBuilder sb) {
    if (node != null) {
      print_r(node.right, level + 1, sb);
      sb.append("\t".repeat(Math.max(0, level)));
      sb.append(node.val);
      sb.append("\n");
      print_r(node.left, level + 1, sb);
    }
  }

  public String prettyPrint() {
    StringBuilder sb = new StringBuilder();
    print_r(this, 0, sb);
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
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
}
