package leetcode.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TreeNodeTest {

  @Nested
  class FromTest {
    @Test
    void singleNode() {
      assertThat(TreeNode.from(1)).isEqualTo(new TreeNode(1));
    }

    @Test
    void twoNodes() {
      TreeNode root = TreeNode.from(1, 2);
      assertThat(root.left).isEqualTo(new TreeNode(2));
      assertThat(root.right).isNull();
    }

    @Test
    void threeNodes() {
      TreeNode root = TreeNode.from(2, 1, 3);
      assertThat(root.val).isEqualTo(2);
      assertThat(root.left).isEqualTo(new TreeNode(1));
      assertThat(root.right).isEqualTo(new TreeNode(3));
    }
  }
}
