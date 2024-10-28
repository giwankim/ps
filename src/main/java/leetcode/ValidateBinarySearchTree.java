package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import leetcode.support.TreeNode;

public class ValidateBinarySearchTree {
  public boolean isValidBST(TreeNode root) {
    TreeNode prev = null;
    TreeNode iter = root;
    Deque<TreeNode> stack = new ArrayDeque<>();
    while (iter != null || !stack.isEmpty()) {
      while (iter != null) {
        stack.push(iter);
        iter = iter.left;
      }
      iter = stack.pop();
      if (prev != null && prev.val >= iter.val) {
        return false;
      }
      prev = iter;
      iter = iter.right;
    }
    return true;
  }
}
