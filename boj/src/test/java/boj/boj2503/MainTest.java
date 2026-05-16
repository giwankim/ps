package boj.boj2503;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {

  // Step 1: smallest input (N == 1, the lower bound 1 <= N <= 100) and the simplest
  // semantics — a 3-strike hint is the game-ending guess, so exactly that number
  // (123) is the only candidate.
  @Test
  @StdIo({"1", "123 3 0"})
  void singleThreeStrikeHintPinsTheAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 2: 0 strikes 0 balls — none of the guessed digits appear at all, so the
  // answer is any distinct 3-digit arrangement of the other 6 digits: 6*5*4 = 120.
  @Test
  @StdIo({"1", "123 0 0"})
  void zeroStrikeZeroBallExcludesGuessedDigits(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("120");
  }

  // Step 3: 0 strikes 3 balls — all three guessed digits are present but every one
  // is misplaced. That is a derangement of {1,2,3}: only 231 and 312 qualify -> 2.
  @Test
  @StdIo({"1", "123 0 3"})
  void zeroStrikeThreeBallIsADerangement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 4: 1 strike 2 balls — all three digits present (1+2=3) with exactly one in
  // its position. Permutations of 3 items with exactly one fixed point -> 3.
  @Test
  @StdIo({"1", "123 1 2"})
  void oneStrikeTwoBallHasExactlyOneFixedPoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 5: the worked example straight from the problem statement — four hints
  // whose only consistent answers are 324 and 328, so the count is 2. This is the
  // authoritative oracle anchor.
  @Test
  @StdIo({"4", "123 1 1", "356 1 0", "327 2 0", "489 0 1"})
  void statementExampleYieldsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 6: multiple hints must be intersected (logical AND), not handled
  // independently — two constraints together leave 10 candidates.
  @Test
  @StdIo({"2", "123 1 1", "145 1 0"})
  void multipleHintsAreIntersected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Step 7: robustness beyond the guaranteed input domain. The statement promises
  // contradiction-free input, but two mutually exclusive 3-strike hints have no
  // consistent answer; the count must then be 0, not a crash or a stale default.
  @Test
  @StdIo({"2", "123 3 0", "124 3 0"})
  void contradictoryHintsYieldZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }
}
