package boj.boj14453;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 14453 Hoof, Paper, Scissors -- USACO 2017 January Contest, Silver (problem 691). Not to be
 * confused with BOJ 14456 (the Bronze problem with numbered gestures).
 *
 * <p>Farmer John plays {@code N} known games of Hoof/Paper/Scissors against Bessie, who can predict
 * every one of FJ's gestures. Bessie is lazy: she plays one gesture for a while and is willing to
 * switch gestures <em>at most once</em>, so her play splits into a prefix run and a suffix run
 * around some change point {@code x} (with {@code x = 0} or {@code x = N} meaning she never
 * switches). Hoof beats Scissors, Scissors beats Paper, and Paper beats Hoof. Find the maximum
 * number of games she can win.
 *
 * <p><strong>Key reduction.</strong> Within a single run Bessie throws one gesture, and each
 * gesture beats exactly one other, so the most she can win across a run equals the frequency of the
 * single most common gesture FJ throws in that run. Hence the answer is {@code max over x of
 * (maxFreq(games[0..x)) + maxFreq(games[x..N)))}. The beats-mapping never constrains her choice; it
 * only relabels which gesture she throws to collect a given run.
 *
 * <p>Constraints: {@code 1 <= N <= 100,000}; each of the {@code N} following lines is exactly one
 * of {@code H}, {@code P}, or {@code S}. The answer is a win count in {@code [0, N]}, so it always
 * fits a signed 32-bit {@code int} -- there is no overflow trap here.
 *
 * <p>Input: line 1 is {@code N}; the next {@code N} lines each carry one gesture. Output is a
 * single line: the maximum number of games Bessie can win.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"5", "P", "P", "H", "P", "S"})
  void officialSampleSwitchesAfterTheFourthGame(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 4: prefix PPHP has Paper*3 (throw Scissors -> win 3); suffix S wins 1 -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Smallest legal input: N = 1, and the smallest size where a switch can matter, N = 2. ---

  @Test
  @StdIo({"1", "H"})
  void singleGameIsAlwaysWon(StdOut out) throws IOException {
    Main.main(new String[0]);
    // One game, no switch possible: Bessie throws the beating gesture (Paper beats Hoof) -> 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "H", "P"})
  void twoDifferentGesturesAreBothWonWithOneSwitch(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 1: Paper beats the H, then Scissors beats the P -> 1 + 1 = 2.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Uniform sequences: no switch is needed; the answer is N. Each also pins the beats rule. ---

  @Test
  @StdIo({"4", "H", "H", "H", "H"})
  void allHoofGesturesAreSweptByPlayingPaper(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Paper beats Hoof: throw Paper every game, never switch -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"4", "P", "P", "P", "P"})
  void allPaperGesturesAreSweptByPlayingScissors(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Scissors beats Paper: throw Scissors every game, never switch -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"4", "S", "S", "S", "S"})
  void allScissorsGesturesAreSweptByPlayingHoof(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Hoof beats Scissors: throw Hoof every game, never switch -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- A single switch between two clean blocks wins every game. ---

  @Test
  @StdIo({"6", "H", "H", "H", "P", "P", "P"})
  void twoEqualBlocksAreFullyWonBySwitchingInTheMiddle(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 3: Paper beats HHH (3), then Scissors beats PPP (3) -> 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"5", "H", "H", "P", "P", "P"})
  void twoUnequalBlocksAreFullyWonBySwitchingAtTheBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 2: 2 hooves then 3 papers -> 2 + 3 = 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- One switch cannot rescue an outlier stranded inside the other run (answer < N). ---

  @Test
  @StdIo({"6", "H", "H", "P", "H", "P", "P"})
  void loneHoofInsideThePaperRunIsSacrificed(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best split after game 2: HH (2) then PHPP whose Paper*3 wins 3; the stray H is lost -> 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5", "S", "H", "H", "P", "P"})
  void leadingScissorsIsSacrificedWhenTheTwoMainBlocksPayMore(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best split after game 3: SHH wins Hoof*2, then PP wins 2; the lone leading S is lost -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Interleaved gestures: a single switch cannot beat the most frequent gesture. ---

  @Test
  @StdIo({"5", "P", "H", "P", "H", "P"})
  void interleavedGesturesGainNothingFromSwitching(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No prefix/suffix pair beats Paper*3; the optimum equals the no-switch baseline -> 3.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The optimal change point can sit hard against either boundary. ---

  @Test
  @StdIo({"5", "S", "P", "P", "P", "P"})
  void optimalSwitchHappensRightAfterTheFirstGame(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 1: Hoof beats the leading S (1), then Scissors beats PPPP (4) -> 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5", "P", "P", "P", "P", "S"})
  void optimalSwitchHappensRightBeforeTheLastGame(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Split after game 4: Scissors beats PPPP (4), then Hoof beats the trailing S (1) -> 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Upper bounds: full-length input exercises the O(N) requirement and BufferedReader speed.
  // ---

  @Test
  void fullLengthUniformSequenceWinsEveryGame() throws IOException {
    int n = 100_000;
    char[] gestures = repeat('H', n);
    // Uniform input at the size limit: one gesture sweeps all N games, no switch -> 100000.
    assertThat(runMain(buildInput(gestures))).isEqualTo("100000");
  }

  @Test
  void fullLengthTwoBlockSequenceWinsEveryGameAcrossTheSwitch() throws IOException {
    int n = 100_000;
    char[] gestures = new char[n];
    java.util.Arrays.fill(gestures, 0, n / 2, 'H');
    java.util.Arrays.fill(gestures, n / 2, n, 'P');
    // 50,000 hooves then 50,000 papers: one switch wins both halves -> 50000 + 50000 = 100000.
    assertThat(runMain(buildInput(gestures))).isEqualTo("100000");
  }

  @Test
  void fullLengthThreeBlockSequenceStrandsTheSmallestBlock() throws IOException {
    int n = 100_000;
    char[] gestures = new char[n];
    java.util.Arrays.fill(gestures, 0, 30_000, 'H');
    java.util.Arrays.fill(gestures, 30_000, 80_000, 'P');
    java.util.Arrays.fill(gestures, 80_000, n, 'S');
    // One switch captures only two of the three blocks: best is H(30000) then P(50000); the
    // 20,000 trailing S are stranded -> 30000 + 50000 = 80000.
    assertThat(runMain(buildInput(gestures))).isEqualTo("80000");
  }

  @Test
  void mixedSequenceWithStructureMatchesTheBruteForceOracle() throws IOException {
    int n = 2_000;
    char[] gestures = new char[n];
    for (int i = 0; i < n; i++) {
      if (i < n / 2) {
        gestures[i] = (i % 5 == 0) ? 'S' : 'H'; // first half: mostly hooves, sprinkled scissors
      } else {
        gestures[i] = (i % 7 == 0) ? 'S' : 'P'; // second half: mostly papers, sprinkled scissors
      }
    }
    // Independent O(N^2) oracle over every change point validates the linear-time solution on a
    // non-trivial mixed input whose answer comes from an interior switch.
    assertThat(runMain(buildInput(gestures))).isEqualTo(Integer.toString(maxWinsOracle(gestures)));
  }

  /** Builds BOJ 14453 input: the count {@code N} then one gesture per line. */
  private static String buildInput(char[] gestures) {
    StringBuilder sb = new StringBuilder();
    sb.append(gestures.length).append('\n');
    for (char g : gestures) {
      sb.append(g).append('\n');
    }
    return sb.toString();
  }

  /** A gesture array of {@code count} copies of {@code gesture}. */
  private static char[] repeat(char gesture, int count) {
    char[] gestures = new char[count];
    java.util.Arrays.fill(gestures, gesture);
    return gestures;
  }

  /**
   * Independent oracle: the maximum wins over every change point {@code x in [0, N]}, where each
   * run scores the frequency of its most common gesture. Brute {@code O(N^2)} -- only for small
   * inputs.
   */
  private static int maxWinsOracle(char[] gestures) {
    int n = gestures.length;
    int best = 0;
    for (int x = 0; x <= n; x++) {
      best = Math.max(best, bestRun(gestures, 0, x) + bestRun(gestures, x, n));
    }
    return best;
  }

  /** Wins from a single run over {@code [from, to)}: the count of its most common gesture. */
  private static int bestRun(char[] gestures, int from, int to) {
    int hoof = 0;
    int paper = 0;
    int scissors = 0;
    for (int i = from; i < to; i++) {
      switch (gestures[i]) {
        case 'H' -> hoof++;
        case 'P' -> paper++;
        default -> scissors++;
      }
    }
    return Math.max(hoof, Math.max(paper, scissors));
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
