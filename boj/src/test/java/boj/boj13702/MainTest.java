package boj.boj13702;

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
 * BOJ 13702 이상한 술집 (The Strange Bar) -- yet another "binary search on the answer" (parametric
 * search), the drinking cousin of 1654's cable-cutting and 2805's tree-felling.
 *
 * <p>Eun-sang orders {@code N} pots (주전자) of makgeolli -- each pot a single fixed-size vessel
 * holding a random volume -- and must serve {@code K} friends (himself included) one identical
 * per-person amount. Pots are never mixed: after pouring the per-person amount out of a pot as many
 * whole times as it allows, whatever remains in that pot is thrown away (the statement's example
 * pours 1002, 802 and 705 ml pots at 401 ml and discards 200, 0 and 304 ml). So a per-person amount
 * {@code x} drains a pot of volume {@code v} into {@code floor(v / x)} servings, the total at
 * amount {@code x} is {@code count(x) = sum of floor(v / x)} over the {@code N} pots, and the
 * answer is the <em>largest</em> {@code x} with {@code count(x) >= K}. Because {@code count} is
 * monotonically non-increasing in {@code x}, the feasible amounts form a downward-closed range
 * {@code [?, answer]}.
 *
 * <p>Input: line 1 is {@code "N K"} -- the pot count {@code N} ({@code N <= 10,000}) and the head
 * count {@code K} ({@code K <= 1,000,000}), with {@code N <= K} always (you cannot have more pots
 * than drinkers). The next {@code N} lines each hold one pot's volume, a natural number <em>or
 * zero</em> up to {@code 2^31 - 1} (i.e. up to {@link Integer#MAX_VALUE}). Output: the single
 * maximum per-person volume in milliliters. (Spec triangulated across GitHub accepted solutions and
 * blog mirrors of the statement while acmicpc.net was unreachable; the published sample {@code 3 5
 * / 1002 802 705 -> 401} corroborates it -- at {@code x = 401} the servings are {@code 2 + 2 + 1 =
 * 5 >= 5}, and at {@code x = 402} they fall to {@code 2 + 1 + 1 = 4 < 5}.)
 *
 * <p>13702 shares 1654's machinery but differs in two ways that get their own fixtures:
 *
 * <ul>
 *   <li><b>A pot's volume may be exactly {@code 0}.</b> An empty pot pours zero servings and must
 *       not derail the tally (it never divides by zero either, since {@code x >= 1})
 *       ({@link #zeroVolumePotsContributeNoServings}). 1654's cables were strictly positive.
 *   <li><b>Feasibility is NOT guaranteed, so the answer may be {@code 0}.</b> 1654 promised the
 *       cables could always make {@code N} pieces; here {@code N <= K} plus meager pots can leave
 *       even {@code x = 1} short of {@code K} servings, and the only honest answer is {@code 0} --
 *       nobody can be poured a positive amount
 *       ({@link #infeasibleWhenEvenOneMilliliterCannotServeEveryone}). A solution that initializes
 *       its answer to {@code 1}, or whose search floor never reaches {@code 0}, misprints here.
 * </ul>
 *
 * <p>The remaining fixtures pin the things every parametric search gets wrong:
 *
 * <ul>
 *   <li><b>Maximize the amount, don't stop at the first feasible one.</b> Feasible amounts are
 *       downward-closed; the answer is the top, not the first {@code x} the search finds feasible
 *       ({@link #returnsTheMaximumFeasibleVolumeNotTheFirst}).
 *   <li><b>Floor division -- the dregs are discarded.</b> A pot yields {@code floor(v / x)} whole
 *       servings, never rounded up ({@link #leftoverIsDiscardedSoVolumeUsesFloorDivision}), and a
 *       pot smaller than the serving simply yields zero
 *       ({@link #potsSmallerThanTheServingContributeZeroPeople}).
 *   <li><b>Count across all pots, not just the biggest.</b> The official sample already pins this
 *       -- a solution eyeing only the 1002 ml pot would answer {@code floor(1002/5) = 200}, not
 *       401.
 *   <li><b>The answer may equal a pot's exact volume</b> -- an evenly-divisible pot pays out and
 *       the amount can sit right on its volume ({@link #answerCanEqualAPotsExactVolume}).
 *   <li><b>One pot feeds many drinkers</b> ({@code N} may be far below {@code K})
 *       ({@link #manyPeopleFromFewPots}).
 *   <li><b>The midpoint needs 64 bits.</b> The search's high end is the largest pot, up to
 *       {@code Integer.MAX_VALUE}, so a naive {@code (lo + hi) / 2} overflows {@code int} -- on the
 *       first iteration when the largest pot is {@code 2^31 - 1}
 *       ({@link #maxPotVolumeNearIntMaxAvoidsMidpointOverflow}), and even for pots below the
 *       {@code int} ceiling once {@code lo} climbs and {@code lo + hi} crosses it
 *       ({@link #midpointSumOverflowsIntEvenWhenEveryPotFitsInInt}). The serving-count accumulator,
 *       by contrast, stays bounded by roughly {@code 2K + 2N} at every probed amount (since
 *       {@code count(answer + 1) < K <= 1,000,000} and one step down at most doubles it), so it
 *       fits an {@code int} -- the genuine overflow trap is the midpoint, not the count.
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N log maxVol)} search
 * (~30 sweeps of 10,000 pots) against an {@code O(maxVol * N)} linear walk of the billion-wide
 * volume range, and {@link #randomizedInputsMatchLinearScanOracle} cross-checks the judged output
 * against an obviously-correct top-down linear scan over candidate amounts -- including infeasible
 * batches whose answer is {@code 0}. The {@code Main} under test is an empty stub that reads
 * nothing and prints nothing, so every assertion here is RED until the parametric search is
 * implemented.
 */
class MainTest {

  // --- The official sample from the statement: three pots, five drinkers. The end-to-end smoke
  // test, and already enough to pin two things -- that count *sums across pots* (a solution eyeing
  // only the largest pot, 1002, would answer floor(1002/5)=200, not 401) and that the amount is
  // *maximized* (x=400 is also feasible: 2+2+1=5, but the answer climbs to the top, 401). ---

  // 3 5 / 1002 802 705: at x=401 the servings are 2, 2, 1 -> 5 >= 5; at x=402 they fall to 2, 1, 1
  // -> 4 < 5.
  @Test
  @StdIo({"3 5", "1002", "802", "705"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("401");
  }

  // --- Smallest input (N = 1, K = 1): the lone pot is poured entirely to the single drinker, so
  // the
  // answer is the whole pot. volume 10: at x=10 -> floor(10/10)=1 >= 1; at x=11 -> 0 < 1. ---

  @Test
  @StdIo({"1 1", "10"})
  void singlePotPouredToTheLonePersonGivesTheWholePot(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- The crux of parametric search: the answer is the *top* of the feasible range, not the first
  // feasible amount met. pots {6,9}, K=3: amounts 1..4 are all feasible (x=3 -> 2+3=5 >= 3; x=4 ->
  // 1+2=3 >= 3) and the answer is the maximum of them, 4; x=5 -> 1+1=2 < 3. A search that returns
  // the midpoint where it first sees feasibility -- or returns `lo` past the boundary -- misprints.
  // ---

  @Test
  @StdIo({"2 3", "6", "9"})
  void returnsTheMaximumFeasibleVolumeNotTheFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Floor division, dregs discarded: a single 7ml pot poured for K=2 servings yields
  // floor(7/x).
  // At x=3 that is 2 >= 2 (two 3ml servings, 1ml thrown away); at x=4 it is 1 < 2. Answer 3. A
  // solution that rounds up -- ceil(7/3)=3 -- or demands exact division misjudges the pour. ---

  @Test
  @StdIo({"1 2", "7"})
  void leftoverIsDiscardedSoVolumeUsesFloorDivision(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A pot smaller than the per-person amount yields zero servings, never a negative: floor
  // division gives 0 for free. pots {3,100}, K=10: at x=10 the 3ml pot gives 0 and the 100ml pot
  // gives 10 -> 10 >= 10; at x=11 it is 0 + 9 = 9 < 10. Answer 10. ---

  @Test
  @StdIo({"2 10", "3", "100"})
  void potsSmallerThanTheServingContributeZeroPeople(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- 13702-specific: a pot's volume may be exactly 0 (the statement allows "자연수 또는 0"). An empty
  // pot pours zero servings and must drop out of the tally without dividing by zero. pots {0,20},
  // K=4: at x=5 the empty pot gives 0 and the 20ml pot gives 4 -> 4 >= 4; at x=6 it is 0 + 3 = 3 <
  // 4. Answer 5. A solution that chokes on a 0 entry (e.g. tries 0/x as a guard, or skips reading
  // it) breaks here. ---

  @Test
  @StdIo({"2 4", "0", "20"})
  void zeroVolumePotsContributeNoServings(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- 13702-specific: feasibility is NOT guaranteed, so the answer can be 0. pots {1,1}, K=3:
  // even
  // the smallest positive amount x=1 pours only 1+1=2 < 3 servings, so no one can be poured a
  // positive amount and the answer is 0. This is the opposite of 1654, where >= 1 piece was always
  // makeable -- here N <= K (2 <= 3) plus meager pots starves K. A search that initializes its
  // answer to 1, or whose floor never reaches 0, misprints. ---

  @Test
  @StdIo({"2 3", "1", "1"})
  void infeasibleWhenEvenOneMilliliterCannotServeEveryone(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The answer may land exactly on a pot's volume: an evenly-divisible pot pays out and the
  // amount can sit right on it. pots {200,200}, K=2: at x=200 each gives 200/200 = 1 -> 2 >= 2; at
  // x=201 each gives 0 -> 0 < 2. Answer 200 == pot volume, pinning that the upper bound is
  // *inclusive* of a pot volume. ---

  @Test
  @StdIo({"2 2", "200", "200"})
  void answerCanEqualAPotsExactVolume(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  // --- N may be far below K: one big pot is poured for many drinkers. pot {1000}, K=100: at x=10
  // ->
  // 1000/10 = 100 >= 100; at x=11 -> 1000/11 = 90 < 100. Answer 10 = floor(1000/100), the
  // single-pot
  // closed form. Pins that the serving count scales far past N (here N=1, K=100). ---

  @Test
  @StdIo({"1 100", "1000"})
  void manyPeopleFromFewPots(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Uniform pots split evenly. pots {4,4,4}, K=6: at x=2 each pours 2 -> 6 >= 6; at x=3 each
  // pours 1 -> 3 < 6. Answer 2. A clean check that the feasibility total scales with the pot count.
  // ---

  @Test
  @StdIo({"3 6", "4", "4", "4"})
  void uniformPotsSplitEvenly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Upper bound of the search space AND the first-iteration midpoint trap. The largest pot is
  // 2^31 - 1 == Integer.MAX_VALUE, so the search's high end starts there. With N=K=1 the lone pot
  // is
  // poured once, so the answer is the whole pot, 2147483647 (floor(x/x)=1 >= 1; one ml more gives
  // 0). A naive `int mid = (lo + hi) / 2` overflows on the *first* iteration -- 1 + 2147483647
  // wraps
  // negative, the amount goes nonsensical, and the search collapses to a wrong (typically 0)
  // answer.
  // Passing requires a long midpoint or the overflow-safe `lo + (hi - lo)/2`. ---

  @Test
  @StdIo({"1 1", "2147483647"})
  void maxPotVolumeNearIntMaxAvoidsMidpointOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2147483647");
  }

  // --- The midpoint trap fires even when *every* value fits in int. pot {1500000000}, K=1: the
  // answer is the whole 1,500,000,000ml pot. The first probe (lo=1, hi=1.5e9) is safe, but once it
  // proves feasible lo climbs to ~7.5e8 while hi stays at 1.5e9, and `lo + hi` ~ 2.25e9 overflows
  // the 2,147,483,647 int ceiling on the *second* iteration -- the amount wraps negative and the
  // search settles on a too-small answer (~750,000,000). So long arithmetic is required for the
  // midpoint even though no single pot approaches Integer.MAX_VALUE. ---

  @Test
  @StdIo({"1 1", "1500000000"})
  void midpointSumOverflowsIntEvenWhenEveryPotFitsInInt(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1500000000");
  }

  // --- Upper bounds on N and K: 10,000 pots, each 1,000,000,000ml, with K = 1,000,000 (the
  // maximum). A uniform cellar gives the closed form count(x) = N * floor(maxVol / x); feasibility
  // count(x) >= K reduces to floor(1e9 / x) >= 100, so the largest feasible amount is floor(1e9 /
  // 100) = 10,000,000. The O(N log maxVol) search runs ~30 sweeps of 10,000 pots (~3e5 ops) where
  // an
  // O(maxVol * N) walk of the billion-wide volume range -- 1e13 ops -- would never finish. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 10_000;
    int[] pots = new int[n];
    Arrays.fill(pots, 1_000_000_000);
    int k = 1_000_000;
    // count(x) = n * floor(1e9 / x) >= k  <=>  floor(1e9 / x) >= 100  =>  xMax = floor(1e9 / 100).
    assertThat(runMain(potInput(pots, k))).isEqualTo("10000000");
  }

  // --- Small random cellars with narrow volumes (including 0ml pots) -- so feasible ranges are
  // short and boundary off-by-ones (return lo vs hi, `<` vs `<=` in the predicate, floor vs ceil)
  // surface -- checked against an independent top-down linear scan. K is drawn in [N, max(N,total)
  // +
  // 3], honoring the N <= K constraint while straddling total: K <= total batches are feasible
  // (answer >= 1) and K > total batches are infeasible (answer 0), so both regimes are exercised.
  // The scan shares no binary-search logic with a judge solution, so the two agree only when both
  // are right. ---

  @Test
  void randomizedInputsMatchLinearScanOracle() throws IOException {
    Random rnd = new Random(13702);
    for (int trial = 0; trial < 1000; trial++) {
      int n = 1 + rnd.nextInt(8);
      int[] pots = new int[n];
      long total = 0;
      for (int i = 0; i < n; i++) {
        pots[i] = rnd.nextInt(21); // volumes in [0, 20], zero allowed
        total += pots[i];
      }
      int bound = (int) Math.max(n, total) + 3;
      int k = n + rnd.nextInt(bound - n + 1); // K in [N, max(N,total)+3]: N <= K, straddling total
      String expected = Integer.toString(maxServingVolume(pots, k));
      assertThat(runMain(potInput(pots, k)))
          .as("pots=%s K=%d", Arrays.toString(pots), k)
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the obviously-correct top-down linear scan. Walk candidate per-person
   * amounts from the largest pot down to {@code 1} and return the first that pours at least
   * {@code k} servings -- which, because the serving count is monotonically non-increasing in the
   * amount, is the maximum feasible amount. Returns {@code 0} when no positive amount serves
   * {@code k} (an infeasible cellar, e.g. every pot empty). Uses no binary search, so it shares no
   * logic with a judge solution.
   *
   * @implNote {@code O(maxVol * N)} time, where {@code N} is {@code pots.length} and {@code maxVol}
   *     is the largest pot volume -- acceptable only because the randomized fixtures keep volumes
   *     small.
   */
  private static int maxServingVolume(int[] pots, int k) {
    int maxVol = 0;
    for (int v : pots) {
      maxVol = Math.max(maxVol, v);
    }
    for (int x = maxVol; x >= 1; x--) {
      long servings = 0;
      for (int v : pots) {
        servings += v / x;
      }
      if (servings >= k) {
        return x;
      }
    }
    return 0; // infeasible: no positive amount pours k servings, so nobody is served
  }

  /**
   * Renders the cellar as BOJ 13702 input: {@code "N K"} on line 1, then one pot volume per line.
   */
  private static String potInput(int[] pots, int k) {
    StringBuilder sb = new StringBuilder();
    sb.append(pots.length).append(' ').append(k).append('\n');
    for (int v : pots) {
      sb.append(v).append('\n');
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
