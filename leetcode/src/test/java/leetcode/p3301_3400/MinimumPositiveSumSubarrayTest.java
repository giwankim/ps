package leetcode.p3301_3400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class MinimumPositiveSumSubarrayTest {
  MinimumPositiveSumSubarray sut = new MinimumPositiveSumSubarray();

  // ===========================================================================================
  // The floor and the meaning of "positive" (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor. nums.length = 1 forces l = r = 1, so the only subarray is the element itself
  @Test
  void singlePositiveElementIsItsOwnSum() {
    assertThat(sut.minimumSumSubarray(List.of(5), 1, 1)).isEqualTo(5);
  }

  // Step 2: the mirror of Step 1. The only subarray sums to -5, which is not greater than 0, so
  //         nothing qualifies. A solution that minimizes over every sum answers -5, and one that
  //         minimizes the absolute value answers 5
  @Test
  void singleNegativeElementHasNoPositiveSubarray() {
    assertThat(sut.minimumSumSubarray(List.of(-5), 1, 1)).isEqualTo(-1);
  }

  // Step 3: "greater than 0" is strict. A sum of exactly 0 does not qualify, so a solution that
  //         filters with sum >= 0 answers 0
  @Test
  void zeroSumIsNotPositive() {
    assertThat(sut.minimumSumSubarray(List.of(0), 1, 1)).isEqualTo(-1);
  }

  // Step 4: among several qualifying sums the smallest wins. Returning the first one found answers
  //         7, and maximizing answers 9
  @Test
  void smallestOfSeveralPositiveSumsWins() {
    assertThat(sut.minimumSumSubarray(List.of(7, 2, 9), 1, 1)).isEqualTo(2);
  }

  // ===========================================================================================
  // The length window, both ends inclusive (Steps 5-9).
  // ===========================================================================================

  // Step 5: l is a floor on the length. The single element 1 is the smallest positive sum overall
  //         but is too short. Length 2 gives 6 and 10, length 3 gives 11. Ignoring l answers 1
  @Test
  void subarraysShorterThanLAreExcluded() {
    assertThat(sut.minimumSumSubarray(List.of(1, 5, 5), 2, 3)).isEqualTo(6);
  }

  // Step 6: the mirror of Step 5, r is a ceiling. The whole array sums to 1 but has length 3.
  //         Lengths 1 and 2 give 5, 5, 10 and two negative sums. Ignoring r answers 1
  @Test
  void subarraysLongerThanRAreExcluded() {
    assertThat(sut.minimumSumSubarray(List.of(5, 5, -9), 1, 2)).isEqualTo(5);
  }

  // Step 7: l itself is allowed. The answer is the length-2 prefix with sum 1. Treating the lower
  //         bound as exclusive leaves only the whole array and answers 10
  @Test
  void lengthExactlyLCounts() {
    assertThat(sut.minimumSumSubarray(List.of(3, -2, 9), 2, 3)).isEqualTo(1);
  }

  // Step 8: the mirror of Step 7, r itself is allowed. Only the whole array sums to 1. Treating
  //         the upper bound as exclusive, or only trying length l, answers 4
  @Test
  void lengthExactlyRCounts() {
    assertThat(sut.minimumSumSubarray(List.of(4, 4, -7), 1, 3)).isEqualTo(1);
  }

  // Step 9: every length in between counts too. The answer is the length-2 prefix with sum 1,
  //         while l = 1 and r = 4. A solution that only tries the two extreme lengths answers 3,
  //         and so does one that assumes the shortest window is always the cheapest
  @Test
  void lengthStrictlyBetweenLAndRCounts() {
    assertThat(sut.minimumSumSubarray(List.of(6, -5, 3, 9), 1, 4)).isEqualTo(1);
  }

  // ===========================================================================================
  // Interactions: skipped sums, the -1 sentinel, window edges, repeated prefix sums (Steps 10-16).
  // ===========================================================================================

  // Step 10: non-positive sums are skipped, not returned, and they do not hide the positive ones.
  //          Sums are -3, 2, -3, -1, -1, -4, so the only positive one is 2. A plain minimum
  //          subarray sum answers -4
  @Test
  void negativeSumsAreSkippedInFavorOfThePositiveOne() {
    assertThat(sut.minimumSumSubarray(List.of(-3, 2, -3), 1, 3)).isEqualTo(2);
  }

  // Step 11: Step 3 again, now with a positive alternative. The windows of length 2 sum to 0 and
  //          1. Filtering with sum >= 0 answers 0
  @Test
  void zeroSumWindowIsSkippedInFavorOfThePositiveOne() {
    assertThat(sut.minimumSumSubarray(List.of(2, -2, 3), 2, 2)).isEqualTo(1);
  }

  // Step 12: -1 means no subarray inside the length window is positive, even when shorter ones
  //          are. The only length-3 subarray sums to -1. Ignoring l answers 1
  @Test
  void positiveSubarraysOutsideTheLengthWindowDoNotPreventMinusOne() {
    assertThat(sut.minimumSumSubarray(List.of(4, -6, 1), 3, 3)).isEqualTo(-1);
  }

  // Step 13: every sum of every length is 0, so the answer is the -1 sentinel and never 0 or a
  //          leaked "infinity" initial value
  @Test
  void allZerosHaveNoPositiveSubarray() {
    assertThat(sut.minimumSumSubarray(List.of(0, 0, 0), 1, 3)).isEqualTo(-1);
  }

  // Step 14: the answer is the last window. Length-2 sums are 18, 18, 1. A loop that stops one
  //          window early answers 18
  @Test
  void answerInTheLastWindow() {
    assertThat(sut.minimumSumSubarray(List.of(9, 9, 9, -8), 2, 2)).isEqualTo(1);
  }

  // Step 15: the mirror of Step 14, the answer is the first window. Length-2 sums are 1, 18, 18.
  //          A loop that starts one window late answers 18
  @Test
  void answerInTheFirstWindow() {
    assertThat(sut.minimumSumSubarray(List.of(-8, 9, 9, 9), 2, 2)).isEqualTo(1);
  }

  // Step 16: prefix sums are [0, 0, 1], and the answer 1 is the last prefix minus the second 0.
  //          An ordered-set solution that keeps prefix sums in a plain set drops both zeros when
  //          the first one slides out of the length window, finds nothing below 1, and answers -1
  @Test
  void equalPrefixSumsAreTrackedSeparately() {
    assertThat(sut.minimumSumSubarray(List.of(0, 1), 1, 1)).isEqualTo(1);
  }

  // ===========================================================================================
  // The official examples (Steps 17-19).
  // ===========================================================================================

  // Step 17: positive sums in range are 1, 5, 2, 3. The winner needs a negative element inside
  //          it, so skipping negatives is wrong. The window of length 2 with sum -1 must be
  //          skipped as well, and a plain minimum answers -1
  @Test
  void leetCodeExample1() {
    assertThat(sut.minimumSumSubarray(List.of(3, -2, 1, 4), 2, 3)).isEqualTo(1);
  }

  // Step 18: length-2 sums are 0, -1, -2 and length-3 sums are -3, 0. The single elements 2 and 1
  //          are positive but too short, so ignoring l answers 1, and sum >= 0 answers 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.minimumSumSubarray(List.of(-2, 2, -3, 1), 2, 3)).isEqualTo(-1);
  }

  // Step 19: all positive, so the cheapest subarray is the shortest allowed one at the smallest
  //          end. Ignoring l answers 1, and returning the whole array answers 10
  @Test
  void leetCodeExample3() {
    assertThat(sut.minimumSumSubarray(List.of(1, 2, 3, 4), 2, 4)).isEqualTo(3);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 20-23): nums.length = 100 and values at -1000 and 1000. The input is
  // so small that even the cubic brute force needs about 10^6 operations, so these pin extreme
  // values, not a complexity class, and carry no timeouts.
  // ===========================================================================================

  // Step 20: the largest possible answer. l = r = nums.length leaves only the whole array, whose
  //          sum is 100 * 1000. Ignoring l answers 1000
  @Test
  void maxLengthAllMaxValuesWholeArrayOnly() {
    assertThat(sut.minimumSumSubarray(ramp(100, i -> 1000), 100, 100)).isEqualTo(100_000);
  }

  // Step 21: the most negative input over the widest length window. Every one of the 5050
  //          subarrays is negative. A plain minimum answers -100000
  @Test
  void maxLengthAllMinValuesHaveNoPositiveSubarray() {
    assertThat(sut.minimumSumSubarray(ramp(100, i -> -1000), 1, 100)).isEqualTo(-1);
  }

  // Step 22: 1000, -1000, 1000, ... and only length 2 allowed. All 99 windows sum to exactly 0.
  //          Ignoring either length bound answers 1000, and sum >= 0 answers 0
  @Test
  void maxLengthAlternatingExtremesPairsAllCancel() {
    assertThat(sut.minimumSumSubarray(ramp(100, i -> i % 2 == 0 ? 1000 : -1000), 2, 2))
        .isEqualTo(-1);
  }

  // Step 23: a deterministic scramble over the full value range with a length window in the
  //          middle. Trying only length 30, or only lengths 30 and 70, answers 12. Zero-sum
  //          windows exist, so sum >= 0 answers 0, and a plain minimum answers -2116
  @Test
  void maxLengthScrambledValuesMidLengthWindow() {
    assertThat(sut.minimumSumSubarray(ramp(100, i -> (i * 373) % 2001 - 1000), 30, 70))
        .isEqualTo(2);
  }

  // ===========================================================================================
  // Hygiene (Step 24).
  // ===========================================================================================

  // Step 24: one instance answers several inputs of different sizes, largest in the middle. This
  //          catches prefix sums or a running best cached on the instance and not reset per call
  @Test
  void sameInstanceAnswersManyInputs() {
    assertThat(sut.minimumSumSubarray(List.of(3, -2, 1, 4), 2, 3)).isEqualTo(1);
    assertThat(sut.minimumSumSubarray(ramp(100, i -> (i * 373) % 2001 - 1000), 30, 70))
        .isEqualTo(2);
    assertThat(sut.minimumSumSubarray(List.of(-5), 1, 1)).isEqualTo(-1);
    assertThat(sut.minimumSumSubarray(List.of(1, 2, 3, 4), 2, 4)).isEqualTo(3);
  }

  private static List<Integer> ramp(int n, IntUnaryOperator value) {
    return IntStream.range(0, n).map(value).boxed().toList();
  }
}
