package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SameTreeTest {
  SameTree sut = new SameTree();

  @ParameterizedTest
  @MethodSource
  void emptyTree(TreeNode p, TreeNode q, boolean expected) {
    assertThat(sut.isSameTree(p, q)).isEqualTo(expected);
  }

  static Stream<Arguments> emptyTree() {
    return Stream.of(
        Arguments.argumentSet("both trees are empty", TreeNode.of(), TreeNode.of(), true),
        Arguments.argumentSet("left tree is empty", TreeNode.of(1), TreeNode.of(), false),
        Arguments.argumentSet("right tree is empty", TreeNode.of(), TreeNode.of(1), false));
  }

  @ParameterizedTest
  @MethodSource
  void isSameTree(TreeNode p, TreeNode q, boolean expected) {
    boolean actual = sut.isSameTree(p, q);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isSameTree() {
    return Stream.of(
        Arguments.argumentSet("identical trees", TreeNode.of(1, 2, 3), TreeNode.of(1, 2, 3), true),
        Arguments.argumentSet(
            "different structure", TreeNode.of(1, 2), TreeNode.of(1, null, 2), false),
        Arguments.argumentSet(
            "different values", TreeNode.of(1, 2, 1), TreeNode.of(1, 1, 2), false));
  }
}
