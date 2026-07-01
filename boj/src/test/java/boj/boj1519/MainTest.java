package boj.boj1519;

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
 * BOJ 1519 부분 문자열 뽑기 게임 ("Substring Picking Game") -- a win/lose game-theory DP over the natural
 * numbers.
 *
 * <p>A board shows a natural number {@code N}. Two players alternate; a move picks a <b>proper
 * substring</b> of the board number -- a positive integer formed by a contiguous run of its decimal
 * digits, excluding the whole number itself -- and subtracts it from the board. For example a board
 * of {@code 2309} offers {@code 2, 3, 9, 23, 30, 230, 309}: a leading-zero run collapses to its
 * integer value (the run {@code "09"} is just {@code 9}) and a zero-valued run is not a legal move.
 * A player who cannot move <b>loses</b>; this is why every single-digit board {@code 1..9} is a
 * losing position -- its only substring is itself, which is excluded. The program prints the
 * <b>smallest</b> move the first player can make that forces a win, or {@code -1} if the first
 * player cannot win.
 *
 * <p><b>Winning/losing recurrence.</b> Treating a board value {@code i} as a game position,
 * {@code i} is a <i>winning</i> position iff some legal move {@code ps} leaves the opponent on a
 * <i>losing</i> position {@code i - ps}; it is <i>losing</i> iff every move leaves the opponent
 * winning (or no move exists). Because a move only ever decreases the board, the positions form a
 * DAG and the outcomes are well defined bottom-up from the single digits. The answer for the root
 * {@code N} is the smallest winning move, or {@code -1} when {@code N} itself is losing. Note this
 * folds two questions -- "can the first player win?" and "with which smallest move?" -- into a
 * single value per position.
 *
 * <p><b>Why the tie-break is subtle.</b> The required move is the smallest <i>winning</i> move, not
 * the smallest substring. From {@code 23} the substrings are {@code {2, 3}}; subtracting {@code 2}
 * reaches {@code 21}, which is winning for the opponent, so {@code 2} is rejected and the answer is
 * {@code 3} (reaching the losing {@code 20}).
 * {@link #smallestWinningMoveSkipsTheLosingSmallerSubstring} pins this; a solver that printed the
 * smallest available substring would answer {@code 2}.
 *
 * <p><b>I/O contract.</b> Input is a single line, the board {@code N} with {@code 1 <= N <=
 * 1,000,000}. Output is a single line: the smallest winning first move, or {@code -1}.
 *
 * <p><b>Oracle.</b> The randomized sweep and the maximum-input case take their expected answers
 * from {@link #computeResults(int)}, an independent iterative bottom-up solver, rather than from
 * hand calculation. The hand-computed positions ({@code N <= 101}) are asserted directly, and each
 * one is a small worked example in the comment beside it, so those cases in turn anchor the oracle
 * the larger tests rely on.
 */
class MainTest {

  // --- Single-digit boards: no proper substring exists, so the player to move cannot move. ---

  // N = 1 is the smallest legal board. Its only substring "1" is the whole number, which is
  // excluded, so the first player has no legal move and immediately loses -> -1. Pins the lower
  // bound of the input range and the "cannot move => lose" rule.
  @Test
  @StdIo("1")
  void singleDigitOneHasNoMoveAndLoses(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // N = 9 is the largest single-digit board; like every digit it offers no proper substring, so the
  // first player loses -> -1. Guards against an off-by-one that treats 9 as movable.
  @Test
  @StdIo("9")
  void largestSingleDigitStillHasNoMove(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Smallest winning board. ---

  // N = 10's only proper substring is "1"; subtracting it reaches 9, a single-digit (losing)
  // position, so 10 is winning and the answer is 1. The "0" run is value 0 and is not a legal move,
  // so it never appears as a candidate.
  @Test
  @StdIo("10")
  void smallestTwoDigitBoardWinsBySubtractingOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A losing two-digit board reached by the only move landing on a winner. ---

  // N = 11's only substring is "1"; the single move 11 - 1 = 10 lands the opponent on a winning
  // position, so 11 has no move to a losing position and is itself losing -> -1.
  @Test
  @StdIo("11")
  void subtractingOneFromElevenHandsTheOpponentAWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // N = 20's sole substring is "2" ("0" is value 0 and illegal); 20 - 2 = 18 is winning for the
  // opponent, so 20 is a losing position -> -1. A second, structurally different losing case from
  // 11, here forced by the zero units digit pruning every move but one.
  @Test
  @StdIo("20")
  void roundTwentyIsALosingPositionWithItsSoleSubstring(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Winning via the repeated digit. ---

  // N = 22's only distinct substring is "2"; 22 - 2 = 20 is a losing position, so 22 is winning and
  // the answer is 2. Exercises substring de-duplication (both digits are 2) and a win whose move is
  // the repeated digit itself.
  @Test
  @StdIo("22")
  void repeatedDigitBoardWinsBySubtractingItself(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The tie-break: smallest WINNING move, not smallest substring. ---

  // N = 23 offers {2, 3}. Subtracting 2 reaches 21, which is winning for the opponent, so 2 is
  // not a winning move; subtracting 3 reaches the losing 20, so the answer is 3. A solver that
  // printed the smallest available substring would wrongly answer 2 -- this case separates them.
  @Test
  @StdIo("23")
  void smallestWinningMoveSkipsTheLosingSmallerSubstring(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // N = 21 offers {1, 2}, and BOTH are winning: 21 - 1 = 20 and 21 - 2 = 19 are both losing
  // positions. With several winning moves the rule demands the smallest, so the answer is 1. Pins
  // the minimum-over-winning-moves selection (distinct from 23, where only one move won).
  @Test
  @StdIo("21")
  void picksTheSmallestAmongSeveralWinningMoves(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // N = 29 offers {2, 9}. Subtracting 2 reaches the winning 27, so 2 fails; only subtracting 9
  // reaches the losing 20, so the answer is 9 -- the larger substring is the sole winning move.
  // Complements 23 by making the units (not the tens) digit the winning pick.
  @Test
  @StdIo("29")
  void onlyTheUnitsDigitWinsFromTwentyNine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // N = 30's sole substring is "3"; 30 - 3 = 27 is winning, so 30 is a losing position -> -1.
  // It is reached only after several plies of the recurrence (27 -> 25 -> 23 -> ... all win),
  // exercising a deeper bottom-up dependency than the 11/20 cases.
  @Test
  @StdIo("30")
  void roundThirtyIsLosing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Multi-digit substrings and leading-zero handling. ---

  // N = 100 offers {1, 10}: the runs "0", "00" are value 0 and excluded, "1" gives 1 and "10" gives
  // 10. Subtracting 1 reaches 99, which is winning, so 1 fails; subtracting the two-digit substring
  // 10 reaches the losing 90, so the answer is 10. The single most important hand case: the winning
  // move is a multi-digit substring, and the zero runs must be pruned for it to be found.
  @Test
  @StdIo("100")
  void multiDigitSubstringIsTheOnlyWinningMove(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // N = 101 offers {1, 10} ("01" collapses to 1, a duplicate). Both moves lose for the mover:
  // 101 - 1 = 100 is winning (answer 10 above) and 101 - 10 = 91 is winning, so 101 is itself a
  // losing position -> -1. A three-digit losing board whose every multi-digit move still lands the
  // opponent on a winner.
  @Test
  @StdIo("101")
  void threeDigitLosingPositionRejectsBothSubstrings(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Randomized cross-check against the independent bottom-up oracle. ---

  // Many random boards (1..5000, mixing one- through four-digit numbers, winning and losing
  // positions, single- and multi-digit winning moves, and the leading-zero runs of any board with a
  // 0 digit) are each solved and compared to computeResults. A fixed seed keeps the sweep
  // reproducible across JVMs; the small upper bound keeps every run's bottom-up table cheap while
  // still covering far more shapes than the hand cases above.
  @Test
  void randomBoardsMatchTheIndependentOracle() throws IOException {
    int maxN = 5_000;
    int[] oracle = computeResults(maxN);
    Random rng = new Random(1519L);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rng.nextInt(maxN); // 1..maxN
      assertThat(runMain(Integer.toString(n))).as("N=%d", n).isEqualTo(Integer.toString(oracle[n]));
    }
  }

  // --- The maximum legal board is parsed and solved within the time limit. ---

  // N = 1,000,000 is the largest legal input. Its answer is cross-checked against the independent
  // O(maxN * L^2) oracle since hand calculation is hopeless at this size, and the @Timeout guards
  // against an accidentally super-linear-per-board implementation (the reference solutions run well
  // under two seconds). The boj module already enlarges the test JVM stack, so a deeply recursive
  // solution is given room; the oracle here is iterative and so is immune regardless.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumBoardIsSolvedWithinTheTimeLimit() throws IOException {
    int n = 1_000_000;
    int[] oracle = computeResults(n);
    assertThat(runMain(Integer.toString(n))).isEqualTo(Integer.toString(oracle[n]));
  }

  /**
   * Independent bottom-up oracle: {@code result[i]} is the smallest legal move that wins from a
   * board showing {@code i}, or {@code -1} when {@code i} is a losing position (the player to move
   * cannot force a win). Single-digit boards {@code 0..9} have no proper substring, hence no move,
   * so they are losing. For {@code i >= 10} the position is winning iff some proper substring
   * {@code ps} -- a positive integer formed by contiguous digits of {@code i}, excluding {@code i}
   * itself -- leaves the opponent on a losing position {@code i - ps}; the smallest such {@code ps}
   * is recorded.
   *
   * <p>Written iteratively (not recursively) so it cannot stack-overflow even at {@code i =
   * 1,000,000}, and structured independently of any tabulated or recursive {@link Main}, so
   * agreement is genuine cross-evidence rather than a re-run of the same code.
   *
   * @implNote {@code O(maxN * L^2)} time and {@code O(maxN)} space, where {@code maxN} is the
   *     largest board solved and {@code L <= 7} is the digit count of a board (so {@code L^2}
   *     bounds the substrings inspected per board). Substring values are built incrementally to
   *     avoid per-substring string allocation; a proper substring of an {@code L}-digit board has
   *     at most {@code L - 1} digits, so its value never overflows a signed 32-bit {@code int}.
   */
  private static int[] computeResults(int maxN) {
    int[] result = new int[maxN + 1];
    for (int i = 0; i <= Math.min(9, maxN); i++) {
      result[i] = -1; // no proper substring -> no move -> losing
    }
    for (int i = 10; i <= maxN; i++) {
      String s = Integer.toString(i);
      int len = s.length();
      int best = -1;
      for (int a = 0; a < len; a++) {
        int ps = 0;
        for (int b = a; b < len; b++) {
          ps = ps * 10 + (s.charAt(b) - '0');
          if (a == 0 && b == len - 1) {
            continue; // the whole number is not a proper substring
          }
          if (ps == 0) {
            continue; // a zero-valued run is not a positive move
          }
          if (result[i - ps] == -1 && (best == -1 || ps < best)) {
            best = ps;
          }
        }
      }
      result[i] = best;
    }
    return result;
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
