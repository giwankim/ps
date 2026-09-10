package leetcode.p2201_2300;

import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/count-nodes-equal-to-average-of-subtree/">2265. Count
 * Nodes Equal to Average of Subtree</a>
 */
public class CountNodesEqualToAverageOfSubtree {
  private int ans;

  /**
   * @implNote Time {@code O(n)}, space {@code O(h)} for the recursion stack, where {@code n} is the
   *     number of nodes and {@code h} is the height of the tree.
   */
  public int averageOfSubtree(TreeNode root) {
    ans = 0;
    dfs(root);
    return ans;
  }

  /**
   * Returns {@code {size, sum}} of the subtree rooted at {@code node}, counting the node on the way
   * back up when {@code sum / size} matches its value.
   *
   * @implNote Each node is visited once with {@code O(1)} work beyond its two child calls.
   */
  private int[] dfs(TreeNode node) {
    if (node == null) {
      return new int[] {0, 0};
    }
    int[] result = new int[] {1, node.val};
    int[] left = dfs(node.left);
    result[0] += left[0];
    result[1] += left[1];
    int[] right = dfs(node.right);
    result[0] += right[0];
    result[1] += right[1];
    if (result[1] / result[0] == node.val) {
      ans++;
    }
    return result;
  }
}
