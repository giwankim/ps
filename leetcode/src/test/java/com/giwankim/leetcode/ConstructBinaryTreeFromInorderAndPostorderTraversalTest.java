package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructBinaryTreeFromInorderAndPostorderTraversalTest {
  ConstructBinaryTreeFromInorderAndPostorderTraversal sut;

  @BeforeEach
  void setUp() {
    sut = new ConstructBinaryTreeFromInorderAndPostorderTraversal();
  }

  @Test
  void singleton() {
    int[] inorder = {-1};
    int[] postorder = {-1};
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(TreeNode.of(-1));
  }

  @Test
  void exampleTree() {
    int[] inorder = {9, 3, 15, 20, 7};
    int[] postorder = {9, 15, 7, 20, 3};
    var expected = TreeNode.of(3, 9, 20, null, null, 15, 7);
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(expected);
  }

  @Test
  void leftOnlyChild() {
    int[] inorder = {1, 2};
    int[] postorder = {1, 2};
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(TreeNode.of(2, 1, null));
  }

  @Test
  void rightOnlyChild() {
    int[] inorder = {1, 2};
    int[] postorder = {2, 1};
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(TreeNode.of(1, null, 2));
  }

  @Test
  void leftSkewed() {
    int[] inorder = {1, 2, 3};
    int[] postorder = {1, 2, 3};
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(TreeNode.of(3, 2, null, 1));
  }

  @Test
  void rightSkewed() {
    int[] inorder = {1, 2, 3};
    int[] postorder = {3, 2, 1};
    assertThat(sut.buildTree(inorder, postorder))
        .isEqualTo(TreeNode.of(1, null, 2, null, null, null, 3));
  }

  @Test
  void completeBinaryTree() {
    int[] inorder = {4, 2, 5, 1, 6, 3, 7};
    int[] postorder = {4, 5, 2, 6, 7, 3, 1};
    assertThat(sut.buildTree(inorder, postorder)).isEqualTo(TreeNode.of(1, 2, 3, 4, 5, 6, 7));
  }
}
