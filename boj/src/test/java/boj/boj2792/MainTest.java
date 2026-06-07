package boj.boj2792;

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
 * BOJ 2792 보석 상자 (Jewelry Box) -- "binary search on the answer" (parametric search), the mirror
 * image of sibling 1654's LAN-cable cutting.
 *
 * <p>A box holds jewels in {@code M} colors; color {@code k} has {@code a[k]} jewels. The jewels
 * are handed out to {@code N} children under two rules: every jewel is given away, and a child only
 * ever receives jewels of a single color (a child may receive nothing at all). "Jealousy" is the
 * count held by the child who ends up with the most jewels; the task is to distribute so that
 * jealousy is as <em>small</em> as possible, and print that minimum. Worked example from the
 * statement: 4 red (RRRR) and 7 blue (BBBBBBB) split among 5 children as RR, RR, BB, BB, BBB gives
 * jealousy 3, and no split does better.
 *
 * <p>The decision version is monotone: if a per-child cap of {@code x} jewels is achievable, so is
 * any larger cap. At cap {@code x}, color {@code k} must be spread over {@code ceil(a[k] / x)}
 * children (the last child mops up the remainder), so the children needed are {@code need(x) = sum
 * of ceil(a[k] / x)}, and the cap is achievable exactly when {@code need(x) <= N}. Because
 * {@code need} is non-increasing in {@code x}, the achievable caps form an upward-closed range
 * {@code [answer, max(a)]} and the answer is its <em>bottom</em>: the smallest {@code x} with
 * {@code need(x) <= N}.
 *
 * <p>Input: line 1 is {@code "N M"} -- the child count {@code N} ({@code 1 <= N <= 1,000,000,000})
 * and the color count {@code M} ({@code 1 <= M <= 300,000}), with {@code M <= N} guaranteed. The
 * next {@code M} lines each hold one jewel count, a positive integer in {@code [1, 1,000,000,000]};
 * the {@code k}-th is color {@code k}'s count. Output: the single minimum jealousy. (Spec
 * triangulated across blog mirrors of the statement while acmicpc.net was unreachable; the
 * published sample {@code 5 2 / 4 7 -> 3} corroborates it -- at {@code x = 3} the children needed
 * are {@code ceil(4/3) + ceil(7/3) = 2 + 3 = 5 <= 5}, and at {@code x = 2} they rise to {@code 2 +
 * 4 = 6 > 5}.)
 *
 * <p>The fixtures pin the things a hasty parametric search gets wrong:
 *
 * <ul>
 *   <li><b>Ceiling division, not floor.</b> A leftover jewel still needs its own child, so a color
 *       costs {@code ceil(a / x)} children. This is the exact opposite of sibling 1654, whose
 *       wasted offcut makes it {@code floor}; reusing {@code floor} here under-counts children and
 *       accepts too-small a cap ({@link #jealousyUsesCeilingDivisionForLeftoverJewels}).
 *   <li><b>Minimize the cap, don't maximize it.</b> Achievable caps form {@code [answer, max(a)]};
 *       the answer is the bottom, not (as in 1654) the top. A search that keeps climbing while
 *       feasible returns the wrong end ({@link #returnsTheMinimumFeasibleJealousyNotTheMaximum}).
 *   <li><b>One color per child -- counts are never pooled.</b> The cost is {@code sum of ceil(a[k]
 *       / x)} summed per color, not {@code ceil((sum of a[k]) / x)} on a merged pile; a fixture
 *       where the two diverge pins that colors stay separate
 *       ({@link #eachChildTakesOneColorSoCountsAreNotPooled}).
 *   <li><b>The search space has two ends.</b> When children are scarce ({@code N == M}) every color
 *       goes to exactly one child and jealousy rises to the largest pile, {@code max(a)}
 *       ({@link #jealousyRisesToTheLargestColorWhenChildrenAreScarce}); when children are plentiful
 *       the cap bottoms out at {@code 1}, one jewel per child
 *       ({@link #returnsTheMinimumFeasibleJealousyNotTheMaximum}).
 *   <li><b>Surplus children may receive nothing.</b> The predicate is {@code need(x) <= N}, not
 *       {@code == N}; when {@code N} dwarfs the jewels the extra children simply go empty-handed
 *       and the cap is still 1 ({@link #surplusChildrenMayReceiveNothing}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeUniformInputStaysWithinTimeLimit} pins the {@code O(M log max(a))}
 * search (~30 sweeps of 300,000 colors) against an {@code O(max(a) * M)} linear walk of the
 * billion-wide cap range, and {@link #randomizedInputsMatchLinearScanOracle} cross-checks the
 * judged output against an obviously-correct bottom-up linear scan over candidate caps. Worth
 * noting, because it is a real contrast with sibling 1654: 2792 needs no 64-bit arithmetic. Counts
 * and {@code N} are at most {@code 1e9} (well inside {@code int}); a correct search only ever
 * probes caps near the answer, where {@code need(x)} peaks at roughly {@code 2N + M ~ 2.0e9} --
 * just under {@link Integer#MAX_VALUE} -- and the midpoint {@code lo + hi} stays {@code <= 2 *
 * max(a) <= 2e9} likewise. (1654's {@code hi} reached {@code 2^31 - 1}, so its midpoint genuinely
 * overflowed {@code int}; here it does not.) So no overflow regression appears below -- it would be
 * testing a bug that cannot arise within the stated bounds.
 */
class MainTest {

  // --- The official sample from the statement: 4 red + 7 blue among 5 children -> jealousy 3. The
  // end-to-end smoke test, and already enough to pin two things -- that need() sums *across colors*
  // (ceil(4/3)+ceil(7/3)=5, not a single-pile guess) and that the cap is *minimized* (x=3 is the
  // smallest feasible; x=2 needs 6 > 5 children). It also catches a gross N<->M swap: with the line
  // "5 2" a solution that reads M=5 colors would run off the end of the two count lines. ---

  @Test
  @StdIo({"5 2", "4", "7"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Ceiling division, the crux that flips sibling 1654 on its head: a leftover jewel still
  // needs
  // its own child. One color of 7 jewels among N=2 children: at cap x=4 it costs ceil(7/4)=2 <= 2
  // (children get 4 and 3); at x=3 it costs ceil(7/3)=3 > 2. Answer 4. A solution that reused
  // 1654's
  // floor would judge floor(7/3)=2 <= 2 feasible and misprint 3. This is the single likeliest bug
  // for someone porting the cutting-cables search, so it gets the sharpest fixture. ---

  @Test
  @StdIo({"2 1", "7"})
  void jealousyUsesCeilingDivisionForLeftoverJewels(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Minimize, not maximize: the answer is the *bottom* of the achievable range, not the top.
  // Two colors of 2 among N=4 children: cap x=1 costs ceil(2/1)+ceil(2/1)=4 <= 4 (every jewel its
  // own child), and x=2 also fits (1+1=2 <= 4) -- both feasible, so the achievable set is {1, 2}
  // and
  // the answer is its minimum, 1. A search copied from 1654 that climbs while feasible returns the
  // top, 2. This also pins the lower end of the search space: the cap bottoms out at 1, never 0.
  // ---

  @Test
  @StdIo({"4 2", "2", "2"})
  void returnsTheMinimumFeasibleJealousyNotTheMaximum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- One color per child: need() must sum ceil per color, never merge the piles. Colors {3, 1}
  // among N=2 children: correct is ceil(3/2)+ceil(1/2)=2+1=3 > 2 at x=2, while x=3 gives 1+1=2 <=
  // 2,
  // so the answer is 3 (the 3-pile is one child's whole share). A solution that pooled the jewels
  // into a single pile of 4 would compute ceil(4/2)=2 <= 2 and misprint 2. The "same color only"
  // rule is exactly what this fixture defends. ---

  @Test
  @StdIo({"2 2", "3", "1"})
  void eachChildTakesOneColorSoCountsAreNotPooled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Upper end of the search space: when children are exactly as many as colors (N == M), every
  // color must go to a single child, so jealousy is forced up to the largest pile. Colors {4, 7}
  // among N=2 children: x=7 costs ceil(4/7)+ceil(7/7)=1+1=2 <= 2; x=6 costs 1+2=3 > 2. Answer 7 =
  // max(a). Pins that the cap's high end is max(a) and that the answer can sit right on it. ---

  @Test
  @StdIo({"2 2", "4", "7"})
  void jealousyRisesToTheLargestColorWhenChildrenAreScarce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Surplus children get nothing: the predicate is need(x) <= N, not == N. One color of 2 among
  // N=100 children: at x=1 only need(1)=ceil(2/1)=2 children are used and the other 98 go empty-
  // handed, which is allowed, so the answer is 1. A solution that demanded every child receive a
  // jewel (need == N) would never find x=1 feasible and would over-report. ---

  @Test
  @StdIo({"100 1", "2"})
  void surplusChildrenMayReceiveNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Full-scale values, all near the upper bounds: three colors of 1,000,000,000 among N=1e9
  // children. need(x)=3*ceil(1e9/x) <= 1e9 needs ceil(1e9/x) <= 333,333,333; at x=4 that is
  // 250,000,000 (3*250M=750M <= 1e9), at x=3 it is 333,333,334 (3*=1,000,000,002 > 1e9). Answer 4.
  // Exercises counts and N at the stated maxima and confirms the accumulator handles a need() of
  // ~1e9 around the boundary without trouble. ---

  @Test
  @StdIo({"1000000000 3", "1000000000", "1000000000", "1000000000"})
  void largeCountsAndManyChildrenAreHandledAtScale(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Small random inputs with narrow counts -- so achievable ranges are short and boundary
  // off-by-ones (return lo vs hi, `<` vs `<=` in the predicate, floor vs ceil) surface -- checked
  // against an independent bottom-up linear scan. N is drawn in [M, sum(a)] to honor M <= N and to
  // sweep the whole answer range: N = sum(a) admits the cap 1 (every jewel its own child), N = M
  // forces the cap up to max(a). The scan shares no binary-search logic with a judge solution, so
  // the two agree only when both are right. ---

  @Test
  void randomizedInputsMatchLinearScanOracle() throws IOException {
    Random rnd = new Random(2792);
    for (int trial = 0; trial < 1000; trial++) {
      int m = 1 + rnd.nextInt(6); // M in [1, 6]
      int[] counts = new int[m];
      int total = 0;
      for (int i = 0; i < m; i++) {
        counts[i] = 1 + rnd.nextInt(20); // counts in [1, 20]
        total += counts[i];
      }
      int n = m + rnd.nextInt(total - m + 1); // N in [M, sum(a)]: M <= N, and cap 1 is reachable
      String expected = Integer.toString(minJealousy(counts, n));
      assertThat(runMain(jewelInput(counts, n)))
          .as("counts=%s N=%d", Arrays.toString(counts), n)
          .isEqualTo(expected);
    }
  }

  // --- Upper bounds on M and the count value, with N at its maximum: 300,000 colors, each pile
  // 1,000,000,000 jewels, among N = 1,000,000,000 children. A uniform box gives the closed form
  // need(x) = M * ceil(C / x), so feasibility need(x) <= N reduces to ceil(C / x) <= floor(N / M) =
  // floor(1e9 / 300000) = 3333, whose smallest solution is x = ceil(C / 3333) = ceil(1e9 / 3333) =
  // 300,031. The O(M log C) search runs ~30 sweeps of 300,000 colors (~9e6 ops) where an O(C * M)
  // walk of the billion-wide cap range -- 3e14 ops -- would never finish. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeUniformInputStaysWithinTimeLimit() throws IOException {
    int m = 300_000;
    int[] counts = new int[m];
    Arrays.fill(counts, 1_000_000_000);
    int n = 1_000_000_000;
    // need(x) = M * ceil(1e9 / x) <= N  <=>  ceil(1e9 / x) <= floor(1e9 / 300000) = 3333
    //   =>  x_min = ceil(1e9 / 3333) = 300031.
    assertThat(runMain(jewelInput(counts, n))).isEqualTo("300031");
  }

  /**
   * Independent oracle: the obviously-correct bottom-up linear scan. Walk candidate caps from
   * {@code 1} up to the largest pile and return the first that needs at most {@code n} children --
   * which, because the children needed is non-increasing in the cap, is the minimum feasible cap.
   * Uses no binary search, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(max(a) * M)} time, where {@code M} is {@code counts.length} and
   *     {@code max(a)} is the largest pile -- acceptable only because the randomized fixtures keep
   *     counts small.
   */
  private static int minJealousy(int[] counts, int n) {
    int max = 0;
    for (int c : counts) {
      max = Math.max(max, c);
    }
    for (int cap = 1; cap <= max; cap++) {
      long need = 0;
      for (int c : counts) {
        need += (c + cap - 1) / cap; // ceil(c / cap)
      }
      if (need <= n) {
        return cap;
      }
    }
    return max; // unreachable: at cap = max(a) every color needs one child, and M <= N
  }

  /** Renders the box as BOJ 2792 input: {@code "N M"} on line 1, then one jewel count per line. */
  private static String jewelInput(int[] counts, int n) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(counts.length).append('\n');
    for (int c : counts) {
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
