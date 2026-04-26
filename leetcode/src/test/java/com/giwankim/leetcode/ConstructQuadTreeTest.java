package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.ConstructQuadTree.Node;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ConstructQuadTreeTest {
  ConstructQuadTree sut = new ConstructQuadTree();

  // Step 1: n=1 base case, value 0 — forces the method to produce a leaf
  // rather than null or an empty tree.
  @Test
  void returnsFalseLeafForSingleZeroCell() {
    Node root = sut.construct(new int[][] {{0}});

    assertLeaf(root, false);
  }

  // Step 2: n=1 base case, value 1 — mirror of step 1 for the other boolean,
  // catching any false-as-default assumption in the leaf constructor.
  @Test
  void returnsTrueLeafForSingleOneCell() {
    Node root = sut.construct(new int[][] {{1}});

    assertLeaf(root, true);
  }

  // Step 3: n=2 uniform — the grid is all 1s, so the result must collapse to
  // a single leaf at the root. Catches implementations that always split when
  // n > 1.
  @Test
  void collapsesUniformTwoByTwoGridIntoSingleLeaf() {
    Node root = sut.construct(new int[][] {
      {1, 1},
      {1, 1}
    });

    assertLeaf(root, true);
  }

  // Step 4: n=2 mixed — LeetCode Example 1. The smallest grid that forces a
  // split: every cell differs from its neighbors, so all four 1x1 children
  // must be leaves with the exact per-cell values.
  @Test
  void splitsMixedTwoByTwoGridIntoQuadrantLeaves() {
    Node root = sut.construct(new int[][] {
      {0, 1},
      {1, 0}
    });

    assertInternal(root);
    assertLeaf(root.topLeft, false);
    assertLeaf(root.topRight, true);
    assertLeaf(root.bottomLeft, true);
    assertLeaf(root.bottomRight, false);
  }

  // Step 5: n=4 uniform — the whole grid is all 0s and must collapse to a
  // single root leaf. Catches implementations that only short-circuit
  // uniformity at n=2, or that unconditionally recurse into 4 subgrids
  // whenever n > 1.
  @Test
  void collapsesAllZeroFourByFourGridIntoSingleLeaf() {
    Node root = sut.construct(new int[4][4]);

    assertLeaf(root, false);
  }

  // Step 6: n=4 mixed quadrants — two quadrants are uniform (collapse to
  // leaves) and two are mixed (recurse one more level). Exercises uniform-leaf
  // handling at a non-root level alongside multi-level recursion in the same
  // tree.
  @Test
  void recursivelySplitsMixedQuadrantsUntilTheirRegionsAreUniform() {
    Node root = sut.construct(new int[][] {
      {1, 1, 0, 0},
      {1, 1, 0, 1},
      {1, 1, 1, 1},
      {0, 1, 1, 1}
    });

    assertInternal(root);
    assertLeaf(root.topLeft, true);

    assertInternal(root.topRight);
    assertLeaf(root.topRight.topLeft, false);
    assertLeaf(root.topRight.topRight, false);
    assertLeaf(root.topRight.bottomLeft, false);
    assertLeaf(root.topRight.bottomRight, true);

    assertInternal(root.bottomLeft);
    assertLeaf(root.bottomLeft.topLeft, true);
    assertLeaf(root.bottomLeft.topRight, true);
    assertLeaf(root.bottomLeft.bottomLeft, false);
    assertLeaf(root.bottomLeft.bottomRight, true);

    assertLeaf(root.bottomRight, true);
  }

  // Step 7: n=8 — LeetCode Example 2. Three of the four top-level quadrants
  // are uniform 4x4 regions that must collapse to leaves; the fourth is mixed
  // and must recurse into four uniform 2x2 leaves. Forces the tree to have
  // uneven depth.
  @Test
  void representsEightByEightExampleWithCompressedQuadrants() {
    Node root = sut.construct(new int[][] {
      {1, 1, 1, 1, 0, 0, 0, 0},
      {1, 1, 1, 1, 0, 0, 0, 0},
      {1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 1, 1, 0, 0, 0, 0},
      {1, 1, 1, 1, 0, 0, 0, 0},
      {1, 1, 1, 1, 0, 0, 0, 0},
      {1, 1, 1, 1, 0, 0, 0, 0}
    });

    assertInternal(root);
    assertLeaf(root.topLeft, true);

    assertInternal(root.topRight);
    assertLeaf(root.topRight.topLeft, false);
    assertLeaf(root.topRight.topRight, false);
    assertLeaf(root.topRight.bottomLeft, true);
    assertLeaf(root.topRight.bottomRight, true);

    assertLeaf(root.bottomLeft, true);
    assertLeaf(root.bottomRight, false);
  }

  // Step 8: n=64 — upper bound of the LeetCode constraint (n = 2^x,
  // 0 <= x <= 6). A uniform grid keeps the assertion trivial while proving
  // the algorithm short-circuits uniformity at large n rather than recursing
  // down to all 4096 base cases.
  @Test
  void sixtyFourByConstraintBoundary() {
    int[][] grid = new int[64][64];
    for (int[] row : grid) {
      Arrays.fill(row, 1);
    }

    Node root = sut.construct(grid);

    assertLeaf(root, true);
  }

  private void assertLeaf(Node node, boolean expectedValue) {
    assertThat(node).isNotNull();
    assertThat(node.isLeaf).isTrue();
    assertThat(node.val).isEqualTo(expectedValue);
    assertThat(node.topLeft).isNull();
    assertThat(node.topRight).isNull();
    assertThat(node.bottomLeft).isNull();
    assertThat(node.bottomRight).isNull();
  }

  private void assertInternal(Node node) {
    assertThat(node).isNotNull();
    assertThat(node.isLeaf).isFalse();
    assertThat(node.topLeft).isNotNull();
    assertThat(node.topRight).isNotNull();
    assertThat(node.bottomLeft).isNotNull();
    assertThat(node.bottomRight).isNotNull();
  }
}
