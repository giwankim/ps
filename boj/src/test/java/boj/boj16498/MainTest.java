package boj.boj16498;

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
 * BOJ 16498 작은 벌점 (Smallest Penalty) -- pick one card from each of three hands to minimize the
 * spread of the three chosen values.
 *
 * <p>Three players form a team. Each player holds one or more cards, every card bearing a single
 * integer. Each player lays down exactly one card, leaving three on the table; the penalty is the
 * gap between the largest and smallest of them, {@code max(a, b, c) - min(a, b, c)}, where
 * {@code a}, {@code b}, {@code c} are the laid-down values. Find the smallest penalty achievable
 * over all one-card-per-player choices.
 *
 * <p>Input: line 1 is {@code "A B C"} -- the card counts of players one, two and three ({@code 1 <=
 * A, B, C <= 1,000}); line 2 holds player one's {@code A} values, line 3 player two's {@code B}
 * values, line 4 player three's {@code C} values, each line space-separated. Every value is an
 * integer with {@code |value| <= 100,000,000}, so the widest possible penalty is
 * {@code 200,000,000} -- comfortably inside {@code int}. Output: the single smallest penalty.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Penalty is the spread, and it is minimized.</b> The end-to-end
 *       {@link #smallestPenaltyAcrossThreeHands} pins {@code max - min} over the best triple, not a
 *       sum, not the spread of a fixed (e.g. first-card) triple.
 *   <li><b>A shared value collapses the penalty to zero.</b> When one number lives in all three
 *       hands the answer is {@code 0}, whether buried among distractors
 *       ({@link #sharedValueAcrossAllHandsGivesZeroPenalty}) or trivially
 *       ({@link #identicalSingleCardsGiveZeroPenalty}).
 *   <li><b>The roles rotate.</b> The min, median and max may each come from any player -- the
 *       median sits on player three in {@link #smallestPenaltyAcrossThreeHands}, on player one in
 *       {@link #spreadAcrossNegativeAndPositiveValues}, on player one again (with min on player
 *       three) in {@link #optimumNeedNotUseEachPlayersSmallestCard} -- so a solver that hard-codes
 *       which hand supplies an extreme breaks.
 *   <li><b>The optimum is not "everyone's smallest card".</b>
 *       {@link #optimumNeedNotUseEachPlayersSmallestCard} is solved only by climbing off the
 *       minimums of two hands.
 *   <li><b>Signed values order correctly.</b> All-negative
 *       ({@link #negativeCardsAreMinimizedCorrectly}) and sign-straddling
 *       ({@link #spreadAcrossNegativeAndPositiveValues}) hands must compare without assuming
 *       non-negative inputs.
 *   <li><b>Duplicates and the value bounds are harmless.</b> Repeated cards within a hand do not
 *       distort the spread ({@link #duplicateCardsWithinAHandDoNotDistortTheSpread}), and the full
 *       {@code +/-100,000,000} range yields the maximal {@code 200,000,000} penalty without
 *       overflow ({@link #extremeValueBoundsProduceMaximalSpread}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the intended {@code O((A + B + C)
 * log(A + B + C))} sort-and-sweep against an {@code O(A * B * C)} triple enumeration -- a billion
 * combinations at the upper bound -- and {@link #randomizedInputsMatchBruteForceOracle}
 * cross-checks the judged output against an obviously-correct brute force over all triples. The
 * {@code Main} under test is an empty stub that reads nothing and prints nothing, so every
 * assertion here is RED until the solution is implemented.
 */
class MainTest {

  // --- General end-to-end smoke test: one card per hand chosen to minimize max - min. hands
  // {1,5,9} / {3,7} / {6,10}: the tightest triple is {5,7,6} -> max 7, min 5 -> 2 (the median, 6,
  // comes from player three). A solution summing the cards, or one that just spreads the first
  // cards {1,3,6} -> 5, misprints. ---

  @Test
  @StdIo({"3 2 2", "1 5 9", "3 7", "6 10"})
  void smallestPenaltyAcrossThreeHands(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Smallest input (A = B = C = 1): a single card per hand leaves exactly one triple, so the
  // answer is just its spread. {5} / {2} / {8} -> max 8, min 2 -> 6. ---

  @Test
  @StdIo({"1 1 1", "5", "2", "8"})
  void singleCardPerPlayerIsTheOnlyTriple(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- A value present in all three hands drives the penalty to zero, even when surrounded by
  // distractors. {1,4,7} / {9,4} / {2,4}: choosing 4 from each gives max == min -> 0. A solver that
  // fails to discover the common value would report a positive spread. ---

  @Test
  @StdIo({"3 2 2", "1 4 7", "9 4", "2 4"})
  void sharedValueAcrossAllHandsGivesZeroPenalty(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The zero boundary in its simplest form: three identical single cards. {5} / {5} / {5} -> 0.
  // Pins that an equal triple yields no penalty at all. ---

  @Test
  @StdIo({"1 1 1", "5", "5", "5"})
  void identicalSingleCardsGiveZeroPenalty(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All-negative hands must order and compare correctly (no assumption that values are
  // non-negative). {-10,-3} / {-5,-1} / {-8,-2}: the best triple is {-3,-1,-2} -> max -1, min -3 ->
  // 2. ---

  @Test
  @StdIo({"2 2 2", "-10 -3", "-5 -1", "-8 -2"})
  void negativeCardsAreMinimizedCorrectly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- A spread straddling zero, with the median supplied by player one. {-5,2} / {0,6} / {-2,3}:
  // the tightest triple is {2,0,3} -> max 3, min 0 -> 3. Pins correct max/min across the sign
  // boundary. ---

  @Test
  @StdIo({"2 2 2", "-5 2", "0 6", "-2 3"})
  void spreadAcrossNegativeAndPositiveValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The optimum need not pick each hand's smallest card. {1,10} / {5,11} / {9,12}: taking every
  // minimum gives {1,5,9} -> 8, but climbing to {10,11,9} -> max 11, min 9 -> 2 is far better (and
  // here the min comes from player three, the max from player two). A greedy "spread the minimums"
  // solver misprints 8. ---

  @Test
  @StdIo({"2 2 2", "1 10", "5 11", "9 12"})
  void optimumNeedNotUseEachPlayersSmallestCard(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Repeated cards within a hand do not distort the spread. {4,4} / {1,1} / {7,7} collapses to
  // the lone distinct triple {4,1,7} -> max 7, min 1 -> 6. Pins that duplicates are just choices,
  // not double-counted weights. ---

  @Test
  @StdIo({"2 2 2", "4 4", "1 1", "7 7"})
  void duplicateCardsWithinAHandDoNotDistortTheSpread(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- The full value range +/-100,000,000 parses and compares without overflow, producing the
  // maximal penalty. {-100000000} / {0} / {100000000} -> max 1e8, min -1e8 -> 200000000, which
  // fits in int (< 2,147,483,647) but is well past anything a narrower type or a careless
  // accumulation would survive. ---

  @Test
  @StdIo({"1 1 1", "-100000000", "0", "100000000"})
  void extremeValueBoundsProduceMaximalSpread(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200000000");
  }

  // --- Upper bound on every hand: A = B = C = 1,000. The three hands carry the residue classes
  // 1, 2 and 0 (mod 3) over disjoint runs -- player one holds 3i+1, player two 3i+2, player three
  // 3i+3 for i in [0, 999] -- so any one-per-hand triple covers three distinct residues mod 3 and
  // therefore spans at least three consecutive integers, a spread of 2 (achieved by {3i+1, 3i+2,
  // 3i+3}); a spread of 0 or 1 is impossible. The O((A+B+C) log(A+B+C)) sort-and-sweep settles this
  // instantly where an O(A*B*C) enumeration of 1e9 triples would not, and the nonzero answer stops
  // a degenerate "always print 0" solution from sneaking through. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 1000;
    int[] p1 = new int[n];
    int[] p2 = new int[n];
    int[] p3 = new int[n];
    for (int i = 0; i < n; i++) {
      p1[i] = 3 * i + 1;
      p2[i] = 3 * i + 2;
      p3[i] = 3 * i + 3;
    }
    assertThat(runMain(handsInput(p1, p2, p3))).isEqualTo("2");
  }

  // --- Small random hands drawn from a narrow value band -- so shared values (zero penalty) and
  // small spreads are both common -- checked against an independent brute force over every triple.
  // The narrow band makes the boundary cases (min/median/max rotating across hands, ties, tight
  // clusters) frequent, catching off-by-ones a sort-and-sweep can hide. The oracle shares no logic
  // with a sweep solution, so the two agree only when both are right. ---

  @Test
  void randomizedInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(16498);
    for (int trial = 0; trial < 1000; trial++) {
      int[] p1 = randomHand(rnd);
      int[] p2 = randomHand(rnd);
      int[] p3 = randomHand(rnd);
      String expected = Integer.toString(minPenalty(p1, p2, p3));
      assertThat(runMain(handsInput(p1, p2, p3)))
          .as("p1=%s p2=%s p3=%s", Arrays.toString(p1), Arrays.toString(p2), Arrays.toString(p3))
          .isEqualTo(expected);
    }
  }

  /** A random hand of 1..8 cards, each value in {@code [-10, 10]}. */
  private static int[] randomHand(Random rnd) {
    int size = 1 + rnd.nextInt(8);
    int[] hand = new int[size];
    for (int i = 0; i < size; i++) {
      hand[i] = rnd.nextInt(21) - 10; // values in [-10, 10]
    }
    return hand;
  }

  /**
   * Independent oracle: the obviously-correct brute force. Enumerate every one-card-per-hand triple
   * and return the smallest {@code max - min}. Shares no logic with a sort-and-sweep judge
   * solution, so the two agree only when both are right.
   *
   * @implNote {@code O(A * B * C)} time, where {@code A}, {@code B} and {@code C} are
   *     {@code p1.length}, {@code p2.length} and {@code p3.length} -- acceptable only because the
   *     randomized fixtures keep the hands tiny.
   */
  private static int minPenalty(int[] p1, int[] p2, int[] p3) {
    int best = Integer.MAX_VALUE;
    for (int a : p1) {
      for (int b : p2) {
        for (int c : p3) {
          int max = Math.max(a, Math.max(b, c));
          int min = Math.min(a, Math.min(b, c));
          best = Math.min(best, max - min);
        }
      }
    }
    return best;
  }

  /**
   * Renders three hands as BOJ 16498 input: {@code "A B C"} on line 1, then one hand per line with
   * space-separated values.
   */
  private static String handsInput(int[] p1, int[] p2, int[] p3) {
    StringBuilder sb = new StringBuilder();
    sb.append(p1.length)
        .append(' ')
        .append(p2.length)
        .append(' ')
        .append(p3.length)
        .append('\n');
    appendHand(sb, p1);
    appendHand(sb, p2);
    appendHand(sb, p3);
    return sb.toString();
  }

  private static void appendHand(StringBuilder sb, int[] hand) {
    for (int i = 0; i < hand.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(hand[i]);
    }
    sb.append('\n');
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
