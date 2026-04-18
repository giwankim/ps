package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ConvertSortedArrayToBinarySearchTreeTest {
  ConvertSortedArrayToBinarySearchTree sut = new ConvertSortedArrayToBinarySearchTree();

  // Step 1: single element — base case of the divide-and-conquer recursion.
  // Forces the method to produce a leaf node rather than null or an empty tree.
  @Test
  void singleElementReturnsLeafNode() {
    assertThat(sut.sortedArrayToBST(new int[] {1})).isEqualTo(TreeNode.of(1));
  }

  // Step 2: two elements — the problem accepts either {1, null, 2} or {2, 1};
  // both are height-balanced. Assert properties (inorder + balance) rather than
  // a specific shape so the test isn't bound to a lower-vs-upper midpoint choice.
  @Test
  void twoElementsProduceBalancedBST() {
    int[] nums = {1, 2};
    TreeNode root = sut.sortedArrayToBST(nums);
    assertThat(inorder(root)).containsExactly(1, 2);
    assertThat(isBalanced(root)).isTrue();
  }

  // Step 3: three elements — only one balanced BST shape exists (middle-as-root
  // with two leaves), so exact structure can be asserted.
  //   2
  //  / \
  // 1   3
  // Catches: picking the first or last element as root instead of the middle.
  @Test
  void threeElementsProduceMiddleRootTree() {
    assertThat(sut.sortedArrayToBST(new int[] {1, 2, 3})).isEqualTo(TreeNode.of(2, 1, 3));
  }

  // Step 4: canonical LeetCode Example 1 — negatives, zero, and positives at
  // length 5. The problem explicitly states multiple outputs are accepted
  // (e.g., {0,-3,9,-10,null,5} and {0,-10,5,null,-3,null,9}), so use properties
  // to stay implementation-agnostic.
  @Test
  void fiveElementsWithNegativesAndZero() {
    int[] nums = {-10, -3, 0, 5, 9};
    TreeNode root = sut.sortedArrayToBST(nums);
    assertThat(inorder(root)).containsExactly(-10, -3, 0, 5, 9);
    assertThat(isBalanced(root)).isTrue();
  }

  // Step 5: non-power-of-two length — 10 elements cannot form a perfect binary
  // tree, so the height-balance invariant must be maintained via an uneven
  // left/right split somewhere along the recursion.
  // Catches: implementations that assume an exact-halves partition.
  @Test
  void tenElementsMaintainBalanceWithUnevenSplits() {
    int[] nums = IntStream.rangeClosed(1, 10).toArray();
    TreeNode root = sut.sortedArrayToBST(nums);
    assertThat(inorder(root)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    assertThat(isBalanced(root)).isTrue();
  }

  // Step 6: maximum length per the LeetCode constraint (1 <= nums.length <= 10^4).
  // Same structural properties at the boundary; also exercises recursion depth
  // (~log2(10^4) ≈ 14 for a balanced tree, comfortably within the JVM default stack).
  @Test
  void tenThousandElementsAtConstraintBoundary() {
    int[] nums = IntStream.rangeClosed(1, 10_000).toArray();
    List<Integer> expected = IntStream.of(nums).boxed().toList();
    TreeNode root = sut.sortedArrayToBST(nums);
    assertThat(inorder(root)).isEqualTo(expected);
    assertThat(isBalanced(root)).isTrue();
  }

  // --- helpers -------------------------------------------------------------

  // Inorder traversal. For a BST built from a strictly-increasing sorted array,
  // the inorder sequence must equal the original array — which simultaneously
  // proves the BST property, that all elements are present, and the correct size.
  private List<Integer> inorder(TreeNode root) {
    List<Integer> out = new ArrayList<>();
    inorderVisit(root, out);
    return out;
  }

  private void inorderVisit(TreeNode node, List<Integer> out) {
    if (node == null) {
      return;
    }
    inorderVisit(node.left, out);
    out.add(node.val);
    inorderVisit(node.right, out);
  }

  // Height-balanced: at every node, |h(left) - h(right)| <= 1. Recomputed in
  // O(n log n) naively; acceptable for test-side verification even at n = 10_000.
  private boolean isBalanced(TreeNode node) {
    if (node == null) {
      return true;
    }
    return Math.abs(height(node.left) - height(node.right)) <= 1
        && isBalanced(node.left)
        && isBalanced(node.right);
  }

  private int height(TreeNode node) {
    if (node == null) {
      return -1;
    }
    return 1 + Math.max(height(node.left), height(node.right));
  }
}
