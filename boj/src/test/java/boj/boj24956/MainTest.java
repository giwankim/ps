package boj.boj24956;

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
 * BOJ 24956 나는 정말 휘파람을 못 불어 ("I really can't whistle") -- count the "whistle" subsequences.
 *
 * <p>Given a string {@code s} of length {@code n}, count the number of subsequences that spell a
 * whistle: one {@code 'W'}, then one {@code 'H'} after it, then <em>two or more</em> {@code 'E'}s
 * after that ({@code W H E E}, {@code W H E E E}, ...). Two whistles are different when their
 * chosen index sets differ, so the count is over <em>positions</em>, not over distinct result
 * strings. Because the count grows exponentially, print it modulo {@code 1_000_000_007}. The input
 * is two lines: {@code n} on the first, then {@code s} on the second.
 *
 * <p><b>Why "two or more E", not exactly "WHEE".</b> This is the single subtle point of the problem
 * and the one most likely to be implemented wrong. The accepted transition on each {@code 'E'} is
 * {@code whee = 2 * whee + whe} -- the doubling means an existing whistle may either keep its E-set
 * or absorb the new E, while a new size-2 set is born from each {@code WHE}. That is <em>not</em>
 * the textbook "count occurrences of the subsequence WHEE": for {@code "WHEEE"} the textbook count
 * is {@code C(3,2) = 3}, but the intended answer is {@code 4} (size-2 and size-3 subsets of the
 * three E's). {@link #threeTrailingEsGiveFourWhistles(StdOut)} pins this discriminator down;
 * {@link #fourTrailingEsGiveElevenWhistles(StdOut)} reinforces it with {@code 2^4 - 4 - 1 = 11}.
 *
 * <p>The remaining failure modes each have a guard: a whistle needs a {@code W} strictly before its
 * {@code H} ({@link #hBeforeWHasNoWhistle(StdOut)}), needs at least two E's after that H
 * ({@link #whWithSingleEHasNoWhistle(StdOut)}), counts only E's that follow the H
 * ({@link #eBeforeHIsNotCounted(StdOut)}), shares the trailing E's across every qualifying
 * {@code (W, H)} pair ({@link #multipleWhPairsShareTrailingEs(StdOut)}), ignores characters other
 * than {@code W}/{@code H}/{@code E} ({@link #ignoresCharactersOtherThanWhe(StdOut)}), and reduces
 * the exploding count modulo {@code 1_000_000_007} ({@link #largeTrailingErunIsReducedModulo()}). A
 * randomized cross-check against an exhaustive subsequence oracle
 * ({@link #randomizedSmallInputsMatchExhaustiveOracle()}) covers the corners the hand-picked cases
 * miss.
 */
class MainTest {

  private static final int MOD = 1_000_000_007;

  // --- No whistle: the four ingredients (W, then H, then >= 2 trailing E) are never all present.
  // ---

  // A lone 'W' cannot start a complete whistle -- the base of the DP must stay at 0.
  @Test
  @StdIo({"1", "W"})
  void singleWCharacterHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // A lone 'E' (no preceding W, H) likewise yields nothing.
  @Test
  @StdIo({"1", "E"})
  void singleECharacterHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // "WH" has the prefix but no E at all, so no whistle exists.
  @Test
  @StdIo({"2", "WH"})
  void whWithoutAnyEHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // "WHE" stops one E short: a whistle needs at least TWO E's, so the answer is 0, not 1.
  @Test
  @StdIo({"3", "WHE"})
  void whWithSingleEHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Plenty of E's but no "WH" before them -- nothing to anchor a whistle.
  @Test
  @StdIo({"4", "EEEE"})
  void allEsWithoutWhPrefixHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Order matters: in "HWEE" the only W sits AFTER the only H, so no valid (W before H) pair
  // exists.
  @Test
  @StdIo({"4", "HWEE"})
  void hBeforeWHasNoWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Minimal whistle and the doubling discriminator. ---

  // The smallest possible whistle, "WHEE", has exactly one realisation.
  @Test
  @StdIo({"4", "WHEE"})
  void minimalWhistleHasExactlyOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // THE discriminator: "WHEEE" has three trailing E's, so the whistles are every subset of >= 2 of
  // them -- C(3,2) + C(3,3) = 3 + 1 = 4. A "count the subsequence WHEE" solution would wrongly say
  // 3.
  @Test
  @StdIo({"5", "WHEEE"})
  void threeTrailingEsGiveFourWhistles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Four trailing E's: subsets of size >= 2 number 2^4 - 4 - 1 = 11, confirming the >= 2 reading at
  // a slightly larger scale than the 4-case.
  @Test
  @StdIo({"6", "WHEEEE"})
  void fourTrailingEsGiveElevenWhistles(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Multiplicity of W's and H's: every qualifying (W, H) pair contributes the same E-subsets.
  // ---

  // Two W's both precede the single H, so each pairs with the {E,E} set: 2 * 1 = 2 whistles.
  @Test
  @StdIo({"5", "WWHEE"})
  void multipleWsBeforeHEachFormWhistle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // "WHWHEE": qualifying (W,H) pairs are (W0,H1), (W0,H3), (W2,H3) -- three pairs, each able to use
  // the trailing {E,E}, so 3 whistles. Pins the "share the trailing E's across pairs" behaviour.
  @Test
  @StdIo({"6", "WHWHEE"})
  void multipleWhPairsShareTrailingEs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // An E that appears BEFORE the H must not be counted: in "WEHEE" only the two E's after the H
  // qualify, giving a single whistle. A solution that counts all E's after the W would say 4.
  @Test
  @StdIo({"5", "WEHEE"})
  void eBeforeHIsNotCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // "WHEHE": (W0,H1) sees the E's at indices 2 and 4 (two E's -> 1 whistle); (W0,H3) sees only the
  // single E at index 4 (-> 0). Total 1 -- each H counts only the E's that follow IT.
  @Test
  @StdIo({"5", "WHEHE"})
  void interleavedHsCountEsAfterEachH(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // "WHEEWHEE": the first H sees all four E's (2^4 - 4 - 1 = 11), and both H's see the final {E,E}
  // (1 each), so 11 + 1 + 1 = 13. A medium hand-computed vector mixing several pairs and E-sets.
  @Test
  @StdIo({"8", "WHEEWHEE"})
  void twoSeparateWhistleClustersCombine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("13");
  }

  // --- Characters that are not W/H/E are inert padding and must be skipped. ---

  // "QWQHQEQEQ" is "WHEE" buried in 'Q' noise; the Q's contribute nothing, so the answer is 1.
  @Test
  @StdIo({"9", "QWQHQEQEQ"})
  void ignoresCharactersOtherThanWhe(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Modulo: the count doubles on every trailing E, so it overflows int long before n is large.
  // ---

  // "WH" followed by 60 E's is a single (W,H) pair over 60 E's: 2^60 - 61 whistles, which dwarfs
  // both int and the modulus. The answer must be that value reduced mod 1_000_000_007. This fails
  // for any solution that forgets to take the modulus (or that overflows a 32-bit accumulator).
  @Test
  void largeTrailingErunIsReducedModulo() throws IOException {
    int k = 60;
    String s = "WH" + "E".repeat(k);
    String expected = Long.toString(singlePairWhistles(k));
    assertThat(runMain(whistleInput(s))).isEqualTo(expected);
  }

  // --- Randomised cross-check against an exhaustive subsequence oracle on tiny strings. Short
  // lengths and an E-heavy alphabet (plus a little non-WHE noise) make multi-E whistles frequent,
  // catching off-by-one and "exactly two E" mistakes the hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchExhaustiveOracle() throws IOException {
    Random rnd = new Random(24956);
    for (int trial = 0; trial < 500; trial++) {
      int length = 4 + rnd.nextInt(11); // 4..14, small enough for 2^length enumeration
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
        int roll = rnd.nextInt(20);
        char c;
        if (roll < 6) {
          c = 'W'; // 30%
        } else if (roll < 11) {
          c = 'H'; // 25%
        } else if (roll < 18) {
          c = 'E'; // 35%
        } else {
          c = 'A'; // 10% non-WHE noise
        }
        sb.append(c);
      }
      String s = sb.toString();
      String expected = Long.toString(enumerateWhistleCount(s));
      assertThat(runMain(whistleInput(s))).as("s=%s", s).isEqualTo(expected);
    }
  }

  // --- Maximum-size inputs: a linear streaming pass finishes instantly; an O(n^2) pair scan times
  // out. The exact official bound on n is unknown (site down), so 1,000,000 stands in as a
  // representative large length. ---

  // "WH" then ~10^6 E's: one (W,H) pair over a huge run, so the answer is (2^(n-2) - (n-2) - 1) mod
  // p. Exercises linear time, the modulus, and overflow handling all at once at scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void handlesMillionLengthWhistleInLinearTime() throws IOException {
    int n = 1_000_000;
    int k = n - 2;
    String s = "WH" + "E".repeat(k);
    String expected = Long.toString(singlePairWhistles(k));
    assertThat(runMain(whistleInput(s))).isEqualTo(expected);
  }

  // ~10^6 E's with no "WH" anywhere: the answer is 0 even at maximum size, exercising the no-match
  // path at scale (and confirming a giant input does not spuriously accumulate).
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void handlesMillionLengthNoWhistleInputAtScale() throws IOException {
    String s = "E".repeat(1_000_000);
    assertThat(runMain(whistleInput(s))).isEqualTo("0");
  }

  /**
   * Independent oracle: the number of whistle subsequences, found by enumerating every subset of
   * positions and accepting exactly those that spell {@code W}, then {@code H}, then {@code >= 2}
   * {@code E}s in index order. Obviously correct, exponential, and trustworthy only for tiny
   * strings.
   *
   * @implNote Brute force in {@code O(2^N * N)} time, enumerating all {@code 2^N} index subsets and
   *     scanning each -- where {@code N} is the string length {@code s.length()}. Callers must keep
   *     {@code N <= 30} (here {@code <= 14}). The result fits a {@code long} for these sizes and is
   *     well below the modulus, so no reduction is needed.
   */
  private static long enumerateWhistleCount(String s) {
    int n = s.length();
    long count = 0;
    for (int mask = 0; mask < (1 << n); mask++) {
      if (isWhistleSubset(s, mask)) {
        count++;
      }
    }
    return count;
  }

  /**
   * True when the positions selected by {@code mask}, read in increasing index order, spell exactly
   * one {@code 'W'}, then one {@code 'H'}, then two or more {@code 'E'}s.
   */
  private static boolean isWhistleSubset(String s, int mask) {
    int stage = 0; // 0: want W, 1: want H, 2: counting E's
    int eCount = 0;
    for (int i = 0; i < s.length(); i++) {
      if ((mask & (1 << i)) == 0) {
        continue;
      }
      char c = s.charAt(i);
      if (stage == 0) {
        if (c != 'W') {
          return false;
        }
        stage = 1;
      } else if (stage == 1) {
        if (c != 'H') {
          return false;
        }
        stage = 2;
      } else {
        if (c != 'E') {
          return false;
        }
        eCount++;
      }
    }
    return stage == 2 && eCount >= 2;
  }

  /**
   * Number of whistles a single {@code (W, H)} pair makes over {@code k} trailing E's: every subset
   * of size {@code >= 2}, i.e. {@code 2^k - k - 1}, taken mod {@code 1_000_000_007}.
   */
  private static long singlePairWhistles(int k) {
    long pow = modPow(2, k);
    return ((pow - (k + 1)) % MOD + MOD) % MOD;
  }

  /**
   * Computes {@code base^exp mod 1_000_000_007} by binary exponentiation.
   *
   * @implNote {@code O(log E)} multiplications -- where {@code E} is the exponent {@code exp}.
   */
  private static long modPow(long base, long exp) {
    long result = 1;
    long b = base % MOD;
    long e = exp;
    while (e > 0) {
      if ((e & 1) == 1) {
        result = result * b % MOD;
      }
      b = b * b % MOD;
      e >>= 1;
    }
    return result;
  }

  /** Builds a BOJ 24956 input from a string: the length {@code n} on one line, then {@code s}. */
  private static String whistleInput(String s) {
    return s.length() + "\n" + s + "\n";
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
