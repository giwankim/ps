package boj.boj28447;

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
 * BOJ 28447 마라탕 재료 고르기 (Choosing Malatang Ingredients) -- pick exactly {@code K} of {@code N}
 * ingredients to maximize the total pairwise compatibility.
 *
 * <p>Input: the first line is {@code N K}; the next {@code N} lines form a symmetric {@code N x N}
 * compatibility matrix where {@code C[i][j]} is the compatibility of ingredients {@code i} and
 * {@code j}. The matrix has a zero diagonal ({@code C[i][i] = 0}) and is symmetric ({@code C[i][j]
 * = C[j][i]}). The taste of a chosen group {@code G} is the sum of {@code C[i][j]} over every
 * unordered pair {@code i < j} drawn from {@code G}. Output: the largest taste achievable by a
 * group of exactly {@code K} ingredients.
 *
 * <p>Constraints: {@code 1 <= N <= 10}; {@code 1 <= K <= N}; {@code -1000 <= C[i][j] <= 1000}. The
 * tiny {@code N} invites brute force over all {@code C(N, K) <= C(10, 5) = 252} subsets.
 *
 * <p>Two properties shape these fixtures. First, {@code K} is the <em>exact</em> group size, never
 * a cap -- so when {@code K = 1} no pair exists and the answer is always {@code 0}, and when
 * {@code K = N} the whole matrix is forced. Second, compatibilities may be negative, so a group of
 * {@code K >= 2} ingredients can be forced into a negative total; an implementation that seeds its
 * best answer with {@code 0} instead of {@code -infinity} is caught by the all-negative cases
 * below. The answer's magnitude is bounded by {@code C(10, 2) * 1000 = 45,000}, comfortably inside
 * a 32-bit {@code int}.
 */
class MainTest {

  // --- Official samples from the statement. ---

  @Test
  @StdIo({"4 3", "0 1 2 3", "1 0 -2 6", "2 -2 0 5", "3 6 5 0"})
  void officialSampleChoosesTheBestThreeIngredients(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ingredients 1,2,4: C12 + C14 + C24 = 1 + 3 + 6 = 10, the maximum over all size-3 subsets.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"4 1", "0 1 2 3", "1 0 -2 6", "2 -2 0 5", "3 6 5 0"})
  void singleIngredientHasNoPairsSoTasteIsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = 1: a lone ingredient forms no pair, so the taste sum is empty -> 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Smallest worlds and the K = 1 rule. ---

  @Test
  @StdIo({"1 1", "0"})
  void smallestPossibleInputTrivialZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // One ingredient, K = 1: no pair to score, so the taste is 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Forced pairs: K = N = 2 leaves no choice but the single edge. ---

  @Test
  @StdIo({"2 2", "0 7", "7 0"})
  void twoIngredientsForcedPairReturnsThatEdge(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The only group of size 2 scores C12 = 7.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"2 2", "0 -1000", "-1000 0"})
  void negativeOnlyForcedPairReturnsNegativeTaste(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = 2 forces the single negative edge; the answer is -1000, NOT clamped up to 0.
    assertThat(out.capturedString().trim()).isEqualTo("-1000");
  }

  // --- K < N gives freedom: the optimal group skips harmful pairs. ---

  @Test
  @StdIo({"3 2", "0 5 -1000", "5 0 -1000", "-1000 -1000 0"})
  void choosingFewerIngredientsAvoidsNegativeEdges(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Pairs: {1,2}=5, {1,3}=-1000, {2,3}=-1000. Picking {1,2} sidesteps ingredient 3 -> 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"4 3", "0 5 5 -10", "5 0 5 -10", "5 5 0 -10", "-10 -10 -10 0"})
  void leaveOneOutExcludesTheWorstConnectedIngredient(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = N - 1, so exactly one ingredient is dropped. Dropping #4 (all -10 edges) keeps the
    // mutually friendly trio {1,2,3}: 5 + 5 + 5 = 15.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- K = N: the entire matrix is forced. ---

  @Test
  @StdIo({"3 3", "0 1 2", "1 0 3", "2 3 0"})
  void mustUseAllIngredientsWhenKEqualsN(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every pair is included: C12 + C13 + C23 = 1 + 2 + 3 = 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Degenerate values. ---

  @Test
  @StdIo({"4 2", "0 0 0 0", "0 0 0 0", "0 0 0 0", "0 0 0 0"})
  void allZeroCompatibilitiesGiveZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every pair scores 0, so any size-2 group totals 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"4 2", "0 1 2 3", "1 0 -2 6", "2 -2 0 5", "3 6 5 0"})
  void pickingTwoSelectsTheSingleBestEdge(StdOut out) throws IOException {
    Main.main(new String[0]);
    // K = 2 reduces to the maximum edge: max(1, 2, 3, -2, 6, 5) = 6 (ingredients 2 and 4).
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Bounds and fuzzing: generated matrices driven through stdin/stdout, with an independent
  // subset-enumeration oracle. ---

  @Test
  void fullyCompatibleMatrixAtMaximumGivesLargestTaste() throws IOException {
    int n = 10;
    int[][] c = filledOffDiagonal(n, 1000); // every pair at the +1000 ceiling.
    // K = N forces all C(10, 2) = 45 pairs: 45 * 1000 = 45,000, the heaviest legal answer.
    assertThat(runMain(buildInput(c, n))).isEqualTo("45000");
  }

  @Test
  void fullyHostileMatrixAtMinimumForcedFull() throws IOException {
    int n = 10;
    int[][] c = filledOffDiagonal(n, -1000); // every pair at the -1000 floor.
    // K = N forces all 45 pairs even though each hurts: 45 * -1000 = -45,000, the lightest answer.
    assertThat(runMain(buildInput(c, n))).isEqualTo("-45000");
  }

  @Test
  void randomPairSelectionMatchesOracle() throws IOException {
    int n = 10;
    int k = 2;
    int[][] c = randomSymmetric(n, new Random(284471L));
    // K = 2 is the "best single edge" case; the brute-force oracle confirms the picked pair.
    assertThat(runMain(buildInput(c, k))).isEqualTo(Long.toString(maxTaste(k, c)));
  }

  @Test
  void randomMidSizeSelectionMatchesOracle() throws IOException {
    int n = 10;
    int k = 5;
    int[][] c = randomSymmetric(n, new Random(284475L));
    // The hardest band: C(10, 5) = 252 candidate groups, mixing positive and negative edges.
    assertThat(runMain(buildInput(c, k))).isEqualTo(Long.toString(maxTaste(k, c)));
  }

  @Test
  void randomNearFullSelectionMatchesOracle() throws IOException {
    int n = 10;
    int k = 8;
    int[][] c = randomSymmetric(n, new Random(284478L));
    // K = N - 2: only C(10, 8) = 45 groups, each a "drop the worst two ingredients" choice.
    assertThat(runMain(buildInput(c, k))).isEqualTo(Long.toString(maxTaste(k, c)));
  }

  @Test
  void smallerRandomMatrixMatchesOracle() throws IOException {
    int n = 7;
    int k = 4;
    int[][] c = randomSymmetric(n, new Random(28447L));
    // A second shape (N = 7) to vary the dimensions away from the N = 10 ceiling.
    assertThat(runMain(buildInput(c, k))).isEqualTo(Long.toString(maxTaste(k, c)));
  }

  /** Builds BOJ 28447 input: an {@code "N K"} header followed by the {@code N x N} matrix rows. */
  private static String buildInput(int[][] c, int k) {
    int n = c.length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (j > 0) {
          sb.append(' ');
        }
        sb.append(c[i][j]);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /** Symmetric, zero-diagonal matrix with every off-diagonal entry set to {@code value}. */
  private static int[][] filledOffDiagonal(int n, int value) {
    int[][] c = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        c[i][j] = value;
        c[j][i] = value;
      }
    }
    return c;
  }

  /** Random symmetric, zero-diagonal matrix with entries uniform in {@code [-1000, 1000]}. */
  private static int[][] randomSymmetric(int n, Random rng) {
    int[][] c = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        int v = rng.nextInt(2001) - 1000;
        c[i][j] = v;
        c[j][i] = v;
      }
    }
    return c;
  }

  /**
   * Independent oracle: brute force over every {@code N}-bit subset, scoring only those of size
   * {@code K} by summing {@code C[i][j]} for each unordered pair {@code i < j}, and returning the
   * maximum. Seeded with {@code Long.MIN_VALUE} so an all-negative matrix yields a negative answer.
   */
  private static long maxTaste(int k, int[][] c) {
    int n = c.length;
    long best = Long.MIN_VALUE;
    for (int mask = 0; mask < (1 << n); mask++) {
      if (Integer.bitCount(mask) != k) {
        continue;
      }
      long sum = 0;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) == 0) {
          continue;
        }
        for (int j = i + 1; j < n; j++) {
          if ((mask & (1 << j)) != 0) {
            sum += c[i][j];
          }
        }
      }
      best = Math.max(best, sum);
    }
    return best;
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
