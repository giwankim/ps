package leetcode.p0401_0500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumMovesToEqualArrayElementsTest {
  MinimumMovesToEqualArrayElements sut = new MinimumMovesToEqualArrayElements();

  // ===========================================================================================
  // The floor (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= nums.length). One element is already equal to itself, and a
  //         move raises n - 1 = 0 elements, so no move could change anything anyway. A simulation
  //         that makes a move before checking for equality never terminates here
  @Test
  void singleElementNeedsNoMoves() {
    assertThat(sut.minMoves(new int[] {7})).isZero();
  }

  // Step 2: with two elements a move raises exactly one of them, so the smaller one climbs to meet
  //         the larger — [1, 5] -> [2, 5] -> ... -> [5, 5] in 4 moves
  @Test
  void twoElementsRaiseTheSmallerOneEachMove() {
    assertThat(sut.minMoves(new int[] {1, 5})).isEqualTo(4);
  }

  // Step 3: the mirror of Step 2. Nothing says the array is sorted, so the smaller element can come
  //         second. A solution that takes nums[0] as the minimum answers -4
  @Test
  void smallerElementCanComeSecond() {
    assertThat(sut.minMoves(new int[] {5, 1})).isEqualTo(4);
  }

  // Step 4: already equal, with a value above zero. A running minimum seeded with 0 instead of the
  //         first element (or Integer.MAX_VALUE) never drops to 5 and answers 20
  @Test
  void equalElementsNeedNoMoves() {
    assertThat(sut.minMoves(new int[] {5, 5, 5, 5})).isZero();
  }

  // ===========================================================================================
  // Which elements a move raises (Steps 5-8).
  //
  // A move raises every element but one. Two easy misreadings: a move raises a single element,
  // or every element ends up at the original maximum. Example 1 cannot tell the first misreading
  // apart from the real rule (both answer 3), so Steps 5 and 6 exist to do it.
  // ===========================================================================================

  // Step 5: two small elements and one large. Each move skips the 5 and raises both 1s together, so
  //         4 moves reach [5, 5, 5]. Reading a move as raising a single element answers 8 here,
  //         because each 1 would need its own 4 moves
  @Test
  void bothSmallElementsRiseTogetherInOneMove() {
    assertThat(sut.minMoves(new int[] {1, 1, 5})).isEqualTo(4);
  }

  // Step 6: the mirror of Step 5 — one small element and two large. Every move must raise at least
  //         one of the 5s, so the elements cannot meet at 5. They meet at 9: the 1 is raised in all
  //         8 moves and each 5 sits out 4 of them. Reading a move as raising a single element
  //         answers 4, and so does max - min
  @Test
  void elementsMeetAboveTheOriginalMaximum() {
    assertThat(sut.minMoves(new int[] {1, 5, 5})).isEqualTo(8);
  }

  // Step 7: unsorted, with the minimum repeated in the middle and at neither end. Both 2s are
  //         raised in every move, and everything meets at 16 after 14 moves. Taking nums[0] as the
  //         minimum answers -11, taking nums[n - 1] answers -21, and max - min answers 7
  @Test
  void repeatedMinimumAwayFromTheEnds() {
    assertThat(sut.minMoves(new int[] {7, 2, 4, 2, 9})).isEqualTo(14);
  }

  // Step 8: the lower half of the value range (-10^9 <= nums[i]). Only the gaps between elements
  //         matter, so Step 6's [1, 5, 5] shifted down by 8 still takes 8 moves. The elements now
  //         meet at 1, which crosses zero on the way, and nothing about that is special
  @Test
  void negativeValuesBehaveLikeAnyOthers() {
    assertThat(sut.minMoves(new int[] {-7, -3, -3})).isEqualTo(8);
  }

  // ===========================================================================================
  // Official examples and a broader shape (Steps 9-11).
  // ===========================================================================================

  // Step 9: LeetCode Example 1. The explanation's trace [1, 2, 3] -> [2, 3, 3] -> [3, 4, 3] ->
  //         [4, 4, 4] shows the elements meeting at 4, above the original maximum of 3. max - min
  //         answers 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.minMoves(new int[] {1, 2, 3})).isEqualTo(3);
  }

  // Step 10: LeetCode Example 2 — an array that starts out equal needs no moves at all. A running
  //          minimum seeded with 0 answers 3
  @Test
  void leetCodeExample2() {
    assertThat(sut.minMoves(new int[] {1, 1, 1})).isZero();
  }

  // Step 11: seven elements in no particular order, with the minimum repeated. Everything meets at
  //          25 after 24 moves. It is the broad regression the pointed cases above are not:
  //          max - min answers 8, reading a move as raising a single element answers 32, and
  //          taking nums[0] as the minimum answers 3
  @Test
  void mixedSevenElementArray() {
    assertThat(sut.minMoves(new int[] {4, 1, 7, 1, 9, 3, 6})).isEqualTo(24);
  }

  // ===========================================================================================
  // The answer fits in int, the meeting value need not (Step 12).
  // ===========================================================================================

  // Step 12: the statement promises only that the answer fits in 32 bits. Here the answer is
  //          1,500,000,000, but the five 10^9s each sit out 300,000,000 of those moves, so the
  //          elements meet at 2,200,000,000, above Integer.MAX_VALUE. A brute force that applies
  //          moves to the array itself overflows partway. Its comparisons then go wrong: the
  //          version that raises everything but the maximum by max - min loops forever
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void meetingValueCanExceedIntEvenThoughTheAnswerFits() {
    int[] nums = {
      700_000_000, 1_000_000_000, 1_000_000_000, 1_000_000_000, 1_000_000_000, 1_000_000_000
    };

    assertThat(sut.minMoves(nums)).isEqualTo(1_500_000_000);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= nums.length <= 10^5, -10^9 <= nums[i] <= 10^9, and the
  // answer fits in a 32-bit int).
  //
  // What these pin down is the running time. A single pass over the array is O(n) and finishes in
  // milliseconds at n = 10^5. A move-by-move simulation makes up to 2 * 10^9 moves, each of which
  // scans the array. The jump simulation from Step 12 had made over 80,000 O(n) rounds and was
  // still running after 10 seconds on Steps 16 and 17. An O(n^2) solution that rescans for
  // the minimum once per element is 10^10 steps and took about 6 seconds when measured. The
  // 2-second limit sits far above the single pass and well below all of those.
  //
  // These cases do not pin down the type of a running sum. A sum over 10^5 elements reaches 10^14,
  // far past int, but Java int addition, subtraction and multiplication wrap modulo 2^32, so an int
  // accumulator still lands on any final answer that fits.
  //
  // They run on a separate thread because JUnit's default SAME_THREAD mode only compares elapsed
  // time after the method returns: against a solution slow enough to matter, a same-thread timeout
  // hangs the build instead of reporting the failure it exists to report.
  // ===========================================================================================

  // Step 13: both ends of the value range in two elements. The single gap of 2,000,000,000 is the
  //          largest answer two elements can have, and it fits under Integer.MAX_VALUE
  //          (2,147,483,647) with little to spare. A move-by-move simulation has to make every one
  //          of those moves even on an array this short, which took about 6.5 seconds when measured
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void twoElementsSpanTheWholeValueRange() {
    assertThat(sut.minMoves(new int[] {-1_000_000_000, 1_000_000_000})).isEqualTo(2_000_000_000);
  }

  // Step 14: Step 4 at maximum length — 10^5 copies of the top of the value range, so nothing ever
  //          moves. A running minimum seeded with 0 answers 276,447,232 here (10^14 wrapped into
  //          int), and nothing about the answer being 0 excuses an O(n^2) rescan
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllEqualAtTheTopOfTheRange() {
    int[] nums = new int[100_000];
    Arrays.fill(nums, 1_000_000_000);

    assertThat(sut.minMoves(nums)).isZero();
  }

  // Step 15: 10^5 elements at the bottom of the range and one at the top, in the middle of the
  //          array. Every move skips the top element and raises the other 99,999, so the answer is
  //          that one 2 * 10^9 gap. Reading a move as raising a single element answers
  //          199,998,000,000,000, which does not fit in any int
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithOneElementAtTheTop() {
    int[] nums = new int[100_000];
    Arrays.fill(nums, -1_000_000_000);
    nums[50_000] = 1_000_000_000;

    assertThat(sut.minMoves(nums)).isEqualTo(2_000_000_000);
  }

  // Step 16: the mirror of Step 15, and Step 12 at scale — every element at the top of the range
  //          except one, 20,000 lower, in the middle. That one element is raised in every one of
  //          the 1,999,980,000 moves, while each of the other 99,999 sits out 20,000 of them, so
  //          the elements meet at 2,999,960,000, far above Integer.MAX_VALUE. max - min answers
  //          20,000, and so does reading a move as raising a single element
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithOneElementBelowTheRest() {
    int[] nums = new int[100_000];
    Arrays.fill(nums, 1_000_000_000);
    nums[50_000] = 1_000_000_000 - 20_000;

    assertThat(sut.minMoves(nums)).isEqualTo(1_999_980_000);
  }

  // Step 17: 10^5 elements scattered over -20,000..19,999 by a deterministic ramp that wraps every
  //          five or so indices. The minimum of -20,000 occurs only twice, at indices 32,745 and
  //          72,745, nowhere near either end, and nums[0] is -7,655. Taking nums[0] as the minimum
  //          answers 765,370,000, and max - min answers 39,999. Reading a move as raising a single
  //          element is close but still wrong, at 2,000,030,000
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthScrambledRamp() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> (i * 7919 + 12_345) % 40_000 - 20_000);

    assertThat(sut.minMoves(nums)).isEqualTo(1_999_870_000);
  }

  // ===========================================================================================
  // Hygiene (Step 18).
  // ===========================================================================================

  // Step 18: several arrays answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that keeps its running sum or minimum on the
  //          instance instead of resetting it per call answers the later arrays from the earlier
  //          ones' leftovers
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    int[] oneAtTheTop = new int[100_000];
    Arrays.fill(oneAtTheTop, -1_000_000_000);
    oneAtTheTop[50_000] = 1_000_000_000;

    assertThat(sut.minMoves(new int[] {1, 2, 3})).isEqualTo(3);
    assertThat(sut.minMoves(new int[] {-7, -3, -3})).isEqualTo(8);
    assertThat(sut.minMoves(oneAtTheTop)).isEqualTo(2_000_000_000);
    assertThat(sut.minMoves(new int[] {7})).isZero();
    assertThat(sut.minMoves(new int[] {5, 1})).isEqualTo(4);
  }
}
