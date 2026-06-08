package boj.boj15831;

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
 * BOJ 15831 준표의 조약돌 (Junpyo's Pebbles).
 *
 * <p>A line of N pebbles, each labeled {@code 'B'} (black) or {@code 'W'} (white), is given
 * alongside two bounds b and w. The answer is the maximum length of a contiguous substring that
 * contains at most b black pebbles and at least w white pebbles, or {@code 0} if no such substring
 * exists.
 */
class MainTest {

  // --- A representative worked example. With N=5, b=1, w=2 and "WBWWB" the candidate windows
  // are [0..3] WBWW (1 B, 3 W -> ok, length 4), [1..4] BWWB (2 B, 2 W -> too many blacks),
  // [0..4] (2 B -> too many), and various shorter ones. The maximum is 4, attained by the prefix
  // ending one position before the second black. Anchors the basic two-pointer behavior where
  // the right pointer extends greedily and the left pointer only moves once the black cap is
  // exceeded. ---

  @Test
  @StdIo({"5 1 2", "WBWWB"})
  void workedExampleNormalSlidingWindowScenario(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Smallest possible input that yields a non-zero answer: a single white pebble with b=0 and
  // w=1. The only window is the whole string, which has 0 blacks (<=0) and 1 white (>=1), so the
  // answer is 1. Pins that N=1 is handled and that the answer counts a one-character window. ---

  @Test
  @StdIo({"1 0 1", "W"})
  void singlePebbleWhiteSatisfiesBothBounds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Mirror of the previous case: a single black pebble with w=1 cannot satisfy the white
  // requirement no matter the window, so the answer is 0. Pins that no-valid-window reports 0
  // (not -1, not a negative width from the pointers passing each other). ---

  @Test
  @StdIo({"1 0 1", "B"})
  void singlePebbleBlackCannotProvideAnyWhite(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Five blacks in a row with w=1: there is no white anywhere, so no window can satisfy the
  // white requirement. A solution that only checks the black cap (which every window trivially
  // meets when b=0 only blocks all blacks) and forgets the white floor would wrongly report 0
  // for the wrong reason -- here the answer must be 0 because of insufficient whites, and a
  // solution that updates the answer on b-only success would report 1 for any single-character
  // window. Pins that BOTH bounds gate the answer. ---

  @Test
  @StdIo({"5 0 1", "BBBBB"})
  void allBlacksWithPositiveWhiteRequirementYieldsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Five whites in a row with b=0, w=1: every prefix and every suffix is valid, and the whole
  // string is too (0 blacks <= 0, 5 whites >= 1). The answer is N=5. Pins that a fully-valid input
  // is reported as the full length and that b=0 does not falsely shrink whites-only windows. ---

  @Test
  @StdIo({"5 0 1", "WWWWW"})
  void allWhitesUnderZeroBlackCapYieldsLengthN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Edge of the white-floor: w=0 means "any non-negative number of whites is fine", so the
  // answer reduces to the longest contiguous window with at most b blacks. With "BWBWBW" and b=2,
  // the whole string has 3 blacks so the full window is invalid; trimming either end drops one
  // black down to 2 and yields length 5 (e.g., [1..5] = "WBWBW"). A solution that requires
  // strictly positive w would wrongly skip every window with zero whites and might report a
  // smaller value. Pins the w=0 boundary. ---

  @Test
  @StdIo({"6 2 0", "BWBWBW"})
  void zeroWhiteRequirementCollapsesToAtMostBBlackOnly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- The complement of the all-blacks case: a string of all blacks with b large enough to
  // accept everything but w=2 cannot be satisfied because the string has zero whites. The answer
  // is 0. Pins that a generous b does not let a solution forget to count whites. ---

  @Test
  @StdIo({"4 4 2", "BBBB"})
  void zeroWhitesInStringWithPositiveWhiteRequirementYieldsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- b larger than the string length effectively disables the black cap, so the answer is just
  // "longest window with at least w whites". The whole string "WBWWB" has 3 whites, which exceeds
  // w=2, so the answer is N=5. Pins the b>=N degenerate case and that a solution does not bound
  // b by something smaller (e.g., by the number of blacks). ---

  @Test
  @StdIo({"5 5 2", "WBWWB"})
  void blackBudgetGreaterThanLengthLeavesOnlyWhiteConstraint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- The optimal window ends at the LAST character: "BWWWBW" with b=1, w=3. The full window
  // [0..5] has 2 blacks (positions 0 and 4) -- too many. The window [1..5] "WWWBW" has 1 black
  // and 4 whites, satisfying both bounds with length 5. No length-5 window starting at 0 is
  // valid. Pins that the algorithm continues to update the answer when the right pointer is at
  // r=n (i.e., does not exit the loop one iteration too early). ---

  @Test
  @StdIo({"6 1 3", "BWWWBW"})
  void optimalWindowEndsAtLastCharacter(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Mirror of the previous case: the optimal window STARTS at position 0. "WBWWWB" with b=1,
  // w=3: [0..4] "WBWWW" has 1 black and 4 whites -> length 5; the full window adds the trailing
  // black and exceeds the cap. Pins that a solution does not erroneously skip the prefix when
  // shrinking from the left. ---

  @Test
  @StdIo({"6 1 3", "WBWWWB"})
  void optimalWindowStartsAtFirstCharacter(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Whole-string validity. "BWWBW" with b=3, w=1 has 2 blacks (<=3) and 3 whites (>=1), so
  // the entire window is valid and the answer is N=5. A solution that always shrinks at least
  // one step or that never records the answer until after a left-pointer advance would report
  // 4 or less. Pins that the answer is updated before any pointer movement when the initial
  // window is already valid. ---

  @Test
  @StdIo({"5 3 1", "BWWBW"})
  void wholeStringSatisfiesBothBounds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- A black CLUSTER in the middle splits the string into two regions: "WWBBWWW" with b=1,
  // w=2. Any window spanning both blacks (positions 2 and 3) has 2 blacks and is rejected. The
  // best windows are [0..2] "WWB" (length 3) and [3..6] "BWWW" (length 4); the right side wins.
  // Pins the shrink-left branch when the black cap is exceeded by a back-to-back pair. ---

  @Test
  @StdIo({"7 1 2", "WWBBWWW"})
  void blackClusterForcesWindowToOneSide(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Two distinct windows of equal maximum length: "WBWBW" with b=1, w=2. Both [0..2] "WBW"
  // and [2..4] "WBW" satisfy the bounds with length 3, and no longer window does (the full
  // string has 2 blacks). The answer 3 is unique even though two witness positions exist; a
  // solution that, say, freezes the answer on the first valid window without re-checking later
  // ones would still report 3 here. The deeper purpose: pins that the algorithm reports the
  // LENGTH and is not sensitive to which window it picks. ---

  @Test
  @StdIo({"5 1 2", "WBWBW"})
  void twoEquallyLongValidWindowsYieldUniqueLength(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Randomised cross-check against an independent O(N^2) brute force that enumerates every
  // window. Random strings of length 1..8 over {B, W} with random b and w in [0..n] cover every
  // hand-case mode: all-blacks, all-whites, mixed with cluster, valid whole, empty answer, and
  // both endpoint-optimal placements. Catches off-by-one, wrong-bound-direction, and "uses index
  // when it meant value" bugs that the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForce() throws IOException {
    Random rnd = new Random(15831);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rnd.nextInt(8); // 1..8 pebbles
      int b = rnd.nextInt(n + 1); // 0..n
      int w = rnd.nextInt(n + 1); // 0..n
      String s = randomBwString(n, rnd);
      String input = inputFor(n, b, w, s);
      String expected = Integer.toString(bruteForce(s, b, w));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // --- Large all-whites input. N=200000 of 'W' with b=0 and w=1: the whole string is the answer
  // (zero blacks, all whites). This exercises the algorithm at scale on the path where the right
  // pointer sweeps from 0 to n while the left pointer never advances -- if the solution
  // accidentally has O(N^2) behavior (say, by rescanning the window on each step) it will time
  // out here. The timeout also rules out an obvious quadratic brute force. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allWhitesMaxInputForcesPureRightPointerSweep() throws IOException {
    int n = 200_000;
    char[] chars = new char[n];
    java.util.Arrays.fill(chars, 'W');
    String s = new String(chars);
    assertThat(runMain(inputFor(n, 0, 1, s))).isEqualTo(Integer.toString(n));
  }

  // --- Large alternating BWBWBW... input with the black cap set one below the total black count
  // (b = n/2 - 1) and the white floor set to the total white count (w = n/2). The whole string
  // has too many blacks, so the answer is achieved by dropping the leftmost black via shrinking
  // the left pointer to position 1: the window [1..n-1] has n/2-1 blacks and n/2 whites,
  // satisfying both bounds with length n-1. The mirror (dropping the rightmost black) gives a
  // window with one fewer white, which fails the floor -- so the algorithm must actually shrink
  // left, not just trim the right. Exercises the shrink-left branch at maximum scale. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void alternatingPebblesMaxInputForcesShrinkLeft() throws IOException {
    int n = 200_000;
    char[] chars = new char[n];
    for (int i = 0; i < n; i++) {
      chars[i] = (i % 2 == 0) ? 'B' : 'W';
    }
    String s = new String(chars);
    int b = n / 2 - 1;
    int w = n / 2;
    assertThat(runMain(inputFor(n, b, w, s))).isEqualTo(Integer.toString(n - 1));
  }

  // Reference brute force: for every window [i..j], count blacks and whites and update the best
  // length when both bounds hold. O(N^2), obviously correct, trustworthy only for the tiny N
  // used by the randomised oracle.
  private static int bruteForce(String s, int b, int w) {
    int best = 0;
    for (int i = 0; i < s.length(); i++) {
      int blacks = 0;
      int whites = 0;
      for (int j = i; j < s.length(); j++) {
        if (s.charAt(j) == 'B') {
          blacks++;
        } else {
          whites++;
        }
        if (blacks <= b && whites >= w) {
          best = Math.max(best, j - i + 1);
        }
      }
    }
    return best;
  }

  private static String randomBwString(int n, Random rnd) {
    char[] chars = new char[n];
    for (int i = 0; i < n; i++) {
      chars[i] = rnd.nextBoolean() ? 'B' : 'W';
    }
    return new String(chars);
  }

  private static String inputFor(int n, int b, int w, String s) {
    return n + " " + b + " " + w + "\n" + s + "\n";
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
