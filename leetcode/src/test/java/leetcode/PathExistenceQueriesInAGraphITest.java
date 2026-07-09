package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PathExistenceQueriesInAGraphITest {
  PathExistenceQueriesInAGraphI sut = new PathExistenceQueriesInAGraphI();

  // Step 1: the smallest possible input — one node asked about itself; every node has a trivial
  // path to itself, so no edges are needed at all
  @Test
  void selfQueryOnSingleNodeIsTrue() {
    assertThat(sut.pathExistenceQueries(1, new int[] {5}, 0, new int[][] {{0, 0}}))
        .containsExactly(true);
  }

  // Step 2: LeetCode Example 1 — |1 - 3| = 2 exceeds maxDiff = 1, so the two nodes stay apart
  // while the self query still answers true
  @Test
  void gapBeyondMaxDiffDisconnectsThePair() {
    assertThat(sut.pathExistenceQueries(2, new int[] {1, 3}, 1, new int[][] {{0, 0}, {0, 1}}))
        .containsExactly(true, false);
  }

  // Step 3: the same pair with maxDiff = 2 — the bound is inclusive (|nums[i] - nums[j]| <=
  // maxDiff), so a gap exactly equal to maxDiff forms an edge
  @Test
  void gapExactlyMaxDiffStillConnects() {
    assertThat(sut.pathExistenceQueries(2, new int[] {1, 3}, 2, new int[][] {{0, 1}}))
        .containsExactly(true);
  }

  // Step 4: LeetCode Example 2 — nodes 1 and 3 have |5 - 8| = 3 > maxDiff yet connect through
  // node 2, so queries are about paths, not direct edges, and answers keep query order
  @Test
  void pathMayRunThroughAnIntermediateNode() {
    assertThat(sut.pathExistenceQueries(
            4, new int[] {2, 5, 6, 8}, 2, new int[][] {{0, 1}, {0, 2}, {1, 3}, {2, 3}}))
        .containsExactly(false, false, true, true);
  }

  // Step 5: Example 2's graph queried with each pair reversed — edges are undirected, so [v, u]
  // must answer exactly like [u, v]
  @Test
  void queryEndpointsAreUnordered() {
    assertThat(sut.pathExistenceQueries(4, new int[] {2, 5, 6, 8}, 2, new int[][] {{3, 1}, {2, 0}}))
        .containsExactly(true, false);
  }

  // Step 6: five values each one step apart with maxDiff = 1 — the endpoints differ by 4, far
  // beyond maxDiff, but the chain of small steps joins them; a direct |nums[u] - nums[v]| <=
  // maxDiff check passes steps 1-5 and fails here
  @Test
  void chainOfSmallStepsConnectsDistantValues() {
    assertThat(sut.pathExistenceQueries(5, new int[] {1, 2, 3, 4, 5}, 1, new int[][] {{0, 4}}))
        .containsExactly(true);
  }

  // Step 7: maxDiff = 0 at the constraint floor — only exactly equal values connect, so the
  // duplicate pair joins while the value one step away stays out
  @Test
  void zeroMaxDiffConnectsOnlyEqualValues() {
    assertThat(sut.pathExistenceQueries(3, new int[] {1, 1, 2}, 0, new int[][] {{0, 1}, {1, 2}}))
        .containsExactly(true, false);
  }

  // Step 8: gaps of 8 and 9 cut the sorted array into segments {0,1}, {2,3}, {4} — pairs inside
  // a segment connect, pairs across any gap never do, however close later values sit together
  @Test
  void componentsAreGapDelimitedSegments() {
    assertThat(sut.pathExistenceQueries(
            5, new int[] {1, 2, 10, 11, 20}, 1, new int[][] {{0, 1}, {2, 3}, {1, 2}, {3, 4}, {0, 4}
            }))
        .containsExactly(true, true, false, false, false);
  }

  // Step 9: ceiling-size input — 1e5 nodes one step apart except for a width-2 jump in the
  // middle, and 1e5 queries alternating a same-side neighbor pair (true) with a cross-jump pair
  // (false); walking the graph per query costs ~2.5e9 node visits, so components must be
  // precomputed once
  @Test
  void maxSizeSplitChainAnswersEveryQuery() {
    int n = 100_000;
    boolean[] expected = new boolean[n];
    for (int i = 0; i < n; i += 2) {
      expected[i] = true;
    }
    assertThat(sut.pathExistenceQueries(n, splitChain(n), 1, alternatingQueries(n)))
        .isEqualTo(expected);
  }

  // Step 10: 1e5 copies of the same value with maxDiff = 0 — one component holding ~5e9 implied
  // edges, so any solution that materializes adjacency dies here while a component view stays
  // linear
  @Test
  void maxSizeDuplicatesFormOneComponentWithoutEdgeLists() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 42);
    assertThat(sut.pathExistenceQueries(n, nums, 0, mirrorQueries(n)))
        .hasSize(n)
        .containsOnly(true);
  }

  /** Sorted values one apart, with a width-2 jump between the lower and upper halves. */
  private static int[] splitChain(int n) {
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i < n / 2 ? i : i + 1;
    }
    return nums;
  }

  /** Query i asks a same-side neighbor pair when i is even, a cross-jump pair when i is odd. */
  private static int[][] alternatingQueries(int n) {
    int[][] queries = new int[n][];
    for (int i = 0; i < n; i++) {
      queries[i] = i % 2 == 0 ? new int[] {i, i + 1} : new int[] {i, n - 1 - i};
    }
    return queries;
  }

  /** Query i pairs node i with its mirror node n-1-i. */
  private static int[][] mirrorQueries(int n) {
    int[][] queries = new int[n][];
    for (int i = 0; i < n; i++) {
      queries[i] = new int[] {i, n - 1 - i};
    }
    return queries;
  }
}
