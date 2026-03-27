package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructBinaryTreeFromPreorderAndInorderTraversalTest {
  ConstructBinaryTreeFromPreorderAndInorderTraversal sut;

  @BeforeEach
  void setUp() {
    sut = new ConstructBinaryTreeFromPreorderAndInorderTraversal();
  }

  @Test
  void singleton() {
    int[] preorder = {-1};
    int[] inorder = {-1};
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(TreeNode.of(-1));
  }

  @Test
  void exampleTree() {
    int[] preorder = {3, 9, 20, 15, 7};
    int[] inorder = {9, 3, 15, 20, 7};
    var expected = TreeNode.of(3, 9, 20, null, null, 15, 7);
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(expected);
  }

  @Test
  void leftOnlyChild() {
    int[] preorder = {2, 1};
    int[] inorder = {1, 2};
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(TreeNode.of(2, 1, null));
  }

  @Test
  void rightOnlyChild() {
    int[] preorder = {1, 2};
    int[] inorder = {1, 2};
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(TreeNode.of(1, null, 2));
  }

  @Test
  void leftSkewed() {
    int[] preorder = {3, 2, 1};
    int[] inorder = {1, 2, 3};
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(TreeNode.of(3, 2, null, 1));
  }

  @Test
  void rightSkewed() {
    int[] preorder = {1, 2, 3};
    int[] inorder = {1, 2, 3};
    assertThat(sut.buildTree(preorder, inorder))
        .isEqualTo(TreeNode.of(1, null, 2, null, null, null, 3));
  }

  @Test
  void completeBinaryTree() {
    int[] preorder = {1, 2, 4, 5, 3, 6, 7};
    int[] inorder = {4, 2, 5, 1, 6, 3, 7};
    assertThat(sut.buildTree(preorder, inorder)).isEqualTo(TreeNode.of(1, 2, 3, 4, 5, 6, 7));
  }
}
