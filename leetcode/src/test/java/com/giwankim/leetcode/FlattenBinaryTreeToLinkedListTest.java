package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlattenBinaryTreeToLinkedListTest {
  FlattenBinaryTreeToLinkedList sut;

  @BeforeEach
  void setUp() {
    sut = new FlattenBinaryTreeToLinkedList();
  }

  @Test
  void emptyTree() {
    TreeNode root = null;
    sut.flatten(root);
    assertThat(root).isNull();
  }

  @Test
  void singleton() {
    TreeNode root = TreeNode.of(1);
    sut.flatten(root);
    assertThat(root).isEqualTo(TreeNode.of(1));
  }

  @Test
  void leftOnlyChild() {
    TreeNode root = TreeNode.of(1, 2, null);
    sut.flatten(root);
    assertThat(root).isEqualTo(TreeNode.of(1, null, 2));
  }

  @Test
  void rightOnlyChild() {
    TreeNode root = TreeNode.of(1, null, 2);
    sut.flatten(root);
    assertThat(root).isEqualTo(TreeNode.of(1, null, 2));
  }

  @Test
  void leftSkewed() {
    TreeNode root = TreeNode.of(3, 2, null, 1);
    sut.flatten(root);
    assertThat(root).isEqualTo(TreeNode.of(3, null, 2, null, null, null, 1));
  }

  @Test
  void rightSkewed() {
    TreeNode root = TreeNode.of(1, null, 2, null, null, null, 3);
    sut.flatten(root);
    assertThat(root).isEqualTo(TreeNode.of(1, null, 2, null, null, null, 3));
  }

  @Test
  void exampleTree() {
    TreeNode root = TreeNode.of(1, 2, 5, 3, 4, null, 6);
    sut.flatten(root);
    TreeNode expected =
        TreeNode.builder()
            .val(1)
            .right(
                TreeNode.builder()
                    .val(2)
                    .right(
                        TreeNode.builder()
                            .val(3)
                            .right(
                                TreeNode.builder()
                                    .val(4)
                                    .right(
                                        TreeNode.builder()
                                            .val(5)
                                            .right(TreeNode.builder().val(6).build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();
    assertThat(root).isEqualTo(expected);
  }
}
