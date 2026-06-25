package boj.boj2216;

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
 * BOJ 2216 문자열과 점수 ("Strings and Score") -- global sequence alignment, the Needleman-Wunsch DP.
 *
 * <p>Given two strings X and Y, blanks may be inserted anywhere into either string so that the two
 * become the same length; the strings are then laid out one above the other and each aligned column
 * is scored: <b>A</b> if the two characters are equal (a match, {@code 0 < A <= 10,000}), <b>B</b>
 * if exactly one of the two is a blank (a gap, {@code -10,000 <= B < 0}), or <b>C</b> if both are
 * real characters but differ (a mismatch, {@code -10,000 <= C < 0}). A column of two blanks is
 * forbidden. The task is to maximize the total score over all ways of inserting blanks.
 *
 * <p><b>I/O contract.</b> Line 1 holds the three integers {@code A B C}. Line 2 is X and line 3 is
 * Y; each string is non-empty and at most 3,000 characters. The program prints a single line: the
 * maximum achievable total score.
 *
 * <p><b>The interesting corner.</b> When two differing characters meet, the solver must weigh
 * taking the single mismatch ({@code + C}) against splitting that column into two gaps ({@code +
 * 2B}); the sign of {@code C - 2B} -- not a fixed choice -- decides the winner. Several cases below
 * pin both outcomes, including two ({@link #interiorMismatchIsKeptWhenItBeatsTwoGaps} /
 * {@link #interiorMismatchIsBrokenIntoTwoGapsWhenCheaper}) that feed the same strings with B and C
 * swapped and watch the optimal alignment's shape flip.
 *
 * <p><b>Range of the answer.</b> With lengths up to 3,000 and magnitudes up to 10,000 the score
 * stays well inside a signed 32-bit {@code int}: at most {@code 3000 * 10,000 = 3e7} when every
 * column matches, and no worse than {@code 6000 * -10,000 = -6e7} when every column is a gap. The
 * oracles nonetheless accumulate in {@code long} so the cross-checks cannot overflow regardless of
 * the instance under test.
 *
 * <p><b>Oracles.</b> Small generated inputs are checked against {@link #bruteForceMaxScore}, which
 * computes the answer through the <em>monotone-matching</em> reformulation -- an all-gaps baseline
 * of {@code B*(|X|+|Y|)} plus the best strictly increasing set of aligned columns -- an algorithm
 * structurally unlike the cell-by-cell DP a solver writes, so agreement is genuine evidence rather
 * than a shared bug. The maximum-size instance, where enumeration is infeasible, is cross-checked
 * against {@link #dpMaxScore}, an independently written bottom-up tabulation, and a separate
 * identical-strings case is anchored to the pure closed form {@code |X| * A}.
 */
class MainTest {

  // --- Base case: a single matching column earns exactly the match reward. ---

  // X = "a", Y = "a", A = 10. One column, equal characters -> A = 10. This isolates the dp[1][1]
  // diagonal-match branch that every longer alignment ultimately bottoms out on.
  @Test
  @StdIo({"10 -1 -5", "a", "a"})
  void singleMatchingPairScoresTheMatchReward(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- A differing pair: two gaps beat the mismatch when 2B > C. ---

  // X = "a", Y = "b" with B = -1, C = -5. Aligning them is a mismatch (-5); splitting into two gaps
  // "a-" over "-b" costs 2B = -2, which is larger. The optimum is -2. A solver that blindly walks
  // the diagonal whenever both characters are present would wrongly print -5.
  @Test
  @StdIo({"10 -1 -5", "a", "b"})
  void differingPairTakesTwoGapsWhenCheaperThanAMismatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-2");
  }

  // --- The mirror: the mismatch beats two gaps when C > 2B. ---

  // The same "a"/"b" pair but with B = -5, C = -1. Now the single mismatch (-1) beats two gaps
  // (2B = -10), so the optimum is -1. Together with the previous case this pins the three-way max:
  // neither the gap branch nor the diagonal branch may be hard-wired.
  @Test
  @StdIo({"10 -5 -1", "a", "b"})
  void differingPairTakesTheMismatchWhenCheaperThanTwoGaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Identical strings: every column is a match. ---

  // X = Y = "abc", A = 10 -> three matches -> 30. Guards against a solver that fails to reward the
  // straight diagonal, or that gratuitously inserts gaps where a clean match is available.
  @Test
  @StdIo({"10 -1 -5", "abc", "abc"})
  void identicalStringsScoreEveryColumnAsAMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30");
  }

  // --- A single interior deletion: one gap aligns the shared characters. ---

  // X = "abc", Y = "ac". The optimum matches a and c and gaps the extra 'b': 10 - 1 + 10 = 19.
  // Folding 'b' into a mismatch instead (a/a, b/c, c/-) would score 10 - 5 - 1 = 4, so this case
  // checks that a mid-string gap is preferred when it preserves two matches.
  @Test
  @StdIo({"10 -1 -5", "abc", "ac"})
  void oneInteriorGapAlignsTheSharedCharacters(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  // --- The published example, pinned exactly. ---

  // A B C = 10 -1 -5, X = "abc", Y = "dc". The optimum matches the trailing c (+10) and gaps the
  // non-matching prefix "ab" against "d" (three gaps, -3) for 7. A run that printed the wrong
  // branch
  // or mishandled the gap baseline would miss this canonical value.
  @Test
  @StdIo({"10 -1 -5", "abc", "dc"})
  void officialSampleIsAnswered(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Disjoint strings: gap everything when gaps beat mismatches. ---

  // X = "ab", Y = "cd" with B = -1, C = -5 and no characters in common. Gapping all four characters
  // ("ab--" over "--cd") costs 4B = -4, beating two diagonal mismatches (2C = -10). Optimum -4.
  @Test
  @StdIo({"10 -1 -5", "ab", "cd"})
  void disjointStringsGapEverythingWhenGapsBeatMismatches(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-4");
  }

  // --- The mirror for disjoint strings: align diagonally when mismatches beat gaps. ---

  // The same "ab"/"cd" but with B = -5, C = -1. Two diagonal mismatches (2C = -2) now beat four
  // gaps (4B = -20), so the optimum is -2. Confirms the gap-vs-mismatch tradeoff also governs
  // multi-column alignments, not just single pairs.
  @Test
  @StdIo({"10 -5 -1", "ab", "cd"})
  void disjointStringsAlignDiagonallyWhenMismatchesBeatGaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-2");
  }

  // --- Repeated characters: only one copy can match; the rest are gapped. ---

  // X = "aaa", Y = "a". Exactly one 'a' aligns (+10) and the two surplus 'a's become gaps (2B = -2)
  // for 8. This exercises the gap-only base row dp[i][0] (the unmatched X tail) and guards against
  // a
  // solver that rewards an 'a' more than once against a single Y character.
  @Test
  @StdIo({"10 -1 -5", "aaa", "a"})
  void extraCopiesAreGappedAroundASingleMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- A repeated block matched once, with the duplicate block gapped. ---

  // X = "abcabc", Y = "abc", A = 100, B = C = -1. One "abc" block matches (3 * 100 = 300) and the
  // other three characters are gapped (3B = -3) for 297. A larger instance than the singletons
  // above, mixing several matches with several gaps in one alignment.
  @Test
  @StdIo({"100 -1 -1", "abcabc", "abc"})
  void repeatedBlockMatchesOnceAndGapsTheRemainder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("297");
  }

  // --- Order matters: a reversed pair cannot match both characters. ---

  // X = "ab", Y = "ba". Because matches must keep their left-to-right order, a and b cannot both be
  // matched simultaneously. The optimum matches one of them and gaps the other two characters
  // (10 + 2B = 8); a solver that matched on multiset membership instead of order would over-credit.
  @Test
  @StdIo({"10 -1 -5", "ab", "ba"})
  void orderIsRespectedSoOnlyOneSwappedCharCanMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- A common subsequence matched, with the in-between characters gapped. ---

  // X = "abcde", Y = "ace", A = 5, B = -2, C = -3. The shared subsequence a, c, e matches (3 * 5)
  // while the interleaved 'b' and 'd' are gapped (2B = -4) for 11. Exercises alternating
  // match/gap columns across a longer alignment.
  @Test
  @StdIo({"5 -2 -3", "abcde", "ace"})
  void commonSubsequenceIsMatchedWithGapsForTheRest(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- A mismatch is kept in the interior when it is cheaper than two gaps. ---

  // X = "axc", Y = "ayc" with B = -5, C = -1. The flanking a and c match (+20); the differing
  // middle
  // column is kept as a single mismatch (C = -1) rather than split into two gaps (2B = -10), for
  // 19.
  @Test
  @StdIo({"10 -5 -1", "axc", "ayc"})
  void interiorMismatchIsKeptWhenItBeatsTwoGaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  // --- The same strings with B and C swapped: the middle column becomes two gaps. ---

  // X = "axc", Y = "ayc" with B = -1, C = -5. The a and c still match (+20), but now splitting the
  // differing middle into two gaps (2B = -2) beats the mismatch (C = -5), for 18. Identical input
  // strings to the case above, yet the optimal alignment's shape changes -- proof the solver weighs
  // C against 2B per column rather than committing to one structure.
  @Test
  @StdIo({"10 -1 -5", "axc", "ayc"})
  void interiorMismatchIsBrokenIntoTwoGapsWhenCheaper(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // --- Boundary parsing: A at its max and both penalties at their min. ---

  // A = 10,000, B = C = -10,000, X = Y = "z". The lone column matches for the maximum single-column
  // reward, 10,000. Confirms the extreme legal magnitudes parse and that the match branch wins even
  // when the penalties are as punishing as allowed.
  @Test
  @StdIo({"10000 -10000 -10000", "z", "z"})
  void maximumMagnitudeValuesAreParsedAndScored(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10000");
  }

  // --- Worst-case single column with both penalties at the floor. ---

  // A = 1, B = C = -10,000, X = "a", Y = "b". A single mismatch costs C = -10,000 while two gaps
  // cost 2B = -20,000, so the mismatch wins at -10,000. Stresses the negative extreme of the answer
  // and that the per-column max still distinguishes one penalty from two.
  @Test
  @StdIo({"1 -10000 -10000", "a", "b"})
  void worstCaseSingleColumnPicksTheLargerPenalty(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-10000");
  }

  // --- Randomized cross-check against the independent monotone-matching oracle. ---

  // Random short strings over a small alphabet (so matches and mismatches both occur often) with
  // randomized A, B, C spanning the legal ranges are each answered and compared to the brute-force
  // matching oracle. The small lengths keep the exponential enumeration cheap while still mixing
  // every branch -- the gap base rows, the C-vs-2B choice, and the order constraint -- across
  // hundreds of shapes. A fixed seed keeps the sweep reproducible across JVMs.
  @Test
  void randomSmallInputsMatchTheMatchingOracle() throws IOException {
    Random rng = new Random(2216L);
    for (int trial = 0; trial < 300; trial++) {
      int a = 1 + rng.nextInt(10000); // 1..10000
      int b = -1 - rng.nextInt(10000); // -1..-10000
      int c = -1 - rng.nextInt(10000); // -1..-10000
      String x = randomString(rng, 1 + rng.nextInt(6), 3); // length 1..6, alphabet {a,b,c}
      String y = randomString(rng, 1 + rng.nextInt(6), 3);
      String expected = Long.toString(bruteForceMaxScore(a, b, c, x, y));
      assertThat(runMain(buildInput(a, b, c, x, y)))
          .as("A=%d B=%d C=%d X=%s Y=%s", a, b, c, x, y)
          .isEqualTo(expected);
    }
  }

  // --- Maximum-length identical strings anchored to the pure closed form. ---

  // Two identical 3,000-character strings (random content, fed as both X and Y). Every column
  // matches regardless of the content, so the score is exactly |X| * A = 3000 * 10000 = 30,000,000,
  // a value verifiable by arithmetic alone and independent of any DP. Larger than the hand cases,
  // it
  // stresses the inner loops at the maximum length while keeping the expectation a closed form.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumLengthIdenticalStringsScoreEveryColumn() throws IOException {
    int n = 3000;
    String s = randomString(new Random(30000L), n, 4);
    assertThat(runMain(buildInput(10000, -10000, -10000, s, s))).isEqualTo("30000000");
  }

  // --- Maximum-length mixed strings cross-checked against the independent DP oracle. ---

  // Two random 3,000-character strings over a small alphabet, with randomized legal A, B, C,
  // compared against the bottom-up tabulation oracle (exhaustive enumeration is infeasible at this
  // size). The DP is O(|X| * |Y|) ~ 9e6 cells, comfortably inside the limit; @Timeout guards
  // against
  // an accidentally exponential or otherwise pathological implementation, and the oracle guards
  // correctness at the largest legal input.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumLengthInputsAreScoredWithinTheTimeLimit() throws IOException {
    Random rng = new Random(22160L);
    int a = 1 + rng.nextInt(10000);
    int b = -1 - rng.nextInt(10000);
    int c = -1 - rng.nextInt(10000);
    String x = randomString(rng, 3000, 4); // alphabet {a,b,c,d} -> frequent matches and mismatches
    String y = randomString(rng, 3000, 4);
    String expected = Long.toString(dpMaxScore(a, b, c, x, y));
    assertThat(runMain(buildInput(a, b, c, x, y))).isEqualTo(expected);
  }

  /**
   * Independent oracle via the monotone-matching reformulation: every alignment scores
   * {@code B*(|X|+|Y|)} for gapping every character, plus, for each column promoted from two gaps
   * to an aligned pair, the gain {@code (A or C) - 2B}. The promoted columns form a strictly
   * increasing matching of X- and Y-positions, so the maximum score is that all-gaps baseline plus
   * the best such matching. Computing the answer this way -- rather than cell by cell -- shares no
   * structure with the DP under test, so agreement is a real cross-check.
   *
   * @implNote Delegates to {@link #bestMatchingGain}, whose enumeration is exponential in the
   *     string lengths, so callers must keep both strings short (here at most six characters).
   */
  private static long bruteForceMaxScore(int a, int b, int c, String x, String y) {
    long allGapsBaseline = (long) b * (x.length() + y.length());
    return allGapsBaseline + bestMatchingGain(a, b, c, x, y, 0, 0);
  }

  /**
   * Best total gain over the all-gaps baseline obtainable by aligning a strictly increasing set of
   * columns drawn from {@code x[i..]} and {@code y[j..]}; each aligned pair {@code (p, q)} adds
   * {@code (x[p]==y[q] ? A : C) - 2B} (the cost of an aligned column minus the two gaps it
   * replaces). Returns 0 when no further column is worth aligning.
   *
   * @implNote Brute force: tries every next pair {@code (p >= i, q >= j)} and recurses on
   *     {@code (p+1, q+1)}, with no memoization, so the work is exponential in {@code m + n} where
   *     {@code m = x.length()} and {@code n = y.length()} are the string lengths -- intended only
   *     for the short strings of the small-input sweep.
   */
  private static long bestMatchingGain(int a, int b, int c, String x, String y, int i, int j) {
    long best = 0L; // align no further columns
    for (int p = i; p < x.length(); p++) {
      for (int q = j; q < y.length(); q++) {
        long perPair = (x.charAt(p) == y.charAt(q) ? a : c) - 2L * b;
        best = Math.max(best, perPair + bestMatchingGain(a, b, c, x, y, p + 1, q + 1));
      }
    }
    return best;
  }

  /**
   * Independent bottom-up DP oracle used where enumeration is infeasible: tabulates
   * {@code dp[i][j]} = best score aligning the first {@code i} characters of {@code x} with the
   * first {@code j} of {@code y}, from the gap-only base rows up. Written separately from
   * {@link Main} so it can stand in as an expected-value source at the maximum input size.
   *
   * @implNote {@code O(m * n)} time and space, where {@code m = x.length()} and {@code n =
   *     y.length()} are the string lengths; accumulates in {@code long} so the oracle cannot
   *     overflow.
   */
  private static long dpMaxScore(int a, int b, int c, String x, String y) {
    int m = x.length();
    int n = y.length();
    long[][] dp = new long[m + 1][n + 1];
    for (int i = 1; i <= m; i++) {
      dp[i][0] = (long) i * b;
    }
    for (int j = 1; j <= n; j++) {
      dp[0][j] = (long) j * b;
    }
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        long best = Math.max(dp[i - 1][j] + b, dp[i][j - 1] + b);
        long aligned = (x.charAt(i - 1) == y.charAt(j - 1)) ? a : c;
        dp[i][j] = Math.max(best, dp[i - 1][j - 1] + aligned);
      }
    }
    return dp[m][n];
  }

  /** Builds a length-{@code len} string of the first {@code alphabet} lowercase letters. */
  private static String randomString(Random rng, int len, int alphabet) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      sb.append((char) ('a' + rng.nextInt(alphabet)));
    }
    return sb.toString();
  }

  /** Builds BOJ 2216 input: {@code "A B C"} on line 1, then X on line 2 and Y on line 3. */
  private static String buildInput(int a, int b, int c, String x, String y) {
    return a + " " + b + " " + c + "\n" + x + "\n" + y + "\n";
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
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
