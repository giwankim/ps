package leetcode.p2901_3000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class SmallestMissingIntegerGreaterThanSequentialPrefixSumTest {
  SmallestMissingIntegerGreaterThanSequentialPrefixSum sut =
      new SmallestMissingIntegerGreaterThanSequentialPrefixSum();

  // Step 1: smallest valid input (1 <= nums.length) — the prefix of just nums[0] is sequential,
  // so the sum is 1, and since 1 is in the array the search moves up to 2
  @Test
  void singleElementSkipsItselfAndReturnsTheNextInteger() {
    assertThat(sut.missingInteger(new int[] {1})).isEqualTo(2);
  }

  // Step 2: two elements one apart extend the prefix, so the sum is 1 + 2 = 3
  @Test
  void sequentialPairSumsBothElements() {
    assertThat(sut.missingInteger(new int[] {1, 2})).isEqualTo(3);
  }

  // Step 3: a second element that is not nums[0] + 1 ends the prefix at index 0
  @Test
  void nonSequentialSecondElementLimitsThePrefixToTheFirstElement() {
    assertThat(sut.missingInteger(new int[] {1, 3})).isEqualTo(2);
  }

  // Step 4: an entirely sequential array sums to the whole array — 1+2+3+4+5 = 15
  @Test
  void fullySequentialArraySumsEveryElement() {
    assertThat(sut.missingInteger(new int[] {1, 2, 3, 4, 5})).isEqualTo(15);
  }

  // Step 5: "sequential" means +1 exactly, so a repeat of nums[0] breaks the run;
  // the sum is 1 and the search must then skip 1, 2 and 3 to land on 4
  @Test
  void duplicateFirstElementBreaksThePrefix() {
    assertThat(sut.missingInteger(new int[] {1, 1, 2, 3})).isEqualTo(4);
  }

  // Step 6: an array of equal values never extends past index 0
  @Test
  void allEqualElementsLimitThePrefixToTheFirstElement() {
    assertThat(sut.missingInteger(new int[] {2, 2, 2})).isEqualTo(3);
  }

  // Step 7: a descending step is not sequential either — the prefix stays [3], sum 3
  @Test
  void descendingArrayLimitsThePrefixToTheFirstElement() {
    assertThat(sut.missingInteger(new int[] {3, 2, 1})).isEqualTo(4);
  }

  // Step 8: the prefix must start at index 0 — the longer run [1,2,3,4] sitting later in the
  // array is irrelevant, so the sum is 5 rather than 10
  @Test
  void longerSequentialRunAfterTheStartIsIgnored() {
    assertThat(sut.missingInteger(new int[] {5, 1, 2, 3, 4})).isEqualTo(6);
  }

  // Step 9: the sum itself is the answer when it is absent, even though smaller values are
  // present — values below the sum can never be the answer
  @Test
  void valuesSmallerThanTheSumAreIgnored() {
    assertThat(sut.missingInteger(new int[] {4, 5, 6, 1, 2, 3})).isEqualTo(15);
  }

  // Step 10: membership is checked against the whole array, not just the prefix —
  // 6, 7 and 8 all sit outside the prefix yet still push the answer to 9
  @Test
  void skipsPresentValuesFoundOutsideThePrefix() {
    assertThat(sut.missingInteger(new int[] {1, 2, 3, 6, 7, 8})).isEqualTo(9);
  }

  // Step 11: LeetCode Example 1 — the prefix [1,2,3] sums to 6, which is missing
  @Test
  void leetCodeExample1() {
    assertThat(sut.missingInteger(new int[] {1, 2, 3, 2, 5})).isEqualTo(6);
  }

  // Step 12: LeetCode Example 2 — the prefix [3,4,5] sums to 12, and 12, 13, 14 are all
  // present (13 and 14 out of order), so the answer is 15
  @Test
  void leetCodeExample2() {
    assertThat(sut.missingInteger(new int[] {3, 4, 5, 1, 12, 14, 13})).isEqualTo(15);
  }

  // Step 13: upper value bound (nums[i] <= 50) — the answer may exceed every array value
  @Test
  void maximumValueSingleElementReturnsFiftyOne() {
    assertThat(sut.missingInteger(new int[] {50})).isEqualTo(51);
  }

  // Step 14: a prefix ending at the value ceiling still sums normally — 48+49+50 = 147
  @Test
  void prefixEndingAtTheValueCeilingSumsNormally() {
    assertThat(sut.missingInteger(new int[] {48, 49, 50})).isEqualTo(147);
  }

  // Step 15: largest possible sum — 50 sequential elements 1..50 total 1275, far above the
  // value ceiling, so a search bounded by 50 (or by the array length) would fail here
  @Test
  void maximumLengthSequentialPrefixSumsToTwelveSeventyFive() {
    assertThat(sut.missingInteger(IntStream.rangeClosed(1, 50).toArray())).isEqualTo(1275);
  }
}
