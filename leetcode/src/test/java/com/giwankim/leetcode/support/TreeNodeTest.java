package com.giwankim.leetcode.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TreeNodeTest {
  @Nested
  class FactoryMethodTest {
    @Test
    void singleNode() {
      assertThat(TreeNode.of(1)).isEqualTo(new TreeNode(1));
    }

    @Test
    void twoNodes() {
      TreeNode root = TreeNode.of(1, 2);
      assertThat(root.left).isEqualTo(new TreeNode(2));
      assertThat(root.right).isNull();
    }

    @Test
    void threeNodes() {
      TreeNode root = TreeNode.of(2, 1, 3);
      assertThat(root.val).isEqualTo(2);
      assertThat(root.left).isEqualTo(new TreeNode(1));
      assertThat(root.right).isEqualTo(new TreeNode(3));
    }

    @Test
    void withNull() {
      TreeNode root = TreeNode.of(1, null, 2, null, null, 3);
      assertThat(root.left).isNull();
      assertThat(root.right.val).isEqualTo(2);
      assertThat(root.right.right).isNull();
      assertThat(root.right.left.val).isEqualTo(3);
    }
  }

  @Nested
  class PrettyPrintTest {
    @Test
    void singleNode() {
      TreeNode root = new TreeNode(1);
      assertThat(root.prettyPrint()).isEqualTo("1\n");
    }

    @Test
    void threeNodes() {
      TreeNode root = TreeNode.of(2, 1, 3);
      assertThat(root.prettyPrint()).isEqualTo("\t3\n2\n\t1\n");
    }
  }
}
