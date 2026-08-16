package atcoder.abc471.a;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 471 A -- Nine or Nein.
 *
 * <p>One line holds two integers A and B (1 ≤ A ≤ 100, 1 ≤ B ≤ 100). Print {@code Nine} when at
 * least one of A+B, A-B, A×B, A÷B equals 9, and {@code Nein} otherwise.
 *
 * <p>Two of the four operations are easy to get wrong. A-B is signed and is never reordered, so a
 * pair whose difference is -9 must still answer {@code Nein}. A÷B is exact rational division, not
 * the truncating quotient Java's {@code /} gives on ints, so A÷B = 9 means A = 9B and nothing else
 * -- 19 ÷ 2 is 9.5, not 9. The remaining trap is the input itself: a literal 9 among the operands
 * says nothing about the answer, which is why the official samples end on 9 9.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo("16 7")
  void officialSampleOneMatchesOnTheDifference(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("66 7")
  void officialSampleTwoMatchesOnNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("9 1")
  void officialSampleThreeMatchesOnTheProductAndTheQuotientAtOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("9 9")
  void officialSampleFourShowsThatANineInTheInputIsNotItselfAMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  // --- One operation at a time: each pair below is answered by a single operation, so
  // dropping that operation from the check flips exactly this test. ---

  @Test
  @StdIo("8 1")
  void sumAloneCarriesTheMatch(StdOut out) throws IOException {
    // 8+1 = 9, while 7, 8 and 8 miss.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("1 8")
  void sumStillCarriesTheMatchWhenTheDifferenceGoesNegative(StdOut out) throws IOException {
    // 1+8 = 9 with A-B = -7, so the sum must be judged on its own terms.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("3 3")
  void productAloneCarriesTheMatch(StdOut out) throws IOException {
    // 3×3 = 9, while 6, 0 and 1 miss.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("18 2")
  void quotientAloneCarriesTheMatch(StdOut out) throws IOException {
    // 18÷2 = 9, while 20, 16 and 36 miss.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  // --- The difference is signed and keeps its operand order: B-A = 9 is not a match. ---

  @Test
  @StdIo("1 10")
  void differenceOfNegativeNineIsNotAMatch(StdOut out) throws IOException {
    // |A-B| = 9 would wrongly answer Nine here.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("91 100")
  void differenceOfNegativeNineIsNotAMatchAtTheCeilingEither(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("100 91")
  void differenceMatchesAtTheCeiling(StdOut out) throws IOException {
    // The same two operands the other way round: 100-91 = 9.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  // --- The quotient is exact: A÷B = 9 holds only when A = 9B, so every pair whose
  // truncated quotient is 9 without dividing evenly must answer Nein. ---

  @Test
  @StdIo("19 2")
  void quotientOfNinePointFiveIsNotAMatch(StdOut out) throws IOException {
    // A bare A/B == 9 on ints truncates 9.5 to 9 and wrongly answers Nine.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("99 10")
  void quotientJustShortOfTenIsNotAMatch(StdOut out) throws IOException {
    // 99÷10 = 9.9 truncates to 9.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("90 10")
  void quotientThatDividesEvenlyIsAMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("99 11")
  void largestExactQuotientInRangeIsAMatch(StdOut out) throws IOException {
    // 9×11 = 99 is the last multiple of nine that fits A ≤ 100.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nine");
  }

  @Test
  @StdIo("100 11")
  void oneStepPastTheLargestExactQuotientIsNotAMatch(StdOut out) throws IOException {
    // 100÷11 truncates to 9 with a remainder of 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("2 18")
  void quotientKeepsItsOperandOrder(StdOut out) throws IOException {
    // 2÷18 = 1/9; only the reversed B÷A would see a nine here.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  // --- The four corners of the constraint box, none of which reaches nine. ---

  @Test
  @StdIo("1 1")
  void smallestOperandsMatchNothing(StdOut out) throws IOException {
    // 2, 0, 1, 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("100 100")
  void largestOperandsMatchNothing(StdOut out) throws IOException {
    // 200, 0, 10000, 1 -- the widest sum and product the constraints allow.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("100 1")
  void widestSpreadMatchesNothing(StdOut out) throws IOException {
    // 101, 99, 100, 100 -- the largest difference and quotient available.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }

  @Test
  @StdIo("1 100")
  void widestReversedSpreadMatchesNothing(StdOut out) throws IOException {
    // 101, -99, 100, 1/100 -- the most negative difference available.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Nein");
  }
}
