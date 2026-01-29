package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinaryTreePathsTest {

  @ParameterizedTest
  @MethodSource
  void binaryTreePaths(TreeNode root, List<String> expected) {
    List<String> actual = new BinaryTreePaths().binaryTreePaths(root);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  private static Stream<Arguments> binaryTreePaths() {
    return Stream.of(
        Arguments.of(TreeNode.from(1, 2, 3, null, 5), List.of("1->2->5", "1->3")),
        Arguments.of(TreeNode.from(1), List.of("1")));
  }
}
