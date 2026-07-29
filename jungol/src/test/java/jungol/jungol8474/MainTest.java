package jungol.jungol8474;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 8474 Leaf Leaf.
 *
 * <p>A tree with N nodes (2 ≤ N ≤ 200,000) arrives as N-1 edge lines {@code X Y}. A leaf is a node
 * with exactly one incident edge, and every tree with at least two nodes has at least two leaves.
 * Pick two leaves whose distance is minimal and print two lines: the minimum distance, then the two
 * leaf numbers in either order. The judge is special—any optimal pair is accepted—so every test
 * here either enumerates all accepted outputs or is built on a tree whose optimal pair is unique.
 */
class MainTest {

  // --- Official samples. Sample #1 ties between the pairs {1,5} and {5,6}, so all four
  // printable orders pass; sample #2 has a single closest pair. ---

  @Test
  @StdIo({"6", "5 3", "3 4", "4 1", "2 3", "2 6"})
  void officialSampleOneAcceptsEitherClosestPairAtDistanceThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("3");
    assertThat(lines[1]).isIn("1 5", "5 1", "5 6", "6 5");
  }

  @Test
  @StdIo({"8", "1 2", "2 3", "2 4", "4 5", "4 6", "6 7", "7 8"})
  void officialSampleTwoFindsTheOnlyPairAtDistanceTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("2");
    assertThat(lines[1]).isIn("1 3", "3 1");
  }

  // --- Smallest tree: with N = 2 both endpoints are leaves and there is no internal
  // node at all, so the answer is the lone edge itself. ---

  @Test
  @StdIo({"2", "1 2"})
  void twoNodeTreeAnswersWithItsOnlyEdge(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("1");
    assertThat(lines[1]).isIn("1 2", "2 1");
  }

  // --- Chains (subtask #1: exactly two leaves). The only leaves are the two ends,
  // however the edges are ordered and wherever node 1 sits. ---

  @Test
  @StdIo({"5", "1 2", "2 3", "3 4", "4 5"})
  void bareChainLeavesOnlyItsTwoEndsToPick(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("4");
    assertThat(lines[1]).isIn("1 5", "5 1");
  }

  @Test
  @StdIo({"5", "2 4", "5 2", "1 3", "4 1"})
  void scrambledChainEdgesWithNodeOneInsideStillReachTheEnds(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The path is 3-1-4-2-5: node 1 is internal and the edges arrive shuffled.
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("4");
    assertThat(lines[1]).isIn("3 5", "5 3");
  }

  // --- Even distances: twin leaves grafted on one spine node meet two apart, beating
  // the chain ends. ---

  @Test
  @StdIo({"7", "1 2", "2 3", "3 4", "4 5", "3 6", "3 7"})
  void twinLeavesOnOneSpineNodeBeatTheChainEnds(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("2");
    assertThat(lines[1]).isIn("6 7", "7 6");
  }

  // --- Odd distances: the closest pair can straddle an edge, one leaf on each endpoint,
  // sharing no common neighbor. ---

  @Test
  @StdIo({"10", "1 2", "2 3", "3 4", "4 5", "5 6", "6 7", "7 8", "4 9", "5 10"})
  void closestPairCanStraddleAnEdgeWithNoCommonNeighbor(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Leaves 9 and 10 hang on the adjacent spine nodes 4 and 5; both chain ends sit
    // three steps out, so every rival pair is at distance four or more.
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("3");
    assertThat(lines[1]).isIn("9 10", "10 9");
  }

  // --- Spider: legs of length one, two, and three around one center; the two shortest
  // legs added together make the answer. ---

  @Test
  @StdIo({"7", "1 2", "1 3", "3 4", "1 5", "5 6", "6 7"})
  void spiderPicksItsTwoShortestLegs(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("3");
    assertThat(lines[1]).isIn("2 4", "4 2");
  }

  // --- Greedy trap: the pair nearest node 1 sits three apart, but a twin-leaf cluster
  // at the far end of the spine is only two apart. ---

  @Test
  @StdIo({"9", "1 2", "2 3", "3 4", "4 5", "5 6", "5 7", "2 8", "8 9"})
  void remoteTwinClusterBeatsTheLeafPairNearNodeOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("2");
    assertThat(lines[1]).isIn("6 7", "7 6");
  }

  // --- Ties across the tree: a perfect binary tree holds two sibling pairs at the same
  // distance, and the special judge accepts either one. ---

  @Test
  @StdIo({"7", "1 2", "1 3", "2 4", "2 5", "3 6", "3 7"})
  void perfectBinaryTreeAcceptsEitherSiblingPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    String[] lines = out.capturedString().trim().split("\\R");
    assertThat(lines[0]).isEqualTo("2");
    assertThat(lines[1]).isIn("4 5", "5 4", "6 7", "7 6");
  }
}
