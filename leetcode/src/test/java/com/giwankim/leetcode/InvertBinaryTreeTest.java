package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InvertBinaryTreeTest {

  @ParameterizedTest
  @MethodSource
  void invertTree(TreeNode root, TreeNode expected) {
    TreeNode actual = new InvertBinaryTree().invertTree(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> invertTree() {
    return Stream.of(
        Arguments.of(TreeNode.from(4, 2, 7, 1, 3, 6, 9), TreeNode.from(4, 7, 2, 9, 6, 3, 1)),
        Arguments.of(TreeNode.from(2, 1, 3), TreeNode.from(2, 3, 1)),
        Arguments.of(TreeNode.from(), TreeNode.from()));
  }
}
