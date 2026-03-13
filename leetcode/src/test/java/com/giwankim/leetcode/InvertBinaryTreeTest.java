package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InvertBinaryTreeTest {
  InvertBinaryTree sut = new InvertBinaryTree();

  @Test
  void nullTree() {
    assertThat(sut.invertTree(null)).isNull();
  }

  @Test
  void nullTree2() {
    assertThat(sut.invertTree2(null)).isNull();
  }

  @ParameterizedTest
  @MethodSource
  void invertTree(TreeNode root, TreeNode expected) {
    assertThat(sut.invertTree(root)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("invertTree")
  void invertTree2(TreeNode root, TreeNode expected) {
    assertThat(sut.invertTree2(root)).isEqualTo(expected);
  }

  private static Stream<Arguments> invertTree() {
    return Stream.of(
        Arguments.of(TreeNode.of(4, 2, 7, 1, 3, 6, 9), TreeNode.of(4, 7, 2, 9, 6, 3, 1)),
        Arguments.of(TreeNode.of(2, 1, 3), TreeNode.of(2, 3, 1)));
  }
}
