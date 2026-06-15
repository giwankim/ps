package boj.boj1497;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1497 기타콘서트 (Guitar Concert).
 *
 * <p>There are N guitars and M songs (1 ≤ N ≤ 10, 1 ≤ M ≤ 50). The first input line holds N and M;
 * each of the next N lines holds a guitar's name followed by a length-M string of 'Y'/'N' flags,
 * where the j-th flag is 'Y' when that guitar can play song j and 'N' otherwise.
 *
 * <p>Using a set of guitars lets you play the union of the songs those guitars can play. The most
 * songs anyone can play is therefore the union over all N guitars; print the minimum number of
 * guitars needed to reach that maximum. Because N ≤ 10, the 2^N ≤ 1024 subsets can all be examined:
 * the answer is the smallest subset whose combined coverage equals the full union. When no guitar
 * can play any song the maximum is zero songs and the answer is defined to be -1.
 */
class MainTest {

  // --- Official sample. ---

  @Test
  @StdIo({"4 5", "GIBSON YYYNN", "FENDER YYNNY", "EPIPHONE NNNYY", "ESP YNNNN"})
  void officialSampleCoversAllFiveSongsWithTwoGuitars(StdOut out) throws IOException {
    Main.main(new String[0]);
    // GIBSON plays {1,2,3} and EPIPHONE plays {4,5}, so together they cover all five songs — and no
    // single guitar covers all five, so two is the minimum.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Smallest input: a single guitar and a single song. ---

  @Test
  @StdIo({"1 1", "A Y"})
  void singleGuitarThatPlaysItsOnlySongNeedsItself(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The lone guitar plays the lone song, so one guitar reaches the maximum of one song.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1 1", "A N"})
  void whenNoGuitarCanPlayAnySongTheAnswerIsMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The only guitar plays nothing, so the maximum playable count is zero songs; the problem
    // defines the output as -1 in that case rather than 0.
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- One guitar can be enough: the minimum is not "every guitar that helps". ---

  @Test
  @StdIo({"3 4", "ALPHA YYYY", "BETA YNNN", "GAMMA NNYN"})
  void oneGuitarCoveringEverySongMakesTheOthersRedundant(StdOut out) throws IOException {
    Main.main(new String[0]);
    // ALPHA already plays all four songs, so the maximum is reached by ALPHA alone; BETA and GAMMA
    // add nothing and the answer is one, not three.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3 4", "BIG YYYY", "P1 YYNN", "P2 NNYY"})
  void oneFullGuitarIsPreferredOverTwoPartialOnesThatTogetherMatchIt(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // P1 {1,2} plus P2 {3,4} also cover all four songs, but BIG covers them by itself; the minimum
    // is one, so the algorithm must not settle for the two-guitar combination.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2 3", "A YYN", "B YYN"})
  void twoIdenticalGuitarsNeedOnlyOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both guitars cover exactly {1,2}; either one alone reaches the maximum, so the answer is one.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Guitars must be combined: no single guitar reaches the maximum. ---

  @Test
  @StdIo({"2 4", "LEFT YYNN", "RIGHT NNYY"})
  void twoDisjointHalvesBothNeededToCoverEverySong(StdOut out) throws IOException {
    Main.main(new String[0]);
    // LEFT plays {1,2} and RIGHT plays {3,4} with no overlap, so reaching all four songs requires
    // both guitars.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3 3", "A YNN", "B NYN", "C NNY"})
  void eachGuitarContributesItsOwnUniqueSongSoAllAreNeeded(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The three guitars cover {1}, {2} and {3} respectively; every song has exactly one provider,
    // so all three guitars are required.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"3 5", "X YYYNN", "Y NNYYY", "Z YYNNN"})
  void aGuitarWhoseSongsAreAlreadyCoveredIsDropped(StdOut out) throws IOException {
    Main.main(new String[0]);
    // X {1,2,3} and Y {3,4,5} together cover all five songs in two guitars; Z {1,2} is fully
    // subsumed by X, so the minimum stays two.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The maximum may be fewer than M songs: some songs are playable by no one. ---

  @Test
  @StdIo({"3 4", "A YYNN", "B YNNN", "C NYNN"})
  void songsNoGuitarCanPlayDoNotCountTowardTheMaximum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Songs 3 and 4 are 'N' for every guitar, so the most anyone can play is the two songs {1,2};
    // A covers both by itself, so the answer is one — the count is measured against the achievable
    // maximum (two songs), not against M (four).
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Bit-width boundary: M can exceed 32, so song indices must not alias in a 32-bit mask. ---

  @Test
  @StdIo({
    "2 50",
    "A YNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN",
    "B NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNYNNNNNNNNNNNNNNNNN"
  })
  void songsThirtyTwoApartStayDistinctRequiringASixtyFourBitMask(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A plays only song 1 and B plays only song 33. Those indices differ by 32, so a 32-bit int
    // shift (1 << 32 wrapping to 1 << 0) would alias them into a single song and answer 1; with a
    // 64-bit mask they are two disjoint songs needing both guitars, so the answer is 2.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"1 50", "A NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNY"})
  void theFiftiethSongAtTheUpperBoundIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single guitar plays only the 50th song — the last column at the M upper bound — which
    // must still register, giving a maximum of one song reachable by the one guitar.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Upper bound: the largest guitar count, N = 10. ---

  @Test
  @StdIo({
    "10 10",
    "G1 YNNNNNNNNN",
    "G2 NYNNNNNNNN",
    "G3 NNYNNNNNNN",
    "G4 NNNYNNNNNN",
    "G5 NNNNYNNNNN",
    "G6 NNNNNYNNNN",
    "G7 NNNNNNYNNN",
    "G8 NNNNNNNYNN",
    "G9 NNNNNNNNYN",
    "G10 NNNNNNNNNY"
  })
  void tenGuitarsEachOwningOneSongRequireAllTenToCoverEverything(StdOut out) throws IOException {
    Main.main(new String[0]);
    // At the N = 10 bound each guitar is the sole provider of one of the ten songs, so covering all
    // ten songs forces every guitar to be used — the largest answer the constraints allow.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Remaining official samples from the statement. ---

  @Test
  @StdIo({"3 5", "GIBSON YNYYN", "FENDER NNNYY", "TAYLOR YYYYY"})
  void officialSampleOneGuitarThatPlaysEverySongWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    // TAYLOR alone covers all five songs, so the answer is 1 — fewer guitars beats any larger set.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3 2", "AB YN", "AA YN", "BA NN"})
  void officialSampleDuplicateCoverageNeedsOnlyOneGuitar(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Song 2 is unplayable by anyone, so the best coverage is just song 1 — one guitar (AB or AA).
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({
    "5 7",
    "FENDER YYNNYNN",
    "GIBSON YYYNYNN",
    "CRAFTER NNNNNYY",
    "EPIPHONE NNYNNNN",
    "BCRICH NNNYNNN"
  })
  void officialSampleThreeGuitarsCoverAllSevenSongs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // GIBSON {1,2,3,5} + CRAFTER {6,7} + BCRICH {4} = all seven; song 4 and songs 6,7 each have a
    // sole provider, forcing exactly three guitars.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"2 25", "GIBSON NNNNNNNNNNNNNNNNNNNNNNNNN", "FENDER NNNNNNNNNNNNNNNNNNNNNNNNN"})
  void officialSampleAllSilentGuitarsReturnMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both guitars are silent across all 25 songs: maximum coverage is 0 songs, so the answer is
    // -1.
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- The set-cover trap: a greedy "take the widest guitar first" overpays. ---

  @Test
  @StdIo({"3 6", "ALPHA YYYNNN", "BETA NNNYYY", "GAMMA YYNYYN"})
  void widestFirstGreedyOverpaysButOptimalUsesTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    // ALPHA {1,2,3} + BETA {4,5,6} cover all six with two guitars. Greedily taking the widest
    // guitar
    // GAMMA {1,2,4,5} first leaves songs 3 and 6, each with a sole provider, forcing three — so the
    // brute-force answer is 2, not 3.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Generated grids driven through stdin/stdout, cross-checked against a brute-force oracle.
  // ---

  @Test
  void denseRandomGridMatchesOracle() throws IOException {
    boolean[][] canPlay = randomGrid(10, 50, new Random(1497L), 2); // ~50% Y.
    // Dense coverage at the N, M ceiling: nearly every song reachable, stressing the count
    // tie-break.
    assertThat(runMain(buildInput(canPlay))).isEqualTo(solve(canPlay));
  }

  @Test
  void sparseRandomGridMatchesOracle() throws IOException {
    boolean[][] canPlay = randomGrid(10, 50, new Random(14971L), 8); // ~12.5% Y.
    // Sparse coverage: many songs stay unplayable, exercising the "coverage below M" path at scale.
    assertThat(runMain(buildInput(canPlay))).isEqualTo(solve(canPlay));
  }

  @Test
  void midWidthRandomGridMatchesOracle() throws IOException {
    boolean[][] canPlay = randomGrid(8, 33, new Random(149703L), 3); // ~33% Y, M just past 32.
    // A second shape with M straddling the 32-bit boundary, to vary dimensions and bit widths.
    assertThat(runMain(buildInput(canPlay))).isEqualTo(solve(canPlay));
  }

  /**
   * Builds BOJ 1497 input: an {@code "N M"} header then one {@code "NAME YN..."} line per guitar.
   */
  private static String buildInput(boolean[][] canPlay) {
    int n = canPlay.length;
    int m = canPlay[0].length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(m).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(guitarName(i)).append(' ');
      for (int s = 0; s < m; s++) {
        sb.append(canPlay[i][s] ? 'Y' : 'N');
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /** Distinct two-letter uppercase names {@code "AA".."AJ"} for the {@code i}-th guitar. */
  private static String guitarName(int i) {
    return "" + 'A' + (char) ('A' + i);
  }

  /**
   * Random {@code N x M} playability grid where each cell is {@code Y} with probability {@code 1 /
   * oneInYields} (so a larger {@code oneInYields} produces a sparser grid).
   */
  private static boolean[][] randomGrid(int n, int m, Random rng, int oneInYields) {
    boolean[][] g = new boolean[n][m];
    for (int i = 0; i < n; i++) {
      for (int s = 0; s < m; s++) {
        g[i][s] = rng.nextInt(oneInYields) == 0;
      }
    }
    return g;
  }

  /**
   * Independent oracle: brute force over every {@code N}-bit guitar subset, taking the union of its
   * songs. Tracks the maximum coverage and, among subsets reaching it, the fewest guitars. Returns
   * {@code "-1"} when the best coverage is zero (no guitar plays any song).
   */
  private static String solve(boolean[][] canPlay) {
    int n = canPlay.length;
    int m = canPlay[0].length;
    int bestCover = 0;
    int minCount = Integer.MAX_VALUE;
    for (int mask = 0; mask < (1 << n); mask++) {
      boolean[] covered = new boolean[m];
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) == 0) {
          continue;
        }
        for (int s = 0; s < m; s++) {
          if (canPlay[i][s]) {
            covered[s] = true;
          }
        }
      }
      int cover = 0;
      for (boolean played : covered) {
        if (played) {
          cover++;
        }
      }
      int count = Integer.bitCount(mask);
      if (cover > bestCover) {
        bestCover = cover;
        minCount = count;
      } else if (cover == bestCover) {
        minCount = Math.min(minCount, count);
      }
    }
    return bestCover == 0 ? "-1" : Integer.toString(minCount);
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
