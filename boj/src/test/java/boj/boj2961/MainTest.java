package boj.boj2961;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2961 도영이가 만든 맛있는 음식 (The delicious food Doyeong made).
 *
 * <p>There are N ingredients (1 ≤ N ≤ 10), each with a sourness S and a bitterness B. For any
 * chosen non-empty subset, the dish's sourness is the <em>product</em> of the chosen sournesses and
 * its bitterness is the <em>sum</em> of the chosen bitternesses. At least one ingredient must be
 * used — water is not a dish. The answer is the smallest achievable |sourness − bitterness|,
 * printed on a single line.
 *
 * <p>The input guarantees that, using every ingredient, both the product of sournesses and the sum
 * of bitternesses are positive integers below 1,000,000,000. Because every sourness is at least 1,
 * no subset's product exceeds the full product, so every intermediate value stays within int range.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"1", "3 10"})
  void officialSampleForcesTheLoneIngredient(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The only ingredient must be used: sourness 3, bitterness 10, difference 7.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"2", "3 8", "5 8"})
  void officialSampleCombinesBothIngredients(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Using both: sourness 3×5 = 15, bitterness 8+8 = 16, difference 1 — beats 5 or 3 alone.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4", "1 7", "2 6", "3 8", "4 9"})
  void officialSampleUsesAProperSubsetOfFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ingredients 2,3,4: sourness 2×3×4 = 24, bitterness 6+8+9 = 23, difference 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- N = 1: the lone ingredient is forced (the lower bound on N). ---

  @Test
  @StdIo({"1", "7 7"})
  void singleIngredientWithEqualTastesGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Sourness 7 equals bitterness 7, so the difference is 0 — the smallest possible answer.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "2 1000000"})
  void singleIngredientForcesALargeDifferenceWhenTastesDiverge(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With one ingredient there is no choice: |2 − 1000000| = 999998.
    assertThat(out.capturedString().trim()).isEqualTo("999998");
  }

  // --- Exact balance (difference 0) reachable only by the right combination. ---

  @Test
  @StdIo({"2", "2 1", "3 5"})
  void productOfSournessCanExactlyMatchSumOfBitterness(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both ingredients: sourness 2×3 = 6 equals bitterness 1+5 = 6, difference 0;
    // neither single ingredient (1 or 2) reaches 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2", "4 4", "4 4"})
  void identicalIngredientsAreEvaluatedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Either copy alone balances (4 vs 4 → 0); using both overshoots (16 vs 8 → 8). Answer 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The optimum may be a single ingredient, a proper subset, or the whole set. ---

  @Test
  @StdIo({"3", "3 4", "10 1", "10 1"})
  void aSingleIngredientCanBeStrictlyBest(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ingredient 1 alone: |3 − 4| = 1. Any combination multiplies sourness by 10 and overshoots.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3", "1 5", "4 1", "5 1"})
  void aProperTwoIngredientSubsetCanBeStrictlyBest(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ingredients 1 and 3: sourness 1×5 = 5, bitterness 5+1 = 6, difference 1 — better than every
    // single ingredient (best is 3) and better than the full set (20 vs 7 → 13).
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "6 36", "6 1"})
  void usingEveryIngredientCanBeStrictlyBest(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both ingredients: sourness 6×6 = 36, bitterness 36+1 = 37, difference 1 — better than
    // ingredient 1 (30) or ingredient 2 (5) alone.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Multiplicative growth: extra ingredients explode sourness past the bitterness sum. ---

  @Test
  @StdIo({"3", "10 1", "10 1", "10 1"})
  void addingIngredientsOvershootsWhenSournessGrowsFast(StdOut out) throws IOException {
    Main.main(new String[0]);
    // One ingredient: |10 − 1| = 9. Two: |100 − 2| = 98. Three: |1000 − 3| = 997. Best is 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- Upper bounds: N = 10 and a product approaching the int-safe 1,000,000,000 ceiling. ---

  @Test
  @StdIo({"10", "1 2", "1 3", "1 4", "1 5", "1 6", "1 7", "1 8", "1 9", "1 10", "1 11"})
  void tenUnitSournessIngredientsReduceToPickingTheSmallestBitterness(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Every sourness is 1, so any subset's product is 1; the bitterness sum closest to 1 is the
    // smallest single bitterness, 2, giving |1 − 2| = 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"10", "7 1", "7 1", "7 1", "7 1", "7 1", "7 1", "7 1", "7 1", "7 1", "7 1"})
  void fullProductOfTenSournessesStaysWithinIntRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The whole set computes 7^10 = 282,475,249 (still < 1e9, so int-safe) against bitterness 10.
    // The best dish is a single ingredient: |7 − 1| = 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"2", "30000 29999", "30000 1"})
  void largeFullProductNearTheCeilingIsComputedSafely(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The full set's sourness is 30000×30000 = 900,000,000 (just under 1e9); the best dish is
    // ingredient 1 alone: |30000 − 29999| = 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }
}
