package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BalancedBinaryTreeTest {

  @ParameterizedTest
  @MethodSource("cases")
  void isBalanced(TreeNode root, boolean expected) {
    assertThat(new BalancedBinaryTree().isBalanced(root)).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(
            new TreeNode(3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7))),
            true),
        Arguments.of(
            new TreeNode(
                1,
                new TreeNode(2, new TreeNode(3, new TreeNode(4), new TreeNode(4)), new TreeNode(3)),
                new TreeNode(2)),
            false));
  }
}
