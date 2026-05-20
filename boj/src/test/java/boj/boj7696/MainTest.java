package boj.boj7696;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 7696 반복하지 않는 수 (Non-repeating numbers).
 */
class MainTest {

  // --- The sentinel value 0 terminates input immediately and must not produce any output. ---

  @Test
  @StdIo({"0"})
  void terminatorOnlyInputProducesNoOutput(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString()).isEmpty();
  }

  // --- The first non-repeating positive integer is 1 itself. ---

  @Test
  @StdIo({"1", "0"})
  void firstIndexMapsToOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Every single-digit number trivially has unique digits, so idx 9 is the last single-digit
  //     value before any skipping can occur. ---

  @Test
  @StdIo({"9", "0"})
  void lastSingleDigitIndexMapsToNine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- 10 has digits {1,0} with no repeats, so idx 10 is the first two-digit element. A common
  //     bug — treating the leading "10" as "1 followed by 0" and rejecting it — would skip past
  //     this and answer 12. ---

  @Test
  @StdIo({"10", "0"})
  void firstTwoDigitIndexMapsToTenAndDoesNotRejectTheZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- 11 is the first integer with a repeated digit and must be skipped, so idx 11 lands on 12.
  //     An implementation that forgot to skip would print 11. ---

  @Test
  @StdIo({"11", "0"})
  void firstRepeatedDigitNumberIsSkippedSoIndexElevenIsTwelve(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- 22 is the second skipped value. After 1-9 (9 entries), then 10,12..19 (9 entries), then
  //     20,21 (2 entries) we land at idx 21, which is 23. This catches an off-by-one in the
  //     decade transition just like the 11→12 case but at a different decade. ---

  @Test
  @StdIo({"21", "0"})
  void doubleTwoIsSkippedSoIndexTwentyOneIsTwentyThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("23");
  }

  // --- There are exactly 9 single-digit + 81 two-digit (9 leading × 9 trailing-distinct) =
  //     90 non-repeating values up through 98, so idx 90 = 98. ---

  @Test
  @StdIo({"90", "0"})
  void ninetiethIndexIsTheLastTwoDigitValueNinetyEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("98");
  }

  // --- Right after idx 90 = 98 the next four candidates 99, 100, 101 are all skipped (digit 9
  //     repeats; digit 0 repeats; digit 1 repeats), and 102 is the next valid value. This is the
  //     2-digit → 3-digit boundary test: an enumeration that resets state per digit-length, or
  //     that accepts 100/101, would land on the wrong value here. ---

  @Test
  @StdIo({"91", "0"})
  void crossingIntoThreeDigitsSkipsNinetyNineHundredAndOneHundredOne(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("102");
  }

  // --- After idx 98 = 109, the entire block 110..119 is skipped because every one of those
  //     numbers repeats the leading 1. The next valid value is 120, so idx 99 = 120. This makes
  //     sure a contiguous run of rejections is handled correctly, not just isolated ones. ---

  @Test
  @StdIo({"99", "0"})
  void entireOneHundredTensBlockIsSkippedSoIndexNinetyNineIsOneHundredTwenty(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("120");
  }

  // --- After idx 99 = 120, the candidates 121 and 122 both repeat a digit, so 123 follows.
  //     Combined with the previous test this fixes the entire shape of the 100s transition. ---

  @Test
  @StdIo({"100", "0"})
  void indexOneHundredIsOneHundredTwentyThreeAfterSkippingOneTwoOneAndOneTwoTwo(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("123");
  }

  // --- Multiple queries in a single run must each be answered, in order, on their own line, and
  //     the final 0 must terminate without printing anything. A loop that processed only the first
  //     query, or that printed the terminator, would fail here. ---

  @Test
  @StdIo({"1", "11", "91", "0"})
  void severalQueriesInOneSessionAreAnsweredInOrderAndZeroTerminatesSilently(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("1", "12", "102");
  }
}
