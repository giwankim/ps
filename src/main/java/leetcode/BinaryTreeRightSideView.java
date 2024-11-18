package leetcode;

import leetcode.support.TreeNode;

import java.util.*;

public class BinaryTreeRightSideView {
  public List<Integer> rightSideView(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }

    List<Integer> result = new ArrayList<>();

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        TreeNode node = queue.poll();
        if (i == 0) {
          result.add(node.val);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
        if (node.left != null) {
          queue.offer(node.left);
        }
      }
    }

    return result;
  }
}
