package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LowestCommonAncestorOfBinaryTreeTest {
  @ParameterizedTest
  @MethodSource
  void lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q, TreeNode expected) {
    TreeNode actual = new LowestCommonAncestorOfBinaryTree().lowestCommonAncestor(root, p, q);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> lowestCommonAncestor() {
    TreeNode root1 = TreeNode.from(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    TreeNode root2 = TreeNode.from(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    TreeNode root3 = TreeNode.from(1, 2);
    return Stream.of(
        Arguments.of(root1, root1.left, root1.right, root1),
        Arguments.of(root2, root2.left, root2.left.right.right, root2.left),
        Arguments.of(root3, root3, root3.left, root3));
  }
}
