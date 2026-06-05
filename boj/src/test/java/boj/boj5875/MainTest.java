package boj.boj5875;

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
 * BOJ 5875 오타
 *
 * <p>Kifa likes correct bracket strings but typed one on a cramped keyboard, so he may have swapped
 * a single opening bracket for a closing one (or vice versa). Given the line he typed -- consisting
 * only of {@code '('} and {@code ')'} -- report how many positions could be changed, one character
 * at a time, to turn the whole line into a correct bracket string. A correct bracket string is
 * defined recursively: {@code ()} is correct; if {@code A} is correct then {@code (A)} is correct;
 * if {@code A} and {@code B} are correct then {@code AB} is correct (the empty string is not
 * included, so the shortest correct string has length 2).
 *
 * <p>Input: a single line, the typed bracket sequence, of length {@code 1 <= L <= 100,000}. Output:
 * a single integer, the number of single-character changes that yield a correct bracket string
 * (which is {@code 0} when no single change can fix it). Time limit 1 s, memory 128 MB.
 *
 * <p><b>The guarantee shapes the answer.</b> Kifa makes <em>at most one</em> typo, so every judged
 * input is either already correct (zero typos) or a correct string with exactly one bracket
 * flipped. A single flip changes the open-minus-close total {@code T} by {@code +/-2}, so the
 * universe of inputs splits cleanly:
 *
 * <ul>
 *   <li>{@code T == 0}: already correct. No single flip keeps it correct (any flip pushes {@code T}
 *       to {@code +/-2}), so the answer is {@code 0} -- see
 *       {@link #alreadyCorrectMinimalCountsZero}.
 *   <li>{@code T == 2} (one excess open): only an open bracket can be flipped to a close, and only
 *       where every prefix beyond it stays at depth {@code >= 2}. The count is the number of opens
 *       after the last point the running depth was {@code <= 1} -- see
 *       {@link #excessOpenOnlyFinalOpenIsFlippable} for the reset boundary.
 *   <li>{@code T == -2} (one excess close): only a close before (and including) the first point the
 *       running depth dips to {@code -1} can be flipped back -- see
 *       {@link #excessCloseAlternatingPrefixCountsThree}.
 * </ul>
 *
 * <p>The tests pin behavior with a brute-force oracle ({@link #bruteForceFixablePoints}) that
 * literally re-implements the output definition: flip each position, test correctness, count the
 * successes. That oracle is obviously correct but {@code O(L^2)}; it agrees with the linear judge
 * answer on every in-domain string, which is exactly what
 * {@link #randomizedInDomainInputsMatchBruteForceOracle} cross-checks. Because the linear trick
 * relies on the at-most-one-typo guarantee, all inputs here stay in-domain (even length, balanced
 * or balanced with one flip); out-of-domain strings such as a lone {@code ")"} are never judged and
 * are not asserted.
 */
class MainTest {

  // --- The sole official sample. ---

  // ()(())))  : depth 1,0,1,2,1,0,-1,-2. The excess-close branch: the four ')' up to and including
  // the first depth = -1 (positions 2, 5, 6, 7 one-indexed) can each be flipped back to '(' to
  // recover a correct string, so the answer is 4.
  @Test
  @StdIo("()(())))")
  void officialSampleCountsFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Already-correct inputs (zero typos, allowed by "at most one"): the answer is always 0. ---

  // () : the shortest correct string. Flipping either bracket unbalances it, so 0.
  @Test
  @StdIo("()")
  void alreadyCorrectMinimalCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // (()) : a nested correct string. Every single flip breaks the balance -> 0.
  @Test
  @StdIo("(())")
  void alreadyCorrectNestedCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // ()() : a concatenated correct string. Still no single flip preserves correctness -> 0.
  @Test
  @StdIo("()()")
  void alreadyCorrectConcatenatedCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Smallest typo in each direction (length 2). ---

  // (( : one excess open (T = 2). Flipping the second '(' gives "()"; flipping the first gives ")("
  // which is wrong. Exactly one fix -> 1.
  @Test
  @StdIo("((")
  void minimalExcessOpenCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // )) : one excess close (T = -2). Flipping the first ')' gives "()"; flipping the second gives
  // ")(" which is wrong. Exactly one fix -> 1.
  @Test
  @StdIo("))")
  void minimalExcessCloseCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Excess-open (T = 2) cases: which opens are flippable depends on running depth. ---

  // ((() : depth 1,2,3,2. Depth is <= 1 only at the very start, so the 2nd and 3rd opens both sit
  // at
  // depth >= 2 thereafter and either can become ')': flipping the 2nd open gives "()()", flipping
  // the 3rd gives "(())". Two fixes -> 2.
  @Test
  @StdIo("((()")
  void excessOpenTwoFlippableOpensCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // ()(( : depth 1,0,1,2. The leading "()" returns depth to 0, resetting the candidate opens, so of
  // the two trailing opens only the final one can be flipped ("()()"); flipping the third gives
  // "())(" which dips negative. Exactly one fix -> 1. This pins the reset boundary at depth <= 1.
  @Test
  @StdIo("()((")
  void excessOpenOnlyFinalOpenIsFlippable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // (()(() : depth 1,2,1,2,3,2. The two flippable opens are interior (indices 3 and 4, zero-based),
  // touching neither end: flipping index 3 gives "(())()", flipping index 4 gives "(()())". Two
  // fixes that a "first/last open only" shortcut would miss -> 2.
  @Test
  @StdIo("(()(()")
  void excessOpenInteriorFlippableOpensCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Excess-close (T = -2) cases: closes up to the first depth = -1 are flippable. ---

  // ()()) ) : depth 1,0,1,0,-1. The first depth = -1 lands at the last char; the three ')' seen by
  // then (indices 1, 3, 4) each flip back to a correct string, e.g. "((()))", "()(())", "()()()".
  // Three fixes over an alternating prefix -> 3.
  @Test
  @StdIo("()()))")
  void excessCloseAlternatingPrefixCountsThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // (())) ) : depth 1,2,1,0,-1. Same answer as the alternating case but reached through a nested
  // prefix, so the running depth climbs to 2 before dipping; the three ')' before the first -1
  // (indices 2, 3, 4) are the fixes -> 3.
  @Test
  @StdIo("(())))")
  void excessCloseNestedPrefixCountsThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Maximum length (L = 100,000): O(L) time and the count's full range. ---

  // 49,999 opens then 50,001 closes: a deeply nested correct string with its last open flipped to a
  // close. The depth climbs to 49,999 then descends, first hitting -1 after the 50,000th close, so
  // every one of those 50,000 closes can be flipped back -> 50,000. An O(L^2) flip-and-rescan would
  // not finish within the 1 s judge limit.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxLengthExcessCloseCountsHalf() throws IOException {
    String input = "(".repeat(49_999) + ")".repeat(50_001);
    assertThat(runMain(input)).isEqualTo("50000");
  }

  // 50,001 opens then 49,999 closes: the mirror case, a nested correct string with its first close
  // flipped to an open. Depth is <= 1 only after the first char, so the remaining 50,000 opens are
  // each flippable -> 50,000. Guards O(L) time on the excess-open branch at full scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxLengthExcessOpenCountsHalf() throws IOException {
    String input = "(".repeat(50_001) + ")".repeat(49_999);
    assertThat(runMain(input)).isEqualTo("50000");
  }

  // 50,000 repetitions of "()": a maximum-length already-correct input (zero typos). No single flip
  // keeps it correct -> 0, computed without scanning every flip.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxLengthAlreadyCorrectCountsZero() throws IOException {
    String input = "()".repeat(50_000);
    assertThat(runMain(input)).isEqualTo("0");
  }

  // --- Randomized cross-check against the brute-force oracle over in-domain inputs only. Short
  // lengths and a mix of untouched/flipped balanced strings make excess opens, excess closes, and
  // the already-correct case all frequent, catching off-by-one boundary bugs the hand-picked cases
  // might miss. ---

  @Test
  void randomizedInDomainInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(5875);
    for (int trial = 0; trial < 2000; trial++) {
      int pairs = 1 + rnd.nextInt(12); // length 2..24
      char[] s = randomCorrectBracketString(rnd, pairs).toCharArray();
      // "at most one typo": leave it correct one time in four, otherwise flip a single character.
      if (rnd.nextInt(4) != 0) {
        int i = rnd.nextInt(s.length);
        s[i] = flip(s[i]);
      }
      String input = new String(s);
      String expected = Integer.toString(bruteForceFixablePoints(input));
      assertThat(runMain(input)).as("input=%s", input).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: re-implements the output definition directly by flipping each position in
   * turn and counting the flips that leave a correct bracket string. Obviously correct, far too
   * slow for the judge, trustworthy for short strings.
   *
   * @implNote {@code O(n^2)} time, where {@code n} is {@code s.length()}: an {@code O(n)}
   *     correctness scan per candidate position.
   */
  private static int bruteForceFixablePoints(String s) {
    char[] c = s.toCharArray();
    int count = 0;
    for (int i = 0; i < c.length; i++) {
      char original = c[i];
      c[i] = flip(original);
      if (isCorrectBracketString(c)) {
        count++;
      }
      c[i] = original;
    }
    return count;
  }

  /** A bracket string is correct iff no prefix has more closes than opens and the totals match. */
  private static boolean isCorrectBracketString(char[] c) {
    int balance = 0;
    for (char ch : c) {
      balance += ch == '(' ? 1 : -1;
      if (balance < 0) {
        return false;
      }
    }
    return balance == 0;
  }

  /**
   * Builds a random correct bracket string with {@code pairs} matched pairs (length {@code 2 *
   * pairs}) via a non-negative random walk: emit {@code '('} while any open remains, {@code ')'}
   * only while the running depth is positive, choosing freely when both are legal.
   *
   * @implNote {@code O(n)} time, where {@code n} is {@code 2 * pairs}.
   */
  private static String randomCorrectBracketString(Random rnd, int pairs) {
    StringBuilder sb = new StringBuilder(pairs * 2);
    int opensLeft = pairs;
    int depth = 0;
    int remaining = pairs * 2;
    while (remaining > 0) {
      boolean canOpen = opensLeft > 0;
      boolean canClose = depth > 0;
      boolean open = canOpen && (!canClose || rnd.nextBoolean());
      if (open) {
        sb.append('(');
        opensLeft--;
        depth++;
      } else {
        sb.append(')');
        depth--;
      }
      remaining--;
    }
    return sb.toString();
  }

  private static char flip(char bracket) {
    return bracket == '(' ? ')' : '(';
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
