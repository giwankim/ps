package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InvertBinaryTreeTest {

  @ParameterizedTest
  @MethodSource
  void invertTree(TreeNode root, TreeNode expected) {
    assertThat(new InvertBinaryTree().invertTree(root)).isEqualTo(expected);
  }

  static Stream<Arguments> invertTree() {
    return Stream.of(
        Arguments.of(
            new TreeNode(2, new TreeNode(1), new TreeNode(3)),
            new TreeNode(2, new TreeNode(3), new TreeNode(1))),
        Arguments.of(null, null));
  }
}
