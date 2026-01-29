package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinaryTreeLevelOrderTraversalTest {

  @ParameterizedTest
  @MethodSource
  void levelOrder(TreeNode root, List<List<Integer>> expected) {
    List<List<Integer>> actual = new BinaryTreeLevelOrderTraversal().levelOrder(root);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> levelOrder() {
    return Stream.of(
        Arguments.of(
            TreeNode.from(3, 9, 20, null, null, 15, 7),
            List.of(List.of(3), List.of(9, 20), List.of(15, 7))),
        Arguments.of(TreeNode.from(1), Collections.singletonList(List.of(1))),
        Arguments.of(TreeNode.from(), Collections.emptyList()));
  }
}
