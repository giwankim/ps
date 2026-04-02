package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class LowestCommonAncestorOfABinaryTreeTest {
  LowestCommonAncestorOfABinaryTree sut = new LowestCommonAncestorOfABinaryTree();

  @Test
  void twoNodes() {
    TreeNode root = TreeNode.of(1, 2);
    assertThat(sut.lowestCommonAncestor(root, root, root.left)).isSameAs(root);
  }

  @Test
  void siblings() {
    TreeNode root = TreeNode.of(1, 2, 3);
    assertThat(sut.lowestCommonAncestor(root, root.left, root.right)).isSameAs(root);
  }

  @Test
  void bothInLeftSubtree() {
    TreeNode root = TreeNode.of(1, 2, 3, 4, 5);
    assertThat(sut.lowestCommonAncestor(root, root.left.left, root.left.right)).isSameAs(root.left);
  }

  @Test
  void bothInRightSubtree() {
    TreeNode root = TreeNode.of(1, 2, 3, null, null, 6, 7);
    assertThat(sut.lowestCommonAncestor(root, root.right.left, root.right.right))
        .isSameAs(root.right);
  }

  @Test
  void pIsAncestorOfQ() {
    TreeNode root = TreeNode.of(1, 2, 3, 4, 5);
    assertThat(sut.lowestCommonAncestor(root, root.left, root.left.left)).isSameAs(root.left);
  }

  @Test
  void leetCodeExample1() {
    //         3
    //       /   \
    //      5     1
    //     / \   / \
    //    6   2 0   8
    //       / \
    //      7   4
    TreeNode root = TreeNode.of(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    // p=5, q=1 → LCA is root
    assertThat(sut.lowestCommonAncestor(root, root.left, root.right)).isSameAs(root);
  }

  @Test
  void leetCodeExample2() {
    TreeNode root = TreeNode.of(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    // p=5, q=4 → LCA is 5 (p is ancestor of q)
    assertThat(sut.lowestCommonAncestor(root, root.left, root.left.right.right))
        .isSameAs(root.left);
  }

  @Test
  void deepNodesAcrossSubtrees() {
    TreeNode root = TreeNode.of(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    // p=7 (deep left), q=8 (right leaf) → LCA is root
    assertThat(sut.lowestCommonAncestor(root, root.left.right.left, root.right.right))
        .isSameAs(root);
  }
}
