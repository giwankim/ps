package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidateBinarySearchTreeTest {
  @ParameterizedTest
  @MethodSource
  void isValidBST(TreeNode root, boolean expected) {
    boolean actual = new ValidateBinarySearchTree().isValidBST(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isValidBST() {
    return Stream.of(
        Arguments.of(TreeNode.from(2, 1, 3), true),
        Arguments.of(TreeNode.from(5, 1, 4, null, null, 3, 6), false),
        Arguments.of(TreeNode.from(-2147483648, null, 2147483647), true));
  }
}
