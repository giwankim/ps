package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class ValidateBinarySearchTreeTest {
  ValidateBinarySearchTree sut = new ValidateBinarySearchTree();

  // Base case: single node is trivially valid

  @Test
  void shouldReturnTrueForSingleNode() {
    //   1
    // Catches: implementations that require children to exist
    assertThat(sut.isValidBST(TreeNode.of(1))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(1))).isTrue();
  }

  // Two-node trees: force actual value comparison

  @Test
  void shouldReturnTrueWhenLeftChildIsSmaller() {
    //   2
    //  /
    // 1
    // Catches: hardcoded return true from base cases
    assertThat(sut.isValidBST(TreeNode.of(2, 1))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(2, 1))).isTrue();
  }

  @Test
  void shouldReturnTrueWhenRightChildIsLarger() {
    // 1
    //  \
    //   2
    // Catches: only checking left children
    assertThat(sut.isValidBST(TreeNode.of(1, null, 2))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(1, null, 2))).isTrue();
  }

  @Test
  void shouldReturnFalseWhenLeftChildIsGreater() {
    //   1
    //  /
    // 2
    // Catches: never returning false (first false case)
    assertThat(sut.isValidBST(TreeNode.of(1, 2))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(1, 2))).isFalse();
  }

  @Test
  void shouldReturnFalseWhenRightChildIsSmaller() {
    // 2
    //  \
    //   1
    // Catches: only detecting left-side violations
    assertThat(sut.isValidBST(TreeNode.of(2, null, 1))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(2, null, 1))).isFalse();
  }

  // Three-node valid BST: both children present

  @Test
  void shouldReturnTrueForSimpleValidBST() {
    //   2
    //  / \
    // 1   3
    // Catches: implementations that only check one child direction
    assertThat(sut.isValidBST(TreeNode.of(2, 1, 3))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(2, 1, 3))).isTrue();
  }

  // Equal values: BST requires strict inequality

  @Test
  void shouldReturnFalseWhenLeftChildEqualsRoot() {
    //   2
    //  /
    // 2
    // Catches: using < instead of <= in violation check
    assertThat(sut.isValidBST(TreeNode.of(2, 2))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(2, 2))).isFalse();
  }

  @Test
  void shouldReturnFalseWhenRightChildEqualsRoot() {
    // 2
    //  \
    //   2
    // Catches: using > instead of >= in violation check
    assertThat(sut.isValidBST(TreeNode.of(2, null, 2))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(2, null, 2))).isFalse();
  }

  // Multi-level invalid BST: right child violates parent

  @Test
  void shouldReturnFalseWhenRightChildIsLessThanRoot() {
    //     5
    //    / \
    //   1   4
    //      / \
    //     3   6
    // Node 4 is a right child of 5 but 4 < 5.
    // Catches: only checking immediate leaf violations
    assertThat(sut.isValidBST(TreeNode.of(5, 1, 4, null, null, 3, 6))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(5, 1, 4, null, null, 3, 6))).isFalse();
  }

  // Ancestor constraint: the classic trap

  @Test
  void shouldReturnFalseWhenNodeViolatesAncestorConstraint() {
    //     5
    //    / \
    //   4   6
    //      / \
    //     3   7
    // Node 3 is valid vs parent 6 (3 < 6) but invalid vs ancestor 5 (3 < 5).
    // Catches: naive parent-child-only checking
    assertThat(sut.isValidBST(TreeNode.of(5, 4, 6, null, null, 3, 7))).isFalse();
    assertThat(sut.isValidBST2(TreeNode.of(5, 4, 6, null, null, 3, 7))).isFalse();
  }

  // Deeper / skewed trees: ensure traversal beyond depth 2

  @Test
  void shouldReturnTrueForLeftSkewedBST() {
    //     3
    //    /
    //   2
    //  /
    // 1
    // Catches: off-by-one in traversal depth
    assertThat(sut.isValidBST(TreeNode.of(3, 2, null, 1))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(3, 2, null, 1))).isTrue();
  }

  @Test
  void shouldReturnTrueForRightSkewedBST() {
    // 1
    //  \
    //   2
    //    \
    //     3
    // Catches: traversal not following right chains fully
    assertThat(sut.isValidBST(TreeNode.of(1, null, 2, null, null, null, 3))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(1, null, 2, null, null, null, 3))).isTrue();
  }

  // Integer boundary: sentinel initialization bug

  @Test
  void shouldReturnTrueForSingleNodeAtIntegerMinValue() {
    // -2147483648
    // Catches: initializing prev to Integer.MIN_VALUE as sentinel
    assertThat(sut.isValidBST(TreeNode.of(Integer.MIN_VALUE))).isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(Integer.MIN_VALUE))).isTrue();
  }

  @Test
  void shouldReturnTrueForMinAndMaxIntegerValues() {
    // -2147483648
    //            \
    //             2147483647
    // Catches: integer overflow when computing bounds (e.g., root.val + 1 overflows int)
    assertThat(sut.isValidBST(TreeNode.of(Integer.MIN_VALUE, null, Integer.MAX_VALUE)))
        .isTrue();
    assertThat(sut.isValidBST2(TreeNode.of(Integer.MIN_VALUE, null, Integer.MAX_VALUE)))
        .isTrue();
  }
}
