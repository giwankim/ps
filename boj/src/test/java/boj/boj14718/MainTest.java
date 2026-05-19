package boj.boj14718;

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
 * BOJ 14718 용감한 용사 진수 (Brave warrior Jinsu).
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"3 3", "10 5 5", "5 10 5", "5 5 10"})
  void officialSampleOneBeatAllWithEachSoldierToppingADifferentAxis(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30");
  }

  @Test
  @StdIo({"3 1", "234 23 342", "35 4634 34", "46334 6 789"})
  void officialSampleTwoBeatAtLeastOnePicksTheCheapestSoldier(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("599");
  }

  @Test
  @StdIo({"3 2", "30 30 30", "10 500 10", "50 10 50"})
  void officialSampleThreeBeatTwoChoosesTheCheaperPairNotCheapestSums(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("130");
  }

  // --- Smallest input: a single soldier. ---

  @Test
  @StdIo({"1 1", "7 3 9"})
  void singleSoldierAnswerIsItsOwnStatSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("19");
  }

  // --- Zero stats: the lower bound of the answer is 0. ---

  @Test
  @StdIo({"2 1", "0 0 0", "5 5 5"})
  void zeroStatSoldierMakesBeatingOneFree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3 3", "0 0 0", "0 0 0", "0 0 0"})
  void allZeroSoldiersCostNothingEvenToBeatThemAll(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All soldiers identical. ---

  @Test
  @StdIo({"3 2", "5 5 5", "5 5 5", "5 5 5"})
  void identicalSoldiersCostOneSoldierWorthOfStats(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Pareto dominance: beating the strong one beats the weak one for free. ---

  @Test
  @StdIo({"2 1", "1 1 1", "5 5 5"})
  void dominatedConfigurationBeatAtLeastOneTakesTheWeakSoldier(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"2 2", "1 1 1", "5 5 5"})
  void dominatingSoldierForcesItsStatsWhenBothMustBeBeaten(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Anti-greedy: the optimal pair includes the highest-sum soldier. ---

  @Test
  @StdIo({"4 2", "0 0 10", "0 10 0", "0 0 11", "10 0 0"})
  void cheapestPairUsesTheLargestSumSoldierBecauseAxesAreShared(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Upper stat bound: 1,000,000; the int sum stays well within range. ---

  @Test
  @StdIo({"2 2", "1000000 1000000 1000000", "1 1 1"})
  void maximumStatsBeatBothSumsToThreeMillion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3000000");
  }

  @Test
  @StdIo({"3 1", "1000000 1 1", "1 1000000 1", "1 1 1000000"})
  void eachSoldierSpikesADifferentAxisBeatOneIsStillExpensive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000002");
  }

  // --- Upper size bound: N = 100. soldier i = (i, i, i), so beating <= m soldiers
  //     needs min(x, y, z) >= m, i.e. the cheapest answer for K is 3 * K. ---

  @Test
  void hundredSoldiersBeatAllNeedsTheTopOfEveryAxis() throws IOException {
    assertThat(runMain(diagonalSoldiers(100, 100))).isEqualTo("300");
  }

  @Test
  void hundredSoldiersBeatAtLeastOneIsTheCheapestSoldier() throws IOException {
    assertThat(runMain(diagonalSoldiers(100, 1))).isEqualTo("3");
  }

  @Test
  void hundredSoldiersBeatHalfNeedsHalfTheStatsOnEveryAxis() throws IOException {
    assertThat(runMain(diagonalSoldiers(100, 50))).isEqualTo("150");
  }

  /** Builds input for {@code n} soldiers where soldier {@code i} has stats {@code (i, i, i)}. */
  private static String diagonalSoldiers(int n, int k) {
    StringBuilder sb = new StringBuilder().append(n).append(' ').append(k).append('\n');
    for (int i = 1; i <= n; i++) {
      sb.append(i).append(' ').append(i).append(' ').append(i).append('\n');
    }
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
