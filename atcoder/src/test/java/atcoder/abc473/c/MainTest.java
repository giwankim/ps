package atcoder.abc473.c;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 473 C -- Change Schools.
 *
 * <p>Line 1 holds N and K (1 ≤ N ≤ 2 x 10^5, 1 ≤ K ≤ N); line 2 holds A_1 ... A_N (1 ≤ A_i ≤ K),
 * the class of each current student. Takahashi joins one of the K classes, raising its size by one,
 * and is happy iff no class then has more students than his -- a tie keeps him happy. Print how
 * many of the K classes make him happy.
 *
 * <p>With M the size of the largest class, joining class j makes him happy iff c_j + 1 ≥ M: the
 * classes that qualify are exactly those of size M or M - 1, so the count of qualifying classes
 * never depends on which class is which. Each misreading gets its own guard: counting only the
 * size-M tier and missing the classes that qualify by tying
 * ({@link #aTieWithTheLargestClassIsHappy}, and official sample one, where the M - 1 tier holds two
 * of the three answers), demanding strict majority so that even a tie is sad (the same tie test,
 * whose runner-up would be dropped), letting the tier reach down to M - 2
 * ({@link #twoBelowTheLargestClassIsSad}), and counting empty classes
 * ({@link #anEmptyClassCannotCatchUp}, where class 3 has nobody and joining it still loses).
 *
 * <p>The boundaries are N = K = 1, one student and one class ({@link #aLoneStudentInTheOnlyClass}),
 * a single class holding everybody (official sample two), and the answer reaching K when every
 * class is a largest class ({@link #classesTiedAtTheMaxAllQualify} and
 * {@link #allSingletonClassesQualify}). {@link #aScatteredRosterQualifiesOnlyTheTopTwoTiers}
 * interleaves class sizes 5, 4, 4, 3, 1, 0 to exercise every tier of one roster at once.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"8 5", "3 3 5 5 4 4 3 2"})
  void officialSampleOneCountsTheTopClassAndBothRunnersUp(StdOut out) throws IOException {
    // Sizes are 0 1 3 2 2: class 3 (size 3 = M) plus classes 4 and 5 (size 2 = M - 1) qualify.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"6 1", "1 1 1 1 1 1"})
  void officialSampleTwoHasOnlyOneClassToJoin(StdOut out) throws IOException {
    // K = 1: with no other class to outgrow his, the only class always makes him happy.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"14 8", "6 1 5 3 8 4 3 4 3 5 1 2 5 1"})
  void officialSampleThreeCountsThreeMaxClassesAndOneRunnerUp(StdOut out) throws IOException {
    // Classes 1, 3, 5 have size 3 = M and class 4 has size 2 = M - 1. Reading K before N would
    // consume only eight values and print 6 instead.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The happy condition is c + 1 >= M: ties count, two below does not. ---

  @Test
  @StdIo({"5 2", "1 1 1 2 2"})
  void aTieWithTheLargestClassIsHappy(StdOut out) throws IOException {
    // Joining class 2 makes it 3, tying class 1's 3: no class has MORE, so both classes qualify.
    // Demanding strict majority, or counting only the size-M tier, prints 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"4 2", "1 1 1 2"})
  void twoBelowTheLargestClassIsSad(StdOut out) throws IOException {
    // Class 2 has size 1 = M - 2: joining it reaches 2, still behind class 1's 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3 3", "1 1 2"})
  void anEmptyClassCannotCatchUp(StdOut out) throws IOException {
    // Class 3 exists but has nobody: joining it reaches 1, behind class 1's 2. Classes 1 and 2
    // qualify; counting all K classes prints 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The answer can reach K. ---

  @Test
  @StdIo({"3 3", "1 2 3"})
  void allSingletonClassesQualify(StdOut out) throws IOException {
    // Every class has size 1 = M: joining any of them makes it the unique largest.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"6 3", "1 2 3 1 2 3"})
  void classesTiedAtTheMaxAllQualify(StdOut out) throws IOException {
    // Three classes tied at size 2: each join breaks its own tie upward, so all of K qualifies
    // even though the M - 1 tier is empty.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Boundaries and a mixed roster. ---

  @Test
  @StdIo({"1 1", "1"})
  void aLoneStudentInTheOnlyClass(StdOut out) throws IOException {
    // N and K at their floors: one class of one student, and he happily makes it two.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"17 6", "3 1 2 4 1 3 2 5 1 4 2 3 1 2 4 3 1"})
  void aScatteredRosterQualifiesOnlyTheTopTwoTiers(StdOut out) throws IOException {
    // Interleaved sizes 5 4 4 3 1 0 for classes 1..6: class 1 (size 5 = M) and classes 2 and 3
    // (size 4 = M - 1) qualify; the size-3, size-1, and empty classes all stay sad.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }
}
