package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SequentialDigitsTest {
  SequentialDigits sut = new SequentialDigits();

  // Step 1: LeetCode Example 1 — the range [100, 300] holds exactly two sequential-digit numbers,
  // 123 and 234; the next run 345 is already above 300
  @Test
  void firstExampleFindsBothThreeDigitRuns() {
    assertThat(sut.sequentialDigits(100, 300)).containsExactly(123, 234);
  }

  // Step 2: LeetCode Example 2 — the range crosses a digit-length boundary, and generating by
  // starting digit emits 12345 right after 1234, so the result must be re-sorted numerically to
  // place 12345 after 6789
  @Test
  void secondExampleSortsAcrossDigitLengths() {
    assertThat(sut.sequentialDigits(1000, 13000))
        .containsExactly(1234, 2345, 3456, 4567, 5678, 6789, 12345);
  }

  // Step 3: a range collapsed to a single sequential-digit number — both endpoints are inclusive,
  // so [123, 123] keeps it; a strict comparison on either bound would drop it
  @Test
  void bothEndpointsAreInclusive() {
    assertThat(sut.sequentialDigits(123, 123)).containsExactly(123);
  }

  // Step 4: [124, 233] sits strictly between the neighbors 123 and 234, missing each by one — the
  // answer is an empty list (not null), and neither bound may leak
  @Test
  void gapBetweenNeighborsYieldsEmptyList() {
    assertThat(sut.sequentialDigits(124, 233)).isEmpty();
  }

  // Step 5: 21 has descending digits and 22 a repeated digit — "one more than the previous" rules
  // out both, guarding against reading the rule as "adjacent digits differ by one"
  @Test
  void descendingOrRepeatedDigitsDoNotCount() {
    assertThat(sut.sequentialDigits(21, 22)).isEmpty();
  }

  // Step 6: 890 and 901 would both qualify if the sequence wrapped 9 -> 0 modulo ten; the range
  // [890, 902] holds no true sequential-digit number, pinning that runs stop at the digit 9
  @Test
  void sequenceDoesNotWrapAroundFromNineToZero() {
    assertThat(sut.sequentialDigits(890, 902)).isEmpty();
  }

  // Step 7: the constraint floor low = 10 — neither 10 (0 is not 1 + 1) nor 11 (repeated) counts,
  // so 12 is the smallest sequential-digit integer any input can produce
  @Test
  void constraintFloorAdmitsOnlyTwelve() {
    assertThat(sut.sequentialDigits(10, 13)).containsExactly(12);
  }

  // Step 8: the constraint ceiling high = 10^9 — 123456789 is the longest possible run and the
  // largest answer overall, sitting just below the ceiling; extending it would need a tenth digit
  @Test
  void constraintCeilingCapturesTheLongestRun() {
    assertThat(sut.sequentialDigits(123_456_789, 1_000_000_000)).containsExactly(123_456_789);
  }

  // Step 9: the full constraint range [10, 10^9] must enumerate all 36 sequential-digit numbers —
  // every contiguous substring of "123456789" of length two through nine — in sorted order, which
  // groups them by length because each length's largest (e.g. 89) is below the next length's
  // smallest (123)
  @Test
  void fullConstraintRangeEnumeratesAllThirtySixNumbers() {
    assertThat(sut.sequentialDigits(10, 1_000_000_000))
        .containsExactly(
            12, 23, 34, 45, 56, 67, 78, 89, 123, 234, 345, 456, 567, 678, 789, 1234, 2345, 3456,
            4567, 5678, 6789, 12345, 23456, 34567, 45678, 56789, 123456, 234567, 345678, 456789,
            1234567, 2345678, 3456789, 12345678, 23456789, 123456789);
  }
}
