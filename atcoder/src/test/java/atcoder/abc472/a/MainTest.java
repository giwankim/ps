package atcoder.abc472.a;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 472 A -- A.
 *
 * <p>One line holds a string S (1 ≤ |S| ≤ 100) of uppercase English letters. Print the string
 * obtained by replacing every character of S other than {@code A} with {@code .}.
 *
 * <p>The rewrite happens in place, so the answer is exactly as long as S and every column holds
 * either the {@code A} that was already there or a {@code .}. Collecting the A's instead of
 * overwriting around them prints {@code AAA} for {@code BANANA} and an empty line for a string
 * holding no A at all, which is what the three official samples -- one A, three A's, none -- are
 * there to separate. Nothing else about a letter matters: {@code A} is matched exactly, and the A's
 * may run from none to all 100.
 */
class MainTest {

  /** |S| at its ceiling of 100: four runs of the 24 letters B through Y, each closed by an A. */
  private static final String LONGEST_INPUT = "BCDEFGHIJKLMNOPQRSTUVWXYA"
      + "BCDEFGHIJKLMNOPQRSTUVWXYA"
      + "BCDEFGHIJKLMNOPQRSTUVWXYA"
      + "BCDEFGHIJKLMNOPQRSTUVWXYA";

  // --- Official samples. ---

  @Test
  @StdIo("ATCODER")
  void officialSampleOneKeepsTheSingleAItOpensWith(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("A......");
  }

  @Test
  @StdIo("BANANA")
  void officialSampleTwoKeepsAllThreeAs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo(".A.A.A");
  }

  @Test
  @StdIo("CORRECT")
  void officialSampleThreeKeepsNoneOfTheSeven(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo(".......");
  }

  // --- Non-A characters are overwritten where they stand rather than dropped, so the answer is
  // always as long as S. ---

  @Test
  @StdIo("BA")
  void aNonAAheadOfTheAIsDottedRatherThanDropped(StdOut out) throws IOException {
    // Filtering S down to its A's prints A here: one column short, and with the A in the wrong
    // one.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo(".A");
  }

  @Test
  @StdIo("AB")
  void aNonABehindTheAIsDottedRatherThanDropped(StdOut out) throws IOException {
    // The same two letters the other way round, which a filter cannot tell from the pair above.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("A.");
  }

  // --- Every A survives, wherever it sits and however many there are. ---

  @Test
  @StdIo("ZZZA")
  void anAInTheLastColumnIsKept(StdOut out) throws IOException {
    // A scan that stops one index short drops it and prints three dots.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("...A");
  }

  @Test
  @StdIo("ZAAZ")
  void adjacentAsAreBothKept(StdOut out) throws IOException {
    // Every A is judged on its own; one replacement pass over S does not consume the next A.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo(".AA.");
  }

  @Test
  @StdIo("AAAAA")
  void aStringOfNothingButAsComesBackUnchanged(StdOut out) throws IOException {
    // The polarity check: dotting the A's rather than everything else prints five dots here.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("AAAAA");
  }

  // --- A is matched exactly; no other letter is spared. ---

  @Test
  @StdIo("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
  void everyLetterButAIsDottedAcrossTheWholeAlphabet(StdOut out) throws IOException {
    // Both ends of the alphabet go, so no ordering test -- keep everything from A onward, keep
    // everything up to Z -- stands in for the equality.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("A" + ".".repeat(25));
  }

  // --- The |S| boundaries. ---

  @Test
  @StdIo("A")
  void theShortestStringIsKeptWhenItIsAnA(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("A");
  }

  @Test
  @StdIo("Z")
  void theShortestStringBecomesOneDotWhenItIsNotAnA(StdOut out) throws IOException {
    // The answer is a line carrying a single dot, not an empty line.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo(".");
  }

  @Test
  @StdIo(LONGEST_INPUT)
  void theLongestStringKeepsAllHundredColumns(StdOut out) throws IOException {
    // |S| at its ceiling. S opens on a non-A and closes on an A, so neither end may be trimmed.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo((".".repeat(24) + "A").repeat(4));
  }
}
