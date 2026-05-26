package boj.boj16472;

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

/** BOJ 16472 고냥이 (Cat). */
class MainTest {

  // --- Smallest input (|S| = 1): a single character is always a length-1 window with one distinct
  // letter, so it fits any N >= 1. Guards the trivial lower bound. ---

  @Test
  @StdIo({"1", "a"})
  void singleCharacterStringReturnsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"26", "z"})
  void singleCharacterStringWithLargeNReturnsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Whole string is one repeated letter: distinct count is 1, so even N = 1 admits the entire
  // string. The answer must be |S|, not capped at any smaller window. ---

  @Test
  @StdIo({"1", "aaaa"})
  void allIdenticalCharactersReturnWholeLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- N exceeds the number of distinct letters: the constraint is slack, so the whole string
  // qualifies. Catches a solution that wrongly insists on using *exactly* N distinct letters. ---

  @Test
  @StdIo({"2", "aaaa"})
  void nGreaterThanDistinctCountReturnsWholeLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- N equals the total distinct count of the whole string: every window fits, so the answer is
  // the full length even though the string is not a single repeated character. ---

  @Test
  @StdIo({"3", "abcabc"})
  void nEqualsTotalDistinctReturnsWholeLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- N larger than the string length: still bounded by |S|. The distinct count (3) is well under
  // N (10), so the answer is the whole string, 3. Guards against reading N as an upper bound on the
  // answer length rather than on distinct letters. ---

  @Test
  @StdIo({"10", "abc"})
  void nLargerThanStringLengthReturnsWholeLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- N = 1 reduces to "longest run of a single character". In "aabba" the runs are aa, bb, a, so
  // the answer is 2 rather than the full length. ---

  @Test
  @StdIo({"1", "aabba"})
  void longestSingleCharacterRunWhenNIsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- N = 1 with no character ever repeating consecutively: every maximal single-letter run has
  // length 1, so the answer is 1. ---

  @Test
  @StdIo({"1", "abababab"})
  void alternatingCharactersWithNOneReturnsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "abcde"})
  void allDistinctCharactersWithNOneReturnsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Two interleaved letters with N = 2: the whole string has exactly 2 distinct, so the answer
  // is the full length 8. Pairs with the N = 1 case above on the same string to show N drives the
  // result. ---

  @Test
  @StdIo({"2", "abababab"})
  void alternatingTwoCharactersWithNTwoReturnsWhole(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Core sliding behavior: "abcba" with N = 2. The full string has 3 distinct; the best window
  // is "bcb" (length 3). Forces the window to both grow and shrink. ---

  @Test
  @StdIo({"2", "abcba"})
  void slidesWindowWhenDistinctExceedsN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Optimal window sits in the middle, away from both ends: "aabbbc" with N = 2 yields "aabbb"
  // (length 5); the trailing 'c' introduces a third letter. Guards against only ever measuring a
  // window anchored at the string start or end. ---

  @Test
  @StdIo({"2", "aabbbc"})
  void optimalWindowInMiddle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Optimal window at the very start: "aaabcd" with N = 1 -> the leading run "aaa" (length 3).
  // ---

  @Test
  @StdIo({"1", "aaabcd"})
  void optimalWindowAtStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Optimal window at the very end: "abcddd" with N = 1 -> the trailing run "ddd" (length 3). A
  // solution that forgets to compare the final window after the loop ends would miss this. ---

  @Test
  @StdIo({"1", "abcddd"})
  void optimalWindowAtEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The central frequency-bookkeeping trap. In "aabbc" with N = 2 the answer is "aabb" (4).
  // When
  // 'c' enters, the window must shrink past BOTH leading 'a's: removing the first 'a' leaves count
  // a = 1, so the distinct count must NOT drop yet; only removing the second 'a' (count -> 0) frees
  // a slot. A solution that decrements distinct on every removal computes the wrong window. ---

  @Test
  @StdIo({"2", "aabbc"})
  void shrinkingDecrementsDistinctOnlyWhenCountReachesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Three equal-length blocks with N = 2: "aaabbbccc" admits "aaabbb" and "bbbccc" (both 6) but
  // not the whole string (3 distinct). Confirms the maximum is held across a shrink, not reset. ---

  @Test
  @StdIo({"2", "aaabbbccc"})
  void mustShrinkWhenThirdBlockEnters(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Upper bound on N: all 26 letters once each, N = 26. Every letter is allowed, so the answer
  // is the full length 26. Exercises the largest legal N. ---

  @Test
  @StdIo({"26", "abcdefghijklmnopqrstuvwxyz"})
  void fullAlphabetWithNTwentySixReturnsWhole(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("26");
  }

  // --- One below the maximum: 26 distinct letters with N = 25. Any 25-length window omits exactly
  // one letter, so the answer is 25; the full 26-length string has too many distinct. Confirms the
  // boundary just under the alphabet size. ---

  @Test
  @StdIo({"25", "abcdefghijklmnopqrstuvwxyz"})
  void fullAlphabetWithNTwentyFiveDropsOneCharacter(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("25");
  }

  // --- Performance, maximum length, single distinct letter. |S| = 100,000 all 'a' with N = 1 must
  // return 100,000. An O(n^2) scan (~10^10 ops) blows the time limit; the intended O(n) sliding
  // window is instant. Also checks the answer (100,000) is printed correctly as an int. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllSameCharacterRunsWithoutBruteForce() throws IOException {
    assertThat(runMain(inputFor(1, repeatedChar(100_000, 'a')))).isEqualTo("100000");
  }

  // --- Performance with a capped window at scale. A period-5 string ("abcde" x 20,000) has the
  // property that every length-5 window contains all 5 distinct letters, so with N = 4 the answer
  // is
  // exactly 4 even though the string is 100,000 long. Exercises constant churn of the window over a
  // maximal input. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largePeriodicAlphabetCapsAtNDistinct() throws IOException {
    assertThat(runMain(inputFor(4, periodic("abcde", 20_000)))).isEqualTo("4");
  }

  // --- Performance, whole-string path at scale. Same period-5 string with N = 5: the entire string
  // has exactly 5 distinct letters, so the answer is the full 100,000. Distinguishes the "answer is
  // the whole string" branch from the capped branch above on identical input. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largePeriodicAlphabetWithNEqualsDistinctReturnsWhole() throws IOException {
    assertThat(runMain(inputFor(5, periodic("abcde", 20_000)))).isEqualTo("100000");
  }

  // --- Randomized cross-check against an independent O(n^2) brute force. Small alphabets and short
  // strings make distinct-count changes, ties, and full-string fits all frequent, with N sampled
  // across 1..6 so it lands below, at, and above the string's distinct count. Catches shrink,
  // reset,
  // and off-by-one window-length bugs the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(42);
    for (int trial = 0; trial < 500; trial++) {
      int alphabetSize = 1 + rnd.nextInt(6); // 1..6 distinct possible letters
      int length = 1 + rnd.nextInt(40); // 1..40 characters
      String s = randomString(length, alphabetSize, rnd);
      int n = 1 + rnd.nextInt(6); // 1..6
      String input = inputFor(n, s);
      String expected = Integer.toString(bruteForceLongest(n, s));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference O(n^2) selection: the longest window starting at each index whose distinct-letter
  // count stays <= n. Obviously correct but too slow for the judge; trustworthy only for tiny n.
  private static int bruteForceLongest(int n, String s) {
    int best = 0;
    int len = s.length();
    for (int i = 0; i < len; i++) {
      int[] freq = new int[26];
      int distinct = 0;
      for (int j = i; j < len; j++) {
        if (freq[s.charAt(j) - 'a']++ == 0) {
          distinct++;
        }
        if (distinct > n) {
          break;
        }
        best = Math.max(best, j - i + 1);
      }
    }
    return best;
  }

  private static String randomString(int length, int alphabetSize, Random rnd) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append((char) ('a' + rnd.nextInt(alphabetSize)));
    }
    return sb.toString();
  }

  private static String repeatedChar(int length, char c) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(c);
    }
    return sb.toString();
  }

  private static String periodic(String unit, int repeats) {
    StringBuilder sb = new StringBuilder(unit.length() * repeats);
    for (int i = 0; i < repeats; i++) {
      sb.append(unit);
    }
    return sb.toString();
  }

  private static String inputFor(int n, String s) {
    return n + "\n" + s + "\n";
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
