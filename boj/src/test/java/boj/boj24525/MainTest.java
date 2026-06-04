package boj.boj24525;

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
 * BOJ 24525 SKK 문자열 ("SKK String"), 2022 성균관대학교 프로그래밍 경진대회 Open Contest, problem C.
 *
 * <p>Given a single string {@code S} of uppercase English letters ({@code 1 <= |S| <= 100,000}),
 * print the length of the <em>longest contiguous substring</em> that is an "SKK string", or
 * {@code -1} if none exists. A substring is an SKK string when <b>both</b> hold:
 *
 * <ol>
 *   <li>the number of {@code 'K'}s equals exactly twice the number of {@code 'S'}s
 *       ({@code count('K') == 2 * count('S')}), and
 *   <li>it contains at least one {@code 'S'} and at least one {@code 'K'} (so {@code count('S') >=
 *       1} and {@code count('K') >= 2}).
 * </ol>
 *
 * <p>Any uppercase letter other than {@code 'S'}/{@code 'K'} is inert "filler": it does not change
 * the counts, but the reported length is the full index span, so filler that falls <em>between</em>
 * the chosen substring's endpoints still counts toward the length.
 *
 * <p><b>The two ways to get this wrong</b> -- and the tests that pin each down:
 *
 * <ul>
 *   <li><b>The ratio must be exact.</b> "{@code SK}" has one {@code S} and one {@code K}; since
 *       {@code 1 != 2*1} it is <em>not</em> an SKK string. A solution that accepts "some S, some K"
 *       wrongly returns 2. {@link #oneSAndOneKFailsTheExactRatio(StdOut)} and friends
 *       ({@link #equalNumbersOfSAndKFailRatio(StdOut)},
 *       {@link #tooManyKsBeyondTwiceSExcludeTheTail(StdOut)}) lock this down; the minimal valid
 *       string is "{@code SKK}" ({@link #minimalSkkStringHasLengthThree(StdOut)}).
 *   <li><b>"At least one S and one K" is not free.</b> Encode {@code S=+2}, {@code K=-1}, filler
 *       {@code 0}; then {@code count('K') == 2*count('S')} exactly when the substring's weighted
 *       sum is {@code 0}, i.e. when two prefix sums are equal. But an <em>all-filler</em> window
 *       also has weighted sum {@code 0} (equal prefix sums), so a bare prefix-sum solution reports
 *       a bogus positive length for inputs like "{@code ABABAB}". The fix is a parallel count of
 *       S-or-K characters, rejecting windows whose endpoints share that count.
 *       {@link #allFillerLettersOutputMinusOne(StdOut)},
 *       {@link #onlyKsWithoutAnySOutputMinusOne(StdOut)} and the single-character cases nail this
 *       trap -- the single subtlest point of the problem.
 * </ul>
 *
 * <p>Beyond the discriminators, {@link #fillerInsideTheWindowCountsTowardLength(StdOut)} pins the
 * "length is the span, not the S/K count" rule; {@link #recurringPrefixUsesEarliestIndex(StdOut)}
 * pins "pick the <em>earliest</em> equal prefix, not the most recent";
 * {@link #optimalWindowAnchoredAtStart(StdOut)} exercises the empty (index-0) prefix; and the
 * {@code @Timeout} max-size cases ({@link #allKsAtMaxLengthOutputMinusOne()},
 * {@link #allSsAtMaxLengthOutputMinusOne()}, {@link #maxLengthRepeatingSkkPatternFillsWholeSpan()})
 * force a linear solution and exercise the extreme prefix values ({@code -100,000 .. +200,000}). A
 * randomized cross-check against an exhaustive oracle
 * ({@link #randomizedSmallInputsMatchBruteForceOracle()}) covers corners the hand-picked cases
 * miss.
 */
class MainTest {

  // --- The three official samples (the anchors). ---

  // Official sample 1: "HELLOWORLD" has neither 'S' nor 'K', so no SKK substring exists -> -1
  // (not 0). Also the canonical all-filler case for the "at least one S and K" guard.
  @Test
  @StdIo("HELLOWORLD")
  void noSorKLettersOutputMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // Official sample 2: in "LUKESKYWALKER" the best window is "LUKESKYWAL" (indices 0..9): one S and
  // two K -> count(K)=2=2*count(S); adding the next K breaks the 2:1 ratio. Interior filler letters
  // L,U,E,Y,W,A count toward the length, so the answer is the span 10, not the S/K count 3.
  @Test
  @StdIo("LUKESKYWALKER")
  void officialSampleLukeSkywalkerIsTen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Official sample 3: "SUNGKYUNKWAN" as a whole has one S and two K (count(K)=2=2*count(S)) with
  // both present, so the entire length-12 string is an SKK string -> 12.
  @Test
  @StdIo("SUNGKYUNKWAN")
  void officialSampleSungkyunkwanIsTwelve(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- The ratio condition: count(K) must be EXACTLY twice count(S). ---

  // "SK" has 1 S and 1 K; 1 != 2*1, so it is NOT an SKK string. A solution that accepts "an S and a
  // K are present" wrongly returns 2. This is the single most common misreading of the problem.
  @Test
  @StdIo("SK")
  void oneSAndOneKFailsTheExactRatio(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The minimal valid SKK string: "SKK" is 1 S and 2 K -> length 3. Off-by-one or
  // missing-empty-prefix bugs would report -1 here.
  @Test
  @StdIo("SKK")
  void minimalSkkStringHasLengthThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // "SKSK" as a whole is 2 S and 2 K (2 != 2*2), so the full string fails; the longest valid window
  // is "KSK" (indices 1..3: 1 S, 2 K) -> 3, not 4. Discriminates "always return the whole length"
  // shortcuts.
  @Test
  @StdIo("SKSK")
  void equalNumbersOfSAndKFailRatio(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // "SKKKK" is 1 S and 4 K, so the whole string (4 != 2*1) fails; the longest valid substring is
  // the prefix "SKK" -> 3. Discriminates solutions that greedily extend to the end of the string.
  @Test
  @StdIo("SKKKK")
  void tooManyKsBeyondTwiceSExcludeTheTail(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // "SSKKKK" is 2 S and 4 K (4 = 2*2) with both present, so the whole length-6 string qualifies.
  // Confirms a multi-S / multi-K full-string match.
  @Test
  @StdIo("SSKKKK")
  void twoSsAndFourKsFillTheWholeString(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- The "at least one S and one K" guard: an all-filler window has weighted sum 0 but is NOT a
  // valid SKK string. This is the subtle correctness trap. ---

  // "ABABAB" has no S and no K. Every substring has weighted sum 0 (equal prefix sums), so a bare
  // prefix-sum solution lacking the count guard would report a positive length; the answer is -1.
  @Test
  @StdIo("ABABAB")
  void allFillerLettersOutputMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // "KK" is 2 K and 0 S: count(K)=2 but 2 != 2*0, and there is no S at all -> -1. Discriminates
  // solutions that forget the "at least one S" half of the requirement.
  @Test
  @StdIo("KK")
  void onlyKsWithoutAnySOutputMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // Minimum-length input |S|=1: a lone 'S' cannot contain a K -> -1.
  @Test
  @StdIo("S")
  void singleSCharacterOutputMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // Minimum-length input |S|=1: a lone 'K' cannot contain an S -> -1.
  @Test
  @StdIo("K")
  void singleKCharacterOutputMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Length is the index span (it includes interior filler), not the count of S/K characters.
  // ---

  // "SXKXK" is S K K interleaved with filler X's: 1 S, 2 K, so it is a valid SKK string spanning
  // all 5 characters -> 5, not 3. Discriminates solutions that report the S/K count instead of the
  // span (end - start).
  @Test
  @StdIo("SXKXK")
  void fillerInsideTheWindowCountsTowardLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Selecting the longest window when a prefix value recurs, and anchoring at the empty prefix.
  // ---

  // "SKKSKK": the weighted prefix sum returns to 0 at indices 0, 3 and 6. The longest sum-0 window
  // uses the EARLIEST occurrence (index 0), giving the whole length-6 string (2 S, 4 K). A solution
  // that stores the most-recent index instead would wrongly return 3.
  @Test
  @StdIo("SKKSKK")
  void recurringPrefixUsesEarliestIndex(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // "KSSKKK" as a whole is 2 S and 4 K (4 = 2*2); the optimal window starts at index 0, exercising
  // the seeded "prefix value 0 first seen at index 0" (the empty prefix) -> 6.
  @Test
  @StdIo("KSSKKK")
  void optimalWindowAnchoredAtStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Maximum-size inputs (|S| up to 100,000): a linear prefix-sum pass finishes well within the
  // 1 s judge limit; an O(n^2) substring scan does not. These also exercise the extreme prefix
  // values: all 'K' reaches -100,000 and all 'S' reaches +200,000. ---

  // 100,000 'K's: every prefix sum is distinct (strictly decreasing) so no window balances, and
  // there is no S anyway -> -1. Hits the -100,000 end of the prefix range at full scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allKsAtMaxLengthOutputMinusOne() throws IOException {
    String s = "K".repeat(100_000);
    assertThat(runMain(skkInput(s))).isEqualTo("-1");
  }

  // 100,000 'S's: strictly increasing prefix sums (no balanced window) and no K -> -1. Hits the
  // +200,000 end of the prefix range at full scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allSsAtMaxLengthOutputMinusOne() throws IOException {
    String s = "S".repeat(100_000);
    assertThat(runMain(skkInput(s))).isEqualTo("-1");
  }

  // "SKK" repeated 33,333 times (length 99,999): each block has weighted sum 0, so the prefix sum
  // returns to 0 at the end and the whole string is one big SKK string (33,333 S, 66,666 K) ->
  // 99,999. An O(n^2) solution times out at this scale; a linear one is instant.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxLengthRepeatingSkkPatternFillsWholeSpan() throws IOException {
    int blocks = 33_333;
    String s = "SKK".repeat(blocks);
    assertThat(runMain(skkInput(s))).isEqualTo(Integer.toString(3 * blocks));
  }

  // --- Randomized cross-check against an exhaustive oracle on short strings. A K-heavy alphabet
  // (plus a little filler) makes valid SKK windows frequent, catching off-by-one, "exactly twice",
  // and span-vs-count mistakes the hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(24525);
    for (int trial = 0; trial < 500; trial++) {
      int length = 1 + rnd.nextInt(16); // 1..16, small enough for the O(n^2) oracle
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
        int roll = rnd.nextInt(20);
        char c;
        if (roll < 5) {
          c = 'S'; // 25%
        } else if (roll < 14) {
          c = 'K'; // 45% (K-heavy so the 2:1 ratio is reachable)
        } else if (roll < 17) {
          c = 'A'; // 15% filler
        } else {
          c = 'B'; // 15% filler
        }
        sb.append(c);
      }
      String s = sb.toString();
      String expected = Integer.toString(longestSkkSubstring(s));
      assertThat(runMain(skkInput(s))).as("s=%s", s).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the length of the longest contiguous substring that is an SKK string, or
   * {@code -1} if none. Obviously correct by construction -- it scans every substring and applies
   * the definition directly ({@code count('K') == 2 * count('S')} with at least one {@code 'S'}) --
   * and trustworthy only for short strings.
   *
   * @implNote Brute force in {@code O(N^2)} time by extending each start index and keeping running
   *     S and K counts -- where {@code N} is the string length {@code s.length()}. Callers must
   *     keep {@code N} small (here {@code <= 16}). A non-negative answer is always {@code >= 3}
   *     (the minimal SKK string "SKK"); {@code -1} signals no valid substring.
   */
  private static int longestSkkSubstring(String s) {
    int n = s.length();
    int best = -1;
    for (int i = 0; i < n; i++) {
      int sCount = 0;
      int kCount = 0;
      for (int j = i; j < n; j++) {
        char c = s.charAt(j);
        if (c == 'S') {
          sCount++;
        } else if (c == 'K') {
          kCount++;
        }
        if (sCount >= 1 && kCount == 2 * sCount) {
          best = Math.max(best, j - i + 1);
        }
      }
    }
    return best;
  }

  /** Builds a BOJ 24525 input from a string: the whole string {@code s} on a single line. */
  private static String skkInput(String s) {
    return s + "\n";
  }

  private static String runMain(String input) throws IOException {
    return capture(input).trim();
  }

  private static String capture(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
