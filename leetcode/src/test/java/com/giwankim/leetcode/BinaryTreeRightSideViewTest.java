package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class BinaryTreeRightSideViewTest {
  BinaryTreeRightSideView sut = new BinaryTreeRightSideView();

  @Test
  void emptyTree() {
    assertThat(sut.rightSideView(TreeNode.of())).isEqualTo(Collections.emptyList());
  }

  @Test
  void singleNode() {
    assertThat(sut.rightSideView(TreeNode.of(1))).isEqualTo(List.of(1));
  }

  @Test
  void leftChildOnly() {
    assertThat(sut.rightSideView(TreeNode.of(1, 2))).isEqualTo(List.of(1, 2));
  }

  @Test
  void rightChildOnly() {
    assertThat(sut.rightSideView(TreeNode.of(1, null, 3))).isEqualTo(List.of(1, 3));
  }

  @Test
  void bothChildren() {
    assertThat(sut.rightSideView(TreeNode.of(1, 2, 3))).isEqualTo(List.of(1, 3));
  }

  @Test
  void leftSubtreeDeeperThanRight() {
    // right side view includes left node at the deepest level
    assertThat(sut.rightSideView(TreeNode.of(1, 2, 3, 4))).isEqualTo(List.of(1, 3, 4));
  }

  @Test
  void leetCodeExample1() {
    assertThat(sut.rightSideView(TreeNode.of(1, 2, 3, null, 5, null, 4)))
        .isEqualTo(List.of(1, 3, 4));
  }

  @Test
  void leetCodeExample2() {
    // left subtree extends two levels deeper — nodes 4 and 5 visible from right
    assertThat(sut.rightSideView(TreeNode.of(1, 2, 3, 4, null, null, null, 5)))
        .isEqualTo(List.of(1, 3, 4, 5));
  }

  @Test
  void leftSkewed() {
    assertThat(sut.rightSideView(TreeNode.of(1, 2, null, 3))).isEqualTo(List.of(1, 2, 3));
  }

  @Test
  void rightSkewed() {
    assertThat(sut.rightSideView(TreeNode.of(1, null, 2, null, null, null, 3)))
        .isEqualTo(List.of(1, 2, 3));
  }
}
