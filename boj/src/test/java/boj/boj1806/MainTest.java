package boj.boj1806;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1806 부분합 (Subarray Sum)
 */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // [5,1,3,5,10,7,4,9,2,8], S=15: no single element reaches 15, but 5+10=15 and 10+7=17 -> 2.
  @Test
  @StdIo({"10 15", "5 1 3 5 10 7 4 9 2 8"})
  void officialSampleShortestQualifyingWindowIsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Impossibility: when the total cannot reach S, the answer is 0. ---

  // Ten 1s total 10, far below S=100, so no window can ever qualify -> 0.
  @Test
  @StdIo({"10 100", "1 1 1 1 1 1 1 1 1 1"})
  void totalSumBelowTargetIsImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Extreme answer lengths: 1 (single element), N (whole array), and a mid-range window. ---

  // A lone 10,000 meets S=10,000 by itself, the shortest possible window -> 1.
  @Test
  @StdIo({"10 10000", "2 2 2 2 10000 2 2 2 2 2"})
  void singleElementMeetingTargetGivesLengthOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Ten 1s sum to exactly S=10 only when all are taken; any nine sum to 9 < 10 -> 10.
  @Test
  @StdIo({"10 10", "1 1 1 1 1 1 1 1 1 1"})
  void wholeArrayIsTheOnlyQualifyingWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Ten 5s, S=30: six elements reach 30 while five only reach 25, so the answer is neither 1 nor N.
  @Test
  @StdIo({"10 30", "5 5 5 5 5 5 5 5 5 5"})
  void uniformArrayNeedsAMidRangeWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Threshold semantics: ">= S" is inclusive, and "S - 1" must not be accepted. ---

  // S=1 is its minimum, and every element is a natural number >= 1, so the first element alone -> 1.
  @Test
  @StdIo({"10 1", "1 1 1 1 1 1 1 1 1 1"})
  void targetOfOneIsMetByASingleElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A lone 19 falls one short of S=20 and must not count; only 19+1=20 qualifies -> 2.
  @Test
  @StdIo({"10 20", "19 1 1 1 1 1 1 1 1 1"})
  void valueOneShortOfTargetNeedsAnotherElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Window position: the qualifying window can sit at either edge of the array. ---

  // [10,9,...], S=19: the prefix 10+9=19 hits S exactly at the start (confirms ">=" is inclusive)
  // and no single element reaches 19 -> 2.
  @Test
  @StdIo({"10 19", "10 9 1 1 1 1 1 1 1 1"})
  void qualifyingWindowAtTheStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // [...,9,10], S=19: the only qualifying window is the suffix 9+10=19, so the right pointer must
  // reach the final element -> 2.
  @Test
  @StdIo({"10 19", "1 1 1 1 1 1 1 1 9 10"})
  void qualifyingWindowAtTheEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Algorithm correctness: minimize over all windows, and respect contiguity. ---

  // [4,3,3,1,1,1,1,5,5,1], S=10: an early window 4+3+3=10 has length 3, but the later 5+5=10 has
  // length 2. The answer must be the global minimum 2, not the first window found.
  @Test
  @StdIo({"10 10", "4 3 3 1 1 1 1 5 5 1"})
  void shorterLaterWindowBeatsLongerEarlierWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // [10,1,1,1,1,1,1,1,1,10], S=20: the two 10s sit at opposite ends. A subset/sort approach would
  // pair them for length 2, but contiguously every window short of the full array drops a 10 and
  // tops out at 18, so only the entire array reaches 20 -> 10.
  @Test
  @StdIo({"10 20", "10 1 1 1 1 1 1 1 1 10"})
  void contiguityMattersWhenLargestElementsAreNotAdjacent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Upper size bound, N = 99,999: must run in linear time and not overflow int. ---

  // 99,999 ones with S=50,000: exactly 50,000 consecutive ones are needed -> 50000. A quadratic
  // scan would blow the time limit; the intended two-pointer sweep returns quickly.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maxSizeArrayFindsAnswerWithinTimeLimit() throws IOException {
    assertThat(runMain(maxLengthInput(1, 50_000))).isEqualTo("50000");
  }

  // 99,999 copies of 10,000 with S=100,000,000 (the maximum S): the running total can climb to
  // 999,990,000 -- the largest the constraints allow and still inside signed-int range -- while the
  // answer is the first 10,000 elements summing to exactly 100,000,000 -> 10000.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maxTotalSumAtIntBoundary() throws IOException {
    assertThat(runMain(maxLengthInput(10_000, 100_000_000))).isEqualTo("10000");
  }

  // 99,999 ones with S=100,000,000: the total 99,999 never reaches S, so the right pointer scans the
  // whole array and the answer is 0. Exercises the impossible branch at maximum scale.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void impossibleAtMaxScaleScansEntireArray() throws IOException {
    assertThat(runMain(maxLengthInput(1, 100_000_000))).isEqualTo("0");
  }

  private static final int MAX_N = 99_999;

  /** Builds a maximum-length (N = 99,999) input: line "N S", then N copies of {@code value}. */
  private static String maxLengthInput(int value, int s) {
    StringBuilder sb = new StringBuilder().append(MAX_N).append(' ').append(s).append('\n');
    for (int i = 0; i < MAX_N; i++) {
      sb.append(value);
      sb.append(i < MAX_N - 1 ? ' ' : '\n');
    }
    return sb.toString();
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
