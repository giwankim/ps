package boj.boj2003;

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

/** BOJ 2003 수들의 합 2 (Sum of numbers 2): count contiguous subarrays of positives summing to M. */
class MainTest {

  // --- Official samples. Anchor the suite against the published examples. ---

  // [1,1,1,1], M=2: the windows (1,2), (2,3), (3,4) each sum to 2 -> 3.
  @Test
  @StdIo({"4 2", "1 1 1 1"})
  void officialSampleFourOnesTargetTwoCountsThreeWindows(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // [1,2,3,4,2,5,3,1,1,2], M=5: windows [2,3], [5], [3,1,1] -> 3 (verified via prefix sums).
  @Test
  @StdIo({"10 5", "1 2 3 4 2 5 3 1 1 2"})
  void officialSampleMixedSequenceCountsThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Single element: the smallest input, n = 1. ---

  // The lone element equals M, so the only window [5] qualifies.
  @Test
  @StdIo({"1 5", "5"})
  void singleElementEqualToTargetCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The lone element is below M and cannot grow (no more elements) -> 0.
  @Test
  @StdIo({"1 5", "3"})
  void singleElementBelowTargetCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The lone element exceeds M; the window cannot shrink below one element, so it stays > M
  // and is never counted. Exercises the "current > m" branch when the window is a single item.
  @Test
  @StdIo({"1 5", "10"})
  void singleElementAboveTargetCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Whole-array and no-match cases. ---

  // Only the entire array [1,2,2] reaches M=5; every proper sub-window falls short.
  @Test
  @StdIo({"3 5", "1 2 2"})
  void wholeArrayIsTheOnlyWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [2,2,2], M=5: window sums are 2, 4, 6, 2, 4, 2 -- M is skipped over -> 0.
  @Test
  @StdIo({"3 5", "2 2 2"})
  void noContiguousWindowSumsToTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // M=100 is larger than the total sum 1+2+3=6, so no window can ever reach it -> 0.
  @Test
  @StdIo({"3 100", "1 2 3"})
  void targetExceedsTotalSumCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Counting semantics: standalone vs multi-element windows, repeats. ---

  // [5,2,3,1], M=5: a single element [5] and a multi-element window [2,3] both count -> 2.
  @Test
  @StdIo({"4 5", "5 2 3 1"})
  void standaloneElementAndMultiElementWindowBothCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // [2,2,2,2,2], M=6: the three sliding triples (0-2), (1-3), (2-4) each sum to 6 -> 3.
  @Test
  @StdIo({"5 6", "2 2 2 2 2"})
  void adjacentEqualWindowsAreCountedSeparately(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Oversized elements: the window must shed values that overshoot M. ---

  // [10,2,3], M=5: the leading 10 overshoots and is shed once 2 is added, exposing [2,3] -> 1.
  @Test
  @StdIo({"3 5", "10 2 3"})
  void largeLeadingElementIsShedBeforeValidWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [2,3,10], M=5: [2,3] is counted, then a trailing 10 forces a full shrink without a
  // spurious extra count -> 1.
  @Test
  @StdIo({"3 5", "2 3 10"})
  void largeTrailingElementDoesNotBreakCounting(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Boundary of M and of the element values. ---

  // M=1 (its minimum): only the single unit element [1], wedged between larger numbers, hits it.
  @Test
  @StdIo({"3 1", "3 1 2"})
  void targetOfOneFindsTheLoneUnitElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Largest element value (30,000) twice; only the full window [30000,30000] reaches M=60,000.
  @Test
  @StdIo({"2 60000", "30000 30000"})
  void maxElementValuesFormASingleWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Upper size bound, N = 10,000: must stay linear and not overflow. ---

  // 10,000 ones with M=2: every adjacent pair sums to 2, giving N-1 = 9,999 windows.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void tenThousandOnesTargetTwoCountsEveryAdjacentPair() throws IOException {
    assertThat(runMain(maxLengthInput(1, 2))).isEqualTo("9999");
  }

  // 10,000 copies of 30,000 with M=300,000,000: the running sum reaches exactly 30,000 * 10,000
  // = 300,000,000, the largest total the constraints allow and still within int range. Only the
  // whole array hits M -> 1; proves the accumulator does not overflow at the ceiling.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maximumTotalSumAtIntBoundaryCountsWholeArray() throws IOException {
    assertThat(runMain(maxLengthInput(30000, 300_000_000))).isEqualTo("1");
  }

  private static final int MAX_N = 10_000;

  /** Builds a maximum-length (N = 10,000) input: line "N M", then N copies of {@code value}. */
  private static String maxLengthInput(int value, int m) {
    StringBuilder sb = new StringBuilder().append(MAX_N).append(' ').append(m).append('\n');
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
