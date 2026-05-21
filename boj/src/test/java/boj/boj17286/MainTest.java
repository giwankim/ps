package boj.boj17286;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 17286 유미 (Yumi).
 *
 * <p>Yumi the cat starts at her own grid position and wants to be hugged by all three people, who
 * stand at fixed grid positions and never move. The answer is the minimum total Euclidean distance
 * over the 3! = 6 orders in which she can visit the three people, with the fractional part dropped
 * (truncated toward zero), printed as a single integer.
 *
 * <p>Input is exactly four lines of {@code "x y"} integer pairs: Yumi's start on line 1, then the
 * three people on lines 2–4. All coordinates satisfy {@code -10 ≤ x, y ≤ 10}; no two of the four
 * positions coincide, so every leg has strictly positive length and the answer is always at least
 * 3.
 *
 * <p>The "truncate, don't round" rule comes straight from the problem statement ("소수점 이하는 버리고 정수만
 * 출력한다"); two independent Korean writeups also call out that the truncation must be applied to the
 * final summed distance and not to each leg, since per-leg truncation drops enough precision to
 * flip answers. Every expected value below is hand-derived against that model and cross-checked
 * against two independent problem mirrors (acmicpc.net being offline at authoring time).
 */
class MainTest {

  // --- The single sample published with the problem. ---

  @Test
  @StdIo({"0 0", "1 0", "2 0", "4 0"})
  void sampleCollinearPeopleOnTheRightAreVisitedInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best order is (0,0) -> (1,0) -> (2,0) -> (4,0): 1 + 1 + 2 = 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The optimum is over orderings, so input order of the people must not matter. ---

  @Test
  @StdIo({"0 0", "4 0", "2 0", "1 0"})
  void permutingThePeopleInTheInputDoesNotChangeTheAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Same three people as the sample, given in reversed order; the minimum tour is still 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Distances are Euclidean, not Manhattan. A 3-4-5 triple pins this. ---

  @Test
  @StdIo({"0 0", "3 4", "6 0", "0 8"})
  void distancesAreEuclideanNotManhattan(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best order is (0,0) -> (6,0) -> (3,4) -> (0,8): 6 + 5 + 5 = 16.
    // A Manhattan-distance solver would produce 7 + 7 + 11 = 25 on the same path.
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Greedy nearest-neighbor from the start is not safe: heading toward the closest
  //     person first can lock you into a worse total than starting away from Yumi. ---

  @Test
  @StdIo({"0 0", "1 1", "5 5", "-3 -3"})
  void optimalTourIsNotAlwaysReachedByVisitingTheClosestPersonFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Greedy picks (1,1) first and yields sqrt(2) + sqrt(32) + sqrt(128) ≈ 18.385 -> 18.
    // The true optimum starts with (-3,-3):
    //   (0,0) -> (-3,-3) -> (1,1) -> (5,5): sqrt(18) + sqrt(32) + sqrt(32) ≈ 15.557 -> 15.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- The decimals are dropped, not rounded; a total of ≈ 6.708 must print as 6, not 7. ---

  @Test
  @StdIo({"0 0", "1 2", "2 4", "3 6"})
  void truncatesTheFractionalPartRatherThanRounding(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The four points are collinear along y = 2x; the best order marches them out in distance
    // order: (0,0) -> (1,2) -> (2,4) -> (3,6) gives three sqrt(5) legs summing to
    // 3*sqrt(5) ≈ 6.708 -> 6. Math.round would produce 7.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Truncation applies to the final sum, not to each leg. Per-leg truncation loses
  //     enough precision here to flip the answer. ---

  @Test
  @StdIo({"0 0", "1 1", "2 0", "3 1"})
  void truncationAppliesToTheFinalSumAndNotToEachLeg(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best order is (0,0) -> (1,1) -> (2,0) -> (3,1): three sqrt(2) legs summing to
    // 3*sqrt(2) ≈ 4.243 -> 4. Truncating each leg before summing gives 1 + 1 + 1 = 3.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Negative coordinates must work; no abs/sign bug in the distance computation. ---

  @Test
  @StdIo({"-1 -1", "-2 -2", "-3 -3", "-4 -4"})
  void negativeCoordinatesAreHandled(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Collinear along y = x in the negative quadrant; the natural outward march wins:
    // (-1,-1) -> (-2,-2) -> (-3,-3) -> (-4,-4) gives 3*sqrt(2) ≈ 4.243 -> 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Coordinate extremes: Yumi sits at one corner of the allowed [-10, 10] square and the
  //     three people sit at the other three. ---

  @Test
  @StdIo({"-10 -10", "10 10", "10 -10", "-10 10"})
  void coordinatesAtTheBoundsOfTheAllowedRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best order avoids the long diagonal across the square:
    //   (-10,-10) -> (10,-10) -> (10,10) -> (-10,10): 20 + 20 + 20 = 60 (and its mirror).
    // Any tour that uses the diagonal pays sqrt(800) ≈ 28.28 at least once and is strictly worse.
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }

  // --- A second truncation case at a different boundary, to lock in floor semantics. ---

  @Test
  @StdIo({"0 0", "1 0", "-1 0", "0 1"})
  void smallTotalTruncatesDownPastTheRoundingBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best order crosses through (0,1) between the two axis-aligned people:
    //   (0,0) -> (1,0) -> (0,1) -> (-1,0) gives 1 + sqrt(2) + sqrt(2) = 1 + 2*sqrt(2) ≈ 3.828 -> 3.
    // Math.round would produce 4.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }
}
