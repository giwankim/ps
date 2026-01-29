package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SubtreeOfAnotherTreeTest {

  @ParameterizedTest
  @MethodSource
  void isSubtree(TreeNode root, TreeNode subRoot, boolean expected) {
    boolean actual = new SubtreeOfAnotherTree().isSubtree(root, subRoot);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isSubtree() {
    return Stream.of(
        Arguments.of(TreeNode.from(3, 4, 5, 1, 2), TreeNode.from(4, 1, 2), true),
        Arguments.of(
            TreeNode.from(3, 4, 5, 1, 2, null, null, null, null, 0), TreeNode.from(4, 1, 2), false),
        Arguments.of(TreeNode.from(3, 4, 5, 1, null, 2), TreeNode.from(3, 1, 2), false));
  }
}
