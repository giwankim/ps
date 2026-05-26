package boj.boj22945;

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
 * BOJ 22945 팀 빌딩 (Team Building).
 *
 * <p>N developers stand in a line, each with a positive ability. A team is exactly two developers A
 * and B; its value is {@code (number of developers standing between A and B) * min(ability of A,
 * ability of B)}. The answer is the maximum team value over every pair.
 *
 * <p>Input is N on line one and the N abilities (in line order) on line two; the program prints the
 * single maximum value. This is the "container with most water" shape: for boundary indices
 * {@code left < right} the value is {@code (right - left - 1) * min(a[left], a[right])}, and a
 * two-pointer that always advances the smaller-ability endpoint inward reaches the optimum in O(N).
 *
 * <p>Two structural facts drive the edge cases below. First, only the two endpoints' abilities
 * matter -- the interior developers contribute only their count, never their values -- so a wide
 * pair with small ends can lose to a narrow pair with large ends. Second, the constraints
 * (triangulated from accepted submissions because acmicpc.net is unreachable: AC sources annotate
 * {@code 2 <= N <= 100,000} and {@code 1 <= x_i <= 10,000}, and the problem author's reference
 * solution uses pure-int arithmetic). The worst-case in-spec answer is {@code (N - 2) * max_x =
 * 99,998 * 10,000 = 999,980,000}, which fits in a 32-bit signed int. Because N can exceed the
 * 10,000 distinct positive abilities available, duplicate abilities must be legal at large N -- the
 * tie branch of the two-pointer is exercised below.
 */
class MainTest {

  // --- The worked example from the statement: abilities 1 4 2 5. The statement illustrates the
  // formula with the (1, 5) pair scoring 2*min(1,5) = 2, but that is NOT the maximum. The two
  // largest, abilities 4 and 5, have one developer (ability 2) between them: 1*min(4,5) = 4. A
  // solution that scores only the illustrated pair, or the two outermost developers, reports 2 and
  // fails here. ---

  @Test
  @StdIo({"4", "1 4 2 5"})
  void workedExampleMaximumIsNotTheIllustratedPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Smallest team-forming input, N = 2: the only pair has nobody between them, so the value is
  // 0 no matter how large the two abilities are. The {@code while (lo < hi)} loop precondition
  // means the single iteration evaluates {@code (1 - 0 - 1) * min = 0} and the negative-width
  // case the formula would otherwise allow is unreachable. ---

  @Test
  @StdIo({"2", "3 7"})
  void twoDevelopersHaveNobodyBetweenThemSoValueIsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Smallest input with a non-zero answer, N = 3 ascending (1 2 3). Only the outer pair has a
  // developer between it: 1*min(1,3) = 1. Pins the basic "one developer between" case. ---

  @Test
  @StdIo({"3", "1 2 3"})
  void threeDevelopersAscending(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The interior value is irrelevant. With 5 1 9 the only spanning pair is (5, 9) with the
  // tiny 1 between them: 1*min(5,9) = 5. A solution that takes the minimum over the whole spanned
  // range (min(5,1,9) = 1) would report 1. Confirms only the two endpoints feed the min. ---

  @Test
  @StdIo({"3", "5 1 9"})
  void valueUsesEndpointMinimumNotInteriorValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Strictly increasing abilities (1 2 3 4 5). Here min(a[i], a[j]) = a[i] for every pair, so
  // the value is (j-i-1)*a[i], maximized by pairing each left end with the far right end: for
  // N = 5 the value (N-2-i)*(i+1) = (3-i)*(i+1) peaks at i = 1 with 2*2 = 4. Exercises the
  // two-pointer always advancing the left pointer. ---

  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void strictlyIncreasingAbilities(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Strictly decreasing abilities (5 4 3 2 1), the mirror image. Now min(a[i], a[j]) = a[j],
  // so the optimum pairs the far left end with each right end and the best is again 4. Exercises
  // the two-pointer always advancing the right pointer. ---

  @Test
  @StdIo({"5", "5 4 3 2 1"})
  void strictlyDecreasingAbilities(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Both endpoints large and far apart: 10 1 2 3 20. The widest pair (10, 20) has three
  // developers between it: 3*min(10,20) = 30, which beats every narrower pair. Confirms the answer
  // can indeed be the two outermost developers when their min is large. ---

  @Test
  @StdIo({"5", "10 1 2 3 20"})
  void widestPairWithLargeEndpointsWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30");
  }

  // --- The opposite pull: 1 100 99 98 2 has tiny ends, so the widest pair (1, 2) scores only
  // 3*min(1,2) = 3. The real optimum is the interior pair (100, 98) with one developer (99)
  // between: 1*min(100,98) = 98. Catches any solution biased toward the extremes. ---

  @Test
  @StdIo({"5", "1 100 99 98 2"})
  void interiorPairBeatsTheExtremes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("98");
  }

  // --- In-spec four-figure abilities at the upper end of the verified cap (10,000): the spanning
  // pair scores 1*min(10000,9999) = 9999. Confirms the endpoint-min logic at maximum legal
  // magnitudes. ---

  @Test
  @StdIo({"3", "10000 1 9999"})
  void largeAbilitiesAtSpecCap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9999");
  }

  // --- Duplicate abilities are legal at any N > 10,000 (the constraint N <= 100,000 with x_i <=
  // 10,000 forces duplicates), and this hand case pins the tie branch deterministically: with
  // 5 5 5 5 the outermost pair scores 2*min(5,5) = 10. The standard tie-break (advance hi when
  // a[lo] == a[hi]) reaches that optimum; a buggy variant that advanced both pointers on equality
  // would skip it. The randomized oracle below cross-checks duplicates at small N. ---

  @Test
  @StdIo({"4", "5 5 5 5"})
  void duplicateAbilitiesAreLegalAndScored(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Randomized cross-check against an independent O(N^2) brute force. Abilities are sampled
  // WITH REPLACEMENT from a small pool (1..10), so duplicates appear frequently and the
  // two-pointer's tie branch is exercised; N spans 2..8, covering zero-answer (N = 2),
  // endpoint-min, interior-optimum, extreme-optimum, and all-equal configurations. The .as()
  // description carries the trial index so a failing iteration is locatable without re-counting
  // RNG calls. ---

  @Test
  void randomizedSmallInputsMatchBruteForce() throws IOException {
    Random rnd = new Random(22945);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(7); // 2..8 developers
      int[] abilities = randomAbilities(n, 10, rnd);
      String input = inputFor(abilities);
      String expected = Long.toString(bruteForce(abilities));
      assertThat(runMain(input)).as("trial=%d input=%n%s", trial, input).isEqualTo(expected);
    }
  }

  // --- Maximum N (100,000) with every ability at the in-spec cap (10,000). Because N exceeds the
  // 10,000 distinct legal abilities, duplicates are forced; here every endpoint pair sees
  // min = 10,000, so the value is just (j - i - 1) * 10,000 and the outermost pair wins:
  // 99,998 * 10,000 = 999,980,000, the maximum answer reachable under the spec. The timeout rules
  // out an O(N^2) brute force at this scale, and the magnitude pins the in-spec arithmetic bound
  // -- an int accumulator suffices, but anything narrower (short / unsigned-32 wrap) would WA. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxNUniformAtCapHitsInSpecMaximumAnswer() throws IOException {
    int[] abilities = new int[100_000];
    Arrays.fill(abilities, 10_000);
    assertThat(runMain(inputFor(abilities))).isEqualTo("999980000");
  }

  // --- Distinct abilities at maximum distinct count (N = 10,000 with abilities 1..10,000). For an
  // increasing array min(a[i], a[j]) = a[i], so the value (N-2-i)*(i+1) peaks at i in {4998,4999}
  // with 5000 * 4999 = 24,995,000. Exercises the two-pointer ALWAYS advancing the LEFT pointer at
  // a non-trivial scale with strict distinctness. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void distinctIncreasingAtMaxDistinctCount() throws IOException {
    int[] abilities = new int[10_000];
    for (int i = 0; i < abilities.length; i++) {
      abilities[i] = i + 1;
    }
    assertThat(runMain(inputFor(abilities))).isEqualTo("24995000");
  }

  // --- The mirror of the test above: N = 10,000 decreasing 10,000..1. By symmetry the optimum is
  // again 24,995,000, but the two-pointer reaches it by ALWAYS advancing the RIGHT pointer; this
  // pins the other direction of the algorithm at scale. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void distinctDecreasingAtMaxDistinctCount() throws IOException {
    int[] abilities = new int[10_000];
    for (int i = 0; i < abilities.length; i++) {
      abilities[i] = 10_000 - i;
    }
    assertThat(runMain(inputFor(abilities))).isEqualTo("24995000");
  }

  // Reference brute force: the maximum over every pair of (developers between) * min(endpoints).
  // O(N^2), obviously correct, trustworthy only for the tiny N used by the randomized oracle. The
  // accumulator is long so the oracle itself never overflows on any int input.
  private static long bruteForce(int[] a) {
    long best = 0;
    for (int i = 0; i < a.length; i++) {
      for (int j = i + 1; j < a.length; j++) {
        best = Math.max(best, (long) (j - i - 1) * Math.min(a[i], a[j]));
      }
    }
    return best;
  }

  // Samples n abilities WITH REPLACEMENT from 1..pool. Duplicates occur naturally; the
  // distinctness once stated in the problem appears to have been edited out (most BaekjoonHub
  // scrapes drop the qualifier and the constraint N <= 100,000 with x_i <= 10,000 makes
  // distinctness impossible at large N), so the oracle covers both distinct and duplicate inputs.
  private static int[] randomAbilities(int n, int pool, Random rnd) {
    int[] abilities = new int[n];
    for (int i = 0; i < n; i++) {
      abilities[i] = 1 + rnd.nextInt(pool);
    }
    return abilities;
  }

  private static String inputFor(int[] abilities) {
    StringBuilder sb = new StringBuilder(abilities.length * 7 + 8);
    sb.append(abilities.length).append('\n');
    for (int i = 0; i < abilities.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(abilities[i]);
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
