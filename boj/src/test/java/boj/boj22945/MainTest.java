package boj.boj22945;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 22945 팀 빌딩 (Team Building).
 *
 * <p>N developers with all-distinct abilities stand in a line. A team is exactly two developers A
 * and B; its value is {@code (number of developers standing between A and B) * min(ability of A,
 * ability of B)}. The answer is the maximum team value over every pair.
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
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Smallest team-forming input, N = 2: the only pair has nobody between them, so the value is
  // 0 no matter how large the two abilities are. Guards reading N and emitting 0 rather than a
  // negative number from a (right - left - 1) = -1 width when the pointers meet. ---

  @Test
  @StdIo({"2", "3 7"})
  void twoDevelopersHaveNobodyBetweenThemSoValueIsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("0");
  }

  // --- Smallest input with a non-zero answer, N = 3 ascending (1 2 3). Only the outer pair has a
  // developer between it: 1*min(1,3) = 1. Pins the basic "one developer between" case. ---

  @Test
  @StdIo({"3", "1 2 3"})
  void threeDevelopersAscending(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("1");
  }

  // --- The interior value is irrelevant. With 5 1 9 the only spanning pair is (5, 9) with the
  // tiny 1 between them: 1*min(5,9) = 5. A solution that takes the minimum over the whole spanned
  // range (min(5,1,9) = 1) would report 1. Confirms only the two endpoints feed the min. ---

  @Test
  @StdIo({"3", "5 1 9"})
  void valueUsesEndpointMinimumNotInteriorValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("5");
  }

  // --- Strictly increasing abilities (1 2 3 4 5). Here min(a[i],a[j]) = a[i] for every pair, so
  // the value is (j-i-1)*a[i], maximized by pairing each left end with the far right end; the best
  // is (5-2-i)*(i+1) over i, i.e. 2*2 = 4. Exercises the two-pointer always advancing the left
  // pointer. ---

  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void strictlyIncreasingAbilities(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Strictly decreasing abilities (5 4 3 2 1), the mirror image. Now min(a[i],a[j]) = a[j], so
  // the optimum pairs the far left end with each right end and the best is again 4. Exercises the
  // two-pointer always advancing the right pointer. ---

  @Test
  @StdIo({"5", "5 4 3 2 1"})
  void strictlyDecreasingAbilities(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Both endpoints large and far apart: 10 1 2 3 20. The widest pair (10, 20) has three
  // developers between it: 3*min(10,20) = 30, which beats every narrower pair. Confirms the answer
  // can indeed be the two outermost developers when their min is large. ---

  @Test
  @StdIo({"5", "10 1 2 3 20"})
  void widestPairWithLargeEndpointsWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("30");
  }

  // --- The opposite pull: 1 100 99 98 2 has tiny ends, so the widest pair (1, 2) scores only
  // 3*min(1,2) = 3. The real optimum is the interior pair (100, 98) with one developer (99)
  // between: 1*min(100,98) = 98. Catches any solution biased toward the extremes. ---

  @Test
  @StdIo({"5", "1 100 99 98 2"})
  void interiorPairBeatsTheExtremes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("98");
  }

  // --- Large distinct abilities whose answer still fits in a 32-bit int: 100000 1 99999. The
  // spanning pair scores 1*min(100000,99999) = 99999. Confirms five/six-figure abilities and the
  // endpoint-min logic without yet stressing overflow. ---

  @Test
  @StdIo({"3", "100000 1 99999"})
  void largeDistinctAbilitiesWithinIntRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("99999");
  }

  // --- Randomized cross-check against an independent O(N^2) brute force that simply tries every
  // pair. Abilities are a shuffled distinct subset (matching the "all different" guarantee) and N
  // spans 2..8, so zero-answer (N = 2), endpoint-min, interior-optimum, and extreme-optimum
  // configurations all occur. Catches pairing, min, and width bugs the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForce() throws IOException {
    Random rnd = new Random(22945);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(7); // 2..8 developers
      int[] abilities = distinctAbilities(n, 30, rnd);
      String input = inputFor(abilities);
      String expected = Long.toString(bruteForce(abilities));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // --- Maximum input, strictly increasing 1..100000. A permutation of 1..N is always legal
  // (distinct positive abilities), and here the optimum is 50000*49999 = 2,499,950,000 -- larger
  // than Integer.MAX_VALUE (2,147,483,647). A solution accumulating the product in int overflows to
  // a negative/wrong value; only 64-bit arithmetic yields the right answer. The timeout also rules
  // out an O(N^2) brute force at this scale. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void strictlyIncreasingMaxInputRequiresLongArithmetic() throws IOException {
    int[] abilities = new int[100_000];
    for (int i = 0; i < abilities.length; i++) {
      abilities[i] = i + 1;
    }
    assertThat(runMain(inputFor(abilities))).isEqualTo("2499950000");
  }

  // --- The same maximum input mirrored: strictly decreasing 100000..1. The optimum is identical
  // (2,499,950,000) but is reached by advancing the right pointer instead of the left, so this
  // covers the other branch of the two-pointer at full scale while re-checking the long-overflow
  // requirement. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void strictlyDecreasingMaxInputRequiresLongArithmetic() throws IOException {
    int[] abilities = new int[100_000];
    for (int i = 0; i < abilities.length; i++) {
      abilities[i] = 100_000 - i;
    }
    assertThat(runMain(inputFor(abilities))).isEqualTo("2499950000");
  }

  // Reference brute force: the maximum over every pair of (developers between) * min(endpoints).
  // O(N^2), obviously correct, trustworthy only for the tiny N used by the randomized oracle. The
  // accumulator is long so the oracle itself never overflows.
  private static long bruteForce(int[] a) {
    long best = 0;
    for (int i = 0; i < a.length; i++) {
      for (int j = i + 1; j < a.length; j++) {
        best = Math.max(best, (long) (j - i - 1) * Math.min(a[i], a[j]));
      }
    }
    return best;
  }

  // Builds n distinct abilities by shuffling 1..pool and taking the first n (pool >= n).
  private static int[] distinctAbilities(int n, int pool, Random rnd) {
    List<Integer> values = new ArrayList<>(pool);
    for (int v = 1; v <= pool; v++) {
      values.add(v);
    }
    Collections.shuffle(values, rnd);
    int[] abilities = new int[n];
    for (int i = 0; i < n; i++) {
      abilities[i] = values.get(i);
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

  private static String[] linesOf(StdOut out) {
    return out.capturedString().replace("\r\n", "\n").trim().split("\n");
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
