package leetcode.p3401_3500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class FindTheLargestAlmostMissingIntegerTest {
  FindTheLargestAlmostMissingInteger sut = new FindTheLargestAlmostMissingInteger();

  // Step 1: smallest valid input (1 <= nums.length, 1 <= k <= nums.length) — the lone element
  // sits in the lone subarray, so it is almost missing and both k = 1 and k = n agree here
  @Test
  void singleElementIsAlmostMissing() {
    assertThat(sut.largestInteger(new int[] {5}, 1)).isEqualTo(5);
  }

  // Step 2: LeetCode Example 3 — with k = 1 every subarray is one element, so a value repeated
  // twice lands in two subarrays and nothing qualifies
  @Test
  void repeatedValueWithWindowOfOneHasNoAnswer() {
    assertThat(sut.largestInteger(new int[] {0, 0}, 1)).isEqualTo(-1);
  }

  // Step 3: 0 is a legal value (0 <= nums[i]) and -1 is the "no answer" sentinel, so returning
  // zero must be distinguishable from returning nothing
  @Test
  void zeroIsAValidAnswerAndNotTheNoAnswerSentinel() {
    assertThat(sut.largestInteger(new int[] {0, 1, 1}, 1)).isEqualTo(0);
  }

  // Step 4: with k = 1 the answer is the largest value occurring exactly once, which is not the
  // largest value in the array — 9 appears twice and is disqualified
  @Test
  void windowOfOnePicksLargestUniqueValueNotLargestValue() {
    assertThat(sut.largestInteger(new int[] {3, 9, 2, 9}, 1)).isEqualTo(3);
  }

  // Step 5: with k = 1 an interior element can win, since every position is its own subarray.
  // Contrast with step 11, where interior positions are always ruled out
  @Test
  void windowOfOneLetsAnInteriorElementWin() {
    assertThat(sut.largestInteger(new int[] {1, 9, 1}, 1)).isEqualTo(9);
  }

  // Step 6: k = n leaves exactly one subarray containing everything, so every distinct value is
  // almost missing and the answer is simply the maximum
  @Test
  void fullWidthWindowReturnsTheMaximum() {
    assertThat(sut.largestInteger(new int[] {3, 9}, 2)).isEqualTo(9);
  }

  // Step 7: same shape as step 2 but k = n — a repeated value now occupies the single subarray
  // once rather than two subarrays, so duplicates no longer disqualify anything
  @Test
  void fullWidthWindowAcceptsDuplicatedValues() {
    assertThat(sut.largestInteger(new int[] {7, 7}, 2)).isEqualTo(7);
  }

  // Step 8: k = n with the maximum in the middle — position is irrelevant when there is only
  // one subarray
  @Test
  void fullWidthWindowReturnsAnInteriorMaximum() {
    assertThat(sut.largestInteger(new int[] {1, 50, 1}, 3)).isEqualTo(50);
  }

  // Step 9: first case of 1 < k < n — only the two ends can appear in a single subarray, and
  // here both are unique so the larger of the two ends wins
  @Test
  void firstElementWinsWhenItIsTheLargerUniqueEnd() {
    assertThat(sut.largestInteger(new int[] {9, 2, 3, 1}, 2)).isEqualTo(9);
  }

  // Step 10: mirror of step 9 — the comparison is between the two ends, not a scan direction
  @Test
  void lastElementWinsWhenItIsTheLargerUniqueEnd() {
    assertThat(sut.largestInteger(new int[] {1, 2, 3, 9}, 2)).isEqualTo(9);
  }

  // Step 11: the interior maximum is ignored when 1 < k < n, because any interior index is
  // covered by at least two subarrays of size k. Contrast with step 8
  @Test
  void interiorMaximumIsIgnoredWhenWindowIsNarrowerThanTheArray() {
    assertThat(sut.largestInteger(new int[] {1, 50, 2}, 2)).isEqualTo(2);
  }

  // Step 12: an end is only a candidate if that value occurs nowhere else — 5 repeats in the
  // interior, so the smaller end wins despite 5 being larger
  @Test
  void firstElementRepeatedInTheInteriorIsDisqualified() {
    assertThat(sut.largestInteger(new int[] {5, 5, 3}, 2)).isEqualTo(3);
  }

  // Step 13: mirror of step 12 — the trailing end is disqualified by its interior duplicate
  @Test
  void lastElementRepeatedInTheInteriorIsDisqualified() {
    assertThat(sut.largestInteger(new int[] {3, 5, 5}, 2)).isEqualTo(3);
  }

  // Step 14: the two ends hold the same value, so it occupies the first and last subarray —
  // two of them. Checking only the interior for duplicates would wrongly return 6
  @Test
  void endsSharingOneValueDisqualifyThatValue() {
    assertThat(sut.largestInteger(new int[] {6, 1, 6}, 2)).isEqualTo(-1);
  }

  // Step 15: both ends are separately disqualified by interior duplicates, leaving no candidate
  @Test
  void bothEndsDisqualifiedYieldsNoAnswer() {
    assertThat(sut.largestInteger(new int[] {4, 4, 2, 2}, 2)).isEqualTo(-1);
  }

  // Step 16: LeetCode Example 1 — 3 and 7 both appear in exactly one subarray of size 3, and 7
  // is the larger
  @Test
  void example1PicksTheLargerOfTwoQualifyingEnds() {
    assertThat(sut.largestInteger(new int[] {3, 9, 2, 1, 7}, 3)).isEqualTo(7);
  }

  // Step 17: LeetCode Example 2 — the trailing 7 also appears in the interior, so only the
  // leading 3 survives even though 7 is larger
  @Test
  void example2PicksTheOnlyQualifyingEnd() {
    assertThat(sut.largestInteger(new int[] {3, 9, 7, 2, 1, 7}, 4)).isEqualTo(3);
  }

  // Step 18: k = n - 1, the widest window that is still narrower than the array — the two
  // subarrays already cover every interior index, so 9 loses to the leading 2
  @Test
  void windowOneShorterThanTheArrayStillRulesOutTheInterior() {
    assertThat(sut.largestInteger(new int[] {2, 5, 9, 1}, 3)).isEqualTo(2);
  }

  // Step 19: upper bounds on both length and value (nums.length = 50, nums[i] = 50) with
  // k = n — the single subarray makes the maximum the answer
  @Test
  void maximumLengthWithFullWidthWindowReturnsTheMaximum() {
    int[] nums = new int[50];
    nums[0] = 50;
    assertThat(sut.largestInteger(nums, 50)).isEqualTo(50);
  }

  // Step 20: maximum length with k = 1 — 7 fills 49 positions and only the interior 50 occurs
  // exactly once
  @Test
  void maximumLengthWithWindowOfOneFindsTheLoneUniqueValue() {
    int[] nums = new int[50];
    Arrays.fill(nums, 7);
    nums[25] = 50;
    assertThat(sut.largestInteger(nums, 1)).isEqualTo(50);
  }

  // Step 21: maximum length with a mid sized window — both ends are unique, so the larger end
  // wins over the 48 zeros between them
  @Test
  void maximumLengthWithMidSizedWindowComparesTheTwoEnds() {
    int[] nums = new int[50];
    nums[0] = 50;
    nums[49] = 49;
    assertThat(sut.largestInteger(nums, 25)).isEqualTo(50);
  }

  // Step 22: maximum length where every value is identical — each end repeats throughout, so
  // there is no almost missing integer at all
  @Test
  void maximumLengthOfIdenticalValuesHasNoAnswer() {
    int[] nums = new int[50];
    Arrays.fill(nums, 50);
    assertThat(sut.largestInteger(nums, 25)).isEqualTo(-1);
  }
}
