package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import leetcode.support.TreeNode;

public class MaximumDepthBinaryTree {
  public int maxDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    Deque<Node> stack = new ArrayDeque<>();
    stack.push(new Node(root, 1));
    int depth = 0;
    while (!stack.isEmpty()) {
      Node node = stack.poll();
      depth = Math.max(depth, node.depth);
      if (node.node.left != null) {
        stack.push(new Node(node.node.left, node.depth + 1));
      }
      if (node.node.right != null) {
        stack.push(new Node(node.node.right, node.depth + 1));
      }
    }
    return depth;
  }

  public static class Node {
    public TreeNode node;
    public int depth;

    public Node(TreeNode node, int depth) {
      this.node = node;
      this.depth = depth;
    }
  }
}
