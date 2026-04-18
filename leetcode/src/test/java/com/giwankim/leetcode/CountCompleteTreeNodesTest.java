package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class CountCompleteTreeNodesTest {
  CountCompleteTreeNodes sut = new CountCompleteTreeNodes();

  @Test
  void emptyTree() {
    assertThat(sut.countNodes(null)).isZero();
  }

  @Test
  void singleton() {
    assertThat(sut.countNodes(TreeNode.of(1))).isEqualTo(1);
  }

  @Test
  void twoLevels() {
    assertThat(sut.countNodes(TreeNode.of(1, 2))).isEqualTo(2);
    assertThat(sut.countNodes(TreeNode.of(1, 2, 3))).isEqualTo(3);
  }

  @Test
  void multipleLevels() {
    assertThat(sut.countNodes(TreeNode.of(1, 2, 3, 4, 5, 6))).isEqualTo(6);
  }

  @Test
  void perfectBinaryTree() {
    assertThat(sut.countNodes(TreeNode.of(1, 2, 3, 4, 5, 6, 7))).isEqualTo(7);
  }

  @Test
  void fourLevels() {
    assertThat(sut.countNodes(TreeNode.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)))
        .isEqualTo(12);
    assertThat(sut.countNodes(TreeNode.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)))
        .isEqualTo(15);
  }
}
