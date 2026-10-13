package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximumAlternatingSumOfSquaresTest {
  MaximumAlternatingSumOfSquares sut = new MaximumAlternatingSumOfSquares();

  // ===========================================================================================
  // The score is of squares, and the order is ours to choose (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor — 1 <= nums.length, so a single element is the shortest legal input. It
  //         sits at index 0, the plus side, and scores its square. A solution that alternates
  //         the raw values, or ranks and sums absolute values, answers 5
  @Test
  void singleElementScoresItsSquare() {
    assertThat(sut.maxAlternatingSum(new int[] {5})).isEqualTo(25);
  }

  // Step 2: the sign is squared away. A lone negative still scores a positive square, and a
  //         solution that forgets to square answers -5
  @Test
  void singleNegativeElementScoresAPositiveSquare() {
    assertThat(sut.maxAlternatingSum(new int[] {-5})).isEqualTo(25);
  }

  // Step 3: the elements may be rearranged, so the larger square goes first, 25 - 9 = 16. Scoring
  //         nums in the order given answers 9 - 25 = -16
  @Test
  void twoElementsPutTheLargerSquareFirst() {
    assertThat(sut.maxAlternatingSum(new int[] {3, 5})).isEqualTo(16);
  }

  // Step 4: the mirror of Step 3, where the input order happens to be right. The larger square
  //         comes from the smaller value, so ranking by value instead of by magnitude puts 3
  //         first and answers 9 - 25 = -16
  @Test
  void largerMagnitudeLeadsEvenWhenItsValueIsSmaller() {
    assertThat(sut.maxAlternatingSum(new int[] {-5, 3})).isEqualTo(16);
  }

  // ===========================================================================================
  // Only the parity of a slot matters: a partition, not an interleave (Steps 5-8).
  // ===========================================================================================

  // Step 5: even indices are plus and index 0 is even, so an odd length has one more plus slot
  //         than minus slots — ceil(n / 2) plus and floor(n / 2) minus. squares = [4, 9, 16]
  //         -> 16 + 9 - 4 = 21. Giving the extra slot to the minus side answers 16 - 9 - 4 = 3
  @Test
  void oddLengthGivesTheExtraSlotToThePlusSide() {
    assertThat(sut.maxAlternatingSum(new int[] {2, 3, 4})).isEqualTo(21);
  }

  // Step 6: where a square lands within its side is irrelevant, so the answer is the larger half
  //         of the squares minus the smaller half, (16 + 9) - (4 + 1) = 20. Sorting the squares
  //         in descending order and alternating pits 16 against 9 and answers 10
  @Test
  void largerHalfIsPlusAndSmallerHalfIsMinus() {
    assertThat(sut.maxAlternatingSum(new int[] {1, 2, 3, 4})).isEqualTo(20);
  }

  // Step 7: equal largest squares both belong on the plus side, (25 + 25) - (1 + 1) = 48. Sorting
  //         descending and alternating subtracts one 25 from the other and answers 0, the same
  //         as scoring the input order
  @Test
  void duplicateLargestSquaresBothGoPlus() {
    assertThat(sut.maxAlternatingSum(new int[] {5, 5, 1, 1})).isEqualTo(48);
  }

  // Step 8: zeros are the cheapest possible minus-side fillers, so both 36 and 49 stay whole,
  //         (49 + 36) - (0 + 0) = 85. The descending interleave answers 49 - 36 = 13 and the
  //         input order answers -13
  @Test
  void zerosFillTheMinusSideForFree() {
    assertThat(sut.maxAlternatingSum(new int[] {0, 0, 6, 7})).isEqualTo(85);
  }

  // ===========================================================================================
  // Signs and equal magnitudes (Steps 9-11).
  // ===========================================================================================

  // Step 9: magnitude ranks across signs, and the input alternates them. squares =
  //         [1, 4, 9, 16, 25] -> (25 + 16 + 9) - (4 + 1) = 45. Ranking by value sends -5 and -3
  //         to the minus side and answers -13, and the input order answers 15
  @Test
  void magnitudeRanksAcrossSigns() {
    assertThat(sut.maxAlternatingSum(new int[] {-1, 2, -3, 4, -5})).isEqualTo(45);
  }

  // Step 10: a value and its negation are the same square, and every copy fills its own slot. At
  //          even length, four 9s split two and two and cancel to 0. Collecting the squares in a
  //          set collapses them into one 9 and answers 9
  @Test
  void equalMagnitudesOfEvenLengthCancelToZero() {
    assertThat(sut.maxAlternatingSum(new int[] {3, -3, -3, 3})).isZero();
  }

  // Step 11: the odd-length mirror of Step 10 — five 9s split three and two, leaving one square,
  //          9. Giving the extra slot to the minus side answers -9
  @Test
  void equalMagnitudesOfOddLengthLeaveOneSquare() {
    assertThat(sut.maxAlternatingSum(new int[] {3, -3, 3, -3, 3})).isEqualTo(9);
  }

  // ===========================================================================================
  // The official examples (Steps 12-13).
  // ===========================================================================================

  // Step 12: LeetCode Example 1 — the arrangement [2, 1, 3] scores 4 - 1 + 9 = 12. It rules out
  //          scoring the input order and both sorted interleaves, which answer 6, and the
  //          extra-slot-to-minus reading, which answers 4
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxAlternatingSum(new int[] {1, 2, 3})).isEqualTo(12);
  }

  // Step 13: LeetCode Example 2 — the explained arrangement [-3, -1, -2, 1, 3, 2] puts its plus
  //          squares in the order 9, 4, 9, which shows that only the parity of a slot matters.
  //          The input order, ranking by value, and the descending interleave all answer 0, and a
  //          set of squares answers 12
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxAlternatingSum(new int[] {1, -1, 2, -2, 3, -3})).isEqualTo(16);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 14-17). |nums[i]| <= 4 * 10^4 caps one square at 1.6 * 10^9, which
  // still fits an int, but two of them do not. Because int arithmetic wraps modulo 2^32, an int
  // accumulator still lands on the right answer whenever the final value fits, so only answers
  // above Integer.MAX_VALUE expose it. nums.length <= 10^5: an O(n^2) selection or insertion
  // sort needs ~10^10 steps and cannot finish; sorting in O(n log n), or counting magnitudes in
  // O(n + 4 * 10^4), finishes in milliseconds.
  // ===========================================================================================

  // Step 14: the value bounds at the shortest length that overflows. squares =
  //          [0, 1.6 * 10^9, 1.6 * 10^9] -> the answer is 3.2 * 10^9 and an int accumulator
  //          answers -1094967296
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void answerAboveIntRangeFromThreeElements() {
    assertThat(sut.maxAlternatingSum(new int[] {40_000, -40_000, 0})).isEqualTo(3_200_000_000L);
  }

  // Step 15: the largest answer the constraints allow — maximum length, half the elements at the
  //          magnitude ceiling and half at 0, which gives 5 * 10^4 times 1.6 * 10^9 = 8 * 10^13.
  //          The zeros sit at the even indices, so scoring the input order answers -8 * 10^13,
  //          and an int accumulator answers a plausible-looking 1939144704
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumAnswerFromHalfExtremesAndHalfZeros() {
    assertThat(sut.maxAlternatingSum(zerosBetweenExtremes(100_000))).isEqualTo(80_000_000_000_000L);
  }

  // Step 16: maximum length sweeping the whole value range, -4 * 10^4 through 4 * 10^4 and then
  //          the first 19999 of them again, so every magnitude appears with both signs and the
  //          sort has real work to do. Ranking by value answers -18000299959999 and the
  //          descending interleave answers 1000050001
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRampOverTheWholeValueRange() {
    assertThat(sut.maxAlternatingSum(wrappingRamp(100_000))).isEqualTo(41_260_625_934_815L);
  }

  // Step 17: Step 11 at the largest odd length — 99999 squares of 1.6 * 10^9 split 50000 and
  //          49999, leaving exactly one. Giving the extra slot to the minus side answers
  //          -1.6 * 10^9
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largestOddLengthOfEqualMagnitudesLeavesOneSquare() {
    assertThat(sut.maxAlternatingSum(alternatingExtremes(99_999))).isEqualTo(1_600_000_000L);
  }

  // ===========================================================================================
  // Hygiene (Step 18).
  // ===========================================================================================

  // Step 18: one instance answers several inputs of different lengths, largest in the middle and
  //          deliberately out of order. A magnitude count array or a running total kept on the
  //          instance instead of rebuilt per call carries 10^5 elements into the calls after it
  @Test
  void oneInstanceAnswersSeveralInputs() {
    assertThat(sut.maxAlternatingSum(new int[] {-5})).isEqualTo(25);
    assertThat(sut.maxAlternatingSum(zerosBetweenExtremes(100_000))).isEqualTo(80_000_000_000_000L);
    assertThat(sut.maxAlternatingSum(new int[] {5, 5, 1, 1})).isEqualTo(48);
    assertThat(sut.maxAlternatingSum(new int[] {3, -3, -3, 3})).isZero();
  }

  private static int[] zerosBetweenExtremes(int n) {
    int[] nums = new int[n];
    for (int i = 1; i < n; i += 2) {
      nums[i] = i % 4 == 1 ? 40_000 : -40_000;
    }
    return nums;
  }

  private static int[] wrappingRamp(int n) {
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i % 80_001 - 40_000);
    return nums;
  }

  private static int[] alternatingExtremes(int n) {
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i % 2 == 0 ? 40_000 : -40_000);
    return nums;
  }
}
