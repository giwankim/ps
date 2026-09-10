package leetcode.p2201_2300;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import leetcode.support.TreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountNodesEqualToAverageOfSubtreeTest {
  CountNodesEqualToAverageOfSubtree sut = new CountNodesEqualToAverageOfSubtree();

  // ===========================================================================================
  // The floor of the constraints and the two Note sentences (Steps 1-8).
  // ===========================================================================================

  // Step 1: the smallest tree the constraints allow is one node, and a lone node is its own
  //         subtree: 7 / 1 = 7. A solution that only examines nodes with children, or that
  //         returns 0 when there is nothing to average against, answers 0
  @Test
  void singleNodeCounts() {
    assertThat(sut.averageOfSubtree(TreeNode.of(7))).isEqualTo(1);
  }

  // Step 2: values start at 0, and an average of 0 is a real average, not a missing one. Three
  //         zeros give 0 / 3 = 0 at the root and 0 / 1 = 0 at each leaf, so all three count. A
  //         solution that treats a zero value or a zero sum as "no node here" answers less
  @Test
  void zeroValuedNodesCount() {
    assertThat(sut.averageOfSubtree(TreeNode.of(0, 0, 0))).isEqualTo(3);
  }

  // Step 3: a leaf always counts, because its subtree is itself. The root here does not:
  //         (10 + 1 + 2) / 3 = 4, not 10. A solution that counts every node answers 3, one that
  //         skips leaves answers 0
  @Test
  void leavesAlwaysCountEvenWhenTheRootDoesNot() {
    assertThat(sut.averageOfSubtree(TreeNode.of(10, 1, 2))).isEqualTo(2);
  }

  // Step 4: the root's subtree is the whole tree, root included: (2 + 1 + 3) / 3 = 2, so the root
  //         joins its two leaves and all three count
  @Test
  void rootCountsWhenTheWholeTreeAveragesToItsValue() {
    assertThat(sut.averageOfSubtree(TreeNode.of(2, 1, 3))).isEqualTo(3);
  }

  // Step 5: the Note's "root and all of its descendants" means every level below, not just the
  //         direct children. In the chain 3 -> 2 -> 4 the root averages (3 + 2 + 4) / 3 = 3 and
  //         counts, the middle node averages (2 + 4) / 2 = 3, not 2, and the leaf counts, so 2. A
  //         solution that averages only a node's direct children sees 2 at the root and answers 1
  @Test
  void averageCoversEveryDescendantNotJustDirectChildren() {
    TreeNode chain = new TreeNode(3, new TreeNode(2, new TreeNode(4), null), null);

    assertThat(sut.averageOfSubtree(chain)).isEqualTo(2);
  }

  // Step 6: the Note's "rounded down": (3 + 4) / 2 = 3.5 rounds down to 3, so the root counts
  //         alongside its leaf. A solution that rounds to nearest, or one that demands the exact
  //         average, rejects the root and answers 1
  @Test
  void averageRoundsDown() {
    assertThat(sut.averageOfSubtree(TreeNode.of(3, 4))).isEqualTo(2);
  }

  // Step 7: rounding down applies however large the fraction is: (5 + 4 + 8) / 3 = 5.67 rounds
  //         down to 5 and all three count. Rounding to nearest gives 6 at the root and answers 2
  @Test
  void roundsDownEvenWhenTheFractionIsAboveOneHalf() {
    assertThat(sut.averageOfSubtree(TreeNode.of(5, 4, 8))).isEqualTo(3);
  }

  // Step 8: rounding down can also reject a node that rounding to nearest would accept:
  //         (2 + 0 + 3) / 3 = 1.67 rounds down to 1, not 2, so only the two leaves count.
  //         Rounding to nearest gives 2 at the root and answers 3
  @Test
  void roundingDownCanAlsoRejectANode() {
    assertThat(sut.averageOfSubtree(TreeNode.of(2, 0, 3))).isEqualTo(2);
  }

  // ===========================================================================================
  // Every node judged on its own, in every shape (Steps 9-12).
  // ===========================================================================================

  // Step 9: the mirror of Step 6, with the leaf on the right. Same sum, same average, same
  //         answer of 2. A walk that only follows left links never reaches the 4, takes the root
  //         for a leaf, and answers 1
  @Test
  void rightChildIsPartOfTheSubtreeToo() {
    assertThat(sut.averageOfSubtree(TreeNode.of(3, null, 4))).isEqualTo(2);
  }

  // Step 10: a node that fails does not disqualify the nodes beneath it. The root averages
  //          (1 + 5 + 9 + 4 + 8) / 5 = 5, not 1, but the 5 below it averages (5 + 4 + 8) / 3 = 5,
  //          and the three leaves count, so 4. A solution that stops descending at the first
  //          mismatch answers 0
  @Test
  void aFailingRootDoesNotHideMatchesBeneathIt() {
    assertThat(sut.averageOfSubtree(TreeNode.of(1, 5, 9, 4, 8))).isEqualTo(4);
  }

  // Step 11: each node is measured against its own subtree, not the whole tree. Root 6 over leaf
  //          2 and node 4, which sits over leaves 3 and 5, averages 20 / 5 = 4 as a whole. That
  //          number belongs to node 4 alone: (4 + 3 + 5) / 3 = 4, so node 4 and the three leaves
  //          count and the root does not, 4. A solution that compares every node against the
  //          whole-tree average matches only the node valued 4 and answers 1
  @Test
  void eachNodeIsMeasuredAgainstItsOwnSubtreeNotTheWholeTree() {
    TreeNode root =
        new TreeNode(6, new TreeNode(2), new TreeNode(4, new TreeNode(3), new TreeNode(5)));

    assertThat(sut.averageOfSubtree(root)).isEqualTo(4);
  }

  // Step 12: every node can count at once. In the perfect tree with 3 at the root, 2 and 4 below
  //          it, and leaves 1, 3, 3, 5, the 2 averages (2 + 1 + 3) / 3 = 2, the 4 averages
  //          (4 + 3 + 5) / 3 = 4, and the root averages 21 / 7 = 3, so the answer is the node
  //          count, 7
  @Test
  void everyNodeCanCount() {
    assertThat(sut.averageOfSubtree(TreeNode.of(3, 2, 4, 1, 3, 3, 5))).isEqualTo(7);
  }

  // ===========================================================================================
  // The official examples (Steps 13-14).
  // ===========================================================================================

  // Step 13: LeetCode Example 1, whose explanation walks all five matches. It rules out "every
  //          internal node counts": the 8 averages (8 + 0 + 1) / 3 = 3 and is the one node left
  //          out. It also shows the rounding in the 5, whose (5 + 6) / 2 = 11 / 2 rounds down to
  //          5, next to the root's exact 24 / 6 = 4
  @Test
  void leetCodeExample1() {
    assertThat(sut.averageOfSubtree(TreeNode.of(4, 8, 5, 0, 1, null, 6))).isEqualTo(5);
  }

  // Step 14: LeetCode Example 2, the single node of Step 1 as the statement prints it
  @Test
  void leetCodeExample2() {
    assertThat(sut.averageOfSubtree(TreeNode.of(1))).isEqualTo(1);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= nodes <= 1000, 0 <= Node.val <= 1000).
  //
  // 1000 nodes is small: even re-walking the whole subtree beneath every node, about 5 * 10^5
  // visits on a chain, finishes in milliseconds, so these timeouts separate a solution that
  // terminates from one that loops or recurses without a base case, not O(n) from O(n^2). The
  // content of these steps is shape and range: a 1000-deep chain in each direction, which Java's
  // default stack handles, the value ceiling, whose total of 1000 * 1000 = 10^6 fits an int with
  // room to spare, and a balanced tree at full size.
  // ===========================================================================================

  // Step 15: a 1000-deep left chain at the value ceiling. Every subtree averages
  //          1000 * k / k = 1000, so every node counts. The root's sum is 10^6, the largest any
  //          input can produce
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumDepthLeftChainAtTheValueCeilingCountsEveryNode() {
    assertThat(sut.averageOfSubtree(leftChain(1000, depth -> 1000))).isEqualTo(1000);
  }

  // Step 16: Step 9 at maximum depth: a right chain valued 1, 2, ..., 1000 from the root down.
  //          The node valued v averages the run v .. 1000, that is (v + 1000) / 2, which is at
  //          least v + 1 for every v up to 998. Only the 999, averaging (999 + 1000) / 2 = 999,
  //          and the leaf 1000 count, so 2. A walk that only follows left links sees a lone root
  //          valued 1 and answers 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumDepthRightChainRisingFromTheRoot() {
    assertThat(sut.averageOfSubtree(rightChain(1000, depth -> depth + 1))).isEqualTo(2);
  }

  // Step 17: the mirror of Step 16, and Step 8 at maximum depth: a left chain valued 1000, 999,
  //          ..., 1 from the root down. The node valued v averages the run v .. 1, that is
  //          (v + 1) / 2 rounded down, which falls below v for every v above 1, so only the leaf
  //          counts. Rounding to nearest lifts the 2's (2 + 1) / 2 to 2 and answers 2
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumDepthLeftChainFallingFromTheRoot() {
    assertThat(sut.averageOfSubtree(leftChain(1000, depth -> 1000 - depth))).isEqualTo(1);
  }

  // Step 18: a complete binary tree of 1000 nodes, each valued by its depth, 0 at the root and 9
  //          at the deepest leaves. A node at depth d whose children are all leaves averages
  //          (d + 2(d + 1)) / 3, or (d + (d + 1)) / 2 with a single leaf child, and both round
  //          down to d, so it counts. A node with a grandchild has every descendant at d + 1 or
  //          more and at least one at d + 2, so its average rounds down to d + 1 or more and it
  //          does not. That is the 500 leaves plus the 250 nodes whose children are all leaves,
  //          750. Rounding to nearest, or averaging only the direct children, drops those 250 and
  //          answers 500
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumSizeCompleteTreeValuedByDepth() {
    TreeNode root = completeTree(1000, index -> 31 - Integer.numberOfLeadingZeros(index + 1));

    assertThat(sut.averageOfSubtree(root)).isEqualTo(750);
  }

  // ===========================================================================================
  // Hygiene (Steps 19-20).
  // ===========================================================================================

  // Step 19: the tree is read, not rewritten. Stashing each subtree's sum or count into the node
  //          it belongs to is a tempting way to avoid returning a pair, and it hands the caller
  //          back a different tree. After the call, LeetCode Example 1 must still equal a fresh
  //          copy of itself, node for node
  @Test
  void treeIsNotModified() {
    TreeNode root = TreeNode.of(4, 8, 5, 0, 1, null, 6);

    sut.averageOfSubtree(root);

    assertThat(root).isEqualTo(TreeNode.of(4, 8, 5, 0, 1, null, 6));
  }

  // Step 20: one instance answers several trees in a row, the largest in the middle, so a count
  //          or a running sum kept on the instance rather than reset per call shows up as the
  //          previous answer bleeding into the next
  @Test
  void oneInstanceAnswersManyTrees() {
    assertThat(sut.averageOfSubtree(TreeNode.of(3, 4))).isEqualTo(2);
    assertThat(sut.averageOfSubtree(leftChain(1000, depth -> 1000))).isEqualTo(1000);
    assertThat(sut.averageOfSubtree(TreeNode.of(10, 1, 2))).isEqualTo(2);
    assertThat(sut.averageOfSubtree(TreeNode.of(7))).isEqualTo(1);
  }

  /** A chain of {@code length} nodes, each on its parent's left link, valued by depth from 0. */
  private static TreeNode leftChain(int length, IntUnaryOperator valueAtDepth) {
    TreeNode node = null;
    for (int depth = length - 1; depth >= 0; depth--) {
      node = new TreeNode(valueAtDepth.applyAsInt(depth), node, null);
    }
    return node;
  }

  /** The mirror of {@link #leftChain}: every node hangs off its parent's right link. */
  private static TreeNode rightChain(int length, IntUnaryOperator valueAtDepth) {
    TreeNode node = null;
    for (int depth = length - 1; depth >= 0; depth--) {
      node = new TreeNode(valueAtDepth.applyAsInt(depth), null, node);
    }
    return node;
  }

  /** A complete binary tree of {@code size} nodes, each valued by its level-order index. */
  private static TreeNode completeTree(int size, IntUnaryOperator valueAtIndex) {
    Integer[] values = new Integer[size];
    Arrays.setAll(values, valueAtIndex::applyAsInt);
    return TreeNode.of(values);
  }
}
