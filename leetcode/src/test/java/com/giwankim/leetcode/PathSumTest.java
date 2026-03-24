package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;

class PathSumTest {
  PathSum sut = new PathSum();

  @Test
  void emptyTree() {
    assertThat(sut.hasPathSum(null, 0)).isFalse();
  }

  @Test
  void singleton() {
    assertThat(sut.hasPathSum(TreeNode.of(1), 1)).isTrue();
    assertThat(sut.hasPathSum(TreeNode.of(1), 2)).isFalse();
  }

  @Test
  void pathExists() {
    assertThat(sut.hasPathSum(TreeNode.of(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1), 22))
        .isTrue();
  }

  @Test
  void pathDoesNotExists() {
    assertThat(sut.hasPathSum(TreeNode.of(1, 2, 3), 5)).isFalse();
  }
}
