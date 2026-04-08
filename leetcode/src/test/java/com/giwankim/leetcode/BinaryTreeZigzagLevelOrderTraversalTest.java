package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.List;
import org.junit.jupiter.api.Test;

class BinaryTreeZigzagLevelOrderTraversalTest {
  BinaryTreeZigzagLevelOrderTraversal sut = new BinaryTreeZigzagLevelOrderTraversal();

  // Step 1: Base case — empty tree has no levels
  @Test
  void emptyTree() {
    assertThat(sut.zigzagLevelOrder(TreeNode.of())).isEqualTo(List.of());
    assertThat(sut.zigzagLevelOrder2(TreeNode.of())).isEqualTo(List.of());
  }

  // Step 2: Single node — one level, zigzag irrelevant
  @Test
  void singleNode() {
    //   1
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1))).isEqualTo(List.of(List.of(1)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1))).isEqualTo(List.of(List.of(1)));
  }

  // Step 3: Left child only — each level has one node, zigzag has no visible effect
  @Test
  void leftChildOnly() {
    //   1
    //  /
    // 2
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2))).isEqualTo(List.of(List.of(1), List.of(2)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2))).isEqualTo(List.of(List.of(1), List.of(2)));
  }

  // Step 4: Right child only — same reasoning as left child
  @Test
  void rightChildOnly() {
    // 1
    //  \
    //   3
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(3)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(3)));
  }

  // Step 5: Two full levels — zigzag first matters here!
  // Level 0 (L→R): [1], Level 1 (R→L): [3, 2]
  // A plain level-order returns [[1],[2,3]] — this test forces the reversal.
  @Test
  void twoFullLevelsZigzag() {
    //   1
    //  / \
    // 2   3
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2, 3)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2, 3)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2)));
  }

  // Step 6: Three full levels — confirms alternation continues
  // Level 0 (L→R): [1], Level 1 (R→L): [3, 2], Level 2 (L→R): [4, 5, 6, 7]
  @Test
  void threeFullLevelsZigzag() {
    //       1
    //      / \
    //     2   3
    //    / \ / \
    //   4  5 6  7
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2, 3, 4, 5, 6, 7)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4, 5, 6, 7)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2, 3, 4, 5, 6, 7)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4, 5, 6, 7)));
  }

  // Step 7: LeetCode example
  // Level 0 (L→R): [3], Level 1 (R→L): [20, 9], Level 2 (L→R): [15, 7]
  @Test
  void leetCodeExample() {
    //     3
    //    / \
    //   9  20
    //      / \
    //     15   7
    assertThat(sut.zigzagLevelOrder(TreeNode.of(3, 9, 20, null, null, 15, 7)))
        .isEqualTo(List.of(List.of(3), List.of(20, 9), List.of(15, 7)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(3, 9, 20, null, null, 15, 7)))
        .isEqualTo(List.of(List.of(3), List.of(20, 9), List.of(15, 7)));
  }

  // Step 8: Sparse third level — zigzag with gaps
  // Level 0 (L→R): [1], Level 1 (R→L): [3, 2], Level 2 (L→R): [4, 5]
  @Test
  void sparseThirdLevelZigzag() {
    //       1
    //      / \
    //     2   3
    //    /     \
    //   4       5
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2, 3, 4, null, null, 5)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4, 5)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2, 3, 4, null, null, 5)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4, 5)));
  }

  // Step 9: Left-skewed — one node per level, zigzag has no visible effect
  @Test
  void leftSkewed() {
    //     1
    //    /
    //   2
    //  /
    // 3
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
  }

  // Step 10: Right-skewed — mirrors left-skewed
  @Test
  void rightSkewed() {
    // 1
    //  \
    //   2
    //    \
    //     3
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, null, 2, null, null, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, null, 2, null, null, null, 3)))
        .isEqualTo(List.of(List.of(1), List.of(2), List.of(3)));
  }

  // Step 11: Asymmetric — one branch deeper, zigzag still alternates
  // Level 0 (L→R): [1], Level 1 (R→L): [3, 2], Level 2 (L→R): [4]
  @Test
  void asymmetricDepthZigzag() {
    //     1
    //    / \
    //   2   3
    //  /
    // 4
    assertThat(sut.zigzagLevelOrder(TreeNode.of(1, 2, 3, 4)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4)));
    assertThat(sut.zigzagLevelOrder2(TreeNode.of(1, 2, 3, 4)))
        .isEqualTo(List.of(List.of(1), List.of(3, 2), List.of(4)));
  }
}
