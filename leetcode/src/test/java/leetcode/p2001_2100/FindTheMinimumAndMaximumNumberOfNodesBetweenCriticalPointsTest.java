package leetcode.p2001_2100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import leetcode.support.ListNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindTheMinimumAndMaximumNumberOfNodesBetweenCriticalPointsTest {
  FindTheMinimumAndMaximumNumberOfNodesBetweenCriticalPoints sut =
      new FindTheMinimumAndMaximumNumberOfNodesBetweenCriticalPoints();

  // ===========================================================================================
  // Fewer than two critical points always answer [-1, -1] (Steps 1-5).
  // ===========================================================================================

  // Step 1: the floor — the constraints allow n = 2, and both nodes are endpoints, so no
  //         critical point can exist. A solution that assumes at least three nodes and
  //         dereferences head.next.next crashes here
  @Test
  void twoNodeListHasNoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 2))).containsExactly(-1, -1);
  }

  // Step 2: a monotonic list has no critical points — the middle 2 is greater than one neighbor
  //         but smaller than the other, so it is neither a maxima nor a minima
  @Test
  void monotonicListHasNoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 2, 3))).containsExactly(-1, -1);
  }

  // Step 3: "fewer than two" includes exactly one — a single maxima has no distinct partner.
  //         A solution that answers [0, 0] for one critical point fails here
  @Test
  void aLoneMaximaIsFewerThanTwoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 1))).containsExactly(-1, -1);
  }

  // Step 4: the mirror of Step 3 — a single minima counts as a critical point too, and one is
  //         still not enough
  @Test
  void aLoneMinimaIsFewerThanTwoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(3, 1, 3))).containsExactly(-1, -1);
  }

  // Step 5: the statement's Note — a node without both a previous and a next node is never
  //         critical, however extreme its value. A solution that one-side-compares the
  //         endpoints sees three critical points here and answers [1, 2]
  @Test
  void endpointsAreNeverCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(9, 1, 9))).containsExactly(-1, -1);
  }

  // ===========================================================================================
  // Measuring distances once two or more critical points exist (Steps 6-11).
  // ===========================================================================================

  // Step 6: two minima and nothing else — critical points at positions 2 and 5 (the flat 3, 3
  //         between them creates none), so both answers are 5 - 2 = 3. A detector that only
  //         recognizes maxima finds zero critical points and answers [-1, -1]
  @Test
  void twoMinimaMeasureTheGapBetweenThem() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(5, 1, 3, 3, 1, 5))).containsExactly(3, 3);
  }

  // Step 7: the mirror of Step 6 — two maxima at positions 2 and 5. A detector that only
  //         recognizes minima answers [-1, -1]
  @Test
  void twoMaximaMeasureTheGapBetweenThem() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 5, 3, 3, 5, 1))).containsExactly(3, 3);
  }

  // Step 8: maxima and minima pair up across kinds — critical points at positions 2 (maxima),
  //         3 (minima), and 4 (maxima). A solution that only measures between same-kind points
  //         (maxima at 2 and 4, distance 2) answers [2, 2] instead of [1, 2]
  @Test
  void maximaAndMinimaPairUpAcrossKinds() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 5, 2, 6, 2))).containsExactly(1, 2);
  }

  // Step 9: distance is the index difference, so adjacent critical points (positions 2, 3, 4)
  //         are 1 apart — despite the title, a solution counting the nodes strictly between
  //         two critical points answers [0, 1]
  @Test
  void adjacentCriticalPointsAreDistanceOneApart() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 1, 3, 1))).containsExactly(1, 2);
  }

  // Step 10: maxDistance is between ANY two critical points — here positions 2 through 6 are
  //          all critical, so the max spans first to last (6 - 2 = 4). A solution that takes
  //          the max over adjacent pairs only answers [1, 1]
  @Test
  void maxDistanceSpansFirstToLastCriticalPoint() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 1, 3, 1, 3, 1)))
        .containsExactly(1, 4);
  }

  // Step 11: unevenly spaced critical points at positions 2, 5, 6, and 9 (gaps 3, 1, 3) — the
  //          min comes from the middle adjacent pair, not the first gap (a solution keeping
  //          only the first gap answers [3, 7]), and the max from the outer two (adjacent-only
  //          max answers [1, 3])
  @Test
  void minComesFromAnInteriorAdjacentPair() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 9, 8, 7, 2, 8, 3, 2, 1, 5)))
        .containsExactly(1, 7);
  }

  // ===========================================================================================
  // Strictly greater / strictly smaller: plateaus never create critical points (Steps 12-13).
  // ===========================================================================================

  // Step 12: neither 3 is strictly greater than the other, so the plateau holds no critical
  //          point at all. A solution comparing with >= sees two maxima and answers [1, 1]
  @Test
  void plateauCreatesNoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 3, 1))).containsExactly(-1, -1);
  }

  // Step 13: an equal neighbor on just one side also disqualifies — only the 1 at position 3 is
  //          critical here; the 2s flanked by equal 2s are not. A >= comparison sees three
  //          critical points and answers [1, 2]
  @Test
  void equalNeighborOnOneSideDisqualifiesANode() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(2, 2, 1, 2, 2))).containsExactly(-1, -1);
  }

  // ===========================================================================================
  // The official examples, verbatim (Steps 14-16).
  // ===========================================================================================

  // Step 14: LeetCode Example 1 — a two-node list has no critical points
  @Test
  void leetCodeExample1() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(3, 1))).containsExactly(-1, -1);
  }

  // Step 15: LeetCode Example 2 — critical points at positions 3, 5, and 6. The explanation
  //          defines distance as the index difference (6 - 5 = 1) and takes the max across ANY
  //          pair (6 - 3 = 3); a max over adjacent pairs only answers [1, 2]
  @Test
  void leetCodeExample2() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(5, 3, 1, 2, 5, 1, 2)))
        .containsExactly(1, 3);
  }

  // Step 16: LeetCode Example 3 — critical points at positions 2 and 5 only: the flat 2, 2
  //          runs create none, and the trailing 7 has no next node. A non-strict comparison
  //          answers [1, 6]
  @Test
  void leetCodeExample3() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 2, 2, 3, 2, 2, 2, 7)))
        .containsExactly(3, 3);
  }

  // ===========================================================================================
  // Constraint bounds: n = 10^5 nodes, values spanning 1..10^5 (Steps 17-18). A single pass is
  // O(n) and finishes in milliseconds; collecting the critical points and comparing all pairs
  // is O(k^2) with k up to n - 2, about 5 * 10^9 comparisons, and cannot finish inside the
  // timeout.
  // ===========================================================================================

  // Step 17: alternating 1 and 100000 across 10^5 nodes makes EVERY interior node critical —
  //          99998 critical points at positions 2 through 99999, so the answer is
  //          [1, 99999 - 2] = [1, 99997]. Both ends of the value range appear
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlternatingListIsHandled() {
    assertThat(sut.nodesBetweenCriticalPoints(alternating(100000))).containsExactly(1, 99997);
  }

  // Step 18: the strictly increasing ramp 1..100000 forces a full-length scan that finds no
  //          critical point at all — the [-1, -1] path must also be O(n)
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthMonotonicListHasNoCriticalPoints() {
    assertThat(sut.nodesBetweenCriticalPoints(ramp(100000))).containsExactly(-1, -1);
  }

  // ===========================================================================================
  // Hygiene (Steps 19-20).
  // ===========================================================================================

  // Step 19: the caller's list is not modified — a solution that overwrites node values or
  //          rewires next pointers while scanning fails the traversal afterwards
  @Test
  void inputListIsNotModified() {
    ListNode head = ListNode.of(5, 3, 1, 2, 5, 1, 2);
    sut.nodesBetweenCriticalPoints(head);
    assertThat(values(head)).containsExactly(5, 3, 1, 2, 5, 1, 2);
  }

  // Step 20: one instance answers several lists of different sizes, largest in the middle —
  //          this catches first/last/min state cached on the instance instead of reset per call
  @Test
  void oneInstanceAnswersManyInputsIndependently() {
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 3, 1, 3, 1))).containsExactly(1, 2);
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(1, 9, 8, 7, 2, 8, 3, 2, 1, 5)))
        .containsExactly(1, 7);
    assertThat(sut.nodesBetweenCriticalPoints(ListNode.of(3, 1))).containsExactly(-1, -1);
  }

  private static ListNode alternating(int n) {
    int[] vals = new int[n];
    Arrays.setAll(vals, i -> i % 2 == 0 ? 1 : 100000);
    return ListNode.of(vals);
  }

  private static ListNode ramp(int n) {
    int[] vals = new int[n];
    Arrays.setAll(vals, i -> i + 1);
    return ListNode.of(vals);
  }

  private static List<Integer> values(ListNode head) {
    List<Integer> out = new ArrayList<>();
    for (ListNode it = head; it != null; it = it.next) {
      out.add(it.val);
    }
    return out;
  }
}
