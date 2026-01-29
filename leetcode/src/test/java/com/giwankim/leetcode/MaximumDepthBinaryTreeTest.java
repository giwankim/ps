package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MaximumDepthBinaryTreeTest {

  @ParameterizedTest
  @MethodSource
  void maxDepth(TreeNode root, int expected) {
    int actual = new MaximumDepthBinaryTree().maxDepth(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maxDepth() {
    return Stream.of(
        Arguments.of(TreeNode.from(3, 9, 20, null, null, 15, 7), 3),
        Arguments.of(TreeNode.from(1, null, 2), 2),
        Arguments.of(TreeNode.from(), 0));
  }
}
