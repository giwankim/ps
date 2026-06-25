package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import leetcode.support.TreeNode;

public class BinarySearchTreeIterator {
  /** @implNote Time {@code O(n) (amortized O(1))}, space {@code O(h)}. */
  static class BSTIterator {
    private final Deque<TreeNode> stack = new ArrayDeque<>();

    public BSTIterator(TreeNode root) {
      pushAllLeftNodes(root);
    }

    public int next() {
      TreeNode node = stack.pop();
      pushAllLeftNodes(node.right);
      return node.val;
    }

    private void pushAllLeftNodes(TreeNode root) {
      while (root != null) {
        stack.push(root);
        root = root.left;
      }
    }

    public boolean hasNext() {
      return !stack.isEmpty();
    }
  }
}
