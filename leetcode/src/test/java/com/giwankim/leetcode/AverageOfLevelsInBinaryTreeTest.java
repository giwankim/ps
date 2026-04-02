package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.List;
import org.junit.jupiter.api.Test;

class AverageOfLevelsInBinaryTreeTest {
  AverageOfLevelsInBinaryTree sut = new AverageOfLevelsInBinaryTree();

  @Test
  void singleton() {
    assertAverages(sut.averageOfLevels(TreeNode.of(1)), 1.0);
  }

  @Test
  void twoLevels() {
    // root=1, left=2, right=3 → level 0: avg 1.0, level 1: avg 2.5
    assertAverages(sut.averageOfLevels(TreeNode.of(1, 2, 3)), 1.0, 2.5);
  }

  @Test
  void leftOnly() {
    // root=1, left=2 → level 0: avg 1.0, level 1: avg 2.0
    assertAverages(sut.averageOfLevels(TreeNode.of(1, 2)), 1.0, 2.0);
  }

  @Test
  void rightOnly() {
    // root=1, right=3 → level 0: avg 1.0, level 1: avg 3.0
    assertAverages(sut.averageOfLevels(TreeNode.of(1, null, 3)), 1.0, 3.0);
  }

  @Test
  void leetCodeExample1() {
    //       3
    //      / \
    //     9  20
    //       /  \
    //      15   7
    assertAverages(sut.averageOfLevels(TreeNode.of(3, 9, 20, null, null, 15, 7)), 3.0, 14.5, 11.0);
  }

  @Test
  void leetCodeExample2() {
    //       3
    //      / \
    //     9  20
    //    / \
    //   15  7
    assertAverages(sut.averageOfLevels(TreeNode.of(3, 9, 20, 15, 7)), 3.0, 14.5, 11.0);
  }

  @Test
  void leftSkewed() {
    // 3 → 2 → 1 (each level has one node)
    assertAverages(sut.averageOfLevels(TreeNode.of(3, 2, null, 1)), 3.0, 2.0, 1.0);
  }

  @Test
  void largeValues() {
    // tests that averaging doesn't overflow with Integer.MAX_VALUE
    assertAverages(
        sut.averageOfLevels(TreeNode.of(2147483647, 2147483647, 2147483647)),
        2147483647.0,
        2147483647.0);
  }

  private static void assertAverages(List<Double> actual, Double... expected) {
    assertThat(actual)
        .usingElementComparator((a, b) -> Math.abs(a - b) <= 1e-5 ? 0 : Double.compare(a, b))
        .containsExactly(expected);
  }
}
