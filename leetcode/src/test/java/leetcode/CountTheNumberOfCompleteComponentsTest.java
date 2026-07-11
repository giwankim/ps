package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CountTheNumberOfCompleteComponentsTest {
  CountTheNumberOfCompleteComponents sut = new CountTheNumberOfCompleteComponents();

  // Step 1: n may be 1 with no edges — a lone vertex is a complete component (every pair of its
  // vertices is trivially connected), so the floor answer is 1, not 0
  @Test
  void loneVertexIsACompleteComponent() {
    assertThat(sut.countCompleteComponents(1, new int[0][])).isEqualTo(1);
  }

  // Step 2: two vertices and no edges — each singleton is its own complete component, so isolated
  // vertices must be discovered from n, not from the edge list
  @Test
  void everyIsolatedVertexCountsSeparately() {
    assertThat(sut.countCompleteComponents(2, new int[0][])).isEqualTo(2);
  }

  // Step 3: a single edge — two vertices joined by their only possible edge form a complete
  // component of size two
  @Test
  void singleEdgePairIsComplete() {
    assertThat(sut.countCompleteComponents(2, new int[][] {{0, 1}})).isEqualTo(1);
  }

  // Step 4: the path 0-1-2 — connected is not enough; the chord 0-2 is missing (2 edges, needs
  // 3*2/2 = 3), so the answer is 0
  @Test
  void connectedPathOfThreeIsNotComplete() {
    assertThat(sut.countCompleteComponents(3, new int[][] {{0, 1}, {1, 2}})).isEqualTo(0);
  }

  // Step 5: closing that path into a triangle supplies the missing chord — 3 edges on 3 vertices
  // is complete
  @Test
  void triangleIsComplete() {
    assertThat(sut.countCompleteComponents(3, new int[][] {{0, 1}, {1, 2}, {0, 2}}))
        .isEqualTo(1);
  }

  // Step 6: LeetCode Example 1 — a triangle, an edge pair, and the isolated vertex 5 are all
  // complete; sizes 3, 2, and 1 must each be recognized in one graph
  @Test
  void mixedSizeCompleteComponentsAllCount() {
    assertThat(sut.countCompleteComponents(6, new int[][] {{0, 1}, {0, 2}, {1, 2}, {3, 4}}))
        .isEqualTo(3);
  }

  // Step 7: LeetCode Example 2 — the star 4-3-5 is connected but misses the edge 4-5, so only the
  // triangle counts; an incomplete component must not poison or inflate the tally
  @Test
  void incompleteComponentBesideACompleteOneIsNotCounted() {
    assertThat(sut.countCompleteComponents(6, new int[][] {{0, 1}, {0, 2}, {1, 2}, {3, 4}, {3, 5}}))
        .isEqualTo(1);
  }

  // Step 8: K4 minus the single chord 2-3 — five of six edges, and vertices 0 and 1 already have
  // full degree 3; a check that any (rather than every) vertex reaches degree m-1 would wrongly
  // accept this component
  @Test
  void oneMissingChordSpoilsAnAlmostCompleteComponent() {
    assertThat(sut.countCompleteComponents(4, new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}}))
        .isEqualTo(0);
  }

  // Step 9: the step-5 triangle with every pair reversed and the list shuffled — edges are
  // undirected, so [2,1] must connect both directions when adjacency is built
  @Test
  void edgePairsAreUndirected() {
    assertThat(sut.countCompleteComponents(3, new int[][] {{2, 1}, {0, 2}, {1, 0}}))
        .isEqualTo(1);
  }

  // Step 10: four components at once — K4, an edge pair, and a singleton are complete while the
  // path 6-7-8 is not; completeness must be judged per component, not from any global edge total
  @Test
  void eachComponentIsJudgedOnItsOwnEdges() {
    assertThat(sut.countCompleteComponents(
            10,
            new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}, {4, 5}, {6, 7}, {7, 8}}))
        .isEqualTo(3);
  }

  // Step 11: the component-count ceiling — 50 vertices and no edges yield 50 singletons, the
  // largest possible answer
  @Test
  void fiftyIsolatedVerticesAreFiftyCompleteComponents() {
    assertThat(sut.countCompleteComponents(50, new int[0][])).isEqualTo(50);
  }

  // Step 12: the edge-count ceiling — K50 packs all 50*49/2 = 1225 allowed edges into one
  // component, and the whole graph is a single complete component
  @Test
  void maxCompleteGraphIsOneComponent() {
    assertThat(sut.countCompleteComponents(50, completeGraphEdges(50))).isEqualTo(1);
  }

  /** Edges of the complete graph on vertices 0..n-1: every pair i &lt; j once. */
  private static int[][] completeGraphEdges(int n) {
    int[][] edges = new int[n * (n - 1) / 2][];
    int idx = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        edges[idx++] = new int[] {i, j};
      }
    }
    return edges;
  }
}
