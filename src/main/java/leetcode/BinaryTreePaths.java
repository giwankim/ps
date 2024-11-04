package leetcode;

import leetcode.support.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class BinaryTreePaths {
  public List<String> binaryTreePaths(TreeNode root) {
    List<String> result = new ArrayList<>();
    binaryTreePaths(root, new StringBuilder(), result);
    return result;
  }

  private void binaryTreePaths(TreeNode root, StringBuilder sb, List<String> result) {
    if (root == null) {
      return;
    }
    int len = sb.length();
    sb.append(root.val);
    if (root.left == null && root.right == null) {
      result.add(sb.toString());
    }
    sb.append("->");
    binaryTreePaths(root.left, sb, result);
    binaryTreePaths(root.right, sb, result);
    sb.setLength(len);
  }
}
