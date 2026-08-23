package atcoder.abc472.b;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 472 B -- Break a Stick.
 *
 * <p>N - 1 notches divide a stick into N parts (2 ≤ N ≤ 100) of lengths L1, L2, ..., LN (1 ≤ Li ≤
 * 100000), given in order from one end. Break the stick at one notch and print the smallest
 * absolute difference between the lengths of the two resulting sticks.
 *
 * <p>A break lands on a notch, so no part is ever split: with prefix sum P at a notch and total T
 * the candidate is |P - (T - P)|, and the answer is the smallest of the N - 1 candidates. The tests
 * below separate the four ways that goes wrong -- halving the stick as if it were freely divisible
 * (which prints T % 2), scanning fewer than all N - 1 notches (the first and the last are the ones
 * easily lost), dropping the absolute value, and reordering the parts, which are laid out along the
 * stick rather than handed over as a multiset. Reversing them is the one reordering no test can
 * catch: notch i from the left is notch N - i from the right and the two pieces merely swap sides,
 * so the asymmetric orders below are aimed at sorting instead.
 */
class MainTest {

  /** Ten parts of the maximum length 100000. */
  private static final String TEN_LONGEST_PARTS =
      "100000 100000 100000 100000 100000 100000 100000 100000 100000 100000";

  /** N and every Li at their ceilings: 100 parts of 100000, the heaviest stick allowed. */
  private static final String HUNDRED_LONGEST_PARTS = TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS
      + " " + TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS
      + " " + TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS
      + " " + TEN_LONGEST_PARTS + " " + TEN_LONGEST_PARTS;

  /** Ten parts of the minimum length 1. */
  private static final String TEN_SHORTEST_PARTS = "1 1 1 1 1 1 1 1 1 1";

  /** Ninety-nine parts of the minimum length 1. */
  private static final String NINETY_NINE_SHORTEST_PARTS = TEN_SHORTEST_PARTS
      + " " + TEN_SHORTEST_PARTS + " " + TEN_SHORTEST_PARTS + " " + TEN_SHORTEST_PARTS
      + " " + TEN_SHORTEST_PARTS + " " + TEN_SHORTEST_PARTS + " " + TEN_SHORTEST_PARTS
      + " " + TEN_SHORTEST_PARTS + " " + TEN_SHORTEST_PARTS + " 1 1 1 1 1 1 1 1 1";

  /** N at its ceiling of 100, as lopsided as the parts go: 99 of length 1, then one of 100000. */
  private static final String HUNDRED_LOPSIDED_PARTS = NINETY_NINE_SHORTEST_PARTS + " 100000";

  // --- Official samples. ---

  @Test
  @StdIo({"4", "5 2 3 8"})
  void officialSampleOneBreaksAtTheThirdOfItsThreeNotches(StdOut out) throws IOException {
    // The three notches offer 8, 4 and 2, so the answer is neither the first nor the largest.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"7", "31 41 59 26 53 58 97"})
  void officialSampleTwoBreaksAfterTheFourthPart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("51");
  }

  @Test
  @StdIo({"10", "67011 35764 33042 24098 63738 98760 17199 68579 21812 45408"})
  void officialSampleThreeHandlesPartsOfFiveDigits(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("28105");
  }

  // --- A break lands on a notch, so no part is ever split. ---

  @Test
  @StdIo({"3", "1 100 1"})
  void oneLongPartInTheMiddleForcesALopsidedBreak(StdOut out) throws IOException {
    // The total of 102 is even, but neither notch comes anywhere near half of it: cutting the
    // stick wherever one likes prints 0, and sorting the parts into 1 1 100 prints 98.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"3", "1 2 3"})
  void anEvenBreakIsReachedWhenANotchLandsOnHalfTheTotal(StdOut out) throws IOException {
    // The other side of the same coin: 0 is a real answer, not a value to be ruled out.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All N - 1 notches are candidates. ---

  @Test
  @StdIo({"4", "1 1 1 100"})
  void theLastNotchIsTried(StdOut out) throws IOException {
    // The candidates fall as the scan advances -- 101, 99, 97 -- so a loop that stops one notch
    // early, at the part rather than the notch count, prints 99.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("97");
  }

  @Test
  @StdIo({"4", "100 1 1 1"})
  void theFirstNotchIsTried(StdOut out) throws IOException {
    // The same three candidates rising -- 97, 99, 101 -- so a loop that opens one notch late,
    // seeding the minimum from the second notch, prints 99.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("97");
  }

  @Test
  @StdIo({"3", "4 3 3"})
  void theNotchShortOfHalfCanBeatTheFirstOneAtOrPastIt(StdOut out) throws IOException {
    // Half of the total of 10 is 5. Prefix 4 gives 2 and prefix 7 gives 4, so walking up to the
    // first notch at or past half and stopping there prints 4.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3", "1 5 4"})
  void theNotchPastHalfCanBeatTheLastOneShortOfIt(StdOut out) throws IOException {
    // The mirror of the case above, again over a total of 10: prefix 1 gives 8 and prefix 6 gives
    // 2, so stopping at the last notch short of half prints 8.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The difference is an absolute value, whichever piece comes out longer. ---

  @Test
  @StdIo({"3", "1 1 100"})
  void everyBreakLeavesTheNearPieceShorter(StdOut out) throws IOException {
    // Prefix minus suffix is -100 then -98, so a minimum taken over the raw difference prints
    // -100 and picks the worse of the two notches while doing it.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("98");
  }

  @Test
  @StdIo({"3", "100 1 1"})
  void everyBreakLeavesTheNearPieceLonger(StdOut out) throws IOException {
    // The same stick end to end, so suffix minus prefix is now the negative one: the guard has to
    // be an absolute value rather than a subtraction written the other way round.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("98");
  }

  // --- The N and Li boundaries. ---

  @Test
  @StdIo({"2", "1 1"})
  void theSmallestStickBreaksEvenly(StdOut out) throws IOException {
    // N and both Li at their floors, over the single notch such a stick has.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2", "1 2"})
  void anOddTotalCannotBreakMoreEvenlyThanOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "1 100000"})
  void theOnlyNotchIsTakenHoweverBadItIs(StdOut out) throws IOException {
    // The widest gap the constraints allow. There is nothing to choose between, so leaving the
    // stick whole is not on offer: the answer is 99999, not the total of 100001.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("99999");
  }

  @Test
  @StdIo({"100", HUNDRED_LONGEST_PARTS})
  void theHeaviestStickBreaksEvenlyDownTheMiddle(StdOut out) throws IOException {
    // A total of 10^7, the ceiling, split 50 parts either side.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"100", HUNDRED_LOPSIDED_PARTS})
  void theLongestStickCanStillWantItsFinalNotch(StdOut out) throws IOException {
    // N at its ceiling with the optimum at notch 99 of 99: 99 against 100000 beats every earlier
    // notch, so the last-notch off-by-one costs two here rather than the usual one.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("99901");
  }
}
