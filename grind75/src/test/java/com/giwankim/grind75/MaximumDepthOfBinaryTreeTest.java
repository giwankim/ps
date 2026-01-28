package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import org.junit.jupiter.api.Test;

class MaximumDepthOfBinaryTreeTest {

  MaximumDepthOfBinaryTree sut = new MaximumDepthOfBinaryTree();

  @Test
  void maxDepth() {
    assertThat(sut.maxDepth(null)).isZero();
    assertThat(sut.maxDepth(new TreeNode(1, null, new TreeNode(2)))).isEqualTo(2);
    assertThat(
            sut.maxDepth(
                new TreeNode(
                    3, new TreeNode(9), new TreeNode(20, new TreeNode(15), new TreeNode(7)))))
        .isEqualTo(3);
  }
}
