package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.List;
import org.junit.jupiter.api.Test;

class BinaryTreeLevelOrderTraversalTest {
  BinaryTreeLevelOrderTraversal sut = new BinaryTreeLevelOrderTraversal();

  // Step 1: Base case — empty tree has no levels
  @Test
  void emptyTree() {
    assertThat(sut.levelOrder(TreeNode.of())).isEqualTo(List.of());
  }

  // Step 2: Single node — one level containing the root
  @Test
  void singleNode() {
    assertThat(sut.levelOrder(TreeNode.of(1))).isEqualTo(List.of(List.of(1)));
  }

  // Step 3: Left child only — still one node per level in breadth-first order
  @Test
  void leftChildOnly() {
    //   1
    //  /
    // 2
    assertThat(sut.levelOrder(TreeNode.of(1, 2))).isEqualTo(List.of(List.of(1), List.of(2)));
  }

  // Step 4: Right child only — null left child should not create an empty slot
  @Test
  void rightChildOnly() {
    // 1
    //  \
    //   3
    assertThat(sut.levelOrder(TreeNode.of(1, null, 3))).isEqualTo(List.of(List.of(1), List.of(3)));
  }

  // Step 5: Two full levels — siblings are grouped together
  @Test
  void twoLevels() {
    //   1
    //  / \
    // 2   3
    assertThat(sut.levelOrder(TreeNode.of(1, 2, 3))).isEqualTo(List.of(List.of(1), List.of(2, 3)));
  }

  // Step 6: Sparse third level — traversal skips nulls and keeps left-to-right order within a level
  @Test
  void sparseThirdLevel() {
    //       1
    //      / \
    //     2   3
    //    /     \
    //   4       5
    assertThat(sut.levelOrder(TreeNode.of(1, 2, 3, 4, null, null, 5)))
        .isEqualTo(List.of(List.of(1), List.of(2, 3), List.of(4, 5)));
  }

  // Step 7: LeetCode example — general multi-level case
  @Test
  void leetCodeExample() {
    //     3
    //    / \
    //   9  20
    //      / \
    //     15   7
    assertThat(sut.levelOrder(TreeNode.of(3, 9, 20, null, null, 15, 7)))
        .isEqualTo(List.of(List.of(3), List.of(9, 20), List.of(15, 7)));
  }

  // Step 8: Left-skewed tree — each depth becomes its own level
  @Test
  void leftSkewed() {
    //     1
    //    /
    //   2
    //  /
    // 3
    assertThat(sut.levelOrder(TreeNode.of(1, 2, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
  }

  // Step 9: Right-skewed tree — mirrors left-skewed, catches left-biased traversals
  @Test
  void rightSkewed() {
    // 1
    //  \
    //   2
    //    \
    //     3
    assertThat(sut.levelOrder(TreeNode.of(1, null, 2, null, null, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
  }

  // Step 10: Complete binary tree — every position at all 3 levels is filled
  @Test
  void completeBinaryTree() {
    //       1
    //      / \
    //     2   3
    //    / \ / \
    //   4  5 6  7
    assertThat(sut.levelOrder(TreeNode.of(1, 2, 3, 4, 5, 6, 7)))
        .isEqualTo(List.of(List.of(1), List.of(2, 3), List.of(4, 5, 6, 7)));
  }

  // Step 11: Asymmetric depth — one branch deeper than the other, levels still separate correctly
  @Test
  void asymmetricDepth() {
    //     1
    //    / \
    //   2   3
    //  /
    // 4
    assertThat(sut.levelOrder(TreeNode.of(1, 2, 3, 4)))
        .isEqualTo(List.of(List.of(1), List.of(2, 3), List.of(4)));
  }
}
