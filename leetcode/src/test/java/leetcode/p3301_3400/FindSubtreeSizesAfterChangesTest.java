package leetcode.p3301_3400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindSubtreeSizesAfterChangesTest {
  static final int MAX_N = 100_000;
  static final int ROUNDS = 20;

  FindSubtreeSizesAfterChanges sut = new FindSubtreeSizesAfterChanges();

  // ===========================================================================================
  // The floor: trees too small for any edge to move (Steps 1-3).
  // ===========================================================================================

  // Step 1: the floor is n == 1. The root is the whole tree, it is never a candidate x, and its
  //         subtree is itself. A solution that reads parent[0] as an index dies on -1 here
  @Test
  void n1RootAloneHasSubtreeSizeOne() {
    assertThat(sut.findSubtreeSizes(new int[] {-1}, "a")).containsExactly(1);
  }

  // Step 2: one edge, two different letters. Node 1 has no ancestor with its letter, so the "do
  //         nothing" rule applies and the answer is the plain subtree sizes of the given tree
  @Test
  void n2DifferentLettersLeavesTheEdgeAlone() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0}, "ab")).containsExactly(2, 1);
  }

  // Step 3: one edge, the same letter. The closest matching ancestor is the current parent, so the
  //         edge is removed and added back. Nothing may be counted twice or lost in that no-op
  @Test
  void n2SameLetterReattachesToTheSameParent() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0}, "aa")).containsExactly(2, 1);
  }

  // ===========================================================================================
  // What a move does to the sizes (Steps 4-8).
  // ===========================================================================================

  // Step 4: the first real move. Chain 0 - 1 - 2 lettered a, b, a: node 2 skips its parent and
  //         hangs off the root, so node 1 loses it. Ignoring the changes answers [3, 2, 1]
  @Test
  void nodeMovesPastItsParentToAMatchingGrandparent() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1}, "aba")).containsExactly(3, 1, 1);
  }

  // Step 5: "closest" is the rule. Chain a, a, b, a: node 3 has two matching ancestors and takes
  //         node 1, the nearer one. A solution that keeps the first match seen on the way down from
  //         the root, the farthest one, sends it to node 0 and answers [4, 2, 1, 1]
  @Test
  void closestMatchingAncestorWinsOverAFartherOne() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2}, "aaba")).containsExactly(4, 3, 1, 1);
  }

  // Step 6: a moved node takes its whole subtree with it. Chain a, b, a, c: node 2 moves to the
  //         root and carries node 3, so node 1 drops from 3 to 1. A solution that patches the
  //         original sizes by subtracting 1 per moved node answers [4, 2, 2, 1]
  @Test
  void movedNodeCarriesItsDescendants() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2}, "abac")).containsExactly(4, 1, 2, 1);
  }

  // Step 7: every letter is tracked on its own. Chain a, b, c, a, b, c: nodes 3, 4 and 5 move to
  //         nodes 0, 1 and 2. Node 0 now has children 1 and 3, node 1 has 2 and 4, node 2 has 5.
  //         A single "last match" variable instead of one per letter cannot produce this
  @Test
  void eachLetterFindsItsOwnAncestor() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2, 3, 4}, "abcabc"))
        .containsExactly(6, 4, 2, 1, 1, 1);
  }

  // Step 8: a chain of one letter. Every node's closest match is its own parent, so nothing moves
  //         and the sizes stay 5, 4, 3, 2, 1. Matching against the farthest ancestor flattens it
  //         into a star and answers [5, 1, 1, 1, 1]
  @Test
  void singleLetterChainKeepsItsShape() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2, 3}, "aaaaa"))
        .containsExactly(5, 4, 3, 2, 1);
  }

  // ===========================================================================================
  // Ancestors only: a matching node off the root path is not a candidate (Steps 9-11).
  // A DFS that records the latest node per letter and forgets to restore it on the way back up
  // turns "ancestor" into "anything visited earlier".
  // ===========================================================================================

  // Step 9: a star of one letter. The leaves are siblings, not ancestors of each other, and each
  //         already hangs off its closest match. The non-restoring DFS chains them together and
  //         answers [5, 4, 3, 2, 1], or [5, 1, 2, 3, 4] when it visits children in reverse
  @Test
  void siblingsWithTheSameLetterDoNotAdoptEachOther() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 0, 0, 0}, "aaaaa"))
        .containsExactly(5, 1, 1, 1, 1);
  }

  // Step 10: node 3 is a child of node 2 and shares its letter with node 1, its uncle, which a DFS
  //          in index order visits first. Nothing moves. The non-restoring DFS hands node 3 to
  //          node 1 and answers [4, 2, 1, 1]
  @Test
  void uncleVisitedEarlierIsNotAnAncestor() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 0, 2}, "acbc")).containsExactly(4, 1, 2, 1);
  }

  // Step 11: the mirror of Step 10 for a stack-based DFS, which visits the higher-numbered child
  //          first. Now node 3 hangs under node 1 and the same-lettered uncle is node 2. Visiting
  //          children in reverse without restoring answers [4, 1, 2, 1]
  @Test
  void uncleVisitedEarlierInReverseOrderIsNotAnAncestor() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 0, 1}, "abcc")).containsExactly(4, 2, 1, 1);
  }

  // ===========================================================================================
  // All changes happen at once, against the original tree (Steps 12-13).
  // ===========================================================================================

  // Step 12: chain a, b, a, b. In the original tree node 2 moves to node 0 and node 3 moves to
  //          node 1. Applying the moves one at a time in index order moves node 2 first, which
  //          takes node 1 off node 3's root path, so node 3 stays put and the answer becomes
  //          [4, 1, 2, 1]
  @Test
  void movesAreDecidedOnTheOriginalTreeNotOneAtATime() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2}, "abab")).containsExactly(4, 2, 1, 1);
  }

  // Step 13: one more link, chain a, b, a, b, a. Node 4 moves to node 2, which has itself moved to
  //          node 0, so node 2 ends with size 2 under the root. Index-order application answers
  //          [5, 1, 3, 1, 1], and subtracting 1 per moved node answers [5, 3, 2, 1, 1]
  @Test
  void nodeCanMoveUnderANodeThatAlsoMoved() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 1, 2, 3}, "ababa"))
        .containsExactly(5, 2, 2, 1, 1);
  }

  // ===========================================================================================
  // Node numbers carry no order (Step 14).
  // ===========================================================================================

  // Step 14: parent[i] may be larger than i. The chain is 0 - 3 - 2 - 1 lettered a, b, a, c, so
  //          node 2 moves to the root and carries node 1. Summing sizes with a loop from n - 1 down
  //          to 1, which assumes every child is numbered above its parent, adds node 2 to the root
  //          before node 1 has reached node 2 and answers [3, 1, 2, 1]
  @Test
  void parentMayHaveALargerIndexThanItsChild() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 2, 3, 0}, "acab")).containsExactly(4, 1, 2, 1);
  }

  // ===========================================================================================
  // The official examples (Steps 15-16).
  // ===========================================================================================

  // Step 15: only node 3 moves, from node 1 to node 0. Nodes 4 and 5 stay because the matching
  //          letters for them are a parent and nothing at all. Node 2 is a leaf with the same
  //          letter as node 3 but is its uncle. Ignoring the changes answers [6, 4, 1, 1, 1, 1]
  @Test
  void leetCodeExample1() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 0, 1, 1, 1}, "abaabc"))
        .containsExactly(6, 3, 1, 1, 1, 1);
  }

  // Step 16: two moves at the same time, and node 2 is listed before its parent, node 4. Node 4
  //          moves to node 0 and node 2 moves from node 4 to node 1, a match that is only on its
  //          root path in the original tree. Applying the moves from the highest index down
  //          answers [5, 1, 1, 1, 2]
  @Test
  void leetCodeExample2() {
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 4, 0, 1}, "abbba"))
        .containsExactly(5, 2, 1, 1, 1);
  }

  // ===========================================================================================
  // Constraint bounds, n == 10^5 (Steps 17-20). The intended solution is one DFS that carries the
  // nearest ancestor of each of the 26 letters, O(n). Rescanning parent to list the children of
  // every node is n^2 == 10^10 steps. Walking up from every node is bounded only by the depth,
  // and on Step 18 that is 2.5 * 10^9 steps to find the matches plus 5 * 10^9 to count the sizes.
  // The chains are 10^5 deep, which is the recursion depth a recursive DFS has to survive.
  //
  // One call is not enough to tell the two apart. Counting sizes by climbing from every node is a
  // tight loop that a warm JVM gets through in about 3 seconds on Step 17, inside the timeout, and
  // the judge still rejects it with Time Limit Exceeded on that exact input. So the chain steps
  // solve the input ROUNDS times: O(n) stays in milliseconds, and the climb needs a minute.
  // ===========================================================================================

  // Step 17: the deepest tree with a single letter. Every node matches its own parent, nothing
  //          moves, and node i keeps the n - i nodes below and including it. This is the judge
  //          case that times out a solution adding 1 to every ancestor of every node
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxDepthChainOfOneLetterIsUnchanged() {
    int[] expected = new int[MAX_N];
    Arrays.setAll(expected, i -> MAX_N - i);

    assertEveryRound(chain(MAX_N), "a".repeat(MAX_N), expected);
  }

  // Step 18: the worst case for walking up the tree. The root is a, the next 49999 nodes are b and
  //          the last 50000 are c. Each c node has a matching parent, but the first b and the first
  //          c have no match, and a size count that climbs from every node pays the full depth.
  //          Nothing moves, so the sizes are again n - i
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxDepthChainOfThreeBlocksIsUnchanged() {
    String s = "a" + "b".repeat(MAX_N / 2 - 1) + "c".repeat(MAX_N / 2);
    int[] expected = new int[MAX_N];
    Arrays.setAll(expected, i -> MAX_N - i);

    assertEveryRound(chain(MAX_N), s, expected);
  }

  // Step 19: the most moves a chain allows. The letters cycle a to z, so every node from 26 on
  //          moves 26 places up and the chain becomes 26 interleaved strands. The sizes start
  //          100000, 96153, 92306 and node 25, the first with a single child, has 3846
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxDepthChainOfCyclingLettersMovesAlmostEveryNode() {
    assertEveryRound(chain(MAX_N), cyclingLetters(MAX_N), cyclingChainSizes(MAX_N));
  }

  // Step 20: the widest tree. The root has 99999 leaves with its own letter, each already attached
  //          to its closest match. Children lists built by scanning parent once per node do not
  //          finish, and a non-restoring DFS strings the leaves into a chain
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxWidthStarOfOneLetterIsUnchanged() {
    int[] parent = new int[MAX_N];
    parent[0] = -1;
    int[] expected = new int[MAX_N];
    Arrays.fill(expected, 1);
    expected[0] = MAX_N;

    assertThat(sut.findSubtreeSizes(parent, "a".repeat(MAX_N))).isEqualTo(expected);
  }

  // ===========================================================================================
  // Hygiene (Step 21).
  // ===========================================================================================

  // Step 21: one instance answers inputs of different sizes, the largest in the middle. Children
  //          lists, the per-letter ancestor table or a sizes array kept on the instance and not
  //          reset per call leak the 1000-node chain into the answers that follow it
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.findSubtreeSizes(new int[] {-1}, "a")).containsExactly(1);
    assertThat(sut.findSubtreeSizes(chain(1000), cyclingLetters(1000)))
        .isEqualTo(cyclingChainSizes(1000));
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 4, 0, 1}, "abbba"))
        .containsExactly(5, 2, 1, 1, 1);
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0, 0, 0, 0}, "aaaaa"))
        .containsExactly(5, 1, 1, 1, 1);
    assertThat(sut.findSubtreeSizes(new int[] {-1, 0}, "ab")).containsExactly(2, 1);
  }

  /**
   * Solves the same input {@link #ROUNDS} times and checks every answer. Each round gets its own
   * copy of {@code parent}, because a solution is allowed to overwrite its input.
   */
  private void assertEveryRound(int[] parent, String s, int[] expected) {
    for (int round = 0; round < ROUNDS; round++) {
      assertThat(sut.findSubtreeSizes(parent.clone(), s)).isEqualTo(expected);
    }
  }

  /** The path 0 - 1 - ... - (n - 1): node i hangs under node i - 1. */
  private static int[] chain(int n) {
    int[] parent = new int[n];
    Arrays.setAll(parent, i -> i - 1);
    return parent;
  }

  /** Letters a to z repeated: node i gets the letter numbered i mod 26. */
  private static String cyclingLetters(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) ('a' + i % 26));
    }
    return sb.toString();
  }

  /**
   * Sizes after the changes for {@link #chain} lettered by {@link #cyclingLetters}. Node i moves
   * under node i - 26 when that exists and stays under node i - 1 otherwise. Either way the new
   * parent has a smaller index, so one pass from the last node up settles every size.
   */
  private static int[] cyclingChainSizes(int n) {
    int[] sizes = new int[n];
    Arrays.fill(sizes, 1);
    for (int i = n - 1; i >= 1; i--) {
      sizes[i >= 26 ? i - 26 : i - 1] += sizes[i];
    }
    return sizes;
  }
}
