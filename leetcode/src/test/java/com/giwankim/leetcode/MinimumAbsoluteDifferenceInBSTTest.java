package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class MinimumAbsoluteDifferenceInBSTTest {
  MinimumAbsoluteDifferenceInBST sut = new MinimumAbsoluteDifferenceInBST();

  @Test
  void twoNodes() {
    assertThat(sut.getMinimumDifference(TreeNode.of(2, 1))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(2, 1))).isOne();
  }

  @Test
  void minBetweenGrandchildAndRoot() {
    // in-order: 104, 227, 236, 701, 911 — min is |236-227|=9 (root and grandchild)
    TreeNode root = TreeNode.of(236, 104, 701, null, 227, null, 911);
    assertThat(sut.getMinimumDifference(root)).isEqualTo(9);
    assertThat(sut.getMinimumDifference2(root)).isEqualTo(9);
  }

  @Test
  void leetCodeExample1() {
    assertThat(sut.getMinimumDifference(TreeNode.of(4, 2, 6, 1, 3))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(4, 2, 6, 1, 3))).isOne();
  }

  @Test
  void leetCodeExample2() {
    assertThat(sut.getMinimumDifference(TreeNode.of(1, 0, 48, null, null, 12, 49))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(1, 0, 48, null, null, 12, 49))).isOne();
  }

  @Test
  void balancedBST() {
    assertThat(sut.getMinimumDifference(TreeNode.of(2, 1, 3))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(2, 1, 3))).isOne();
  }

  @Test
  void leftSkewed() {
    assertThat(sut.getMinimumDifference(TreeNode.of(3, 2, null, 1))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(3, 2, null, 1))).isOne();
  }

  @Test
  void rightSkewed() {
    assertThat(sut.getMinimumDifference(TreeNode.of(1, null, 2, null, null, null, 3))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(1, null, 2, null, null, null, 3))).isOne();
  }

  @Test
  void minBetweenRightmostOfLeftSubtreeAndRoot() {
    // tree:  10 -> left:5 -> right:8
    // in-order: 5, 8, 10 — min is |10-8|=2 (root and grandchild)
    assertThat(sut.getMinimumDifference(TreeNode.of(10, 5, null, null, 8))).isEqualTo(2);
    assertThat(sut.getMinimumDifference2(TreeNode.of(10, 5, null, null, 8))).isEqualTo(2);
  }

  @Test
  void minBetweenLeftmostOfRightSubtreeAndRoot() {
    // tree: 5 -> left:1, right:8 -> left:6
    // in-order: 1, 5, 6, 8 — min is |6-5|=1 (root and grandchild)
    assertThat(sut.getMinimumDifference(TreeNode.of(5, 1, 8, null, null, 6))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(5, 1, 8, null, null, 6))).isOne();
  }

  @Test
  void minBetweenRightChildOfLeftAndRoot() {
    // tree: 5 -> left:1 -> right:4, right:8
    // in-order: 1, 4, 5, 8 — min is |5-4|=1 (root and grandchild)
    assertThat(sut.getMinimumDifference(TreeNode.of(5, 1, 8, null, 4))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(5, 1, 8, null, 4))).isOne();
  }

  @Test
  void largerBalancedTree() {
    assertThat(sut.getMinimumDifference(TreeNode.of(4, 2, 6, 1, 3, 5, 7))).isOne();
    assertThat(sut.getMinimumDifference2(TreeNode.of(4, 2, 6, 1, 3, 5, 7))).isOne();
  }
}
