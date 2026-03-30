package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.BinarySearchTreeIterator.BSTIterator;
import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BinarySearchTreeIteratorTest {
  @Test
  void singleNode() {
    BSTIterator it = new BSTIterator(TreeNode.of(1));

    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(1);
    assertThat(it.hasNext()).isFalse();
  }

  @Test
  void leftSkewedTree() {
    BSTIterator it = new BSTIterator(TreeNode.of(3, 2, null, 1));

    assertThat(it.next()).isEqualTo(1);
    assertThat(it.next()).isEqualTo(2);
    assertThat(it.next()).isEqualTo(3);
    assertThat(it.hasNext()).isFalse();
  }

  @Test
  void rightSkewedTree() {
    BSTIterator it = new BSTIterator(TreeNode.of(1, null, 2, null, null, null, 3));

    assertThat(it.next()).isEqualTo(1);
    assertThat(it.next()).isEqualTo(2);
    assertThat(it.next()).isEqualTo(3);
    assertThat(it.hasNext()).isFalse();
  }

  @Test
  void hasNextDoesNotAdvance() {
    BSTIterator it = new BSTIterator(TreeNode.of(2, 1, 3));

    assertThat(it.hasNext()).isTrue();
    assertThat(it.hasNext()).isTrue();
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(1);

    assertThat(it.hasNext()).isTrue();
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(2);
  }

  @Test
  void leetCodeExample() {
    BSTIterator it = new BSTIterator(TreeNode.of(7, 3, 15, null, null, 9, 20));

    assertThat(it.next()).isEqualTo(3);
    assertThat(it.next()).isEqualTo(7);
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(9);
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(15);
    assertThat(it.hasNext()).isTrue();
    assertThat(it.next()).isEqualTo(20);
    assertThat(it.hasNext()).isFalse();
  }

  @ParameterizedTest
  @MethodSource
  void fullTraversal(TreeNode root, List<Integer> expected) {
    BSTIterator it = new BSTIterator(root);

    List<Integer> actual = new ArrayList<>();
    while (it.hasNext()) {
      actual.add(it.next());
    }
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> fullTraversal() {
    return Stream.of(
        Arguments.of(TreeNode.of(2, 1, 3), List.of(1, 2, 3)),
        Arguments.of(TreeNode.of(7, 3, 15, null, null, 9, 20), List.of(3, 7, 9, 15, 20)),
        Arguments.of(TreeNode.of(4, 2, 6, 1, 3, 5, 7), List.of(1, 2, 3, 4, 5, 6, 7)));
  }
}
