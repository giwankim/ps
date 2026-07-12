package jungol.jungol9659;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 9659 트리 순회 (Tree Traversal).
 *
 * <p>A tree with N nodes (2 ≤ N ≤ 1,000) is rooted at node 1 and given as N-1 lines of {@code cid
 * pid} (child, parent) pairs, followed by a query node x. Print four lines: the distance from the
 * root to x, the number of nodes in x's subtree including x itself, the distance from x to its
 * farthest descendant, and the distance from x to the farthest node anywhere in the tree.
 */
class MainTest {

  // --- Official sample: node 3's farthest node (5, via the root) beats its farthest
  // descendant (2), so metrics 3 and 4 must be computed independently. ---

  @Test
  @StdIo({"10", "2 1", "3 1", "5 2", "6 5", "4 5", "7 4", "8 3", "9 3", "10 9", "3"})
  void officialSampleReportsAllFourMetricsForNodeThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "4", "2", "5");
  }

  // --- Minimal tree N = 2: a root and one child. ---

  @Test
  @StdIo({"2", "2 1", "1"})
  void rootOfMinimalTreeCountsBothNodesAndReachesItsOnlyChildAtDistanceOne(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "2", "1", "1");
  }

  @Test
  @StdIo({"2", "2 1", "2"})
  void leafOfMinimalTreeHasNoDescendantsButStillReachesTheRoot(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Farthest-descendant distance is 0 for a leaf; the farthest node is the parent.
    assertThat(out.capturedLines()).containsExactly("1", "1", "0", "1");
  }

  // --- Chain 1-2-3: depth accumulates and the middle node sees both ends. ---

  @Test
  @StdIo({"3", "2 1", "3 2", "2"})
  void middleOfAChainIsEquidistantFromRootAndLeaf(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "1", "1");
  }

  @Test
  @StdIo({"3", "2 1", "3 2", "3"})
  void bottomOfAChainIsFarthestFromTheRootWithAnEmptySubtreeBelow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "1", "0", "2");
  }

  // --- Star: the farthest node from a leaf is a sibling, reached only through the parent. ---

  @Test
  @StdIo({"4", "2 1", "3 1", "4 1", "2"})
  void starLeafReachesItsFarthestNodeThroughTheParentNotItsSubtree(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Descendant distance is 0, yet a sibling sits two edges away via the root.
    assertThat(out.capturedLines()).containsExactly("1", "1", "0", "2");
  }

  // --- Edge lines arrive child-first: the tree must not depend on input order. ---

  @Test
  @StdIo({"4", "4 3", "3 2", "2 1", "4"})
  void edgesListedDeepestFirstStillBuildTheChain(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each edge names a parent that has not appeared as a child yet.
    assertThat(out.capturedLines()).containsExactly("3", "1", "0", "3");
  }

  // --- More queries against the official sample tree. ---

  @Test
  @StdIo({"10", "2 1", "3 1", "5 2", "6 5", "4 5", "7 4", "8 3", "9 3", "10 9", "1"})
  void rootQueryCountsTheWholeTreeAndItsFarthestNodeIsItsOwnDeepestDescendant(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // For the root, metrics 3 and 4 coincide: every node is a descendant.
    assertThat(out.capturedLines()).containsExactly("0", "10", "4", "4");
  }

  @Test
  @StdIo({"10", "2 1", "3 1", "5 2", "6 5", "4 5", "7 4", "8 3", "9 3", "10 9", "7"})
  void deepLeafQueryCrossesTheRootToReachTheOppositeBranch(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 7 -> 4 -> 5 -> 2 -> 1 -> 3 -> 9 -> 10 spans seven edges.
    assertThat(out.capturedLines()).containsExactly("4", "1", "0", "7");
  }

  @Test
  @StdIo({"10", "2 1", "3 1", "5 2", "6 5", "4 5", "7 4", "8 3", "9 3", "10 9", "2"})
  void innerNodeQueryKeepsDescendantAndGlobalFarthestDistancesApart(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Subtree of 2 is {2, 5, 6, 4, 7}; its deepest descendant 7 is three edges down, but node 10
    // in the sibling branch is four edges away.
    assertThat(out.capturedLines()).containsExactly("1", "5", "3", "4");
  }
}
