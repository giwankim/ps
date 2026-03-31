package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class BinaryTreeMaximumPathSumTest {
  BinaryTreeMaximumPathSum sut = new BinaryTreeMaximumPathSum();

  @Test
  void singleton() {
    assertThat(sut.maxPathSum(TreeNode.of(1))).isOne();
  }

  @Test
  void singleNegativeNode() {
    assertThat(sut.maxPathSum(TreeNode.of(-3))).isEqualTo(-3);
  }

  @Test
  void straightLeftPath() {
    assertThat(sut.maxPathSum(TreeNode.of(1, 2, null, 3))).isEqualTo(6);
  }

  @Test
  void straightRightPath() {
    assertThat(sut.maxPathSum(TreeNode.of(1, null, 2, null, null, null, 3))).isEqualTo(6);
  }

  @Test
  void pathThroughRoot() {
    assertThat(sut.maxPathSum(TreeNode.of(1, 2, 3))).isEqualTo(6);
  }

  @Test
  void negativeChildrenExcluded() {
    assertThat(sut.maxPathSum(TreeNode.of(5, -2, -3))).isEqualTo(5);
  }

  @Test
  void bestPathNotThroughRoot() {
    // path 15 -> 20 -> 7 = 42, not through root (-10)
    assertThat(sut.maxPathSum(TreeNode.of(-10, 9, 20, null, null, 15, 7))).isEqualTo(42);
  }

  @Test
  void allNegative() {
    assertThat(sut.maxPathSum(TreeNode.of(-1, -2, -3))).isEqualTo(-1);
  }

  @Test
  void mixedSignsPickOneSide() {
    // best path: 10 -> 7 = 17, exclude left child (-2)
    assertThat(sut.maxPathSum(TreeNode.of(10, -2, 7))).isEqualTo(17);
  }
}
