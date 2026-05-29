package boj.boj23032;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 23032 서프라이즈~ (Surprise).
 *
 * <p>Given N students with steak-weight requests W[1..N], pick a contiguous range [s..e] and a
 * split point mid (s &le; mid &lt; e) so that the two groups G1 = W[s..mid] and G2 = W[mid+1..e]
 * are each non-empty and contiguous. Let E = |sum(G1) - sum(G2)|. Among all (s, mid, e) triples,
 * minimize E; on ties take the configuration with the largest total sum(G1) + sum(G2). Print that
 * total. Constraints: 2 &le; N &le; 2,000 and 1 &le; W &le; 10,000.
 */
class MainTest {

  // --- Official sample worked through the problem statement: W = [2, 1, 5, 2, 4, 4]. The
  // optimum picks students 2..6 split between 4 and 5: G1 = (1, 5, 2) = 8 and G2 = (4, 4) = 8,
  // so E = 0 and total = 16. Pins the worked example and the "prints total, not difference"
  // contract. ---

  @Test
  @StdIo({"6", "2 1 5 2 4 4"})
  void officialSample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Minimum N = 2 with equal weights: only one possible configuration ({w1} vs {w2}),
  // E = 0, total = 2. Pins the smallest legal input and the "groups must each be non-empty"
  // base case. ---

  @Test
  @StdIo({"2", "1 1"})
  void minimumSizeEqualWeights(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Minimum N = 2 with unequal weights: only one configuration ({3} vs {7}), E = 4, total
  // = 10. Confirms the implementation does not crash or return 0 when no zero-diff split
  // exists. ---

  @Test
  @StdIo({"2", "3 7"})
  void minimumSizeUnequalWeightsForcedSplit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Minimum N = 2 at the extreme value range: W in {1, 10000}. The only configuration
  // gives diff 9999 and total 10001. Pins both ends of the per-student value range. ---

  @Test
  @StdIo({"2", "1 10000"})
  void minimumSizeMaxValueGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10001");
  }

  // --- N = 2 with both at the maximum value 10000: E = 0, total = 20000. Confirms the
  // optimum at the maximum per-cell value. ---

  @Test
  @StdIo({"2", "10000 10000"})
  void minimumSizeBothMaxValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20000");
  }

  // --- All equal weights with EVEN N: the whole array splits perfectly in the middle for
  // E = 0, and that whole-array total is the largest E = 0 total. W = [1,1,1,1,1,1] -> split
  // 1..3 | 4..6, total 6. Guards against a solution that picks a smaller even subrange and
  // forgets to keep growing. ---

  @Test
  @StdIo({"6", "1 1 1 1 1 1"})
  void allEqualEvenLengthWholeArrayWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- All equal weights with ODD N: the full array cannot reach E = 0 (best E = 1), but
  // the largest even subrange (length 4) does -> total 4. Forces the solution to recognize
  // that an INTERIOR subrange can strictly beat the full array on E. A solution that always
  // takes the whole array would report total 5 with E = 1. ---

  @Test
  @StdIo({"5", "1 1 1 1 1"})
  void allEqualOddLengthSubrangeBeatsFullArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Clear tiebreaker: W = [1, 1, 2, 2] gives E = 0 from three places -- {1,1} (total 2),
  // 1..3 split 2|2 (total 4), and {2,2} (total 4) -- and the rule selects the maximum total
  // among those: 4. A solution that returns the FIRST E = 0 it sees (total 2) fails. ---

  @Test
  @StdIo({"4", "1 1 2 2"})
  void tiebreakSelectsMaximumTotalAmongZeroDiff(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Optimum is a strict INTERIOR subrange. W = [10, 1, 1, 1] has best E = 0 only from
  // {1,1} starting at index 2 or 3, total 2. The whole-array best is E = 7 (10 | 3). A
  // solution that fixates on the full array reports 13 with the wrong E; one that anchors at
  // index 1 cannot reach this optimum. ---

  @Test
  @StdIo({"4", "10 1 1 1"})
  void interiorSubrangeAchievesMinimumNotWholeArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Symmetric heavy-ends array W = [100, 1, 1, 100]: the full array splits 1..2 | 3..4
  // as 101|101 with E = 0 and total 202; the subrange {1,1} also gives E = 0 but total 2.
  // Tiebreak picks the FULL array's 202. Distinguishes "prefer interior" bugs (which would
  // wrongly pick 2) from a correct "max total at min diff" choice. Companion to the
  // interior-only case above. ---

  @Test
  @StdIo({"4", "100 1 1 100"})
  void tiebreakChoosesWholeArrayOverInteriorWhenBothReachZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("202");
  }

  // --- Strict subrange beats the whole array on E, NOT a tie. W = [3, 1, 2, 2, 1]:
  // -- whole 1..5 best is E = 1 (totals 9, from mid = 2: 4|5)
  // -- range 1..4 split {3,1}|{2,2} = 4|4 gives E = 0 total 8 (the winner)
  // -- range 1..3 split {3}|{1,2} = 3|3 also gives E = 0 total 6
  // -- range 2..5 split {1,2}|{2,1} = 3|3 also gives E = 0 total 6
  // -- range 3..4 split {2}|{2} = 2|2 also gives E = 0 total 4
  // E = 0 strictly beats E = 1, so the answer is the maximum total among the E = 0 set,
  // which is 8 -- and that is strictly less than the whole-array total 9. The tie-breaker on
  // total applies WITHIN the min-E set, not across it. ---

  @Test
  @StdIo({"5", "3 1 2 2 1"})
  void smallerDiffOnSubrangeBeatsLargerTotalOnWholeArray(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- The optimum requires the two-pointer to extend the RIGHT side past the natural
  // anchor. W = [10, 5, 1, 2, 8] with anchor mid = 4 yields the only E = 0 configuration:
  // range 2..5 split {5,1,2}|{8} = 8|8 total 16. A solution that only walks LEFT from each
  // mid, or that never grows the right group while the left is already heavier, would miss
  // this. ---

  @Test
  @StdIo({"5", "10 5 1 2 8"})
  void requiresAsymmetricRightExtensionFromAnchor(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- The optimum requires the two-pointer to extend the LEFT side past the natural
  // anchor (mirror of the right-extension case). W = [8, 2, 1, 5, 10] with anchor mid = 1
  // gives range 1..4 split {8}|{2,1,5} = 8|8 total 16. Pins the symmetric mirror behavior so
  // a solution that only handles right-growth would fail. ---

  @Test
  @StdIo({"5", "8 2 1 5 10"})
  void requiresAsymmetricLeftExtensionFromAnchor(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Best diff is non-zero and the configuration is unique. W = [3, 5, 2, 4]: the unique
  // best is range 2..4 split {5}|{2,4} = 5|6, E = 1, total 11. Confirms the solution does
  // NOT special-case "E must be 0" and correctly returns the total on a strictly positive
  // minimum. ---

  @Test
  @StdIo({"4", "3 5 2 4"})
  void minimumIsPositiveNotZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Strictly increasing weights with a unique E = 0 attained on a tiny PREFIX subrange.
  // W = [1, 2, 3, 4, 5]: the only E = 0 configuration is range 1..3 split {1,2}|{3} = 3|3,
  // total 6. Every longer subrange's best diff is > 0 (e.g. whole array's best is E = 3, 6|9
  // at mid = 3). A solution that always grows toward longer subranges expecting larger
  // totals would miss this prefix-only optimum. ---

  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void strictlyIncreasingPrefixSubrangeWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- All maximum weights at maximum N stresses the I/O path and the O(N^2) algorithm at
  // its upper bound. N = 2000 of W = 10000: total of full array = 20,000,000, split 1..1000
  // | 1001..2000 each 10,000,000 -> E = 0 wins with the maximum possible total. A 32-bit
  // accumulator would still survive here (2*10^7 fits), but the perf test exists to flag any
  // accidental O(N^3) solution. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeMaxInputAllMaxWeightsRunsFast() throws IOException {
    assertThat(runMain(uniformInput(2000, 10000))).isEqualTo("20000000");
  }

  // --- Minimum weights at maximum N: N = 2000 all 1s. Same shape as the all-equal even-N
  // case but at scale -- whole array split in the middle gives E = 0 total 2000. Exercises
  // the algorithm at the upper N bound with the smallest per-cell value. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeMaxInputAllOnesRunsFast() throws IOException {
    assertThat(runMain(uniformInput(2000, 1))).isEqualTo("2000");
  }

  // --- Maximum N with strictly-alternating values forces lots of two-pointer movement.
  // N = 2000 of the pattern 1, 10000, 1, 10000, ...: every adjacent pair is (1, 10000) with
  // diff 9999, but any window of two same-parity adjacent students includes both values
  // matched. Brute force on N = 2000 is too slow, so we use the closed-form total here:
  // the array sums to 1000 * 10001 = 10,001,000; split 1..1000 | 1001..2000 gives
  // 500 * 10001 each, E = 0 -> total 10,001,000. Pins the perf bound with non-uniform input.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAlternatingInputRunsFast() throws IOException {
    assertThat(runMain(alternatingInput(2000, 1, 10000))).isEqualTo("10001000");
  }

  // --- Randomized cross-check against the trusted O(N^3) brute force. Small N (2..15) and a
  // moderate value range (1..30) make zero-diff ties, single-config minima, interior-wins,
  // and prefix/suffix-wins cases all common, exercising the algorithm across the full
  // (s, mid, e) decision space. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(23032);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(14); // 2..15
      int[] w = new int[n];
      for (int i = 0; i < n; i++) {
        w[i] = 1 + rnd.nextInt(30); // 1..30
      }
      String input = inputFor(w);
      String expected = Integer.toString(bruteForce(w));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference O(N^3) selection: enumerate every (s, mid, e) with 1 <= s <= mid < e <= N,
  // compute |sum(s..mid) - sum(mid+1..e)| as E, and track (min E, max total at min E).
  // Obviously correct but cubic; trustworthy only for the tiny N used by the oracle.
  private static int bruteForce(int[] w) {
    int n = w.length;
    int[] prefix = new int[n + 1];
    for (int i = 0; i < n; i++) {
      prefix[i + 1] = prefix[i] + w[i];
    }
    int bestDiff = Integer.MAX_VALUE;
    int bestTotal = 0;
    for (int s = 1; s <= n; s++) {
      for (int mid = s; mid < n; mid++) {
        for (int e = mid + 1; e <= n; e++) {
          int left = prefix[mid] - prefix[s - 1];
          int right = prefix[e] - prefix[mid];
          int diff = Math.abs(left - right);
          int total = left + right;
          if (diff < bestDiff || (diff == bestDiff && total > bestTotal)) {
            bestDiff = diff;
            bestTotal = total;
          }
        }
      }
    }
    return bestTotal;
  }

  private static String inputFor(int[] w) {
    StringBuilder sb = new StringBuilder();
    sb.append(w.length).append('\n');
    for (int i = 0; i < w.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(w[i]);
    }
    return sb.append('\n').toString();
  }

  private static String uniformInput(int n, int value) {
    StringBuilder sb = new StringBuilder(n * 6 + 16);
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(value);
    }
    return sb.append('\n').toString();
  }

  // N values alternating a, b, a, b, ... starting with a at index 0.
  private static String alternatingInput(int n, int a, int b) {
    StringBuilder sb = new StringBuilder(n * 6 + 16);
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append((i & 1) == 0 ? a : b);
    }
    return sb.append('\n').toString();
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
