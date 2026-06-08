package boj.boj28449;

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
 * BOJ 28449 누가 이길까 (Who Will Win) -- tally the outcomes of a round-robin between two teams.
 *
 * <p>The HI team has {@code N} members and the ARC team has {@code M} members. Every member of one
 * team plays every member of the other exactly once, so the tournament is {@code N * M} matches. In
 * a match the participant with the higher coding skill wins; equal skills draw.
 *
 * <p>Input: line 1 is {@code "N M"} -- the team sizes ({@code 1 <= N, M <= 100,000}); line 2 holds
 * the HI team's {@code N} skills, line 3 the ARC team's {@code M} skills, each space-separated,
 * every skill an integer in {@code [1, 100,000]}. Output: one line {@code "<HI wins> <ARC wins>
 * <draws>"} -- the three totals over all {@code N * M} matches, space-separated, in that order.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>The two official samples pass verbatim.</b> The mixed-skill 4x3 tournament
 *       ({@link #officialSampleOneTalliesMixedSkillTournament}) and the identical-roster 5x5
 *       ({@link #officialSampleTwoIdenticalRostersSplitEvenly}) reproduce the statement's printed
 *       answers {@code "7 4 1"} and {@code "10 10 5"}.
 *   <li><b>The three outcomes land in the right columns.</b> A lone match isolates each result --
 *       HI win ({@link #singleMatchHigherSkilledHiParticipantWins}), ARC win
 *       ({@link #singleMatchHigherSkilledArcParticipantWins}) and draw
 *       ({@link #singleMatchEqualSkillIsADraw}) -- and the smoke test
 *       {@link #winsLossesAndDrawsAreTalliedAcrossEveryCrossTeamMatch} prints three distinct totals
 *       so a solver that transposes the ARC and draw columns misprints.
 *   <li><b>Equal skill is a draw, never a win.</b> When every pairing ties, both win columns stay
 *       zero ({@link #equalSkillsAcrossAllMatchesAreAllDraws}).
 *   <li><b>A clean sweep fills exactly one column.</b> When one team outclasses the other entirely
 *       the count is the full {@code N * M} in the winner's column and zero elsewhere
 *       ({@link #everyHiParticipantStrongerSweepsAllMatches},
 *       {@link #everyArcParticipantStrongerSweepsAllMatches}).
 *   <li><b>Every pairing is counted independently.</b> Duplicate skills within a team are distinct
 *       participants, so repeats multiply the tally rather than collapsing it
 *       ({@link #duplicateSkillsCountEachPairingIndependently}).
 *   <li><b>The totals need 64 bits.</b> At the size bound a single column reaches {@code 100,000 *
 *       100,000 = 10,000,000,000}, well past {@code Integer.MAX_VALUE}; the win
 *       ({@link #hiWinCountExceedsIntRange}) and draw ({@link #drawCountExceedsIntRange}) columns
 *       are each pinned to overflow an {@code int} accumulator.
 * </ul>
 *
 * <p>At scale the same two bound fixtures pin the intended sub-quadratic counting (a sort plus
 * binary search, {@code O((N + M) log M)}) against an {@code O(N * M)} double loop -- ten billion
 * comparisons at the bound -- via a {@link Timeout}, and
 * {@link #randomizedMatchesAgainstBruteForceOracle} cross-checks the judged output against an
 * obviously-correct brute force over all pairings. The {@code Main} under test is an empty stub
 * that reads nothing and prints nothing, so every assertion here is RED until the solution is
 * implemented.
 */
class MainTest {

  // --- Official sample 1 from the problem statement. HI {1000, 90, 3, 20000} vs ARC {1, 3,
  // 100000},
  // a 4x3 = 12-match tournament. Every HI member beats ARC's 1 and 3 but loses to 100000, and HI's
  // own 3 additionally ties ARC's 3 -- so HI 2+2+1+2 = 7, ARC 1+1+1+1 = 4, draw 1. Output "7 4 1".
  // ---

  @Test
  @StdIo({"4 3", "1000 90 3 20000", "1 3 100000"})
  void officialSampleOneTalliesMixedSkillTournament(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7 4 1");
  }

  // --- Official sample 2 from the problem statement. Identical rosters HI {1,2,3,4,5} vs ARC
  // {1,2,3,4,5}, a 5x5 = 25-match tournament. The five equal-skill pairings draw; of the other 20
  // the wins split evenly, C(5,2) = 10 each way. Tally HI 10, ARC 10, draw 5. Output "10 10 5". ---

  @Test
  @StdIo({"5 5", "1 2 3 4 5", "1 2 3 4 5"})
  void officialSampleTwoIdenticalRostersSplitEvenly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10 10 5");
  }

  // --- One match, HI stronger: skills {5} vs {2}. The lone pairing is an HI win, so the tally is
  // 1/0/0. Pins that a strictly higher HI skill scores in the first column. ---

  @Test
  @StdIo({"1 1", "5", "2"})
  void singleMatchHigherSkilledHiParticipantWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 0 0");
  }

  // --- One match, ARC stronger: skills {2} vs {5}. The lone pairing is an ARC win, so the tally is
  // 0/1/0. Pins that a strictly higher ARC skill scores in the second column, not the first. ---

  @Test
  @StdIo({"1 1", "2", "5"})
  void singleMatchHigherSkilledArcParticipantWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1 0");
  }

  // --- One match, equal skill: {4} vs {4}. Equal skills draw, so the tally is 0/0/1 -- neither win
  // column moves. Pins the tie rule against an off-by-one that would award the draw as a win. ---

  @Test
  @StdIo({"1 1", "4", "4"})
  void singleMatchEqualSkillIsADraw(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 1");
  }

  // --- End-to-end smoke test over a 3x2 = 6-match tournament. HI {1,5,7} vs ARC {3,7}:
  //   1 vs 3 -> ARC, 1 vs 7 -> ARC
  //   5 vs 3 -> HI,  5 vs 7 -> ARC
  //   7 vs 3 -> HI,  7 vs 7 -> draw
  // Totals: HI 2, ARC 3, draw 1. The three numbers are all distinct, so a solver that prints the
  // columns in any other order (e.g. wins/draws/losses) misprints. ---

  @Test
  @StdIo({"3 2", "1 5 7", "3 7"})
  void winsLossesAndDrawsAreTalliedAcrossEveryCrossTeamMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3 1");
  }

  // --- Every pairing ties: HI {3,3} vs ARC {3,3}, four matches all drawn. Tally 0/0/4. Pins that
  // equal skills never leak into either win column. ---

  @Test
  @StdIo({"2 2", "3 3", "3 3"})
  void equalSkillsAcrossAllMatchesAreAllDraws(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 4");
  }

  // --- HI outclasses ARC entirely: HI {10,20} vs ARC {1,2,3}, every one of the 2x3 = 6 matches an
  // HI win. Tally 6/0/0. Pins a clean sweep filling exactly the HI column. ---

  @Test
  @StdIo({"2 3", "10 20", "1 2 3"})
  void everyHiParticipantStrongerSweepsAllMatches(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6 0 0");
  }

  // --- Mirror of the sweep, ARC outclassing HI: HI {1,2} vs ARC {10,20,30}, all 6 matches ARC
  // wins.
  // Tally 0/6/0. Pins the sweep filling the ARC column, guarding against a solver hard-coding which
  // team supplies the winner. ---

  @Test
  @StdIo({"2 3", "1 2", "10 20 30"})
  void everyArcParticipantStrongerSweepsAllMatches(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 6 0");
  }

  // --- Duplicate skills are distinct participants, so each pairing is counted on its own. HI
  // {2,2,5}
  // vs ARC {2,5}, 3x2 = 6 matches:
  //   2 vs 2 -> draw, 2 vs 5 -> ARC
  //   2 vs 2 -> draw, 2 vs 5 -> ARC
  //   5 vs 2 -> HI,   5 vs 5 -> draw
  // Totals: HI 1, ARC 2, draw 3. A solver that de-duplicates skills before counting would lose the
  // repeated 2 and misprint. ---

  @Test
  @StdIo({"3 2", "2 2 5", "2 5"})
  void duplicateSkillsCountEachPairingIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3");
  }

  // --- Upper bound with a one-sided sweep: N = M = 100,000, every HI skill 2 and every ARC skill
  // 1,
  // so all 100,000 * 100,000 = 10,000,000,000 matches are HI wins. The expected total overflows a
  // 32-bit int (Integer.MAX_VALUE = 2,147,483,647), pinning a long accumulator; the O((N+M) log M)
  // counting solution settles this instantly where an O(N*M) double loop -- ten billion comparisons
  // -- would blow the time limit. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void hiWinCountExceedsIntRange() throws IOException {
    int n = 100_000;
    int[] hi = filled(n, 2);
    int[] arc = filled(n, 1);
    assertThat(runMain(teamsInput(hi, arc))).isEqualTo("10000000000 0 0");
  }

  // --- Upper bound feeding the draw column: N = M = 100,000, every skill identical, so all
  // 10,000,000,000 matches draw. Pins that the draw accumulator is also a long (a distinct column
  // that could independently overflow) and that an all-draw bound still finishes inside the limit.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void drawCountExceedsIntRange() throws IOException {
    int n = 100_000;
    int[] hi = filled(n, 5);
    int[] arc = filled(n, 5);
    assertThat(runMain(teamsInput(hi, arc))).isEqualTo("0 0 10000000000");
  }

  // --- Small random tournaments drawn from a narrow skill band -- so draws and one-sided pairings
  // are both common -- checked against an independent brute force over every match. The narrow band
  // makes ties and tight comparisons frequent, catching off-by-ones (the >= vs > boundary) that a
  // binary-search count can hide. The oracle shares no logic with a counting solution, so the two
  // agree only when both are right. ---

  @Test
  void randomizedMatchesAgainstBruteForceOracle() throws IOException {
    Random rnd = new Random(28449);
    for (int trial = 0; trial < 1000; trial++) {
      int[] hi = randomTeam(rnd);
      int[] arc = randomTeam(rnd);
      String expected = bruteForce(hi, arc);
      assertThat(runMain(teamsInput(hi, arc)))
          .as("hi=%s arc=%s", Arrays.toString(hi), Arrays.toString(arc))
          .isEqualTo(expected);
    }
  }

  /**
   * A random team of 1..8 members, each skill in {@code [1, 6]} (a narrow band, so ties abound).
   */
  private static int[] randomTeam(Random rnd) {
    int size = 1 + rnd.nextInt(8);
    int[] team = new int[size];
    for (int i = 0; i < size; i++) {
      team[i] = 1 + rnd.nextInt(6); // skills in [1, 6]
    }
    return team;
  }

  /**
   * Independent oracle: the obviously-correct brute force. Compare every HI member against every
   * ARC member and tally the three outcomes, returning the formatted {@code "<HI wins> <ARC wins>
   * <draws>"} line. Shares no logic with a sort-and-count judge solution, so the two agree only
   * when both are right.
   *
   * @implNote {@code O(N * M)} time, where {@code N} and {@code M} are {@code hi.length} and
   *     {@code arc.length} -- acceptable only because the randomized fixtures keep both teams tiny.
   */
  private static String bruteForce(int[] hi, int[] arc) {
    long hiWins = 0;
    long arcWins = 0;
    long draws = 0;
    for (int a : hi) {
      for (int b : arc) {
        if (a > b) {
          hiWins++;
        } else if (a < b) {
          arcWins++;
        } else {
          draws++;
        }
      }
    }
    return hiWins + " " + arcWins + " " + draws;
  }

  /** An array of {@code length} copies of {@code value}. */
  private static int[] filled(int length, int value) {
    int[] a = new int[length];
    Arrays.fill(a, value);
    return a;
  }

  /**
   * Renders two teams as BOJ 28449 input: {@code "N M"} on line 1, the HI skills on line 2 and the
   * ARC skills on line 3, each space-separated.
   */
  private static String teamsInput(int[] hi, int[] arc) {
    StringBuilder sb = new StringBuilder();
    sb.append(hi.length).append(' ').append(arc.length).append('\n');
    appendTeam(sb, hi);
    appendTeam(sb, arc);
    return sb.toString();
  }

  private static void appendTeam(StringBuilder sb, int[] team) {
    for (int i = 0; i < team.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(team[i]);
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
