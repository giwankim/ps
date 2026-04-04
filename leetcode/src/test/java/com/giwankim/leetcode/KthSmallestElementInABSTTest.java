package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class KthSmallestElementInABSTTest {
  KthSmallestElementInABST sut = new KthSmallestElementInABST();

  @Test
  void singletonTree() {
    assertThat(sut.kthSmallest(TreeNode.of(1), 1)).isOne();
  }

  @Test
  void twoNodesLeftChild() {
    //   2
    //  /
    // 1
    assertThat(sut.kthSmallest(TreeNode.of(2, 1), 1)).isOne();
  }

  @Test
  void twoNodesLeftChildKEquals2() {
    //   2
    //  /
    // 1
    assertThat(sut.kthSmallest(TreeNode.of(2, 1), 2)).isEqualTo(2);
  }

  @Test
  void twoNodesRightChild() {
    // 1
    //  \
    //   2
    assertThat(sut.kthSmallest(TreeNode.of(1, null, 2), 2)).isEqualTo(2);
  }

  @Test
  void threeNodesBalanced() {
    //   2
    //  / \
    // 1   3
    assertThat(sut.kthSmallest(TreeNode.of(2, 1, 3), 2)).isEqualTo(2);
  }

  @Test
  void leftSkewed() {
    //     3
    //    /
    //   2
    //  /
    // 1
    assertThat(sut.kthSmallest(TreeNode.of(3, 2, null, 1), 1)).isOne();
  }

  @Test
  void rightSkewed() {
    // 1
    //  \
    //   2
    //    \
    //     3
    assertThat(sut.kthSmallest(TreeNode.of(1, null, 2, null, null, null, 3), 3)).isEqualTo(3);
  }

  @Test
  void kEqualsNodeCount() {
    //   2
    //  / \
    // 1   3
    assertThat(sut.kthSmallest(TreeNode.of(2, 1, 3), 3)).isEqualTo(3);
  }

  @Test
  void leetCodeExample1() {
    //     3
    //    / \
    //   1   4
    //    \
    //     2
    assertThat(sut.kthSmallest(TreeNode.of(3, 1, 4, null, 2), 1)).isOne();
  }

  @Test
  void leetCodeExample2() {
    //       5
    //      / \
    //     3   6
    //    / \
    //   2   4
    //  /
    // 1
    assertThat(sut.kthSmallest(TreeNode.of(5, 3, 6, 2, 4, null, null, 1), 3)).isEqualTo(3);
  }
}
