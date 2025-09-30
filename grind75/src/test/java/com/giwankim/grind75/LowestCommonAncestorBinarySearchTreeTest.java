package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import org.junit.jupiter.api.Test;

class LowestCommonAncestorBinarySearchTreeTest {

  @Test
  void ancestorsOfDifferentBranches() {
    TreeNode p =
        new TreeNode(2, new TreeNode(0), new TreeNode(4, new TreeNode(3), new TreeNode(5)));
    TreeNode q = new TreeNode(8, new TreeNode(7), new TreeNode(9));
    TreeNode root = new TreeNode(6, p, q);

    TreeNode lca = new LowestCommonAncestorBinarySearchTree().lowestCommonAncestor(root, p, q);

    assertThat(lca).isEqualTo(root);
  }

  @Test
  void oneIsAncestorOfTheOther() {
    TreeNode root =
        new TreeNode(
            6,
            new TreeNode(2, new TreeNode(0), new TreeNode(4, new TreeNode(3), new TreeNode(5))),
            new TreeNode(8, new TreeNode(7), new TreeNode(9)));
    TreeNode p = root.left;
    TreeNode q = root.left.right;

    TreeNode lca = new LowestCommonAncestorBinarySearchTree().lowestCommonAncestor(root, p, q);

    assertThat(lca).isEqualTo(p);
  }

  @Test
  void rootIsAncestor() {
    TreeNode root = new TreeNode(2, new TreeNode(1), null);
    TreeNode p = root;
    TreeNode q = root.left;

    TreeNode lca = new LowestCommonAncestorBinarySearchTree().lowestCommonAncestor(root, p, q);

    assertThat(lca).isEqualTo(root);
  }
}
