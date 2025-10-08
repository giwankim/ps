package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiameterOfBinaryTreeTest {
  DiameterOfBinaryTree diameterOfBinaryTree = new DiameterOfBinaryTree();

  @ParameterizedTest
  @MethodSource
  void diameterOfBinaryTree(TreeNode root, int expected) {
    int actual = diameterOfBinaryTree.diameterOfBinaryTree(root);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> diameterOfBinaryTree() {
    return Stream.of(
        Arguments.of(new TreeNode(1, new TreeNode(2), null), 1),
        Arguments.of(
            new TreeNode(1, new TreeNode(2, new TreeNode(4), new TreeNode(5)), new TreeNode(3)),
            3));
  }

  @Test
  void height() {
    assertThat(diameterOfBinaryTree.height(new TreeNode(1))).isZero();
    assertThat(diameterOfBinaryTree.height(new TreeNode(1, new TreeNode(2), null)))
        .isEqualTo(1);
    assertThat(diameterOfBinaryTree.height(new TreeNode(1, new TreeNode(2, new TreeNode(4), new TreeNode(5)), new TreeNode(3))))
        .isEqualTo(2);
  }
}
