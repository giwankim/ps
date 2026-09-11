package leetcode.p3401_3500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Unique3DigitEvenNumbersTest {
  Unique3DigitEvenNumbers sut = new Unique3DigitEvenNumbers();

  // ===========================================================================================
  // One rule at a time (Steps 1-6).
  // ===========================================================================================

  // Step 1: smallest valid input (3 <= digits.length) with a single even digit, so 2 is pinned to
  //         the units place and only the order of 1 and 3 varies -> 132 and 312
  @Test
  void singleEvenDigitMustBeTheUnitsDigit() {
    assertThat(sut.totalNumbers(new int[] {1, 2, 3})).isEqualTo(2);
  }

  // Step 2: the same digits in a different input order. Digits are rearranged freely, not read as
  //         a subsequence -> a solution that only takes indices i < j < k sees 231, which is odd,
  //         and answers 0
  @Test
  void inputOrderDoesNotConstrainDigitPlacement() {
    assertThat(sut.totalNumbers(new int[] {2, 3, 1})).isEqualTo(2);
  }

  // Step 3: 0 is even, so it may end a number -> 130 and 310. A parity check that only accepts
  //         2, 4, 6 and 8 answers 0
  @Test
  void zeroCountsAsAnEvenUnitsDigit() {
    assertThat(sut.totalNumbers(new int[] {0, 1, 3})).isEqualTo(2);
  }

  // Step 4: no leading zeros -> 102, 120 and 210, but not 012. A solution that forgets the rule
  //         answers 4
  @Test
  void zeroCannotLeadTheNumber() {
    assertThat(sut.totalNumbers(new int[] {0, 1, 2})).isEqualTo(3);
  }

  // Step 5: all zeros can only spell 000, which has a leading zero and is not a three-digit
  //         number. Forgetting the rule answers 1
  @Test
  void allZerosFormNothing() {
    assertThat(sut.totalNumbers(new int[] {0, 0, 0})).isZero();
  }

  // Step 6: the mirror of Step 4. Zero is banned only from the hundreds place, so two zeros may
  //         fill the tens and units -> 500 is the one number. Counting index choices answers 2,
  //         because the two zeros swap places to spell 500 twice
  @Test
  void zerosMayFillTheTensAndUnits() {
    assertThat(sut.totalNumbers(new int[] {0, 0, 5})).isEqualTo(1);
  }

  // ===========================================================================================
  // Copies and distinctness (Steps 7-10).
  // ===========================================================================================

  // Step 7: each copy is used at most once per number. Two 2s give 252 and 522, but not 222.
  //         Reusing any present digit freely also admits 222 and 552 and answers 4
  @Test
  void eachCopyOfADigitIsUsedAtMostOnce() {
    assertThat(sut.totalNumbers(new int[] {2, 2, 5})).isEqualTo(2);
  }

  // Step 8: the answer counts distinct numbers, not choices of positions. 122 and 212 are each
  //         spelled twice because the two 2s can swap, so counting index triples answers 4. And
  //         "unique" does not mean three different digits -> that reading answers 0
  @Test
  void swappingEqualCopiesDoesNotCreateANewNumber() {
    assertThat(sut.totalNumbers(new int[] {2, 2, 1})).isEqualTo(2);
  }

  // Step 9: surplus copies add nothing once a number is formable -> only 222, where counting index
  //         triples answers 4 * 3 * 2 = 24
  @Test
  void surplusCopiesOfOneDigitStillFormOneNumber() {
    assertThat(sut.totalNumbers(new int[] {2, 2, 2, 2})).isEqualTo(1);
  }

  // Step 10: the rules together. 0 may sit in the tens or the units but not the hundreds ->
  //          808, 880 and 888. Allowing a leading zero adds 088 for 4, and treating 0 as odd
  //          drops 880 for 2
  @Test
  void zeroJoinsRepeatedDigitsOutsideTheHundredsPlace() {
    assertThat(sut.totalNumbers(new int[] {8, 8, 8, 0})).isEqualTo(3);
  }

  // ===========================================================================================
  // The official examples (Steps 11-14).
  // ===========================================================================================

  // Step 11: LeetCode Example 1 -> 12 numbers, and the Explanation calls out that 222 is not one
  //          of them. Reusing digits freely answers 32, and the subsequence reading answers 3
  @Test
  void leetCodeExample1() {
    assertThat(sut.totalNumbers(new int[] {1, 2, 3, 4})).isEqualTo(12);
  }

  // Step 12: LeetCode Example 2 -> 202 and 220. The second 2 is a real copy and may be used, but
  //          022 has a leading zero. Allowing it answers 3, counting index triples answers 4, and
  //          demanding three different digits answers 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.totalNumbers(new int[] {0, 2, 2})).isEqualTo(2);
  }

  // Step 13: LeetCode Example 3 -> only 666. The six orderings of three equal copies are one
  //          number, not 6
  @Test
  void leetCodeExample3() {
    assertThat(sut.totalNumbers(new int[] {6, 6, 6})).isEqualTo(1);
  }

  // Step 14: LeetCode Example 4 -> no even digit at all, so there is nothing to put in the units
  @Test
  void leetCodeExample4() {
    assertThat(sut.totalNumbers(new int[] {1, 3, 5})).isZero();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 15-19). digits.length <= 10, so there are at most 10 * 9 * 8 = 720
  // ordered index triples and only 450 even three-digit numbers to test. Any enumeration
  // finishes in microseconds, and these timeouts only turn a runaway loop into a failure
  // instead of a hung build. The steps exist for the counts, which separate the wrong readings
  // far more sharply at ten digits than at three.
  // ===========================================================================================

  // Step 15: every digit once -> 9 hundreds * 8 tens for a 0 units, plus 4 evens * 8 * 8 for the
  //          rest, 72 + 256 = 328. Free reuse answers 450, allowing a leading zero answers 360,
  //          treating 0 as odd answers 256, and the subsequence reading answers 34
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfEveryDigitOnce() {
    assertThat(sut.totalNumbers(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})).isEqualTo(328);
  }

  // Step 16: two copies of every even digit -> 96. Counting index triples answers 576, free
  //          reuse 100, allowing a leading zero 120, treating 0 as odd 76, and three different
  //          digits 48
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfPairedEvenDigits() {
    assertThat(sut.totalNumbers(new int[] {0, 0, 2, 2, 4, 4, 6, 6, 8, 8})).isEqualTo(96);
  }

  // Step 17: 0 is the only even digit, so every number ends in 0 over two odd digits -> 5 * 4 = 20
  //          ordered pairs of different odds, plus 110, 330, 550 and 770 from the doubled ones,
  //          24 in all. Treating 0 as odd answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWhereZeroIsTheOnlyEvenDigit() {
    assertThat(sut.totalNumbers(new int[] {1, 3, 5, 7, 9, 1, 3, 5, 7, 0})).isEqualTo(24);
  }

  // Step 18: ten copies of 8 -> only 888, where counting index triples answers 720
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfOneRepeatedEvenDigit() {
    assertThat(sut.totalNumbers(filled(10, 8))).isEqualTo(1);
  }

  // Step 19: nine zeros and a 1 -> the 1 must lead, so only 100. Allowing a leading zero adds
  //          000 and 010 for 3, and counting index triples answers 72
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthOfZerosWithOneLeadingCandidate() {
    int[] digits = filled(10, 0);
    digits[9] = 1;
    assertThat(sut.totalNumbers(digits)).isEqualTo(1);
  }

  // ===========================================================================================
  // Hygiene (Steps 20-21).
  // ===========================================================================================

  // Step 20: the caller's array is left untouched. Sorting it in place, or decrementing entries
  //          as copies are consumed, are both tempting shortcuts
  @Test
  void doesNotModifyTheInput() {
    int[] digits = {4, 0, 9, 4, 1, 0};
    int[] original = digits.clone();
    assertThat(sut.totalNumbers(digits)).isEqualTo(19);
    assertThat(digits).containsExactly(original);
  }

  // Step 21: one instance answers several inputs, the largest in the middle. A digit tally kept
  //          on the instance answers 364 for the third call, and a set of seen numbers that is
  //          never cleared answers 329 for the second
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.totalNumbers(new int[] {0, 0, 5})).isEqualTo(1);
    assertThat(sut.totalNumbers(new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0})).isEqualTo(328);
    assertThat(sut.totalNumbers(new int[] {2, 2, 5})).isEqualTo(2);
    assertThat(sut.totalNumbers(new int[] {1, 3, 5})).isZero();
  }

  private static int[] filled(int n, int value) {
    int[] digits = new int[n];
    Arrays.fill(digits, value);
    return digits;
  }
}
