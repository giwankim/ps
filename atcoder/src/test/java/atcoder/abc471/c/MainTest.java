package atcoder.abc471.c;

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
 * AtCoder ABC 471 C -- Cookies and Greedy Takahashi.
 *
 * <p>N (1 ≤ N ≤ 3 * 10^5) cookies sit at distinct nonzero integer coordinates A_i (-10^9 ≤ A_i ≤
 * 10^9) on a number line. Takahashi starts at coordinate 0 and repeats one action until no cookie
 * is left: walk to the nearest cookie he has not picked up yet -- on a tie, the one with the
 * smaller coordinate -- and pick it up. Print the total distance walked.
 *
 * <p>Because he only ever moves to a nearest cookie, the ones already collected form a contiguous
 * stretch around the origin and he always stands at one of its two ends. So each step is a race
 * between the nearest uncollected cookie to his left and the nearest to his right. That order is
 * not the input order, not the cookies sorted by |A_i|, and not one side swept before the other; he
 * can cross the origin many times. Every step costs |Δ|, so the running total only grows, and it
 * reaches 3 * 10^9 -- past what an int holds.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"4", "-1 -4 2 -11"})
  void officialSampleOneWalks1Then3Then6Then13(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("23");
  }

  @Test
  @StdIo({"10", "1 2 3 4 5 -1 -2 -3 -4 -6"})
  void officialSampleTwoClearsTheNegativesBeforeCrossingBack(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  // --- Each step goes to the nearest cookie from where he stands now, not to the next cookie in
  // input order nor to the next smallest |A_i| overall. ---

  @Test
  @StdIo({"3", "5 1 3"})
  void cookiesOnOneSideAreWalkedOutwardWhateverOrderTheyAreGivenIn(StdOut out) throws IOException {
    // 0 -> 1 -> 3 -> 5 costs 5, the farthest coordinate. Walking the input order costs 11.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"3", "-5 -1 -3"})
  void negativeCookiesAreAlsoWalkedOutwardFromTheOrigin(StdOut out) throws IOException {
    // The mirror image: 0 -> -1 -> -3 -> -5 costs 5, and the input order again costs 11.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"3", "-3 4 -5"})
  void theNearestCookieIsMeasuredFromHisPositionNotFromTheOrigin(StdOut out) throws IOException {
    // 0 -> -3 (3) -> -5 (2) -> 4 (9). From -3 the cookie at -5 is nearer than the one at 4, even
    // though 4 has the smaller magnitude; visiting the cookies in |A_i| order would cost 19.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  @Test
  @StdIo({"4", "-1 3 -9 27"})
  void heCrossesTheOriginOnceMoreForEverySideSwap(StdOut out) throws IOException {
    // 0 -> -1 (1) -> 3 (4) -> -9 (12) -> 27 (36): each cookie is nearer than the next one on the
    // side he is standing on, so he changes sides every step. Clearing the left side and then the
    // right would cost 45, and no expression in min/max alone lands on 53.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("53");
  }

  // --- Equally near cookies are settled toward the smaller coordinate. The choice only shows up
  // in the total when a third cookie is left to walk to afterward. ---

  @Test
  @StdIo({"3", "-2 2 3"})
  void aTieAtTheOriginGoesLeftAndLeavesBothRightCookiesForLater(StdOut out) throws IOException {
    // -2 and 2 are both 2 away from the start, so he takes -2: 0 -> -2 (2) -> 2 (4) -> 3 (1).
    // Taking 2 first would strand -2 behind him and cost 8.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"4", "-1 -4 2 3"})
  void theTieTheStatementCallsOutIsResolvedTheSameWayMidWalk(StdOut out) throws IOException {
    // Sample 1's second step, with a cookie added at 3. Standing at -1 the cookies at -4 and 2 are
    // both 3 away, so he takes -4: 0 -> -1 (1) -> -4 (3) -> 2 (6) -> 3 (1). Taking 2 costs 12.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Distance is unsigned: the total is a sum of |Δ|, never a signed displacement. ---

  @Test
  @StdIo({"1", "7"})
  void aLoneCookieCostsItsCoordinate(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"1", "-7"})
  void aLoneCookieToTheLeftCostsJustAsMuch(StdOut out) throws IOException {
    // Summing coordinates rather than distances would print -7 here.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Constraint boundaries. ---

  @Test
  @StdIo({"2", "1000000000 -1000000000"})
  void theWidestPossibleCrossingOverflowsAnIntTotal(StdOut out) throws IOException {
    // Both cookies are 10^9 away from the start, so he takes -10^9 first and then crosses the
    // full range: 10^9 + 2 * 10^9 = 3 * 10^9. Either order costs the same here; what this pins
    // down is the width of the accumulator, since an int wraps this total to -1294967296.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3000000000");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumNumberOfCookiesIsWalkedWithinTheTimeLimit() throws IOException {
    // N at its ceiling of 3 * 10^5, as two solid blocks of 150000 consecutive coordinates hugging
    // the ends of the allowed range: every integer in [-10^9, -999850001] and every integer in
    // [999850001, 10^9]. He walks to the near edge of the left block, sweeps it out to -10^9,
    // crosses the whole range once, then sweeps the right block:
    //   999850001 + 149999 + 1999850001 + 149999 = 3000000000.
    // Rescanning the uncollected cookies on every step would be ~4.5 * 10^10 comparisons.
    int half = 150_000;
    StringBuilder input = new StringBuilder().append(2 * half).append('\n');
    for (int i = 0; i < half; i++) {
      input.append(-1_000_000_000 + i).append(' ');
    }
    for (int i = 0; i < half; i++) {
      input.append(1_000_000_000 - i).append(' ');
    }
    assertThat(runMain(input.toString())).isEqualTo("3000000000");
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
