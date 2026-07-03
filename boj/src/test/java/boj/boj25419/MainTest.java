package boj.boj25419;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 25419 정수를 끝까지 외치자 ("Shout the integers to the end") -- who wins a turn-based shouting game.
 *
 * <p>Two students take turns shouting integers in {@code [1, n]}. The first student shouts first;
 * thereafter they alternate. If the previously shouted integer is {@code a}, the next student must
 * shout some integer in {@code [a + 1, a + k]} (so the very first shout lies in {@code [1, k]}). A
 * fixed list of banned integers may never be shouted by either student, and no integer above
 * {@code n} may ever be shouted. The student who cannot shout loses. Both play optimally; print
 * {@code 1} if the first student wins and {@code 0} if the second student wins.
 *
 * <p>Constraints (matching the sibling problem 25420, which shares this exact game setup): {@code 1
 * <= n <= 100,000}; {@code 1 <= k <= 100}; the banned list holds between {@code 1} and {@code n}
 * <em>distinct</em> integers, each in {@code [1, n]}, given in ascending order. Note that the
 * banned list is therefore <b>never empty</b>, so every input has a second line with at least one
 * number.
 *
 * <p>This is an <b>impartial combinatorial game</b>. Model the state as the last shouted value
 * {@code a}, with the sentinel {@code a = 0} for "nothing shouted yet" (the first student's turn).
 * From {@code a} the mover may step to any {@code x} with {@code a + 1 <= x <= min(a + k, n)} that
 * is not banned. A state is a <em>winning</em> state for the mover iff some legal step lands the
 * opponent on a <em>losing</em> state; a state with no legal step is losing. The answer is
 * {@code win(0)}: the first student wins iff the start state is winning. Because {@code 0} is never
 * banned and no shout can land on a banned value, every reachable state sits on a non-banned cell.
 *
 * <ul>
 *   <li><b>The output encoding (first vs. second student).</b> {@code 1} means the first student
 *       wins, {@code 0} means the second. The three setter-verified cases from the problem board
 *       pin both outcomes against a real {@code n = 10} game
 *       ({@link #boardCaseAllOddOpeningsBlockedSecondStudentWins(StdOut)},
 *       {@link #boardCaseOpeningOneIsWinningFirstStudentWins(StdOut)},
 *       {@link #boardCaseFewerBansFirstStudentWins(StdOut)}), and two one-move games separate the
 *       two outputs at the smallest scale
 *       ({@link #firstStudentWinsWhenOpponentIsImmediatelyStuck(StdOut)},
 *       {@link #secondStudentWinsWhenFirstStudentCannotOpen(StdOut)}).
 *   <li><b>The first move is capped at {@code k}, not {@code n}.</b> The opening shout must lie in
 *       {@code [1, min(n, k)]}, so a reachable-but-too-far integer cannot be used to open. Holding
 *       {@code n} and the bans fixed while raising {@code k} flips the result, isolating a solver
 *       that opens anywhere in {@code [1, n]}
 *       ({@link #firstMoveCannotReachBeyondKevenIfWithinN(StdOut)},
 *       {@link #raisingKLetsTheFirstStudentOpenAtTheEnd(StdOut)}).
 *   <li><b>A step spans at most {@code k} and never exceeds {@code n}.</b> With {@code k = 1} every
 *       step is exactly {@code +1}, so a single ban becomes an impassable wall whose
 *       <em>position</em> alone decides the winner
 *       ({@link #stepOfOneIsBlockedByABanAtAnOddDistance(StdOut)},
 *       {@link #stepOfOneIsBlockedByABanAtAnEvenDistance(StdOut)}).
 *   <li><b>Banned integers can never be shouted.</b> A banned opener must be skipped, forcing a
 *       jump to the next legal value ({@link #bannedOpenerForcesAJumpToTheNextInteger(StdOut)});
 *       with a generous {@code k} the first student may even leap straight to {@code n}
 *       ({@link #largeKLetsTheFirstStudentLeapToTheEnd(StdOut)}).
 *   <li><b>Optimal play needs the full game tree (memoization), and it must survive full depth.</b>
 *       At {@code n = 100,000} a top-down solver that re-expands shared sub-states is exponential
 *       for {@code k = 100} and recurses {@code n} frames deep for {@code k = 1}; two clocked cases
 *       force a memoized, depth-safe solution
 *       ({@link #deepStepOneChainIsResolvedWithinTheTimeLimit()},
 *       {@link #wideBranchingForcesMemoizationAtScale()}).
 * </ul>
 *
 * <p>The hand-picked answers are cross-checked by two independent oracles built differently from
 * the intended top-down memoization: an exhaustive, un-memoized minimax that transparently plays
 * out every line for tiny games ({@link #bruteForceFirstStudentWins(int, int, boolean[])}), and a
 * bottom-up boolean sweep over the states in descending order for larger games
 * ({@link #bottomUpFirstStudentWins(int, int, boolean[])}). The randomized sweeps
 * ({@link #randomTinyGamesMatchTheBruteForceOracle()},
 * {@link #randomModerateGamesMatchTheBottomUpOracle()}) drive both -- the tiny sweep also pins the
 * bottom-up oracle to the transparent brute force, so the larger cases can trust it.
 */
class MainTest {

  // --- The three setter-verified cases from the problem board. ---

  // From the problem's Q&A board (a case the setter confirmed). n = 10, k = 3, bans 1 2 4 5 7 9.
  // Every opening the first student could play (1, 2, or 3; 1 and 2 are banned) leaves the opponent
  // able to steer the game home, so the second student wins. Pins the 0 output on a real game.
  @Test
  @StdIo({"10 3", "1 2 4 5 7 9"})
  void boardCaseAllOddOpeningsBlockedSecondStudentWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The same board, with 7 no longer banned (n = 10, k = 3, bans 2 4 5 7 9 -> 2 4 5 7 9). Opening
  // with 1 hands the opponent a losing position, so the first student wins. Pins the 1 output and,
  // paired with the case above, shows a single ban swinging the whole game.
  @Test
  @StdIo({"10 3", "2 4 5 7 9"})
  void boardCaseOpeningOneIsWinningFirstStudentWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The third board case: n = 10, k = 3, bans 2 4 5 9. With still fewer walls the first student
  // again has a winning opening, so the answer is 1.
  @Test
  @StdIo({"10 3", "2 4 5 9"})
  void boardCaseFewerBansFirstStudentWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Base cases and the smallest games. ---

  // The smallest board, n = 1, forces the only integer (1) into the ban list (the list must be
  // non-empty and every value lies in [1, n]). The first student has nothing legal to shout and
  // loses at once, so the answer is 0. Guards the n = 1 boundary and the "no opening move" path.
  @Test
  @StdIo({"1 1", "1"})
  void singleIntegerBoardForcesAnImmediateLoss(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The smallest game the first student can win: n = 2, k = 1, ban 2. The first student shouts 1;
  // the opponent must shout 2 but it is banned, so the opponent is stuck and loses. The answer is
  // 1 -- the minimal case separating a first-student win (1) from the no-opening loss below.
  @Test
  @StdIo({"2 1", "2"})
  void firstStudentWinsWhenOpponentIsImmediatelyStuck(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The mirror of the case above: n = 2, k = 1, ban 1. Now the only opening (1, since k = 1) is
  // banned, so the first student cannot move and the second student wins. The answer is 0. Same
  // board as above with the ban moved by one -> the opposite winner, isolating the output encoding.
  @Test
  @StdIo({"2 1", "1"})
  void secondStudentWinsWhenFirstStudentCannotOpen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Family: the opening shout is capped at k, not at n. ---

  // n = 3, k = 2, bans 1 2. The opening must lie in [1, min(n, k)] = [1, 2], and both 1 and 2 are
  // banned, so the first student cannot open and loses (answer 0) -- even though 3 is a perfectly
  // legal, un-banned integer. A solver that lets the opening reach anywhere in [1, n] would shout 3
  // and wrongly report a first-student win.
  @Test
  @StdIo({"3 2", "1 2"})
  void firstMoveCannotReachBeyondKevenIfWithinN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The same board and bans as above (n = 3, bans 1 2) but k = 3. Now the opening window is
  // [1, min(3, 3)] = [1, 3], so the first student opens with 3; the opponent must shout in [4, 6],
  // all above n, and loses. The answer flips to 1. Paired with the k = 2 case, this pins the
  // opening cap at exactly min(n, k).
  @Test
  @StdIo({"3 3", "1 2"})
  void raisingKLetsTheFirstStudentOpenAtTheEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family: with k = 1 a single ban is a wall whose position decides the game. ---

  // n = 5, k = 1, ban 3. Every step is exactly +1, so the run is 0 -> 1 -> 2 and then 3 is banned:
  // the student sitting on 2 cannot advance and loses. Tracing the alternation from the start, the
  // first student loses, so the answer is 0. Exercises the k = 1 boundary and a mid-board wall.
  @Test
  @StdIo({"5 1", "3"})
  void stepOfOneIsBlockedByABanAtAnOddDistance(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The same n = 5, k = 1 board with the wall moved to 2. Now the reachable run is just 0 -> 1, and
  // the student forced onto 1 cannot pass the ban at 2, so the first student wins (answer 1). The
  // ban shifting by one flips the winner, proving the result tracks the wall's exact position.
  @Test
  @StdIo({"5 1", "2"})
  void stepOfOneIsBlockedByABanAtAnEvenDistance(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family: banned integers can never be shouted, so they must be jumped. ---

  // n = 5, k = 2, ban 1. The opening window is [1, 2] but 1 is banned, so the first student must
  // jump to 2; from there the opponent faces a losing position, so the first student wins (answer
  // 1). Confirms a banned opener is skipped rather than ending the game, as long as some other
  // opening remains.
  @Test
  @StdIo({"5 2", "1"})
  void bannedOpenerForcesAJumpToTheNextInteger(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // n = 3, k = 10, ban 1. Here k exceeds n, so the opening window is [1, min(3, 10)] = [1, 3]; with
  // 1 banned the first student leaps straight to 3, leaving the opponent with no legal shout. The
  // answer is 1. Exercises the k > n regime where min(n, k) -- not k -- bounds the reachable
  // shouts.
  @Test
  @StdIo({"3 10", "1"})
  void largeKLetsTheFirstStudentLeapToTheEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A slightly larger structured board: n = 6, k = 2, ban 6. With the top integer walled off, the
  // first student has a winning opening, so the answer is 1. A non-trivial hand case beyond the
  // tiny boards, guarding against an answer tuned to very small n.
  @Test
  @StdIo({"6 2", "6"})
  void sixCellBoardWithABanAtTheTopIsAFirstStudentWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family: memoization is mandatory, and the recursion must survive full depth. ---

  // A maximal k = 1 board, n = 100,000, with a single ban at n. Each state has exactly one move
  // (+1), so the only reachable line is 0 -> 1 -> ... -> 99,999 before the ban at 100,000 stops
  // play -- a chain 100,000 states deep. A naive top-down solver recurses that far and overflows
  // the stack, so this forces an iterative sweep or an enlarged stack. The winner is computed by
  // the
  // independent bottom-up oracle.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void deepStepOneChainIsResolvedWithinTheTimeLimit() throws IOException {
    int n = 100_000;
    int k = 1;
    boolean[] banned = new boolean[n + 1];
    banned[n] = true;
    assertThat(runMain(buildInput(n, k, banned))).isEqualTo(bottomUpFirstStudentWins(n, k, banned));
  }

  // A maximal branching board: n = 100,000, k = 100, with a single ban at n. Every state can step
  // to
  // up to 100 successors, so the un-memoized game tree is astronomically large; memoizing each
  // state's win/lose flag collapses the work to O(n*k) = ten million. An un-memoized top-down
  // solver
  // cannot finish in time. The winner is computed by the independent bottom-up oracle.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void wideBranchingForcesMemoizationAtScale() throws IOException {
    int n = 100_000;
    int k = 100;
    boolean[] banned = new boolean[n + 1];
    banned[n] = true;
    assertThat(runMain(buildInput(n, k, banned))).isEqualTo(bottomUpFirstStudentWins(n, k, banned));
  }

  // --- Randomized cross-checks against the two independent oracles. ---

  // Tiny games cross-checked against an exhaustive, un-memoized minimax that transparently plays
  // out
  // every line. Small boards with dense, random bans pack in stuck positions, jumped walls, and
  // capped openings, so this sweep alone exercises the opening cap, the per-step bound, the ban
  // skipping, and the win/lose alternation across hundreds of random shapes. The same sweep pins
  // the
  // bottom-up oracle to the brute force, licensing its use on the larger boards below.
  @Test
  void randomTinyGamesMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(25419L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 400; trial++) {
      int n = 1 + rng.nextInt(9); // 1..9, small enough for un-memoized minimax
      int k = 1 + rng.nextInt(4); // 1..4
      boolean[] banned = randomBanned(n, rng);
      String expected = bruteForceFirstStudentWins(n, k, banned);
      assertThat(bottomUpFirstStudentWins(n, k, banned))
          .as("oracle disagreement: n=%d k=%d banned=%s", n, k, bannedList(banned))
          .isEqualTo(expected);
      assertThat(runMain(buildInput(n, k, banned)))
          .as("n=%d k=%d banned=%s", n, k, bannedList(banned))
          .isEqualTo(expected);
    }
  }

  // Larger games cross-checked against the bottom-up sweep, a different evaluation order from the
  // intended top-down recursion. Trials alternate between sparse bans (long, branchy games) and
  // dense bans (many walls), and k ranges across the full 1..100, so the sweep reaches the scale
  // where memoization matters while still exercising the step bound and ban skipping.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomModerateGamesMatchTheBottomUpOracle() throws IOException {
    Random rng = new Random(2541900L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 150; trial++) {
      int n = 50 + rng.nextInt(1951); // 50..2000
      int k = 1 + rng.nextInt(100); // 1..100
      boolean dense = (trial % 2 == 0);
      boolean[] banned = randomBannedWithDensity(n, dense ? 0.5 : 0.03, rng);
      assertThat(runMain(buildInput(n, k, banned)))
          .as("n=%d k=%d dense=%b", n, k, dense)
          .isEqualTo(bottomUpFirstStudentWins(n, k, banned));
    }
  }

  /**
   * Independent exhaustive oracle: an un-memoized minimax that, from the start state {@code 0},
   * recursively plays out every legal line and reports {@code "1"} if the first student (the mover
   * at {@code 0}) can force a win, else {@code "0"}. Transparently correct and therefore
   * trustworthy only for tiny boards.
   *
   * @implNote Exponential time -- the game tree branches up to {@code k} ways and is up to
   *     {@code n} deep -- so callers must keep {@code n} small (here at most {@code 9}).
   */
  private static String bruteForceFirstStudentWins(int n, int k, boolean[] banned) {
    return moverWinsBrute(0, n, k, banned) ? "1" : "0";
  }

  private static boolean moverWinsBrute(int a, int n, int k, boolean[] banned) {
    int limit = Math.min(a + k, n);
    for (int next = a + 1; next <= limit; next++) {
      if (banned[next]) {
        continue;
      }
      if (!moverWinsBrute(next, n, k, banned)) {
        return true; // a step leaving the opponent in a losing state
      }
    }
    return false; // no legal step (or all lead to opponent wins) -> the mover loses
  }

  /**
   * Independent bottom-up oracle: sweeps states from {@code n} down to {@code 0}, marking each as a
   * win for the mover iff some legal step {@code [a + 1, min(a + k, n)]} onto a non-banned
   * successor lands on an already-resolved losing state. Returns {@code "1"} iff the start state
   * {@code 0} wins. Iterative (so depth-safe) and a different evaluation order from the intended
   * top-down memoization, which makes agreement real evidence; the tiny sweep additionally checks
   * it against the transparent brute force.
   *
   * @implNote {@code O(n*k)} time and {@code O(n)} space, scanning up to {@code k} successors of
   *     each of the {@code n + 1} states.
   */
  private static String bottomUpFirstStudentWins(int n, int k, boolean[] banned) {
    boolean[] moverWins = new boolean[n + 2];
    for (int a = n; a >= 0; a--) {
      boolean wins = false;
      int limit = Math.min(a + k, n);
      for (int next = a + 1; next <= limit; next++) {
        if (!banned[next] && !moverWins[next]) {
          wins = true;
          break;
        }
      }
      moverWins[a] = wins;
    }
    return moverWins[0] ? "1" : "0";
  }

  /** A random non-empty ban set over {@code [1, n]} (each integer banned with probability 0.5). */
  private static boolean[] randomBanned(int n, Random rng) {
    return randomBannedWithDensity(n, 0.5, rng);
  }

  /**
   * A random ban set over {@code [1, n]} where each integer is banned with the given probability,
   * with at least one integer guaranteed banned so the list is never empty (a problem constraint).
   */
  private static boolean[] randomBannedWithDensity(int n, double probability, Random rng) {
    boolean[] banned = new boolean[n + 1];
    boolean any = false;
    for (int v = 1; v <= n; v++) {
      if (rng.nextDouble() < probability) {
        banned[v] = true;
        any = true;
      }
    }
    if (!any) {
      banned[1 + rng.nextInt(n)] = true;
    }
    return banned;
  }

  /** The banned integers in ascending order, for diagnostic {@code as(...)} messages. */
  private static List<Integer> bannedList(boolean[] banned) {
    List<Integer> list = new ArrayList<>();
    for (int v = 1; v < banned.length; v++) {
      if (banned[v]) {
        list.add(v);
      }
    }
    return list;
  }

  /**
   * Builds BOJ 25419 input: {@code "n k"} on the first line, then the banned integers in ascending
   * order on the second line (always non-empty, per the constraints).
   */
  private static String buildInput(int n, int k, boolean[] banned) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(k).append('\n');
    boolean first = true;
    for (int v = 1; v < banned.length; v++) {
      if (banned[v]) {
        if (!first) {
          sb.append(' ');
        }
        sb.append(v);
        first = false;
      }
    }
    sb.append('\n');
    return sb.toString();
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
