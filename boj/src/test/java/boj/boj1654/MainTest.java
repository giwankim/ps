package boj.boj1654;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1654 랜선 자르기 (Cutting LAN Cables) -- another "binary search on the answer" (parametric
 * search), the cutting cousin of 2805's tree-felling.
 *
 * <p>Oh Young-sik owns {@code K} LAN cables of assorted lengths and must produce {@code N} cables
 * of one common length by cutting the {@code K} he has (already-cut pieces cannot be rejoined, and
 * the offcut is wasted -- a 300cm cable yields two 140cm pieces and throws away 20cm). Find the
 * <em>maximum</em> integer length at which he can make at least {@code N} pieces. A cut length
 * {@code L} turns a cable of length {@code c} into {@code floor(c / L)} pieces, so the total at
 * length {@code L} is {@code count(L) = sum of floor(c / L)} over the {@code K} cables, and the
 * answer is the largest {@code L} with {@code count(L) >= N}. Because {@code count} is
 * monotonically non-increasing in {@code L}, the feasible lengths form a downward-closed range
 * {@code [1, answer]}.
 *
 * <p>Input: line 1 is {@code "K N"} -- the cable count {@code K} ({@code 1 <= K <= 10,000}) and the
 * required piece count {@code N} ({@code 1 <= N <= 1,000,000}), with {@code K <= N} always. The
 * next {@code K} lines each hold one cable length, a natural number up to {@code 2^31 - 1} (i.e.
 * exactly {@link Integer#MAX_VALUE}). It is guaranteed the {@code K} cables can always make
 * {@code N} pieces, so an answer of at least {@code 1} always exists. Output: the single maximum
 * cut length in centimeters. (Spec triangulated across blog mirrors of the statement while
 * acmicpc.net was unreachable; the published sample {@code 4 11 / 802 743 457 539 -> 200}
 * corroborates it -- at {@code L = 200} the cuts are {@code 4 + 3 + 2 + 2 = 11 >= 11}, and at
 * {@code L = 201} they fall to {@code 3 + 3 + 2 + 2 = 10 < 11}.)
 *
 * <p>The fixtures pin the things a hasty parametric search gets wrong:
 *
 * <ul>
 *   <li><b>Maximize the length, don't stop at the first feasible one.</b> Feasible lengths form
 *       {@code [1, answer]}; the answer is its top, not the first length the search finds feasible
 *       ({@link #returnsTheMaximumFeasibleLengthNotTheFirst}).
 *   <li><b>Floor division -- the offcut is wasted.</b> A cable yields {@code floor(c / L)} whole
 *       pieces, never a rounded-up count ({@link #wasteIsDiscardedSoLengthUsesFloorDivision}), and
 *       a cable shorter than the cut simply yields zero, not an error or a negative
 *       ({@link #cablesShorterThanTheCutContributeZeroPieces}).
 *   <li><b>The answer may equal a cable's exact length</b> -- unlike 2805's blade, which never
 *       reaches the tallest tree; an exactly-divisible cable pays out and the cut can sit right on
 *       its length ({@link #answerCanEqualACablesExactLength}).
 *   <li><b>The search-space ends: {@code 1} at the bottom, the longest cable at the top.</b> When
 *       even doubling the cut would starve {@code N}, the answer bottoms out at {@code 1}
 *       ({@link #minimumFeasibleLengthIsOne}); a tiny {@code N} lets the cut rise to the longest
 *       cable itself ({@link #maxCableLengthNearIntMaxAvoidsMidpointOverflow}).
 *   <li><b>{@code N} may dwarf {@code K}</b> -- one long cable feeds many pieces
 *       ({@link #manyPiecesFromFewCables}).
 *   <li><b>The midpoint needs 64 bits.</b> The high end of the search is the longest cable, up to
 *       {@code Integer.MAX_VALUE}, so a naive {@code (lo + hi) / 2} overflows {@code int} -- on the
 *       first iteration when the longest cable is {@code 2^31 - 1}
 *       ({@link #maxCableLengthNearIntMaxAvoidsMidpointOverflow}), and even for cables below the
 *       {@code int} ceiling once {@code lo} climbs and {@code lo + hi} crosses it
 *       ({@link #midpointSumOverflowsIntEvenWhenEveryCableFitsInInt}). The piece-count accumulator,
 *       by contrast, stays bounded by roughly {@code 2N} at every probed length, so the genuine
 *       overflow trap is the midpoint, not the count.
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(K log maxLen)} search
 * (~31 sweeps of 10,000 cables) against an {@code O(maxLen * K)} linear walk of the billion-long
 * length range, and {@link #randomizedInputsMatchLinearScanOracle} cross-checks the judged output
 * against an obviously-correct top-down linear scan over candidate lengths. The {@code Main} under
 * test is an empty stub that reads nothing and prints nothing, so every assertion here is RED until
 * the parametric search is implemented.
 */
class MainTest {

  // --- The official sample from the statement: four cables, eleven pieces wanted. The end-to-end
  // smoke test, and already enough to pin two things -- that counts *sum across cables* (a solution
  // eyeing only the longest cable, 802, would answer floor(802/11)=72, not 200) and that the length
  // is *maximized* (L=199 is also feasible: 4+3+2+2=11, but the answer climbs to the top, 200). ---

  @Test
  @StdIo({"4 11", "802", "743", "457", "539"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  // --- The crux of parametric search: the answer is the *top* of the feasible range, not the first
  // feasible length met. cables {6,9}, N=3: lengths 1..4 are all feasible (L=3 -> 2+3=5 >= 3; L=4
  // ->
  // 1+2=3 >= 3) and the answer is the maximum of them, 4; L=5 -> 1+1=2 < 3. A search that returns
  // the midpoint where it first sees feasibility -- or returns `lo` past the boundary -- misprints.
  // ---

  @Test
  @StdIo({"2 3", "6", "9"})
  void returnsTheMaximumFeasibleLengthNotTheFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Floor division, offcut wasted: a single 7cm cable cut for N=2 pieces yields floor(7/L)
  // pieces. At L=3 that is 2 >= 2 (two 3cm pieces, 1cm thrown away); at L=4 it is 1 < 2. Answer 3.
  // A
  // solution that rounds up -- ceil(7/3)=3 would never under-count -- or that demands exact
  // division
  // misjudges the cut. ---

  @Test
  @StdIo({"1 2", "7"})
  void wasteIsDiscardedSoLengthUsesFloorDivision(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A cable shorter than the cut length yields zero pieces, never a negative (contrast 2805,
  // where a clamp-at-zero was needed because the model *subtracted*; here floor division gives 0
  // for
  // free, and this pins that short cables don't break the tally). cables {3,100}, N=10: at L=10 the
  // 3cm cable gives 0 and the 100cm gives 10 -> 10 >= 10; at L=11 it is 0 + 9 = 9 < 10. Answer 10.
  // ---

  @Test
  @StdIo({"2 10", "3", "100"})
  void cablesShorterThanTheCutContributeZeroPieces(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- The answer may land exactly on a cable's length: an evenly-divisible cable pays out and the
  // cut can sit right on it. cables {200,200}, N=2: at L=200 each gives 200/200 = 1 -> 2 >= 2; at
  // L=201 each gives 0 -> 0 < 2. Answer 200 == cable length. This is the opposite of 2805's blade,
  // which can never reach the tallest tree when M>0 -- pinning that 1654's upper bound is
  // *inclusive*
  // of a cable length. ---

  @Test
  @StdIo({"2 2", "200", "200"})
  void answerCanEqualACablesExactLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  // --- Lower boundary of the search space: when even a 2cm cut would starve N, the answer bottoms
  // out at 1. cable {3}, N=3: at L=1 -> 3 >= 3; at L=2 -> floor(3/2)=1 < 3. Answer 1. Pins that the
  // search floor is 1 (lengths are positive integers) and the answer can reach it -- a search that
  // undershoots to 0 or stops short at 2 misprints. ---

  @Test
  @StdIo({"1 3", "3"})
  void minimumFeasibleLengthIsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- N may dwarf K: one long cable is sliced into many pieces. cable {1000}, N=100: at L=10 ->
  // 1000/10 = 100 >= 100; at L=11 -> 1000/11 = 90 < 100. Answer 10 = floor(1000/100), the single-
  // cable closed form. Pins that the piece count scales far past K (here K=1, N=100) without
  // trouble.
  // ---

  @Test
  @StdIo({"1 100", "1000"})
  void manyPiecesFromFewCables(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Upper bound of the search space AND the first-iteration midpoint trap. The longest cable is
  // 2^31 - 1 == Integer.MAX_VALUE, so the search's high end starts there. With K=N=1 the lone cable
  // needs just one piece, so the answer is the whole cable, 2147483647 (floor(L/L)=1 >= 1; one cm
  // longer gives 0). A naive `int mid = (lo + hi) / 2` overflows on the *first* iteration -- 1 +
  // 2147483647 wraps to a negative, the cut goes nonsensical, and the search collapses to a wrong
  // (typically 0) answer. Passing requires a long midpoint or the overflow-safe `lo + (hi - lo)/2`.
  // ---

  @Test
  @StdIo({"1 1", "2147483647"})
  void maxCableLengthNearIntMaxAvoidsMidpointOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2147483647");
  }

  // --- The midpoint trap fires even when *every* value fits in int. cable {1500000000}, N=1: the
  // answer is the whole 1,500,000,000cm cable. The first probe (lo=1, hi=1.5e9) is safe, but once
  // it
  // proves feasible lo climbs to ~7.5e8 while hi stays at 1.5e9, and `lo + hi` ~ 2.25e9 overflows
  // the 2,147,483,647 int ceiling on the *second* iteration -- the cut wraps negative and the
  // search
  // settles on a too-small answer (~750,000,000). So long arithmetic is required for the sum even
  // though no single cable approaches Integer.MAX_VALUE. ---

  @Test
  @StdIo({"1 1", "1500000000"})
  void midpointSumOverflowsIntEvenWhenEveryCableFitsInInt(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1500000000");
  }

  // --- Upper bounds on K and N: 10,000 cables, each 1,000,000,000cm long, with N = 1,000,000 (the
  // maximum). A uniform forest gives the closed form count(L) = K * floor(maxLen / L); feasibility
  // count(L) >= N reduces to floor(1e9 / L) >= 100, so the largest feasible cut is floor(1e9 / 100)
  // = 10,000,000. The O(K log maxLen) search runs ~31 sweeps of 10,000 cables (~3e5 ops) where an
  // O(maxLen * K) walk of the billion-long length range -- 1e13 ops -- would never finish. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int k = 10_000;
    int[] cables = new int[k];
    Arrays.fill(cables, 1_000_000_000);
    int n = 1_000_000;
    // count(L) = k * floor(1e9 / L) >= n  <=>  floor(1e9 / L) >= 100  =>  Lmax = floor(1e9 / 100).
    assertThat(runMain(cableInput(cables, n))).isEqualTo("10000000");
  }

  // --- Small random batches with narrow lengths -- so feasible ranges are short and boundary
  // off-by-ones (return lo vs hi, `<` vs `<=` in the predicate, floor vs ceil) surface -- checked
  // against an independent top-down linear scan. N is drawn in [K, total] to honor both the K <= N
  // constraint and the guarantee that N pieces are always makeable (at length 1 the cables yield
  // `total` pieces). The scan shares no binary-search logic with a judge solution, so the two agree
  // only when both are right. ---

  @Test
  void randomizedInputsMatchLinearScanOracle() throws IOException {
    Random rnd = new Random(1654);
    for (int trial = 0; trial < 1000; trial++) {
      int k = 1 + rnd.nextInt(8);
      int[] cables = new int[k];
      int total = 0;
      for (int i = 0; i < k; i++) {
        cables[i] = 1 + rnd.nextInt(20); // lengths in [1, 20]
        total += cables[i];
      }
      int n = k + rnd.nextInt(total - k + 1); // N in [K, total]: K <= N and N pieces are makeable
      String expected = Integer.toString(maxCutLength(cables, n));
      assertThat(runMain(cableInput(cables, n)))
          .as("cables=%s N=%d", Arrays.toString(cables), n)
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the obviously-correct top-down linear scan. Walk candidate cut lengths from
   * the longest cable down to {@code 1} and return the first that makes at least {@code n} pieces
   * -- which, because the piece count is monotonically non-increasing in the cut length, is the
   * maximum feasible length. Uses no binary search, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(maxLen * K)} time, where {@code K} is {@code cables.length} and
   *     {@code maxLen} is the longest cable -- acceptable only because the randomized fixtures keep
   *     lengths small.
   */
  private static int maxCutLength(int[] cables, int n) {
    int maxLen = 0;
    for (int c : cables) {
      maxLen = Math.max(maxLen, c);
    }
    for (int len = maxLen; len >= 1; len--) {
      long pieces = 0;
      for (int c : cables) {
        pieces += c / len;
      }
      if (pieces >= n) {
        return len;
      }
    }
    return 0; // unreachable: length 1 makes `total` pieces, which is >= n by the makeability
    // guarantee
  }

  /**
   * Renders the cables as BOJ 1654 input: {@code "K N"} on line 1, then one cable length per line.
   */
  private static String cableInput(int[] cables, int n) {
    StringBuilder sb = new StringBuilder();
    sb.append(cables.length).append(' ').append(n).append('\n');
    for (int c : cables) {
      sb.append(c).append('\n');
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
