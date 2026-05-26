package boj.boj1484;

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
 * BOJ 1484 다이어트 (Diet).
 *
 * <p>Given a positive integer G (G &le; 100,000), print every positive integer r such that r&sup2;
 * - l&sup2; = G for some positive integer l with 0 &lt; l &lt; r. Print each r on its own line in
 * ascending order, or {@code -1} if no such r exists.
 */
class MainTest {

  // --- Smallest input (G = 1) is the minimum natural number. The only candidate factor pair
  // (1, 1) collapses to d = s, which would force l = 0 < 1 -> no solution. Answer -1. Pins the
  // empty-result branch at the input lower bound. ---

  @Test
  @StdIo("1")
  void minimumInputProducesNoSolution(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- G = 2 is in the G mod 4 == 2 residue class, which can never be written as r^2 - l^2: it
  // would require d * s = 2 with d, s the same parity, but (1, 2) is mixed and (2, 1) is the
  // reverse. Answer -1. The classic "impossible by parity argument" case at minimum scale. ---

  @Test
  @StdIo("2")
  void parityImpossibleSmallReturnsMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- G = 4 is a multiple of 4 but still unrepresentable: factor pairs (1, 4) have mixed
  // parity, and (2, 2) would force d = s -> l = 0, which is forbidden. The only "4-mod-4 trap"
  // in the spec. ---

  @Test
  @StdIo("4")
  void fourModFourWithOnlyTrivialFactorIsImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- G = 6 also lies in the mod-4 == 2 class. Companion to the G = 2 case but slightly larger
  // so a buggy "exhaust all l up to G" approach has to iterate more before concluding -1. ---

  @Test
  @StdIo("6")
  void anotherParityImpossibleReturnsMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- G = 3 is the smallest G with any valid answer: factor pair (1, 3) -> r = 2, l = 1, and
  // 2^2 - 1^2 = 3. Output 2. The minimum solvable input; guards the first transition out of the
  // "impossible" region. ---

  @Test
  @StdIo("3")
  void smallestSolvableInputReturnsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- G = 5: factor pair (1, 5) -> r = 3, l = 2. Verify: 3^2 - 2^2 = 9 - 4 = 5. Output 3. A
  // single-answer odd G that uses a non-trivial l > 1. ---

  @Test
  @StdIo("5")
  void oddGiveSingleAnswerR(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- G = 7: factor pair (1, 7) -> r = 4, l = 3. Verify: 4^2 - 3^2 = 16 - 9 = 7. Output 4. A
  // prime odd G that has exactly one valid pair (since only (1, G) is available). ---

  @Test
  @StdIo("7")
  void primeOddGiveSingleAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- G = 8: factor pair (2, 4) -> r = 3, l = 1. Verify: 3^2 - 1^2 = 9 - 1 = 8. Output 3. The
  // SMALLEST even G with any answer; G = 4 has none, G = 6 has none, G = 8 finally works because
  // 8 has a same-parity factor pair other than the trivial (sqrt(8), sqrt(8)). Pins the parity
  // boundary on the even-G side. ---

  @Test
  @StdIo("8")
  void smallestEvenGiveWithAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- G = 9 = 3^2: only valid factor pair is (1, 9) -> r = 5, l = 4 (verify 25 - 16 = 9). The
  // pair (3, 3) would give l = 0, forbidden. Output 5. A perfect-square G that still has exactly
  // one answer. ---

  @Test
  @StdIo("9")
  void perfectSquareGiveSingleAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- G = 16: factor pair (2, 8) -> r = 5, l = 3 (verify 25 - 9 = 16). The pair (4, 4) would
  // give l = 0, forbidden. The pair (1, 16) has mixed parity. Output 5. A power-of-two G that
  // illustrates how only one of several factor pairs is admissible. ---

  @Test
  @StdIo("16")
  void powerOfTwoSingleAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- G = 15: TWO valid factor pairs. (3, 5) -> r = 4, l = 1 (verify 16 - 1 = 15); (1, 15) ->
  // r = 8, l = 7 (verify 64 - 49 = 15). Output two lines "4" then "8" in ascending order. The
  // canonical multi-answer case; pins both "find more than one solution" AND "emit in ascending
  // order" behaviors. A solution that prints in discovery order (e.g. visits the larger r
  // first) would fail. ---

  @Test
  @StdIo("15")
  void twoAnswersEmittedInAscendingOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\n")).containsExactly("4", "8");
  }

  // --- G = 21: factor pairs (3, 7) -> r = 5, l = 2 (verify 25 - 4 = 21); (1, 21) -> r = 11, l =
  // 10 (verify 121 - 100 = 21). Companion to G = 15 with a wider spread between answers (5 vs
  // 11), exercising the algorithm across a longer pointer-sweep. ---

  @Test
  @StdIo("21")
  void twoAnswersWithWiderSpread(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\n")).containsExactly("5", "11");
  }

  // --- G = 27: factor pairs (3, 9) -> r = 6, l = 3 (verify 36 - 9 = 27); (1, 27) -> r = 14, l =
  // 13 (verify 196 - 169 = 27). The smaller r here is NOT 2 or 3 (the algorithm cannot find it
  // by trying tiny l); proves the algorithm scans across the middle of the (l, r) space, not
  // just the boundaries. ---

  @Test
  @StdIo("27")
  void twoAnswersBothInteriorR(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\n")).containsExactly("6", "14");
  }

  // --- G = 45: THREE valid factor pairs. (5, 9) -> r = 7, l = 2 (verify 49 - 4 = 45); (3, 15)
  // -> r = 9, l = 6 (verify 81 - 36 = 45); (1, 45) -> r = 23, l = 22 (verify 529 - 484 = 45).
  // Output three lines "7", "9", "23". A 3-answer case demonstrates the algorithm doesn't stop
  // after the first match and continues to scan the full range. ---

  @Test
  @StdIo("45")
  void threeAnswersAcrossEntireRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\n")).containsExactly("7", "9", "23");
  }

  // --- Maximum-constraint G = 100,000. Factorization 100000 = 2^5 * 5^5 yields twelve admissible
  // same-parity (d, s) pairs with d < s, producing twelve r values from r = 325 (paired with
  // l = 75) up to r = 25001 (paired with l = 24999). Tests both the upper bound of the input
  // range AND the longest possible output for any input. @Timeout guards against an O(G^2)
  // approach (10^10 ops would TLE). ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumGiveProducesAllTwelveAnswers() throws IOException {
    assertThat(runMain("100000\n").split("\n"))
        .containsExactly(
            "325", "350", "550", "665", "1025", "1270", "2510", "3133", "5005", "6254", "12502",
            "25001");
  }

  // --- G = 99,998 lies in the mod-4 == 2 class at near-maximum scale: no same-parity factor
  // pair exists. Output -1. Combines the parity-impossibility branch with a near-maximum G,
  // ensuring the algorithm both terminates in time AND returns the empty-answer signal after
  // scanning through the full candidate range without recording anything. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void nearMaximumImpossibleByParity() throws IOException {
    assertThat(runMain("99998\n")).isEqualTo("-1");
  }

  // --- G = 99,999 = 3^2 * 41 * 271 has an entirely odd factorization, producing six valid
  // odd-odd factor pairs and six r values: 320, 468, 1240, 5560, 16668, 50000. The largest r =
  // 50000 = (G + 1) / 2 sits exactly at the algorithm's termination boundary 2r - 1 = G; a
  // solution that uses {@code while (... < N)} instead of {@code <= N} on the bound would miss
  // it. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void nearMaximumOddProducesSixAnswers() throws IOException {
    assertThat(runMain("99999\n").split("\n"))
        .containsExactly("320", "468", "1240", "5560", "16668", "50000");
  }

  // --- G = 92681 is prime, so the only same-parity factor pair is (1, 92681), producing the
  // single answer r = 46341 (paired with l = 46340; verify 46341^2 - 46340^2 = 92681). What
  // makes this G special: 46341 is the smallest positive integer with r^2 > Integer.MAX_VALUE
  // (46341^2 = 2,147,488,281 vs. MAX_VALUE 2,147,483,647), so when the loop reaches hi = 46341
  // the intermediate hi * hi wraps to a negative int. The algorithm nonetheless emits the right
  // answer: int subtraction is computed mod 2^32, and the TRUE value of hi^2 - lo^2 stays
  // bounded by ~g + 2*hi < Integer.MAX_VALUE throughout (loop invariant), so the modular result
  // equals the true result. Pins this property so a refactor that breaks it -- e.g. comparing
  // hi * hi directly against (g + lo * lo) instead of subtracting, which would compare wrapped
  // values without the canceling modulus -- gets caught here. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void intOverflowBoundaryStillProducesCorrectAnswer() throws IOException {
    assertThat(runMain("92681\n")).isEqualTo("46341");
  }

  // --- Randomized cross-check against an independent brute force over G in [1, 1000]. The
  // range is wide enough that the parity-impossibility classes (G mod 4 == 2 and G = 4) appear
  // frequently alongside single-answer and multi-answer cases. Catches off-by-one bugs in the
  // two-pointer advancement, the termination bound (strict < vs <=), and the empty-result -1
  // branch that the hand-crafted cases might miss. ---

  @Test
  void randomizedSmallGiveMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(1484);
    for (int trial = 0; trial < 200; trial++) {
      int g = 1 + rnd.nextInt(1000);
      String expected = bruteForceExpectedOutput(g);
      assertThat(runMain(g + "\n")).as("G=%d", g).isEqualTo(expected);
    }
  }

  // Reference brute force: enumerate l = 1, 2, ... and for each l check whether l^2 + G is a
  // perfect square with sqrt rounded up; record the integer r each time and stop once 2l + 1 >
  // G (no smaller r^2 - l^2 difference is reachable). Returns "-1" when no answers, otherwise a
  // newline-joined ascending list of r values.
  private static String bruteForceExpectedOutput(int g) {
    StringBuilder sb = new StringBuilder();
    for (long l = 1; 2 * l + 1 <= g; l++) {
      long target = l * l + g;
      long r = (long) Math.sqrt(target);
      while (r * r < target) {
        r++;
      }
      if (r * r == target && r > l) {
        if (sb.length() > 0) {
          sb.append('\n');
        }
        sb.append(r);
      }
    }
    return sb.length() == 0 ? "-1" : sb.toString();
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
