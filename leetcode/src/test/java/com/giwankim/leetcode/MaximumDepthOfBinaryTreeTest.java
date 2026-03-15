package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximumDepthOfBinaryTreeTest {
  MaximumDepthOfBinaryTree sut = new MaximumDepthOfBinaryTree();

  @Test
  void emptyTree() {
    assertThat(sut.maxDepth(null)).isZero();
  }

  @Test
  void rootOnly() {
    assertThat(sut.maxDepth(TreeNode.of(1))).isEqualTo(1);
  }

  @ParameterizedTest
  @MethodSource
  void rootHasChildren(TreeNode root) {
    assertThat(sut.maxDepth(root)).isEqualTo(2);
  }

  static Stream<Arguments> rootHasChildren() {
    return Stream.of(
        Arguments.of(Named.of("left child only", TreeNode.of(1, 2, null))),
        Arguments.of(Named.of("right child only", TreeNode.of(1, null, 2))),
        Arguments.of(Named.of("both children", TreeNode.of(1, 2, 3))));
  }

  @Test
  void maxDepth() {
    TreeNode root = TreeNode.of(3, 9, 20, null, null, 15, 7);
    assertThat(sut.maxDepth(root)).isEqualTo(3);
  }
}
