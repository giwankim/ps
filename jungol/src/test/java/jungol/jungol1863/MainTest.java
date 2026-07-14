package jungol.jungol1863;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 1863 종교 (Religions).
 *
 * <p>A school has n students (0 &lt; n ≤ 50,000) and m surveyed pairs (0 ≤ m ≤ 100,000); the first
 * line holds n and m, then each of the next m lines names two students i and j (1 ≤ i, j ≤ n) who
 * share the same religion. Every student follows at most one religion, sharing is transitive, and
 * any student never named in a pair may follow a religion of their own. Print the maximum possible
 * number of distinct religions—the number of connected components the pairs leave behind.
 */
class MainTest {

  // --- Official samples: a star that drags everyone into one religion, and a cluster
  // that leaves student 1 on their own. ---

  @Test
  @StdIo({"10 9", "1 2", "1 3", "1 4", "1 5", "1 6", "1 7", "1 8", "1 9", "1 10"})
  void officialSampleOneStarsEveryStudentIntoOneReligion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"5 3", "4 5", "3 5", "2 5"})
  void officialSampleTwoLeavesStudentOneBesideTheCluster(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- No pairs at all: every student may follow their own religion, down to a school
  // of one and up to the full 50,000. ---

  @Test
  @StdIo({"1 0"})
  void singleStudentWithNoPairsFollowsOneReligion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"7 0"})
  void noPairsGiveEveryStudentTheirOwnReligion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"50000 0"})
  void maximumSchoolWithNoPairsCountsFiftyThousandReligions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("50000");
  }

  // --- Transitivity: a chain of pairs pulls its whole span into one religion. ---

  @Test
  @StdIo({"5 4", "1 2", "2 3", "3 4", "4 5"})
  void chainOfPairsMergesTransitivelyIntoOneReligion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Redundant pairs: duplicates, reversals, self-pairs, and cycles repeat what is
  // already known, so none of them may shrink the count a second time. ---

  @Test
  @StdIo({"4 4", "1 2", "2 1", "1 2", "3 4"})
  void duplicateAndReversedPairsDoNotMergeTwice(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3 2", "2 2", "1 3"})
  void selfPairChangesNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"4 3", "1 2", "2 3", "3 1"})
  void cyclePairsInsideOneClusterDoNotOvercount(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Three pairs but only two effective merges: the answer is 2, not 4 - 3 = 1.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Cluster shapes: separate clusters count independently, loners keep counting,
  // and a late pair may bridge two clusters that grew on their own. ---

  @Test
  @StdIo({"8 3", "1 2", "2 3", "5 6"})
  void twoClustersAndThreeLonersCountFiveReligions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"6 3", "1 2", "3 4", "2 3"})
  void latePairBridgesTwoGrownClusters(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The bridge 2-3 must merge both whole clusters, not just relabel one member.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }
}
