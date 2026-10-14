package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximizeSumOfSquaresOfDigitsTest {
  MaximizeSumOfSquaresOfDigits sut = new MaximizeSumOfSquaresOfDigits();

  // ===========================================================================================
  // Exactly num digits, no leading zero, and the sum has to fit (Steps 1-6).
  // ===========================================================================================

  // Step 1: the floor — 1 <= num and 1 <= sum, so the smallest input is one digit summing to 1,
  //         and the only good integer is 1 itself
  @Test
  void num1Sum1IsTheDigitOne() {
    assertThat(sut.maxSumOfSquares(1, 1)).isEqualTo("1");
  }

  // Step 2: a sum that is a multiple of 9 leaves no remainder digit to write. A solution that
  //         always appends sum % 9 after the nines answers "90", one digit too many
  @Test
  void num1Sum9IsASingleNine() {
    assertThat(sut.maxSumOfSquares(1, 9)).isEqualTo("9");
  }

  // Step 3: sum == 9 * num is the largest reachable sum, and every digit is a 9. Testing
  //         feasibility with >= instead of > answers "", and appending a zero remainder answers
  //         "990"
  @Test
  void num2Sum18FillsEveryDigitWithNine() {
    assertThat(sut.maxSumOfSquares(2, 18)).isEqualTo("99");
  }

  // Step 4: the mirror of Step 3 — one past 9 * num cannot be reached with num digits, so there
  //         is no good integer and the answer is "". A greedy that skips the feasibility check
  //         and stops after num digits answers "99", whose digits sum to only 18
  @Test
  void num2Sum19IsImpossible() {
    assertThat(sut.maxSumOfSquares(2, 19)).isEmpty();
  }

  // Step 5: a good integer has exactly num digits, so its leading digit cannot be 0. Placing the
  //         digits from the least significant end answers "01", which is the 1-digit integer 1
  @Test
  void num2Sum1KeepsTheLeadingDigitNonZero() {
    assertThat(sut.maxSumOfSquares(2, 1)).isEqualTo("10");
  }

  // Step 6: only the integer has to be positive, not each digit, so a sum below num pads with
  //         zeros. Requiring every digit to be at least 1 answers "", and spreading the sum
  //         evenly answers "11100", which scores 3 instead of 9
  @Test
  void num5Sum3PadsWithZeros() {
    assertThat(sut.maxSumOfSquares(5, 3)).isEqualTo("30000");
  }

  // ===========================================================================================
  // Concentrate the sum: nines, then one remainder digit, then zeros (Steps 7-9). Squares are
  // convex, so moving a unit from a smaller digit to a larger one always raises the score, and
  // the only maximizing digits are sum / 9 nines, one digit sum % 9, and zeros. Every
  // arrangement of those digits ties on score, so the order alone decides the answer.
  // ===========================================================================================

  // Step 7: 81 + 1 = 82 beats the even split "55" at 25 + 25 = 50. Writing the remainder digit
  //         before the nines, or taking the smallest of the tied arrangements, answers "19"
  @Test
  void num2Sum10ConcentratesRatherThanSpreads() {
    assertThat(sut.maxSumOfSquares(2, 10)).isEqualTo("91");
  }

  // Step 8: every arrangement of 9, 4 and 0 scores 97, and the tie goes to the largest integer,
  //         so the remainder follows the nines. Writing the remainder first answers "490", and
  //         taking the smallest tied arrangement answers "409"
  @Test
  void num3Sum13PutsTheRemainderAfterTheNines() {
    assertThat(sut.maxSumOfSquares(3, 13)).isEqualTo("940");
  }

  // Step 9: the zeros pad the right end, after the remainder digit. Padding before the remainder
  //         answers "9003", and writing the remainder first answers "3900"
  @Test
  void num4Sum12PutsTheZerosAfterTheRemainder() {
    assertThat(sut.maxSumOfSquares(4, 12)).isEqualTo("9300");
  }

  // ===========================================================================================
  // The official examples (Steps 10-12).
  // ===========================================================================================

  // Step 10: LeetCode Example 1 — of the good integers 12, 21 and 30, the 30 wins on score, 9
  //          against 5. It rules out spreading the sum, which answers "21", and placing digits
  //          from the least significant end, which answers "03"
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxSumOfSquares(2, 3)).isEqualTo("30");
  }

  // Step 11: LeetCode Example 2 — 89 and 98 tie at 145, and the Explanation exists to show the
  //          tie goes to the larger integer. Taking the smaller tied integer, writing the
  //          remainder first, and placing digits from the least significant end all answer "89"
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxSumOfSquares(2, 17)).isEqualTo("98");
  }

  // Step 12: LeetCode Example 3 — no 1-digit integer sums to 10, and the answer for that is the
  //          empty string, neither null nor "-1". Skipping the feasibility check answers "9" if
  //          it stops after num digits, or "91" if it appends the remainder anyway
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxSumOfSquares(1, 10)).isEmpty();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 13-18). num <= 2 * 10^5 and sum <= 2 * 10^6. 9 * num is at most
  // 1.8 * 10^6, below the sum ceiling, so the largest sums are impossible at every length, and
  // the answer runs up to 2 * 10^5 characters. Enumerating num-digit integers is hopeless, and a
  // digit DP keyed on digits placed and sum left holds up to 4 * 10^11 states and cannot finish.
  // The greedy writes each character once, in O(num). Repeated String concatenation copies
  // ~2 * 10^10 characters, which measured about a second here, so the timeouts do not catch it.
  // ===========================================================================================

  // Step 13: Step 3 at full length — 1.8 * 10^6 is exactly 9 * num, so all 200000 digits are 9.
  //          Testing feasibility with >= instead of > answers ""
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNumAllNines() {
    assertThat(sut.maxSumOfSquares(200_000, 1_800_000))
        .isEqualTo("9".repeat(200_000))
        .hasSize(200_000);
  }

  // Step 14: Step 4 at full length — one past 9 * num. A greedy that stops after num digits
  //          without checking feasibility answers 200000 nines
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNumOnePastAllNinesIsImpossible() {
    assertThat(sut.maxSumOfSquares(200_000, 1_800_001)).isEmpty();
  }

  // Step 15: the longest run of zeros the constraints allow — a 1 followed by 199999 zeros.
  //          Placing digits from the least significant end answers 199999 zeros and then a 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNumSum1IsAOneFollowedByZeros() {
    assertThat(sut.maxSumOfSquares(200_000, 1))
        .isEqualTo("1" + "0".repeat(199_999))
        .hasSize(200_000);
  }

  // Step 16: all three parts at scale — 10^6 = 9 * 111111 + 1, so 111111 nines, then a 1, then
  //          88888 zeros
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNumSplitsIntoNinesRemainderAndZeros() {
    assertThat(sut.maxSumOfSquares(200_000, 1_000_000))
        .isEqualTo("9".repeat(111_111) + "1" + "0".repeat(88_888))
        .hasSize(200_000);
  }

  // Step 17: both ceilings at once. Even at maximum length, 2 * 10^6 exceeds 9 * num, so the
  //          constraints admit impossible inputs at every num and the answer is ""
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumNumMaximumSumIsImpossible() {
    assertThat(sut.maxSumOfSquares(200_000, 2_000_000)).isEmpty();
  }

  // Step 18: the widest gap, a single digit against a sum of 2 * 10^6. A solution that writes
  //          sum / 9 nines before checking the length builds 222222 characters only to discard
  //          them, and one that never checks answers a string far longer than num
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void num1MaximumSumIsImpossible() {
    assertThat(sut.maxSumOfSquares(1, 2_000_000)).isEmpty();
  }

  // ===========================================================================================
  // Hygiene (Step 19).
  // ===========================================================================================

  // Step 19: one instance answers several inputs of different lengths, largest in the middle and
  //          deliberately out of order. A StringBuilder kept on the instance and not cleared per
  //          call prefixes every answer with the one before it
  @Test
  void oneInstanceAnswersSeveralInputs() {
    assertThat(sut.maxSumOfSquares(2, 3)).isEqualTo("30");
    assertThat(sut.maxSumOfSquares(200_000, 1_000_000))
        .isEqualTo("9".repeat(111_111) + "1" + "0".repeat(88_888));
    assertThat(sut.maxSumOfSquares(2, 19)).isEmpty();
    assertThat(sut.maxSumOfSquares(4, 12)).isEqualTo("9300");
  }
}
