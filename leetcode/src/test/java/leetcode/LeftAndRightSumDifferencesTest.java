package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class LeftAndRightSumDifferencesTest {
  LeftAndRightSumDifferences sut = new LeftAndRightSumDifferences();

  // Step 1: smallest legal input (n = 1). Nothing sits to the left or right,
  // so leftSum = rightSum = 0 and the only answer is |0 - 0| = 0.
  @Test
  void singleElement() {
    assertThat(sut.leftRightDifference(new int[] {1})).containsExactly(0);
  }

  // Step 2: two elements. Each endpoint sees the *other* element entirely on
  // one side and nothing on the other, so a trivial "return all zeros" no
  // longer passes -- real left/right sums are now required.
  // leftSum = [0, 2], rightSum = [5, 0] -> [|0-5|, |2-0|] = [5, 2].
  @Test
  void twoElements() {
    assertThat(sut.leftRightDifference(new int[] {2, 5})).containsExactly(5, 2);
  }

  // Step 3: all-equal, odd length. The exact middle is balanced
  // (leftSum == rightSum) so it collapses to 0, with symmetric ends.
  // total = 15; leftSum = [0,5,10], rightSum = [10,5,0] -> [10, 0, 10].
  @Test
  void allEqualElementsAreSymmetricAroundAZeroMiddle() {
    assertThat(sut.leftRightDifference(new int[] {5, 5, 5})).containsExactly(10, 0, 10);
  }

  // Step 4: the canonical problem-statement example (LeetCode 2574, Example 1).
  // leftSum = [0,10,14,22], rightSum = [15,11,3,0] -> [15, 1, 11, 22].
  @Test
  void problemStatementExample() {
    assertThat(sut.leftRightDifference(new int[] {10, 4, 8, 3})).containsExactly(15, 1, 11, 22);
  }

  // Step 5: strictly increasing values. rightSum dominates at the front and
  // leftSum at the back, so the differences fall to a minimum then rise again
  // (a "valley") without ever touching an interior zero.
  // total = 10; leftSum = [0,1,3,6], rightSum = [9,7,4,0] -> [9, 6, 1, 6].
  @Test
  void increasingValuesFormAValley() {
    assertThat(sut.leftRightDifference(new int[] {1, 2, 3, 4})).containsExactly(9, 6, 1, 6);
  }

  // Step 6: a non-uniform interior balance point. At index 3 the left side
  // (1+7+3) equals the right side (5+6), giving an interior 0 even though the
  // values differ -- exercises accumulation across a longer array.
  // total = 28 -> [27, 19, 9, 0, 11, 22].
  @Test
  void nonUniformBalancePointYieldsAnInteriorZero() {
    assertThat(sut.leftRightDifference(new int[] {1, 7, 3, 6, 5, 6}))
        .containsExactly(27, 19, 9, 0, 11, 22);
  }

  // Step 7: upper constraint bounds (n = 1000, every value = 100000). The
  // running sums reach 999 * 100000 ~= 1e8, which still fits in a 32-bit int,
  // so this guards correct accumulation at the largest legal input. For a
  // uniform array the closed form answer[i] = v * |2i - (n - 1)| is used here
  // as an oracle independent of the running-sum algorithm under test.
  @Test
  void maxConstraintsUniformArray() {
    int[] nums = new int[1000];
    Arrays.fill(nums, 100_000);
    int[] expected = new int[1000];
    Arrays.setAll(expected, i -> 100_000 * Math.abs(2 * i - 999));
    assertThat(sut.leftRightDifference(nums)).isEqualTo(expected);
  }
}
