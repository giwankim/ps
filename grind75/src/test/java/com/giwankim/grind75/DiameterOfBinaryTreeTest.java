package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiameterOfBinaryTreeTest {

  DiameterOfBinaryTree sut = new DiameterOfBinaryTree();

  @Test
  void singleNode() {
    TreeNode root = new TreeNode(1);
    int diameter = sut.diameterOfBinaryTree(root);
    assertThat(diameter).isEqualTo(0);
  }

  @Test
  void twoNodes() {
    TreeNode root = new TreeNode(1, new TreeNode(2), null);
    int diameter = sut.diameterOfBinaryTree(root);
    assertThat(diameter).isEqualTo(1);
  }

  @ParameterizedTest
  @MethodSource
  void diameterPassesThroughTheRoot(TreeNode root, int expected) {
    int diameter = sut.diameterOfBinaryTree(root);
    assertThat(diameter).isEqualTo(expected);
  }

  static Stream<Arguments> diameterPassesThroughTheRoot() {
    //     1
    //    / \
    //   2   3
    TreeNode tree1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
    //       1
    //      / \
    //     2   3
    //      \
    //       4
    TreeNode tree2 = new TreeNode(1, new TreeNode(2, null, new TreeNode(4)), new TreeNode(3));
    return Stream.of(Arguments.of(tree1, 2), Arguments.of(tree2, 3));
  }

  @Test
  void diameterDoesNotPassThroughRoot() {
    //        1
    //       /
    //      2
    //     / \
    //    3   5
    //   /     \
    //  4       6
    TreeNode tree =
        TreeNode.builder()
            .val(1)
            .left(
                TreeNode.builder()
                    .val(2)
                    .left(TreeNode.builder().val(3).left(TreeNode.builder().val(4).build()).build())
                    .right(
                        TreeNode.builder().val(5).right(TreeNode.builder().val(6).build()).build())
                    .build())
            .build();
    int diameter = sut.diameterOfBinaryTree(tree);
    assertThat(diameter).isEqualTo(4);
  }
}
