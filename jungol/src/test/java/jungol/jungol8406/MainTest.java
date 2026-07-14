package jungol.jungol8406;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 8406 종교 2 (Religions 2).
 *
 * <p>A school has N students (1 ≤ N ≤ 50,000), each initially following a religion of their own.
 * The first line holds N and Q (1 ≤ Q ≤ 100,000); each of the next Q lines is one operation.
 * Operation {@code 1 x y} merges the religions of students x and y into one group; operation
 * {@code 2 x} prints how many students currently share x's religion, counting x itself. Print one
 * line per type-2 operation, reflecting the state at that moment.
 */
class MainTest {

  // --- Official sample: a query before any merge, one after the first merge, and one
  // after the four students have been pulled together (the final merge closes a cycle). ---

  @Test
  @StdIo({"5 7", "2 2", "1 1 2", "2 2", "1 3 4", "1 2 3", "1 1 4", "2 3"})
  void officialSampleTracksTheGroupAsItGrowsToFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("1", "2", "4");
  }

  // --- Fresh state: before any merge every student is alone, down to a school of one. ---

  @Test
  @StdIo({"1 1", "2 1"})
  void singleStudentSchoolAnswersOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"5 2", "2 3", "2 5"})
  void freshStudentsAnswerOneBeforeAnyMerge(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("1", "1");
  }

  // --- Growth over time: each query reports the size at that moment, queried from a
  // different member each step. ---

  @Test
  @StdIo({"4 6", "1 1 2", "2 2", "1 2 3", "2 3", "1 3 4", "2 1"})
  void chainMergesGrowTheCountStepByStep(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("2", "3", "4");
  }

  // --- Redundant merges: repeats, reversals, self-merges, and cycle-closing merges are
  // no-ops and must not inflate the count. ---

  @Test
  @StdIo({"3 4", "1 1 2", "1 2 1", "1 1 2", "2 2"})
  void repeatedAndReversedMergesDoNotDoubleCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3 2", "1 3 3", "2 3"})
  void selfMergeLeavesTheStudentAlone(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4 5", "1 1 2", "1 3 4", "1 2 3", "1 4 1", "2 2"})
  void cycleClosingMergeKeepsTheCountAtFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Cluster arithmetic: merging two grown groups adds both sizes, visible from any
  // member, while bystanders keep their own counts. ---

  @Test
  @StdIo({"6 7", "1 1 2", "1 2 3", "1 4 5", "1 3 4", "2 5", "2 1", "2 6"})
  void mergingTwoGrownClustersAddsBothSizes(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The trio {1,2,3} absorbs the pair {4,5}; student 6 never joins anyone.
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("5", "5", "1");
  }

  @Test
  @StdIo({"6 5", "1 1 2", "1 3 4", "1 4 5", "2 2", "2 4"})
  void separateClustersReportTheirOwnCounts(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("2", "3");
  }

  // --- Boundary labels: a merge across the full student range must index the last
  // student cleanly. ---

  @Test
  @StdIo({"50000 2", "1 1 50000", "2 50000"})
  void boundaryMergeAcrossTheFullStudentRangeCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }
}
