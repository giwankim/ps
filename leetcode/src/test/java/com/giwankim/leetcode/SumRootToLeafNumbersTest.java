package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SumRootToLeafNumbersTest {

  @ParameterizedTest
  @MethodSource("cases")
  void sumNumbers(TreeNode root, int expected) {
    int actual = new SumRootToLeafNumbers().sumNumbers(root);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("cases")
  void sumNumbersDfs(TreeNode root, int expected) {
    int actual = new SumRootToLeafNumbers().sumNumbersDfs(root);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("cases")
  void sumNumbersBfs(TreeNode root, int expected) {
    int actual = new SumRootToLeafNumbers().sumNumbersBfs(root);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(new TreeNode(1, new TreeNode(2), new TreeNode(3)), 25),
        Arguments.of(
            new TreeNode(4, new TreeNode(9, new TreeNode(5), new TreeNode(1)), new TreeNode(0)),
            1026));
  }
}
