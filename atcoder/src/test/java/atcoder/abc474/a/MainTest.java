package atcoder.abc474.a;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 474 A -- Not X.
 *
 * <p>One line holds an integer X (1 ≤ X ≤ 3). Print any integer between 1 and 3, inclusive, that
 * differs from X; the judge accepts either of the two candidates.
 *
 * <p>X takes only three values and the official samples supply one of each, so the three specs
 * below exhaust the input: a program that clears them all is correct, whatever formula it uses.
 * Each spec accepts both candidates, the way the judge does, but judges the trimmed output as a
 * whole, so printing both candidates on one line or printing nothing is rejected. Between them the
 * specs still rule out every fixed guess. Echoing X fails everywhere and a constant fails on the
 * sample equal to it, while X - 1 falls to 0 at X = 1
 * ({@link #officialSampleOnePrintsTwoOrThreeForOne}) and X + 1 climbs to 4 at X = 3
 * ({@link #officialSampleThreePrintsOneOrTwoForThree}).
 */
class MainTest {

  // --- Official samples: one per possible X, so together they cover the whole input. ---

  @Test
  @StdIo("1")
  void officialSampleOnePrintsTwoOrThreeForOne(StdOut out) throws IOException {
    // X at its floor: X - 1 leaves the range at 0, and echoing X or fixing on 1 prints 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isIn("2", "3");
  }

  @Test
  @StdIo("2")
  void officialSampleTwoPrintsOneOrThreeForTwo(StdOut out) throws IOException {
    // The middle value: both neighbors are valid, so only echoing X or fixing on 2 goes wrong.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isIn("1", "3");
  }

  @Test
  @StdIo("3")
  void officialSampleThreePrintsOneOrTwoForThree(StdOut out) throws IOException {
    // X at its ceiling: X + 1 leaves the range at 4, and echoing X or fixing on 3 prints 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isIn("1", "2");
  }
}
