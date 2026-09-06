package atcoder.abc474.b;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 474 B -- Exit Order.
 *
 * <p>Line 1 holds N (10 ≤ N ≤ 100); line 2 holds a permutation P_1 ... P_N of 1 ... N, where P_i is
 * the seat of the customer who left i-th. Seats 1 to 10 form the first group, 11 to 20 the second,
 * and so on, the last group possibly short. Groups must leave in order, but within a group the
 * order is free. Print {@code Yes} if P obeys the rule and {@code No} otherwise.
 *
 * <p>The rule reduces to one check per customer: the seat P_i and the position i must fall in the
 * same bracket, (x - 1) / 10 for both. Each way of getting that wrong has a guard. Demanding sorted
 * order outright fails official sample one and {@link #eachGroupMayLeaveInAnyOrder}. Bracketing by
 * x / 10 on one side only sends seat 10 and position 10 to different groups, so
 * {@link #seatOrderItselfIsAccepted} prints No; bracketing by x / 10 on both sides is consistent
 * but groups seats 10 through 19 together, which {@link #seatTenMayLeaveFirst} and
 * {@link #swappingSeatsTenAndElevenIsRejected} separate from the truth. A check that stops after
 * the first group, or that only splits the hall at seat 10, passes
 * {@link #swappingSeatsTwentyAndTwentyOneIsRejected}, whose first ten customers are in order.
 *
 * <p>The boundaries are N = 10, a single group in which every order is legal
 * ({@link #theSmallestCaseIsOneGroupSoAnyOrderGoes}), N = 11, where the last group is one lone
 * customer ({@link #aLoneCustomerMayFormTheLastGroup}), and N = 100 with ten full groups
 * ({@link #theLargestCaseAcceptsEveryGroupReversed} and
 * {@link #theLargestCaseRejectsSeatOneHundredLeavingFirst}).
 */
class MainTest {

  /** Seats 1 through 10 in seat order: the first group with no shuffling. */
  private static final String FIRST_GROUP = "1 2 3 4 5 6 7 8 9 10";

  /** Seats 11 through 20 in seat order: the second group with no shuffling. */
  private static final String SECOND_GROUP = "11 12 13 14 15 16 17 18 19 20";

  /** Every group of the N = 100 case back to front: groups in order, each one reversed. */
  private static final String ALL_TEN_GROUPS_REVERSED = "10 9 8 7 6 5 4 3 2 1"
      + " 20 19 18 17 16 15 14 13 12 11"
      + " 30 29 28 27 26 25 24 23 22 21"
      + " 40 39 38 37 36 35 34 33 32 31"
      + " 50 49 48 47 46 45 44 43 42 41"
      + " 60 59 58 57 56 55 54 53 52 51"
      + " 70 69 68 67 66 65 64 63 62 61"
      + " 80 79 78 77 76 75 74 73 72 71"
      + " 90 89 88 87 86 85 84 83 82 81"
      + " 100 99 98 97 96 95 94 93 92 91";

  /** Seats 2 through 99 in seat order: the N = 100 case with its two end seats swapped out. */
  private static final String SEATS_TWO_THROUGH_NINETY_NINE = "2 3 4 5 6 7 8 9 10"
      + " 11 12 13 14 15 16 17 18 19 20"
      + " 21 22 23 24 25 26 27 28 29 30"
      + " 31 32 33 34 35 36 37 38 39 40"
      + " 41 42 43 44 45 46 47 48 49 50"
      + " 51 52 53 54 55 56 57 58 59 60"
      + " 61 62 63 64 65 66 67 68 69 70"
      + " 71 72 73 74 75 76 77 78 79 80"
      + " 81 82 83 84 85 86 87 88 89 90"
      + " 91 92 93 94 95 96 97 98 99";

  // --- Official samples. ---

  @Test
  @StdIo({"25", "1 6 5 7 8 10 2 4 3 9 15 17 12 11 19 20 18 13 14 16 21 23 24 22 25"})
  void officialSampleOneAcceptsThreeGroupsShuffledWithin(StdOut out) throws IOException {
    // Seats 1-10, then 11-20, then 21-25, each group in its own scrambled order.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"11", "11 10 7 2 1 5 6 4 8 9 3"})
  void officialSampleTwoRejectsSeatElevenLeavingFirst(StdOut out) throws IOException {
    // Seat 11 belongs to the second group and must wait for all of 1-10.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No");
  }

  // --- Within a group any order goes; across groups the order is fixed. ---

  @Test
  @StdIo({"20", FIRST_GROUP + " " + SECOND_GROUP})
  void seatOrderItselfIsAccepted(StdOut out) throws IOException {
    // Two full groups leaving seat by seat. Bracketing positions by i / 10 instead of (i - 1) / 10
    // puts the tenth leaver in the second group while seat 10 stays in the first, and prints No.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"20", "4 8 1 10 2 9 6 3 7 5 17 11 20 14 19 12 16 18 13 15"})
  void eachGroupMayLeaveInAnyOrder(StdOut out) throws IOException {
    // Neither group is sorted, yet the first ten leavers are exactly seats 1-10 and the next ten
    // exactly 11-20. Requiring order inside a group prints No.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"20", "1 2 3 4 5 6 7 8 9 11 10 12 13 14 15 16 17 18 19 20"})
  void swappingSeatsTenAndElevenIsRejected(StdOut out) throws IOException {
    // Adjacent seats on opposite sides of the split: 11 leaves tenth and 10 leaves eleventh. A
    // bracket of x / 10 on both seat and position files them both under group 1 and prints Yes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No");
  }

  @Test
  @StdIo({"21", FIRST_GROUP + " 11 12 13 14 15 16 17 18 19 21 20"})
  void swappingSeatsTwentyAndTwentyOneIsRejected(StdOut out) throws IOException {
    // The first group is intact; the fault is that seat 21, the whole third group, leaves before
    // seat 20. A check that stops after the first ten, or that only splits the hall at seat 10,
    // prints Yes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No");
  }

  // --- The last group may be short. ---

  @Test
  @StdIo({"11", FIRST_GROUP + " 11"})
  void aLoneCustomerMayFormTheLastGroup(StdOut out) throws IOException {
    // Official sample two's N with the lone second-group customer leaving last, as they must.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"11", "10 1 2 3 4 5 6 7 8 9 11"})
  void seatTenMayLeaveFirst(StdOut out) throws IOException {
    // Seat 10 is the last member of the first group, so it may lead the way out. A bracket of
    // x / 10 files it under the second group and prints No.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  // --- N at its floor and ceiling. ---

  @Test
  @StdIo({"10", "7 10 3 1 4 5 9 2 6 8"})
  void theSmallestCaseIsOneGroupSoAnyOrderGoes(StdOut out) throws IOException {
    // N = 10 makes a single group, so every permutation obeys the rule, this scramble included.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"100", ALL_TEN_GROUPS_REVERSED})
  void theLargestCaseAcceptsEveryGroupReversed(StdOut out) throws IOException {
    // Ten full groups, each leaving from its highest seat down to its lowest.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"100", "100 " + SEATS_TWO_THROUGH_NINETY_NINE + " 1"})
  void theLargestCaseRejectsSeatOneHundredLeavingFirst(StdOut out) throws IOException {
    // Seat order everywhere except the two ends, which trade places: the tenth group leads and
    // the first group closes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No");
  }
}
