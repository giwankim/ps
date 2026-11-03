package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumMovesToEqualArrayElementsIIITest {
  MinimumMovesToEqualArrayElementsIII sut = new MinimumMovesToEqualArrayElementsIII();

  // ===========================================================================================
  // The floor (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= nums.length, 1 <= nums[i]). One element is already equal to
  //         itself. A do-while simulation that raises the minimum before its first equality check
  //         answers 1
  @Test
  void singleElementNeedsNoMoves() {
    assertThat(sut.minMoves(new int[] {1})).isZero();
  }

  // Step 2: the smallest input with any work to do. The 1 climbs to meet the 2 in one move. A
  //         solution that takes nums[0] as the maximum answers -1
  @Test
  void twoElementsRaiseTheSmallerOne() {
    assertThat(sut.minMoves(new int[] {1, 2})).isEqualTo(1);
  }

  // Step 3: the mirror of Step 2. Nothing says the array is sorted, so the larger element can come
  //         first. A solution that takes nums[n - 1] as the maximum answers -1
  @Test
  void largerElementCanComeFirst() {
    assertThat(sut.minMoves(new int[] {2, 1})).isEqualTo(1);
  }

  // Step 4: already equal, with more than one element. The do-while simulation from Step 1 raises
  //         one 5 before it checks, then has to bring the other three up too, and answers 4
  @Test
  void equalElementsNeedNoMoves() {
    assertThat(sut.minMoves(new int[] {5, 5, 5, 5})).isZero();
  }

  // ===========================================================================================
  // The target is the maximum, wherever it sits (Steps 5-7).
  //
  // A move can only increase an element, so no element can come down and the maximum is the lowest
  // value they can all meet at. Allowing moves in both directions makes the median the best target,
  // as in problem 462.
  // ===========================================================================================

  // Step 5: maximum last. 5 + 3 + 0 = 8 moves. Meeting at the median answers 5, because it lowers
  //         the 9 to 6, and max - min also answers 5, because it counts only the widest gap
  @Test
  void maximumInTheLastPosition() {
    assertThat(sut.minMoves(new int[] {4, 6, 9})).isEqualTo(8);
  }

  // Step 6: the same values with the maximum first. Taking nums[n - 1] as the maximum answers -1
  @Test
  void maximumInTheFirstPosition() {
    assertThat(sut.minMoves(new int[] {9, 4, 6})).isEqualTo(8);
  }

  // Step 7: the same values with the maximum at neither end. Taking nums[0] as the maximum answers
  //         -7 and taking nums[n - 1] answers -1, so only a real scan finds the 9
  @Test
  void maximumAwayFromTheEnds() {
    assertThat(sut.minMoves(new int[] {4, 9, 6})).isEqualTo(8);
  }

  // ===========================================================================================
  // Every element pays its own gap (Steps 8-10).
  //
  // A move raises a single element. Problem 453 raises n - 1 of them, which makes its answer
  // sum - min * n. That formula and this problem's answer each count the gaps to a different end of
  // the array, so Steps 8 and 9 separate them from both sides.
  // ===========================================================================================

  // Step 8: three small elements and one large. Each 2 needs its own 8 moves, 24 in total. Problem
  //         453's formula, the median, and max - min all answer 8, and counting the elements below
  //         the maximum answers 3
  @Test
  void repeatedSmallElementsEachPayTheirOwnGap() {
    assertThat(sut.minMoves(new int[] {2, 2, 2, 10})).isEqualTo(24);
  }

  // Step 9: the mirror of Step 8. One small element and three large, and only the 2 moves, so the
  //         answer is 8. Problem 453's formula answers 24, because there the three 10s are what
  //         have to be accounted for
  @Test
  void repeatedMaximumElementsNeverMove() {
    assertThat(sut.minMoves(new int[] {2, 10, 10, 10})).isEqualTo(8);
  }

  // Step 10: duplicates below the maximum, at more than one level. 8 + 8 + 4 + 4 + 0 = 24.
  //          Collapsing duplicates into a set of distinct values answers 12, and so does the
  //          median. Problem 453's formula answers 16 and max - min answers 8
  @Test
  void duplicatesBelowTheMaximumAreNotCollapsed() {
    assertThat(sut.minMoves(new int[] {1, 1, 5, 5, 9})).isEqualTo(24);
  }

  // ===========================================================================================
  // Official examples and a broader shape (Steps 11-13).
  // ===========================================================================================

  // Step 11: LeetCode Example 1. Everything meets at 3, and the explanation raises nums[1] twice,
  //          so one element can take several moves. The median and max - min both answer 2. Problem
  //          453's formula also answers 3 here, so this example alone cannot rule it out
  @Test
  void leetCodeExample1() {
    assertThat(sut.minMoves(new int[] {2, 1, 3})).isEqualTo(3);
  }

  // Step 12: LeetCode Example 2. It rules out what Example 1 could not: problem 453's formula
  //          answers 1. The median and max - min also answer 1, the median by lowering the 5, which
  //          no move can do. Here the two 4s each need their own move
  @Test
  void leetCodeExample2() {
    assertThat(sut.minMoves(new int[] {4, 4, 5})).isEqualTo(2);
  }

  // Step 13: seven elements in no particular order, with a repeated minimum. Everything meets at 9
  //          after 32 moves. It is the broad regression the pointed cases above are not: problem
  //          453's formula answers 24, the median 17, and max - min 8
  @Test
  void mixedSevenElementArray() {
    assertThat(sut.minMoves(new int[] {4, 1, 7, 1, 9, 3, 6})).isEqualTo(32);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= nums.length <= 100, 1 <= nums[i] <= 100).
  //
  // These do not separate complexity classes. The largest possible answer is 99 * 99 = 9,801, so
  // even a simulation that rescans all 100 elements per move makes about 10^6 steps, and nothing
  // comes near overflowing int. What the bounds pin down is the extreme shapes and the top of the
  // value range. The timeouts only catch a loop that never terminates, on a separate thread because
  // JUnit's default SAME_THREAD mode would hang the build instead of reporting it.
  // ===========================================================================================

  // Step 14: the top of the value range, alone. A frequency table declared as new int[100] and
  //          indexed by value throws ArrayIndexOutOfBoundsException on the 100
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void singleElementAtTheTopOfTheRange() {
    assertThat(sut.minMoves(new int[] {100})).isZero();
  }

  // Step 15: Step 4 at maximum length and maximum value. The do-while simulation from Step 1
  //          answers 100
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllEqualAtTheTopOfTheRange() {
    int[] nums = new int[100];
    Arrays.fill(nums, 100);

    assertThat(sut.minMoves(nums)).isZero();
  }

  // Step 16: the largest answer the constraints allow. 99 elements at 1 and a single 100 in the
  //          middle, so 99 elements each climb 99. Problem 453's formula, the median, max - min and
  //          the distinct-value count all answer 99, and taking either end as the maximum answers
  //          -99
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithOneElementAtTheTop() {
    int[] nums = new int[100];
    Arrays.fill(nums, 1);
    nums[50] = 100;

    assertThat(sut.minMoves(nums)).isEqualTo(9_801);
  }

  // Step 17: the mirror of Step 16. 99 elements at 100 and a single 1 in the middle, so only that
  //          one element moves. Problem 453's formula answers 9,801, exactly swapped with Step 16
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithOneElementAtTheBottom() {
    int[] nums = new int[100];
    Arrays.fill(nums, 100);
    nums[50] = 1;

    assertThat(sut.minMoves(nums)).isEqualTo(99);
  }

  // Step 18: every value from 1 to 100 exactly once, scrambled by a deterministic stride of 37. The
  //          100 sits at index 24, nums[0] is 12 and nums[n - 1] is 75. The gaps sum to
  //          0 + 1 + ... + 99 = 4,950. The median answers 2,500, max - min answers 99, taking
  //          nums[0] as the maximum answers -3,850, and taking nums[n - 1] answers 2,450
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthScrambledPermutation() {
    int[] nums = new int[100];
    Arrays.setAll(nums, i -> (i * 37 + 11) % 100 + 1);

    assertThat(sut.minMoves(nums)).isEqualTo(4_950);
  }

  // ===========================================================================================
  // Hygiene (Step 19).
  // ===========================================================================================

  // Step 19: several arrays answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that keeps its running maximum or sum on the
  //          instance instead of resetting it per call answers the later arrays from the earlier
  //          ones' leftovers
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    int[] oneAtTheTop = new int[100];
    Arrays.fill(oneAtTheTop, 1);
    oneAtTheTop[50] = 100;

    assertThat(sut.minMoves(new int[] {2, 1, 3})).isEqualTo(3);
    assertThat(sut.minMoves(new int[] {4, 4, 5})).isEqualTo(2);
    assertThat(sut.minMoves(oneAtTheTop)).isEqualTo(9_801);
    assertThat(sut.minMoves(new int[] {1})).isZero();
    assertThat(sut.minMoves(new int[] {2, 1})).isEqualTo(1);
  }
}
