package leetcode;

import java.util.*;
import leetcode.support.TreeNode;

public class BinaryTreeRightSideView {
  public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    rightSideView(root, 0, result);
    return result;
  }

  private void rightSideView(TreeNode root, int depth, List<Integer> result) {
    if (root == null) {
      return;
    }
    if (result.size() == depth) {
      result.add(root.val);
    }
    rightSideView(root.right, depth + 1, result);
    rightSideView(root.left, depth + 1, result);
  }
}
