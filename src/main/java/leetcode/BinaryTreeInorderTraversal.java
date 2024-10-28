package leetcode;

import java.util.ArrayList;
import java.util.List;
import leetcode.support.TreeNode;

public class BinaryTreeInorderTraversal {
  public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    inorderTraversal(root, result);
    return result;
  }

  private void inorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) {
      return;
    }
    inorderTraversal(root.left, result);
    result.add(root.val);
    inorderTraversal(root.right, result);
  }
}
