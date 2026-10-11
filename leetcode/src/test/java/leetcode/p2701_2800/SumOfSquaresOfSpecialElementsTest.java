package leetcode.p2701_2800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class SumOfSquaresOfSpecialElementsTest {
  SumOfSquaresOfSpecialElements sut = new SumOfSquaresOfSpecialElements();

  // ===========================================================================================
  // The definition, one clause at a time (Steps 1-5).
  // ===========================================================================================

  // Step 1: the floor is n = 1 (1 <= n), and 1 divides 1, so the lone element is always special.
  //         The answer is its square, 7 * 7 = 49. Summing the special values without squaring
  //         them answers 7
  @Test
  void n1LoneElementIsSpecialAndSquared() {
    assertThat(sut.sumOfSquares(new int[] {7})).isEqualTo(49);
  }

  // Step 2: both indices of n = 2 divide it, so both elements count, and each one is squared on
  //         its own: 3 * 3 + 4 * 4 = 25. Squaring the sum of the special elements answers 49
  @Test
  void n2SquaresEachElementBeforeSumming() {
    assertThat(sut.sumOfSquares(new int[] {3, 4})).isEqualTo(25);
  }

  // Step 3: the array is 1-indexed. For n = 3 the special indices are 1 and 3, the first and last
  //         elements, so the answer is 2 * 2 + 3 * 3 = 13. Testing Java's 0-based index against n
  //         throws ArithmeticException on n % 0, and skipping index 0 to dodge that picks the
  //         middle 5 alone and answers 25
  @Test
  void n3IndicesAreOneBased() {
    assertThat(sut.sumOfSquares(new int[] {2, 5, 3})).isEqualTo(13);
  }

  // Step 4: the test is n % i == 0, the index divides the length, not the other way round. For
  //         n = 4 that keeps indices 1, 2 and 4 for 2 * 2 + 3 * 3 + 5 * 5 = 38. The reversed
  //         reading, i % n == 0, keeps only the last index and answers 25
  @Test
  void n4IndexDividesLengthNotTheReverse() {
    assertThat(sut.sumOfSquares(new int[] {2, 3, 1, 5})).isEqualTo(38);
  }

  // Step 5: specialness belongs to the position, never the value. For n = 5 only indices 1 and 5
  //         qualify, giving 3 * 3 + 2 * 2 = 13, even though every 5 in the middle divides n.
  //         Testing whether the value divides n answers 75
  @Test
  void n5SpecialnessDependsOnIndexNotValue() {
    assertThat(sut.sumOfSquares(new int[] {3, 5, 5, 5, 2})).isEqualTo(13);
  }

  // ===========================================================================================
  // Which indices divide n (Steps 6-9).
  // ===========================================================================================

  // Step 6: a prime length has no divisors but 1 and itself, so only the first and last elements
  //         are special and the 9s in between add nothing: 2 * 2 + 3 * 3 = 13. Summing the square
  //         of every element answers 418
  @Test
  void n7PrimeLengthKeepsOnlyFirstAndLast() {
    assertThat(sut.sumOfSquares(new int[] {2, 9, 9, 9, 9, 9, 3})).isEqualTo(13);
  }

  // Step 7: n divides itself, so the last element is always special. For n = 8 the indices are
  //         1, 2, 4 and 8, giving 1 + 4 + 9 + 16 = 30. A loop over the proper divisors only drops
  //         the trailing 4 and answers 14, and a loop that stops at the square root without adding
  //         each cofactor n / i answers 5
  @Test
  void n8LastElementIsAlwaysSpecial() {
    assertThat(sut.sumOfSquares(new int[] {1, 2, 1, 3, 1, 1, 1, 4})).isEqualTo(30);
  }

  // Step 8: a perfect-square length has a divisor that pairs with itself. For n = 9 the indices
  //         are 1, 3 and 9, giving 2 * 2 + 5 * 5 + 3 * 3 = 38. Enumerating divisor pairs i and
  //         n / i up to the square root without checking i == n / i counts the 5 twice and
  //         answers 63
  @Test
  void n9PerfectSquareCountsItsRootOnce() {
    assertThat(sut.sumOfSquares(new int[] {2, 1, 5, 1, 1, 1, 1, 1, 3})).isEqualTo(38);
  }

  // Step 9: every special index contributes its own square, even when the values repeat. With all
  //         ones the answer is the divisor count, and 12 has six: 1, 2, 3, 4, 6 and 12. Summing
  //         over the set of distinct special values answers 1, and the square-root loop of Step 7
  //         answers 3
  @Test
  void n12AllOnesCountTheDivisors() {
    assertThat(sut.sumOfSquares(filled(12, 1))).isEqualTo(6);
  }

  // ===========================================================================================
  // The official examples (Steps 10-11).
  // ===========================================================================================

  // Step 10: LeetCode Example 1. Indices 1, 2 and 4 divide 4, so 1 + 4 + 16 = 21. Every value here
  //          equals its index, so this example alone cannot tell the index reading from the value
  //          reading that Step 5 rules out. Counting the root 2 twice, as in Step 8, answers 25
  @Test
  void leetCodeExample1() {
    assertThat(sut.sumOfSquares(new int[] {1, 2, 3, 4})).isEqualTo(21);
  }

  // Step 11: LeetCode Example 2. Indices 1, 2, 3 and 6 divide 6, so 4 + 49 + 1 + 9 = 63, and the
  //          19 and 18 at indices 4 and 5 are skipped. The Explanation's nums[6] has no 0-based
  //          counterpart, and the 0-based loop of Step 3 answers 411
  @Test
  void leetCodeExample2() {
    assertThat(sut.sumOfSquares(new int[] {2, 7, 1, 19, 18, 3})).isEqualTo(63);
  }

  // ===========================================================================================
  // The constraint bounds (Steps 12-15).
  //
  // With n <= 50 even a quadratic scan is a few thousand operations, so these steps check the
  // edges of the input space rather than speed and carry no timeout. The answer peaks at
  // 10 * 50 * 50 = 25000 for n = 48, so int never overflows.
  // ===========================================================================================

  // Step 12: n = 50 with every value at its maximum. 50 has six divisors, 1, 2, 5, 10, 25 and 50,
  //          so the answer is 6 * 2500 = 15000. Summing the square of every element answers 125000
  @Test
  void n50AllMaximumValues() {
    assertThat(sut.sumOfSquares(filled(50, 50))).isEqualTo(15_000);
  }

  // Step 13: 48 has ten divisors, more than any other length the constraints allow, so with every
  //          value at 50 it produces the largest possible answer, 10 * 2500 = 25000
  @Test
  void n48MostDivisorsGivesTheLargestAnswer() {
    assertThat(sut.sumOfSquares(filled(48, 50))).isEqualTo(25_000);
  }

  // Step 14: 47 is the largest prime length, so of the ramp 1, 2, ..., 47 only the first and last
  //          values count: 1 + 47 * 47 = 2210. The proper-divisor loop of Step 7 answers 1
  @Test
  void n47LargestPrimeLengthKeepsOnlyTheEnds() {
    assertThat(sut.sumOfSquares(ramp(47))).isEqualTo(2210);
  }

  // Step 15: the ramp 1, 2, ..., 50 makes each value equal its 1-based index, so the answer is the
  //          sum of the squared divisors of 50: 1 + 4 + 25 + 100 + 625 + 2500 = 3255. Reading the
  //          element at Java index i instead of i - 1 throws ArrayIndexOutOfBoundsException at
  //          i = 50
  @Test
  void n50RampSumsTheSquaredDivisors() {
    assertThat(sut.sumOfSquares(ramp(50))).isEqualTo(3255);
  }

  // ===========================================================================================
  // Hygiene (Steps 16-17).
  // ===========================================================================================

  // Step 16: the caller's array is left untouched. Squaring each value back into its own slot
  //          before summing is the tempting shortcut that corrupts it
  @Test
  void inputArrayIsNotModified() {
    int[] nums = {2, 7, 1, 19, 18, 3};
    int[] original = nums.clone();

    sut.sumOfSquares(nums);

    assertThat(nums).containsExactly(original);
  }

  // Step 17: one instance answers inputs of different lengths, out of order and largest in the
  //          middle, so no length, divisor list or running sum may be cached on the instance
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.sumOfSquares(new int[] {1, 2, 3, 4})).isEqualTo(21);
    assertThat(sut.sumOfSquares(ramp(50))).isEqualTo(3255);
    assertThat(sut.sumOfSquares(new int[] {7})).isEqualTo(49);
    assertThat(sut.sumOfSquares(new int[] {2, 7, 1, 19, 18, 3})).isEqualTo(63);
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }

  private static int[] ramp(int n) {
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i + 1);
    return nums;
  }
}
