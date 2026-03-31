package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class SumRootToLeafNumbersTest {
  SumRootToLeafNumbers sut = new SumRootToLeafNumbers();

  @Test
  void singleNode() {
    assertThat(sut.sumNumbers(TreeNode.of(5))).isEqualTo(5);
  }

  @Test
  void zeroDigitInPath() {
    assertThat(sut.sumNumbers(TreeNode.of(1, 0, null))).isEqualTo(10);
  }

  @Test
  void leftSkewedTree() {
    assertThat(sut.sumNumbers(TreeNode.of(1, 2, null, 3))).isEqualTo(123);
  }

  @Test
  void rightSkewedTree() {
    assertThat(sut.sumNumbers(TreeNode.of(1, null, 2, null, null, null, 3))).isEqualTo(123);
  }

  @Test
  void twoLeaves() {
    assertThat(sut.sumNumbers(TreeNode.of(1, 2, 3))).isEqualTo(25);
  }

  @Test
  void leetCodeExample() {
    assertThat(sut.sumNumbers(TreeNode.of(4, 9, 0, 5, 1))).isEqualTo(1026);
  }
}
