package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class CreateBinaryTreeFromDescriptionsTest {
  CreateBinaryTreeFromDescriptions sut = new CreateBinaryTreeFromDescriptions();

  @Test
  void singleLeftChild() {
    int[][] descriptions = {{1, 2, 1}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(1, 2));
  }

  @Test
  void singleRightChild() {
    int[][] descriptions = {{1, 2, 0}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(1, null, 2));
  }

  @Test
  void twoChildren() {
    int[][] descriptions = {{1, 2, 1}, {1, 3, 0}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(1, 2, 3));
  }

  @Test
  void leftSkewed() {
    int[][] descriptions = {{3, 2, 1}, {2, 1, 1}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(3, 2, null, 1));
  }

  @Test
  void rightSkewed() {
    int[][] descriptions = {{1, 2, 0}, {2, 3, 0}};
    assertThat(sut.createBinaryTree(descriptions))
        .isEqualTo(TreeNode.of(1, null, 2, null, null, null, 3));
  }

  @Test
  void completeTree() {
    int[][] descriptions = {{1, 2, 1}, {1, 3, 0}, {2, 4, 1}, {2, 5, 0}, {3, 6, 1}, {3, 7, 0}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(1, 2, 3, 4, 5, 6, 7));
  }

  @Test
  void reusesNodeWhenValueIsBothParentAndChild() {
    int[][] descriptions = {{50, 20, 1}, {20, 15, 1}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(50, 20, null, 15));
  }

  @Test
  void identifiesRootRegardlessOfDescriptionOrder() {
    int[][] descriptions = {{80, 19, 1}, {20, 15, 1}, {50, 80, 0}, {20, 17, 0}, {50, 20, 1}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(50, 20, 80, 15, 17, 19));
  }

  @Test
  void example1() {
    int[][] descriptions = {{20, 15, 1}, {20, 17, 0}, {50, 20, 1}, {50, 80, 0}, {80, 19, 1}};
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(TreeNode.of(50, 20, 80, 15, 17, 19));
  }

  @Test
  void example2() {
    int[][] descriptions = {{1, 2, 1}, {2, 3, 0}, {3, 4, 1}};
    TreeNode expected =
        new TreeNode(1, new TreeNode(2, null, new TreeNode(3, new TreeNode(4), null)), null);
    assertThat(sut.createBinaryTree(descriptions)).isEqualTo(expected);
  }
}
