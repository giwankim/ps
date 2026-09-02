package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ConstructUniformParityArrayITest {
  ConstructUniformParityArrayI sut = new ConstructUniformParityArrayI();

  // ===========================================================================================
  // The floor, n == 1 (Steps 1-2). Keeping nums1[i] is always one of the two choices, so a
  // single element is uniform by itself and needs no partner index j.
  // ===========================================================================================

  // Step 1: the smallest legal input (1 <= n) holds one odd value. There is no j != i, so the
  //         subtraction rule is unavailable and nums2 = [1] is the only construction, already
  //         uniform. A solution that demands a partner index before it can answer, or that only
  //         ever tries to reach all-even (a lone odd has no odd partner to cancel against),
  //         answers false
  @Test
  void n1OddKeepsItself() {
    assertThat(sut.uniformArray(new int[] {1})).isTrue();
  }

  // Step 2: the mirror of Step 1, one even value. nums2 = [2] is uniform. A solution that only
  //         ever tries to reach all-odd (no odd value exists to subtract) answers false
  @Test
  void n1EvenKeepsItself() {
    assertThat(sut.uniformArray(new int[] {2})).isTrue();
  }

  // ===========================================================================================
  // One rule per step, on pairs (Steps 3-4).
  // ===========================================================================================

  // Step 3: mixed parity, the odd first. The odd index may keep 1 (or take 1 - 2 = -1), and the
  //         even index must take 2 - 1 = 1, so nums2 = [1, 1] is all odd. All-even is out of
  //         reach: 1 is the only odd, and odd minus even stays odd. A solution that only reports
  //         whether nums1 is already uniform answers false, as does one that only tries all-even,
  //         and one that only lets index i borrow from a later index j > i (index 1 has no later
  //         partner and is stuck at 2)
  @Test
  void evenTurnsOddBySubtractingTheOdd() {
    assertThat(sut.uniformArray(new int[] {1, 2})).isTrue();
  }

  // Step 4: the odd is larger than the even, so the only route to a uniform array passes through
  //         a negative value: 4 - 7 = -3 is forced at index 0, giving nums2 = [-3, 7] or [-3, 3].
  //         -3 is odd. A solution that tests oddness with x % 2 == 1 (Java gives -3 % 2 == -1),
  //         or that discards negative candidates because nums1 is positive, answers false
  @Test
  void forcedDifferenceIsNegativeAndStillOdd() {
    assertThat(sut.uniformArray(new int[] {4, 7})).isTrue();
  }

  // ===========================================================================================
  // A lone element of one parity among the other (Steps 5-7). The same j may be reused by every
  // i, and j may sit on either side of i.
  // ===========================================================================================

  // Step 5: four evens and the single odd 5 at the end. All-even is impossible (5 has no odd
  //         partner), so all four evens must each subtract the same index 4, giving
  //         nums2 = [-3, -1, 1, 3, 5]. A reading where each j may serve at most one i answers
  //         false (four evens compete for one partner), and so does one that only lets i borrow
  //         from an earlier index j < i (no even has an odd before it)
  @Test
  void loneOddAtTheEndServesEveryEven() {
    assertThat(sut.uniformArray(new int[] {2, 4, 6, 8, 5})).isTrue();
  }

  // Step 6: the mirror of Step 5, the lone odd moved to the front. A solution that only lets i
  //         borrow from a later index j > i answers false here (no even has an odd after it)
  //         while passing Step 5
  @Test
  void loneOddAtTheFrontServesEveryEven() {
    assertThat(sut.uniformArray(new int[] {5, 2, 4, 6, 8})).isTrue();
  }

  // Step 7: the lone element is even. Both parities are reachable: 8 minus any odd is odd, and
  //         the odds can also cancel pairwise (1 - 3 = -2, 3 - 1 = 2, and so on) while 8 keeps
  //         itself. A solution that mirrors the lone-odd worry onto a lone even ("one even has
  //         no even partner") answers false, as does the j > i reading (8 is last and has no
  //         partner at all)
  @Test
  void loneEvenAmongOddsTurnsOdd() {
    assertThat(sut.uniformArray(new int[] {1, 3, 5, 7, 8})).isTrue();
  }

  // ===========================================================================================
  // The official examples (Steps 8-9).
  // ===========================================================================================

  // Step 8: LeetCode Example 1. The printed construction is nums2 = [-1, 3]: 2 - 3 = -1 is
  //         forced, and the explanation calls -1 odd. This is Step 4 in the statement's own
  //         words: a negative difference is a legal, odd element of nums2
  @Test
  void leetCodeExample1() {
    assertThat(sut.uniformArray(new int[] {2, 3})).isTrue();
  }

  // Step 9: LeetCode Example 2. Both values are even, so nums2 = nums1 with no subtraction at
  //         all. A solution that always tries to reach all-odd answers false: even minus even
  //         is even, so no odd value can be manufactured here
  @Test
  void leetCodeExample2() {
    assertThat(sut.uniformArray(new int[] {4, 6})).isTrue();
  }

  // ===========================================================================================
  // The constraint bounds (Steps 10-14). Distinct values in 1..100 cap a single-parity array at
  // 50 elements and force n == 100 to be a permutation of 1..100. An O(n^2) per-index scan is
  // instant at this size. Enumerating the n^n candidate arrays, or even the 2^n keep-or-subtract
  // choices, before checking parity cannot finish.
  // ===========================================================================================

  // Step 10: the largest all-even input, every even value in range. Step 9 at scale: nothing
  //          needs subtracting, and no odd value can be manufactured
  @Test
  void fiftyEvensAreAlreadyUniform() {
    assertThat(sut.uniformArray(evens())).isTrue();
  }

  // Step 11: every even value plus the lone odd 99 at the end (n == 51). Step 5 at scale:
  //          all-even is impossible, all fifty evens share index 50 as their partner, and 49 of
  //          the forced differences (2 - 99 through 98 - 99) are negative
  @Test
  void loneOddAmongFiftyEvens() {
    int[] nums1 = Arrays.copyOf(evens(), 51);
    nums1[50] = 99;

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 12: the mirror of Step 11, every odd value plus the lone even 100 at the end. Step 7 at
  //          scale: the j > i reading has no partner for index 50 and answers false
  @Test
  void loneEvenAmongFiftyOdds() {
    int[] nums1 = new int[51];
    Arrays.setAll(nums1, i -> i < 50 ? 2 * i + 1 : 100);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 13: maximum n, the values 1..100 in order. Fifty of each parity, so both targets are
  //          reachable. The j > i reading answers false because 100 sits last
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAscending() {
    int[] nums1 = new int[100];
    Arrays.setAll(nums1, i -> i + 1);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 14: maximum n arranged as all fifty evens followed by all fifty odds. The mirror of
  //          Step 13 for the j < i reading, which answers false: no even has an odd before it,
  //          and the first odd has no odd before it either
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthEvensThenOdds() {
    int[] nums1 = new int[100];
    Arrays.setAll(nums1, i -> i < 50 ? 2 * i + 2 : 2 * (i - 50) + 1);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // ===========================================================================================
  // Hygiene (Steps 15-16).
  // ===========================================================================================

  // Step 15: the array is the caller's. Sorting it to group the parities, or overwriting entries
  //          with their chosen differences to build nums2 in place, is a tempting shortcut
  @Test
  void inputArrayIsNotModified() {
    int[] nums1 = {2, 4, 6, 8, 5};
    int[] original = nums1.clone();

    sut.uniformArray(nums1);

    assertThat(nums1).containsExactly(original);
  }

  // Step 16: several arrays answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. Every answer is true, so a cached verdict would slip
  //          through, but a scratch buffer or parity table sized by the first call and kept on
  //          the instance overflows on the 100-element array that follows it
  @Test
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    int[] full = new int[100];
    Arrays.setAll(full, i -> i + 1);

    assertThat(sut.uniformArray(new int[] {2, 3})).isTrue();
    assertThat(sut.uniformArray(full)).isTrue();
    assertThat(sut.uniformArray(new int[] {1})).isTrue();
    assertThat(sut.uniformArray(new int[] {4, 6})).isTrue();
    assertThat(sut.uniformArray(new int[] {5, 2, 4, 6, 8})).isTrue();
  }

  private static int[] evens() {
    int[] nums1 = new int[50];
    Arrays.setAll(nums1, i -> 2 * i + 2);
    return nums1;
  }
}
