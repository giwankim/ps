package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InvertBinaryTreeTest {
  InvertBinaryTree sut = new InvertBinaryTree();

  @ParameterizedTest
  @MethodSource
  void invertTree(TreeNode root, TreeNode expected) {
    TreeNode actual = sut.invertTree(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> invertTree() {
    return Stream.of(
        Arguments.of(TreeNode.of(4, 2, 7, 1, 3, 6, 9), TreeNode.of(4, 7, 2, 9, 6, 3, 1)),
        Arguments.of(TreeNode.of(2, 1, 3), TreeNode.of(2, 3, 1)),
        Arguments.of(TreeNode.of(), TreeNode.of()));
  }
}
