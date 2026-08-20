package leetcode.p3301_3400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class RemoveMethodsFromProjectTest {
  private static final int MAX_METHODS = 100_000;
  private static final int DIAMONDS = 25;

  RemoveMethodsFromProject sut = new RemoveMethodsFromProject();

  // Step 1: n may be 1 — the buggy method is the whole project, nothing can invoke it from
  // outside, so it goes and the answer is the empty list rather than null or [0]
  @Test
  void singleMethodProjectRemovesTheOnlyMethod() {
    assertThat(sut.remainingMethods(1, 0, new int[0][])).isEmpty();
  }

  // Step 2: invocations may be empty by the constraints — only k itself is suspicious, and the
  // answer keeps every other method
  @Test
  void buggyMethodWithNoInvocationsLeavesTheRest() {
    assertThat(sut.remainingMethods(3, 1, new int[0][])).containsExactlyInAnyOrder(0, 2);
  }

  // Step 3: 0 invokes 1 invokes 2 — suspicion is transitive, so method 2 is suspicious too;
  // stopping at k's direct callees would wrongly leave [2, 3]
  @Test
  void suspicionSpreadsThroughIndirectInvocations() {
    assertThat(sut.remainingMethods(4, 0, new int[][] {{0, 1}, {1, 2}}))
        .containsExactlyInAnyOrder(3);
  }

  // Step 4: k = 2 is a sink here — reachability runs forward along invocations only, so the group
  // is just {2} and its caller 1 blocks removal; sweeping edges backwards would make all three
  // suspicious and wrongly return []
  @Test
  void reachabilityFollowsInvocationsForwardOnly() {
    assertThat(sut.remainingMethods(3, 2, new int[][] {{0, 1}, {1, 2}}))
        .containsExactlyInAnyOrder(0, 1, 2);
  }

  // Step 5: the group is {2, 0} and method 1 sits outside it invoking 0 — one crossing edge
  // cancels the entire removal, not just the method it points at
  @Test
  void outsideCallerBlocksTheWholeRemoval() {
    assertThat(sut.remainingMethods(3, 2, new int[][] {{1, 0}, {2, 0}}))
        .containsExactlyInAnyOrder(0, 1, 2);
  }

  // Step 6: same blocked shape with methods 3, 4 and 5 untouched by any invocation — a blocked
  // removal returns every method, so returning the non-suspicious ones [2, 3, 4, 5] is wrong
  @Test
  void blockedRemovalReturnsEveryMethod() {
    assertThat(sut.remainingMethods(6, 0, new int[][] {{0, 1}, {2, 0}}))
        .containsExactlyInAnyOrder(0, 1, 2, 3, 4, 5);
  }

  // Step 7: the crossing edge 3->2 lands two hops past k — every member of the group needs its
  // callers checked, not just k
  @Test
  void blockerDeepInsideTheGroupStillCounts() {
    assertThat(sut.remainingMethods(4, 0, new int[][] {{0, 1}, {1, 2}, {3, 2}}))
        .containsExactlyInAnyOrder(0, 1, 2, 3);
  }

  // Step 8: 0 and 1 invoke each other — the cycle must terminate, and the edge 1->0 pointing back
  // into the group is internal, so treating any inbound edge as a blocker would wrongly keep all
  // four methods
  @Test
  void invocationsInsideTheGroupDoNotBlockRemoval() {
    assertThat(sut.remainingMethods(4, 0, new int[][] {{0, 1}, {1, 0}}))
        .containsExactlyInAnyOrder(2, 3);
  }

  // Step 9: methods 2, 3 and 4 invoke each other outside the group {0, 1} — invocations that never
  // cross into the group are irrelevant to whether it can be removed
  @Test
  void invocationsAmongOutsidersDoNotBlockRemoval() {
    assertThat(sut.remainingMethods(5, 0, new int[][] {{0, 1}, {2, 3}, {3, 4}}))
        .containsExactlyInAnyOrder(2, 3, 4);
  }

  // Step 10: LeetCode Example 1 — the group {1, 2} is invoked from outside by both 0 and 3, so
  // nothing is removed
  @Test
  void example1BlockedByOutsideCallers() {
    assertThat(sut.remainingMethods(4, 1, new int[][] {{1, 2}, {0, 1}, {3, 2}}))
        .containsExactlyInAnyOrder(0, 1, 2, 3);
  }

  // Step 11: LeetCode Example 2 — the group {0, 1, 2} has no outside caller, and the surviving
  // edge 3->4 lies entirely outside it
  @Test
  void example2RemovesTheSuspiciousComponent() {
    assertThat(sut.remainingMethods(5, 0, new int[][] {{1, 2}, {0, 2}, {0, 1}, {3, 4}}))
        .containsExactlyInAnyOrder(3, 4);
  }

  // Step 12: LeetCode Example 3 — the invocations form one cycle covering every method, so the
  // group is the whole project and removing it leaves nothing
  @Test
  void example3RemovesEveryMethod() {
    assertThat(sut.remainingMethods(3, 2, new int[][] {{1, 2}, {0, 1}, {2, 0}})).isEmpty();
  }

  // Step 13: a lattice of 25 chained diamonds reaches all 76 methods over 2^25 distinct paths — a
  // traversal that re-explores an already-seen method takes tens of millions of steps on a graph
  // of 100 edges
  @Test
  void diamondLatticeNeedsVisitedMarking() {
    assertThat(sut.remainingMethods(3 * DIAMONDS + 1, 0, diamondLattice())).isEmpty();
  }

  // Step 14: the ceiling chain makes every method suspicious with no outside caller — the whole
  // project goes, and a recursive traversal blows the stack 100,000 frames deep
  @Test
  void maxDepthChainNeedsIterativeTraversal() {
    assertThat(sut.remainingMethods(MAX_METHODS, 0, deepChain())).isEmpty();
  }

  // Step 15: the same depth with method 0 invoking the chain's tail — the single crossing edge
  // blocks removal, so all 100,000 methods come back
  @Test
  void blockedMaxDepthChainReturnsEveryMethod() {
    List<Integer> remaining = sut.remainingMethods(MAX_METHODS, 1, deepChainWithOutsideCaller());
    assertThat(remaining.stream().sorted().toList())
        .isEqualTo(IntStream.range(0, MAX_METHODS).boxed().toList());
  }

  /**
   * Twenty-five chained diamonds on methods 0..75: entry 3j invokes 3j+1 and 3j+2, which both
   * invoke the next entry 3j+3. Only 100 invocations, but 2^25 paths from 0 to the last method.
   */
  private static int[][] diamondLattice() {
    int[][] invocations = new int[4 * DIAMONDS][];
    for (int j = 0; j < DIAMONDS; j++) {
      invocations[4 * j] = new int[] {3 * j, 3 * j + 1};
      invocations[4 * j + 1] = new int[] {3 * j, 3 * j + 2};
      invocations[4 * j + 2] = new int[] {3 * j + 1, 3 * j + 3};
      invocations[4 * j + 3] = new int[] {3 * j + 2, 3 * j + 3};
    }
    return invocations;
  }

  /** Chain 0->1->...->99999: 99,999 invocations, all of them reachable from method 0. */
  private static int[][] deepChain() {
    int[][] invocations = new int[MAX_METHODS - 1][];
    for (int i = 0; i < MAX_METHODS - 1; i++) {
      invocations[i] = new int[] {i, i + 1};
    }
    return invocations;
  }

  /**
   * Chain 1->2->...->99999 with method 0 outside it invoking the tail: the group is 99,999 methods
   * deep and one crossing edge reaches its far end.
   */
  private static int[][] deepChainWithOutsideCaller() {
    int[][] invocations = new int[MAX_METHODS - 1][];
    for (int i = 1; i < MAX_METHODS - 1; i++) {
      invocations[i - 1] = new int[] {i, i + 1};
    }
    invocations[MAX_METHODS - 2] = new int[] {0, MAX_METHODS - 1};
    return invocations;
  }
}
