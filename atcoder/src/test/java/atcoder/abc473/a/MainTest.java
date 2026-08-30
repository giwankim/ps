package atcoder.abc473.a;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 473 A -- Second Half Sum.
 *
 * <p>Line 1 holds an even N (2 ≤ N ≤ 100); line 2 holds A_1 ... A_N (1 ≤ A_i ≤ 100). Print the sum
 * of the latter half of A, that is A_(N/2)+1 + A_(N/2)+2 + ... + A_N.
 *
 * <p>Every value is positive, so each way of picking the wrong elements shows up as a wrong sum:
 * summing the first half instead of the second ({@link #theFirstHalfContributesNothing}, and at
 * full size {@link #aCeilingCaseIgnoresTheLargeValuesInItsFirstHalf}), summing all of A
 * ({@link #equalHalvesStillProduceHalfTheTotal}, where the halves alone cannot tell the direction
 * apart), and the two off-by-one splits -- reaching one element back into the first half
 * ({@link #theLastElementOfTheFirstHalfIsNotCounted}) or starting one element late
 * ({@link #theFirstElementOfTheSecondHalfIsCounted}). The far end has its own guard, since a loop
 * that stops at N - 1 drops only A_N ({@link #theVeryLastElementIsCounted}).
 *
 * <p>The boundaries are N = 2, where the second half is the single element A_2 (official sample two
 * and its mirror), and N = 100 with every A_i = 100, where the answer reaches its ceiling of 5000
 * ({@link #theLargestCaseSumsFiftyHundreds}).
 */
class MainTest {

  /** Ten space-separated 100s; concatenated below into the N = 100 ceiling inputs. */
  private static final String TEN_HUNDREDS = "100 100 100 100 100 100 100 100 100 100";

  /** Ten space-separated 1s. */
  private static final String TEN_ONES = "1 1 1 1 1 1 1 1 1 1";

  /** Fifty space-separated 100s: one half of an N = 100 case. */
  private static final String FIFTY_HUNDREDS = TEN_HUNDREDS + " " + TEN_HUNDREDS + " "
      + TEN_HUNDREDS + " " + TEN_HUNDREDS + " " + TEN_HUNDREDS;

  /** Fifty space-separated 1s. */
  private static final String FIFTY_ONES =
      TEN_ONES + " " + TEN_ONES + " " + TEN_ONES + " " + TEN_ONES + " " + TEN_ONES;

  // --- Official samples. ---

  @Test
  @StdIo({"8", "1 3 7 8 4 2 5 6"})
  void officialSampleOneSumsTheLatterFourOfEight(StdOut out) throws IOException {
    // A_5 + A_6 + A_7 + A_8 = 4 + 2 + 5 + 6; the first half sums to 19, so it cannot leak in.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  @Test
  @StdIo({"2", "1 100"})
  void officialSampleTwoTakesTheSecondOfTwo(StdOut out) throws IOException {
    // N at its floor: the latter half is the single element A_2.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"10", "31 41 59 26 53 58 97 93 23 84"})
  void officialSampleThreeSumsTheLatterFiveOfTen(StdOut out) throws IOException {
    // 58 + 97 + 93 + 23 + 84 = 355.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("355");
  }

  // --- Which half: the first half contributes nothing, however large it is. ---

  @Test
  @StdIo({"2", "100 1"})
  void theFirstHalfContributesNothing(StdOut out) throws IOException {
    // Official sample two mirrored: summing the wrong half prints 100 here.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4", "3 3 3 3"})
  void equalHalvesStillProduceHalfTheTotal(StdOut out) throws IOException {
    // With the halves indistinguishable, only summing all of A goes wrong -- and prints 12.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- The split sits exactly between A_(N/2) and A_(N/2)+1. ---

  @Test
  @StdIo({"4", "1 95 1 1"})
  void theLastElementOfTheFirstHalfIsNotCounted(StdOut out) throws IOException {
    // The spike sits at A_(N/2), the far side of the split: a sum that starts one element early
    // drags the 95 in and prints 97.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"4", "1 1 96 1"})
  void theFirstElementOfTheSecondHalfIsCounted(StdOut out) throws IOException {
    // The spike sits at A_(N/2)+1, the first element owed: a sum that starts one element late
    // drops the 96 and prints 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("97");
  }

  @Test
  @StdIo({"4", "1 1 1 97"})
  void theVeryLastElementIsCounted(StdOut out) throws IOException {
    // The spike moves to A_N: a loop that stops at N - 1 prints 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("98");
  }

  @Test
  @StdIo({"6", "10 20 30 40 50 60"})
  void allDistinctValuesPinTheSplitOfSix(StdOut out) throws IOException {
    // Every misread lands on its own number: the first half sums to 60, all of A to 210, and each
    // off-by-one split shifts the answer by a different amount.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("150");
  }

  // --- N and the values at their ceilings. ---

  @Test
  @StdIo({"100", FIFTY_HUNDREDS + " " + FIFTY_HUNDREDS})
  void theLargestCaseSumsFiftyHundreds(StdOut out) throws IOException {
    // N = 100 with every A_i = 100: the answer at its ceiling of 50 x 100.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5000");
  }

  @Test
  @StdIo({"100", FIFTY_HUNDREDS + " " + FIFTY_ONES})
  void aCeilingCaseIgnoresTheLargeValuesInItsFirstHalf(StdOut out) throws IOException {
    // Fifty 100s ahead of fifty 1s: the wrong half prints 5000, all of A prints 5050.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }
}
