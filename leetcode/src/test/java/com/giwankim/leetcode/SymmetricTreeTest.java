package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SymmetricTreeTest {
  SymmetricTree sut = new SymmetricTree();

  @Test
  void nullRoot() {
    assertThat(sut.isSymmetric(null)).isTrue();
    assertThat(sut.isSymmetric2(null)).isTrue();
  }

  @Test
  void singleton() {
    assertThat(sut.isSymmetric(TreeNode.of(1))).isTrue();
    assertThat(sut.isSymmetric2(TreeNode.of(1))).isTrue();
  }

  @ParameterizedTest
  @MethodSource
  void depthTwo(TreeNode root, boolean expected) {
    assertThat(sut.isSymmetric(root)).isEqualTo(expected);
    assertThat(sut.isSymmetric2(root)).isEqualTo(expected);
  }

  static Stream<Arguments> depthTwo() {
    return Stream.of(
        Arguments.of(TreeNode.of(1, 2, null), false),
        Arguments.of(TreeNode.of(1, null, 3), false),
        Arguments.of(TreeNode.of(1, 2, 2), true),
        Arguments.of(TreeNode.of(1, 2, 3), false));
  }

  @ParameterizedTest
  @MethodSource
  void symmetric(TreeNode root) {
    assertThat(sut.isSymmetric(root)).isTrue();
    assertThat(sut.isSymmetric2(root)).isTrue();
  }

  static Stream<Arguments> symmetric() {
    return Stream.of(
        Arguments.of(TreeNode.of(1, 2, 2, 3, 4, 4, 3)),
        Arguments.of(TreeNode.of(1, 2, 2, null, 3, 3, null)),
        Arguments.of(TreeNode.of(1, 2, 2, null, 3, 3)),
        Arguments.of(TreeNode.of(1, 1, 1, 1, 1, 1, 1)));
  }

  @ParameterizedTest
  @MethodSource
  void asymmetric(TreeNode root) {
    assertThat(sut.isSymmetric(root)).isFalse();
    assertThat(sut.isSymmetric2(root)).isFalse();
  }

  static Stream<Arguments> asymmetric() {
    return Stream.of(
        Arguments.of(TreeNode.of(1, 2, 2, null, 3, null, 3)),
        Arguments.of(TreeNode.of(1, 2, 2, 3, null, null, null)),
        Arguments.of(TreeNode.of(1, 2, 2, 3, null, 3, null)),
        Arguments.of(TreeNode.of(1, 2, 2, 3, 4, 3, 4)),
        Arguments.of(TreeNode.of(1, 1, 1, 1, null, 1, null)));
  }
}
