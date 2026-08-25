package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SmallestMissingMultipleOfKTest {
  SmallestMissingMultipleOfK sut = new SmallestMissingMultipleOfK();

  // ===========================================================================================
  // Which values are candidates, and where the scan starts (Steps 1-5).
  // ===========================================================================================

  // Step 1: the floor — a single element is the shortest legal input, and it happens to be the
  //         first multiple of k = 1, so the answer is the next one. A solution that returns k
  //         whenever it finds nothing better answers 1
  @Test
  void singleElementCoveringTheFirstMultipleReturnsTheSecond() {
    assertThat(sut.missingMultiple(new int[] {1}, 1)).isEqualTo(2);
  }

  // Step 2: the mirror of Step 1 — k itself is absent, so the scan has to begin at k and not at
  //         2k. The lone 3 is not a multiple of 2 and blocks nothing; a scan that starts one
  //         multiple too far in answers 4
  @Test
  void firstMultipleIsTheAnswerWhenAbsent() {
    assertThat(sut.missingMultiple(new int[] {3}, 2)).isEqualTo(2);
  }

  // Step 3: only multiples of k are ever candidates. Nothing in nums divides by 5, so the first
  //         multiple wins even though the array is dense at the bottom; a solution that returns
  //         the smallest missing positive integer answers 4
  @Test
  void valuesThatAreNotMultiplesNeverBlockTheAnswer() {
    assertThat(sut.missingMultiple(new int[] {1, 2, 3}, 5)).isEqualTo(5);
  }

  // Step 4: present multiples are skipped in order — 5 and 10 are both in nums while 3 and 7 are
  //         noise, so the scan lands on 15. A solution that walks nums positionally, expecting
  //         element i to be multiple number i + 1, answers 5
  @Test
  void consecutivePresentMultiplesAreSkipped() {
    assertThat(sut.missingMultiple(new int[] {3, 5, 7, 10}, 5)).isEqualTo(15);
  }

  // Step 5: the missing multiple can sit below every element. 10 and 15 are present but 5 is
  //         not, so the answer is 5; a solution that starts above max(nums), or above the
  //         largest present multiple, answers 20
  @Test
  void gapBelowEveryElementIsFound() {
    assertThat(sut.missingMultiple(new int[] {10, 15}, 5)).isEqualTo(5);
  }

  // ===========================================================================================
  // Duplicates, positivity, and the extremes of k (Steps 6-10).
  // ===========================================================================================

  // Step 6: nums may repeat a value. Three copies of 2 cover one multiple, not three, so the
  //         answer is 4; a solution sized by the element count that returns k times n plus one
  //         answers 8
  @Test
  void duplicatesCoverOneMultipleEach() {
    assertThat(sut.missingMultiple(new int[] {2, 2, 2}, 2)).isEqualTo(4);
  }

  // Step 7: the answer must be a positive multiple, so 0 is never it. Since 1 <= nums[i], zero
  //         is absent from every legal input, and a scan whose multiplier starts at 0 rather
  //         than 1 answers 0 here and on every other case in this file
  @Test
  void zeroIsNeverTheAnswer() {
    assertThat(sut.missingMultiple(new int[] {3, 6}, 3)).isEqualTo(9);
  }

  // Step 8: k may exceed every element. No multiple of 100 can hide among values below it, so
  //         the answer is k itself even though nums is not empty
  @Test
  void kLargerThanEveryElementReturnsK() {
    assertThat(sut.missingMultiple(new int[] {1, 2, 3}, 100)).isEqualTo(100);
  }

  // Step 9: k = 1 is legal and makes every positive integer a multiple, degenerating the problem
  //         into the smallest missing positive integer — here 4, the one gap in 1, 2, 3, 5
  @Test
  void k1DegeneratesToTheSmallestMissingPositive() {
    assertThat(sut.missingMultiple(new int[] {1, 2, 3, 5}, 1)).isEqualTo(4);
  }

  // Step 10: nums arrives unsorted and sorting it is not required. 5, 10 and 15 are all present
  //          and 25 is out of order noise, so the answer is 20; a scan that trusts the input
  //          order and expects element i to be multiple number i + 1 answers 5
  @Test
  void unsortedInputDoesNotChangeTheAnswer() {
    assertThat(sut.missingMultiple(new int[] {15, 10, 5, 25}, 5)).isEqualTo(20);
  }

  // ===========================================================================================
  // The official examples (Steps 11-12).
  // ===========================================================================================

  // Step 11: LeetCode Example 1 — every multiple of 2 up to 8 is present and 3 is noise, so the
  //          answer 10 sits above max(nums). This rules out the smallest-missing-positive
  //          reading, which answers 1
  @Test
  void leetCodeExample1() {
    assertThat(sut.missingMultiple(new int[] {8, 2, 3, 4, 6}, 2)).isEqualTo(10);
  }

  // Step 12: LeetCode Example 2 — the mirror. 10 and 15 are present but 5 is not, so the answer
  //          sits below max(nums). Its explanation exists to rule out scanning up from the
  //          largest present multiple, which answers 20
  @Test
  void leetCodeExample2() {
    assertThat(sut.missingMultiple(new int[] {1, 4, 7, 10, 15}, 5)).isEqualTo(5);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 13-17). nums[i] <= 100 caps the values but not the answer: the
  // first multiple above 100 is missing from every legal input, so answers as large as 200 are
  // reachable and a search bounded by 100, or by max(nums), cannot produce them. At
  // nums.length <= 100 every sane solution is instant, so these timeouts separate a scan that
  // terminates from one that spins forever, not one complexity class from another.
  // ===========================================================================================

  // Step 13: the maximum length holding every legal value, with k = 1 — all of 1 through 100 are
  //          multiples and all are present, so the answer is 101, one past the value ceiling
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k1WithEveryLegalValueAnswersAboveTheCeiling() {
    assertThat(sut.missingMultiple(oneThrough100(), 1)).isEqualTo(101);
  }

  // Step 14: every multiple of 3 that fits under the value ceiling is present — 3 through 99 —
  //          so the answer is the first one that does not fit, 102. A solution that scans only
  //          as far as 100 and then falls back to 100 + k answers 103
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void everyMultipleUnderTheCeilingPushesTheAnswerPastIt() {
    assertThat(sut.missingMultiple(multiplesOf(3, 100), 3)).isEqualTo(102);
  }

  // Step 15: only one multiple of 99 fits under the value ceiling, and a maximum length array
  //          holding every legal value covers it, so the answer is the second multiple, 198.
  //          The 100 + k fallback answers 199 and the positional scan answers 99
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k99WithEveryLegalValueAnswersTheSecondMultiple() {
    assertThat(sut.missingMultiple(oneThrough100(), 99)).isEqualTo(198);
  }

  // Step 16: the largest answer the constraints allow. k is at its ceiling of 100, its only
  //          multiple within the value range is present, and so the answer is 200 — twice the
  //          value ceiling, from an array of length 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k100CoveredAnswersTheLargestValueTheConstraintsAllow() {
    assertThat(sut.missingMultiple(new int[] {100}, 100)).isEqualTo(200);
  }

  // Step 17: maximum length with no distinct values at all — 100 copies of one multiple still
  //          cover only that one multiple, so the answer is 14 rather than anything scaled by
  //          the length; the count-based k times n plus one answers 707
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfOneRepeatedMultipleAnswersTheSecond() {
    assertThat(sut.missingMultiple(repeated(7, 100), 7)).isEqualTo(14);
  }

  // ===========================================================================================
  // Hygiene (Steps 18-19).
  // ===========================================================================================

  // Step 18: sorting nums is a tempting way to walk the multiples in order, but the array
  //          belongs to the caller and has to come back in its original order
  @Test
  void inputArrayIsNotModified() {
    int[] nums = {15, 10, 5, 25};
    int[] original = nums.clone();
    sut.missingMultiple(nums, 5);
    assertThat(nums).containsExactly(original);
  }

  // Step 19: one instance answers several inputs of different lengths, largest in the middle and
  //          deliberately out of order. A seen[] array or a set cached on the instance instead
  //          of rebuilt per call carries 1 through 100 into the calls that follow it
  @Test
  void oneInstanceAnswersSeveralInputs() {
    assertThat(sut.missingMultiple(new int[] {3}, 2)).isEqualTo(2);
    assertThat(sut.missingMultiple(oneThrough100(), 1)).isEqualTo(101);
    assertThat(sut.missingMultiple(new int[] {10, 15}, 5)).isEqualTo(5);
    assertThat(sut.missingMultiple(new int[] {2, 2, 2}, 2)).isEqualTo(4);
  }

  private static int[] oneThrough100() {
    return IntStream.rangeClosed(1, 100).toArray();
  }

  private static int[] multiplesOf(int k, int ceiling) {
    return IntStream.rangeClosed(1, ceiling / k).map(i -> i * k).toArray();
  }

  private static int[] repeated(int value, int count) {
    int[] nums = new int[count];
    Arrays.fill(nums, value);
    return nums;
  }
}
