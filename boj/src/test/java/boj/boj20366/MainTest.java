package boj.boj20366;

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

/** BOJ 20366 같이 눈사람 만들래? (Want to build a snowman?). */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // Diameters [3,5,2,5,9]: snowmen {2,5}=7 and {3,5}=8 differ by 1; no four distinct balls tie ->
  // 1.
  @Test
  @StdIo({"5", "3 5 2 5 9"})
  void officialSampleMinimumDifferenceIsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Smallest input, N = 4: all four balls are used, leaving only three pairings. ---

  // [1,2,3,4]: the pairing {1,4} vs {2,3} gives 5 = 5 -- an exact tie, so 0.
  @Test
  @StdIo({"4", "1 2 3 4"})
  void fourBallsCanFormAnExactTie(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [1,2,3,5]: the three pairings give |3-8|=5, |4-7|=3, |6-5|=1; closest is {1,5} vs {2,3} -> 1.
  @Test
  @StdIo({"4", "1 2 3 5"})
  void fourBallsWithNoTieGiveSmallestGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Distinctness: the four balls need four distinct indices, so two equal-height snowmen that
  //     would have to share a ball are not a valid pairing. ---

  // [10,1,1,1000]: snowmen {10,1}=11 and {10,1}=11 tie, but both need the lone 10 and so cannot
  // coexist. The valid pairings are |11-1001|=990, |11-1001|=990, |1010-2|=1008 -> 990, not 0.
  @Test
  @StdIo({"4", "10 1 1 1000"})
  void equalHeightSnowmenSharingABallAreNotAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("990");
  }

  // --- Duplicate and minimum-value diameters. ---

  // Four identical minimum diameters: every pairing yields 2 vs 2 -> 0. Also exercises the lower
  // value bound, Hᵢ = 1.
  @Test
  @StdIo({"4", "1 1 1 1"})
  void minimumDiameterAllOnesGiveZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- More balls than needed: the search must consider subsets, not just use every ball. ---

  // [1,2,3,4,100]: ignoring the oversized 100, {1,4} vs {2,3} ties at 5 -> 0.
  @Test
  @StdIo({"5", "1 2 3 4 100"})
  void oversizedBallIsIgnoredWhenFourOthersTie(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Unsorted input: a correct solution sorts (or otherwise does not assume input order). ---

  // Unsorted [8,10,3,1,50]: {10,1}=11 and {8,3}=11 tie; the 50 is irrelevant -> 0.
  @Test
  @StdIo({"5", "8 10 3 1 50"})
  void unsortedInputStillFindsTheTie(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Unsorted [100,1,2,4] with no tie: sorted [1,2,4,100], best pairing {1,100} vs {2,4} -> |101-6|
  // = 95.
  @Test
  @StdIo({"4", "100 1 2 4"})
  void unsortedInputWithoutTieGivesCorrectGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("95");
  }

  // --- A larger set where the closest two snowmen span non-adjacent balls. ---

  // [1,2,4,8,16,32]: the nearest two disjoint snowmen are {2,4}=6 and {1,8}=9 -> 3.
  @Test
  @StdIo({"6", "1 2 4 8 16 32"})
  void closestSnowmenAcrossSixBalls(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Maximum diameters: values pressed against the 1,000,000,000 ceiling. ---

  // [1e9,1e9,1,1]: {1e9,1} and {1e9,1} use both big balls and both small balls -> 1000000001 each,
  // 0.
  // The rival pairing {1e9,1e9} vs {1,1} reaches a sum of 2,000,000,000, exercising large
  // magnitudes.
  @Test
  @StdIo({"4", "1000000000 1000000000 1 1"})
  void maximumDiametersFormAnExactTie(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [1,4,999999999,1000000000]: best pairing {1,1e9} vs {4,999999999} -> |1000000001-1000000003| =
  // 2.
  @Test
  @StdIo({"4", "1 4 999999999 1000000000"})
  void nearMaximumDiametersSmallestGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Upper size bound, N = 600: the intended solution must finish well within the time limit.
  // ---

  // A 600-element Erdős–Turán Sidon set (all pairwise sums distinct), so no two snowmen ever tie
  // and
  // the answer is the smallest gap between distinct snowman heights built from four distinct balls:
  // 1.
  // Because the answer is never 0, an O(N⁴) search cannot short-circuit and would exceed the
  // timeout,
  // whereas the intended O(N²·log N) (or two-pointer O(N³)) approach returns quickly.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maximumSizeSidonSetFinishesInTime() throws IOException {
    assertThat(runMain(sidonInput())).isEqualTo("1");
  }

  /**
   * Builds the largest fixture: N = 600 diameters from the Erdős–Turán construction {@code 2·P·i +
   * (i² mod P) + 1} with prime {@code P = 601}. All pairwise sums are distinct, so the minimum
   * height difference is a fixed, nonzero 1, and the maximum diameter (720,003) stays within the
   * bound.
   */
  private static String sidonInput() {
    int n = 600;
    int p = 601;
    StringBuilder sb = new StringBuilder().append(n).append('\n');
    for (int i = 0; i < n; i++) {
      long h = 2L * p * i + (long) i * i % p + 1;
      sb.append(h);
      sb.append(i < n - 1 ? ' ' : '\n');
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
