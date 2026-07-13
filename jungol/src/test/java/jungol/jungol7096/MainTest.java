package jungol.jungol7096;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 7096 고양이 구하기 (Rescue the Cat).
 *
 * <p>A cat sits on branch K of a tree. After the first line holding K, the input is two-line blocks
 * up to a terminating {@code -1}: a lower branch A with the count A_N of branches directly above
 * it, then the A_N branch numbers themselves, so A is closer to the root than any of them. Branch
 * numbers are 1..100 and not necessarily consecutive, every listed branch reaches the root, and the
 * cat never sits on the root. Print the cat's path down to the root on one line, space-separated,
 * from K to the root inclusive.
 */
class MainTest {

  // --- Official sample #1: a single block; the cat hops straight down and its sibling
  // branch 88 stays off the path. ---

  @Test
  @StdIo({"77", "100 2", "77 88", "-1"})
  void officialSampleOnePrintsTheCatsBranchThenTheRoot(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("77 100");
  }

  // --- Minimal tree with the smallest labels: one block, one branch above the root. ---

  @Test
  @StdIo({"2", "1 1", "2", "-1"})
  void minimalTreeWithTheSmallestLabelsDropsOneHopToTheRoot(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 1");
  }

  // --- Chains: the walk visits every branch between the cat and the root, whichever
  // order the blocks arrive in. ---

  @Test
  @StdIo({"9", "2 1", "5", "5 1", "7", "7 1", "9", "-1"})
  void chainListedRootFirstWalksDownEveryBranch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9 7 5 2");
  }

  @Test
  @StdIo({"4", "3 1", "4", "2 1", "3", "1 1", "2", "-1"})
  void chainListedLeafFirstStillFindsTheSameRoot(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each block names a lower branch whose own block only appears later.
    assertThat(out.capturedString().trim()).isEqualTo("4 3 2 1");
  }

  // --- Branching: only ancestors of the cat appear, never siblings or their subtrees,
  // and branches above the cat itself are ignored. ---

  @Test
  @StdIo({"21", "50 3", "10 20 30", "10 2", "11 12", "20 2", "21 22", "30 1", "31", "-1"})
  void pathSkipsSiblingBranchesAndTheirSubtrees(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("21 20 50");
  }

  @Test
  @StdIo({"5", "1 1", "5", "5 2", "6 7", "-1"})
  void catOnAnInnerBranchIgnoresTheBranchesAboveIt(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Branches 6 and 7 hang above the cat; the way down never touches them.
    assertThat(out.capturedString().trim()).isEqualTo("5 1");
  }

  // --- Boundary labels: 1 and 100 are both legal branch numbers and the root need not
  // be branch 1. ---

  @Test
  @StdIo({"1", "100 2", "1 99", "-1"})
  void boundaryLabelsOneAndOneHundredShareThePath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 100");
  }

  // --- Official samples #2 and #3: full trees with scattered blocks and longer descents. ---

  @Test
  @StdIo({"3", "7 1", "8", "9 2", "1 10", "3 1", "4", "10 1", "3", "5 3", "6 9 7", "-1"})
  void officialSampleTwoDescendsThreeBranchesGatheredFromScatteredBlocks(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 10 9 5");
  }

  @Test
  @StdIo({
    "14",
    "25 1",
    "24",
    "4 3",
    "3 1 2",
    "13 3",
    "9 4 11",
    "10 3",
    "20 8 7",
    "32 2",
    "10 21",
    "23 4",
    "13 19 32 22",
    "19 5",
    "12 5 14 17 30",
    "14 3",
    "6 15 16",
    "30 3",
    "18 31 29",
    "24 2",
    "23 26",
    "26 2",
    "27 28",
    "-1"
  })
  void officialSampleThreeDescendsFourLevelsOfTheContestTree(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The cat's branch 14 carries its own subtree (6, 15, 16) that the descent ignores.
    assertThat(out.capturedString().trim()).isEqualTo("14 19 23 24 25");
  }
}
