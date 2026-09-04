package leetcode.p3901_4000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SmallestStableIndexITest {
  SmallestStableIndexI sut = new SmallestStableIndexI();

  // ===========================================================================================
  // The two windows around an index (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor — one element (1 <= nums.length). Both windows are just that element, so its
  //         score is 0 whatever the value is, and even k = 0 accepts index 0. A solution that reads
  //         the element itself as the score, or that starts scanning at index 1, answers -1
  @Test
  void singleElementAlwaysScoresZero() {
    assertThat(sut.firstStableIndex(new int[] {7}, 0)).isZero();
  }

  // Step 2: the prefix window nums[0..i] includes nums[i]. scores = [5, 5, 6, 6, 3] -> index 4. A
  //         running maximum that is updated only after the check sees 5 - 3 = 2 at index 2, where
  //         the 9 has not been folded in yet, and answers 2
  @Test
  void prefixMaximumIncludesTheIndexItself() {
    assertThat(sut.firstStableIndex(new int[] {5, 0, 9, 3, 6}, 3)).isEqualTo(4);
  }

  // Step 3: the mirror of Step 2 — the suffix window nums[i..n-1] includes nums[i] too. scores =
  //         [5, 5, 1] -> index 2. Taking the minimum over nums[i+1..n-1] instead sees 6 - 5 = 1 at
  //         index 1 and answers 1
  @Test
  void suffixMinimumIncludesTheIndexItself() {
    assertThat(sut.firstStableIndex(new int[] {6, 1, 5}, 1)).isEqualTo(2);
  }

  // Step 4: stable means less than OR EQUAL to k. Both scores are exactly 2, so index 0 is stable.
  //         A strict comparison answers -1
  @Test
  void scoreEqualToKIsStable() {
    assertThat(sut.firstStableIndex(new int[] {3, 1}, 2)).isZero();
  }

  // ===========================================================================================
  // Which index is reported (Steps 5-9).
  // ===========================================================================================

  // Step 5: no stable index at all — both scores are 9 against k = 8, and the answer is -1, not a
  //         default of 0 or n - 1
  @Test
  void returnsMinusOneWhenNoIndexIsStable() {
    assertThat(sut.firstStableIndex(new int[] {9, 0}, 8)).isEqualTo(-1);
  }

  // Step 6: the smallest stable index, not the best one. scores = [5, 5, 3, 6, 6, 0] with k = 3:
  //         index 2 is the first stable index even though index 5 scores a perfect 0. A solution
  //         that returns the index with the minimum score, or the last stable index, answers 5
  @Test
  void smallestStableIndexBeatsABetterScoreLater() {
    assertThat(sut.firstStableIndex(new int[] {6, 1, 5, 9, 3, 9}, 3)).isEqualTo(2);
  }

  // Step 7: index 0 is judged on nums[0] against the global minimum alone. scores =
  //         [1, 99, 99, 0], so index 0 is stable no matter how wild the rest of the array is. A
  //         solution that compares the whole array's spread against k answers -1, and one that
  //         begins scanning at index 1 answers 3
  @Test
  void indexZeroIsStableWhenItsValueIsWithinKOfTheGlobalMinimum() {
    assertThat(sut.firstStableIndex(new int[] {2, 100, 1, 100}, 1)).isZero();
  }

  // Step 8: the last index counts too — it is judged on the global maximum against nums[n-1].
  //         scores = [5, 5, 1], so index 2 is the only stable one. A loop that stops at n - 2
  //         answers -1
  @Test
  void lastIndexCanBeTheOnlyStableOne() {
    assertThat(sut.firstStableIndex(new int[] {5, 0, 4}, 1)).isEqualTo(2);
  }

  // Step 9: scores are not monotonic — the prefix maximum and the suffix minimum both rise with i,
  //         so their difference can go either way. scores = [4, 4, 3, 7, 7, 7, 7, 0] with k = 3:
  //         the answer is 2, but a lower-bound binary search for the first score <= k probes
  //         indices 3, 5 and 6, finds all of them unstable, and answers 7
  @Test
  void scoresRiseAndFallSoTheFirstStableIndexCannotBeBinarySearched() {
    assertThat(sut.firstStableIndex(new int[] {5, 1, 4, 9, 2, 2, 2, 9}, 3)).isEqualTo(2);
  }

  // ===========================================================================================
  // k = 0 (Steps 10-11).
  // ===========================================================================================

  // Step 10: k = 0 accepts only an index whose element is at least everything before it AND at most
  //          everything after it. scores = [1, 1, 0, 1, 1] -> index 2. Checking the prefix side
  //          alone answers 0 and checking the suffix side alone answers 1
  @Test
  void k0NeedsAnElementThatIsBothPrefixMaximumAndSuffixMinimum() {
    assertThat(sut.firstStableIndex(new int[] {2, 1, 3, 5, 4}, 0)).isEqualTo(2);
  }

  // Step 11: equal values satisfy both sides at once. Every score of a constant array is 0, so
  //          k = 0 accepts index 0. A solution that demands a strict maximum or strict minimum
  //          answers -1
  @Test
  void k0OnAConstantArrayIsIndexZero() {
    assertThat(sut.firstStableIndex(new int[] {7, 7, 7}, 0)).isZero();
  }

  // ===========================================================================================
  // The official examples (Steps 12-14).
  // ===========================================================================================

  // Step 12: scores = [5, 5, 4, 1] -> index 3, the last one. The Explanation walks all four indices
  //          to rule out the global-spread reading, which answers -1 because 5 - 0 > 3, and the
  //          exclusive-suffix reading of Step 3, which answers 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.firstStableIndex(new int[] {5, 0, 1, 4}, 3)).isEqualTo(3);
  }

  // Step 13: a descending array scores nums[0] - nums[n-1] = 2 at every index, and k = 1 accepts
  //          none of them
  @Test
  void leetCodeExample2() {
    assertThat(sut.firstStableIndex(new int[] {3, 2, 1}, 1)).isEqualTo(-1);
  }

  // Step 14: the floor with the smallest value and the smallest k — 0 - 0 = 0 <= 0
  @Test
  void leetCodeExample3() {
    assertThat(sut.firstStableIndex(new int[] {0}, 0)).isZero();
  }

  // ===========================================================================================
  // The constraint bounds (Steps 15-17). With n <= 100 no complexity class is ruled out — even a
  // rescan of both windows per index is a few thousand operations — and 10^9 - 0 fits in an int,
  // so these steps pin correctness at the edges rather than speed: sentinels sized to the value
  // range and the last index at maximum length. The timeouts only guard against a loop that never
  // advances.
  // ===========================================================================================

  // Step 15: maximum length, descending from 10^9 in steps of 10^7. Every score is 990,000,000, and
  //          k equal to it accepts index 0. A suffix minimum seeded with 100 because "n <= 100"
  //          answers -1, as does the strict comparison of Step 4 at scale
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAtTheValueCeilingWithKEqualToEveryScore() {
    assertThat(sut.firstStableIndex(descendingRamp(), 990_000_000)).isZero();
  }

  // Step 16: maximum length where only the last index is stable — 10^9 first, then a ramp from 0 in
  //          steps of 10^7. The prefix maximum stays 10^9 while the suffix minimum climbs, so the
  //          scores fall from 10^9 to exactly 20,000,000 at index 99. A loop that stops at n - 2
  //          answers -1, and the exclusive-suffix reading of Step 3 answers 98
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWhereOnlyTheLastIndexIsStable() {
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 20_000_000)).isEqualTo(99);
  }

  // Step 17: Step 16 with k one below the last score, so nothing is stable and the answer is -1.
  //          Together with Step 16 this pins the boundary at n - 1 exactly
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithKJustBelowTheLastScoreHasNoStableIndex() {
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 19_999_999)).isEqualTo(-1);
  }

  // ===========================================================================================
  // Hygiene (Steps 18-19).
  // ===========================================================================================

  // Step 18: the input is not modified. Sorting nums in place to find a global extreme is a
  //          tempting shortcut that also happens to give the wrong answer, and the caller keeps
  //          the array
  @Test
  void doesNotModifyTheInput() {
    int[] nums = {5, 0, 1, 4};

    sut.firstStableIndex(nums, 3);

    assertThat(nums).containsExactly(5, 0, 1, 4);
  }

  // Step 19: several inputs answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. k = 10^9 at its ceiling accepts index 0 on any array, since
  //          no score can exceed the value range. A solution that keeps a running maximum or a
  //          suffix table on the instance instead of resetting it per call answers the later
  //          inputs from the earlier ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersInputsOfAnyLengthInAnyOrder() {
    assertThat(sut.firstStableIndex(new int[] {5, 0, 4}, 1)).isEqualTo(2);
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 1_000_000_000)).isZero();
    assertThat(sut.firstStableIndex(new int[] {6, 1, 5, 9, 3, 9}, 3)).isEqualTo(2);
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 20_000_000)).isEqualTo(99);
    assertThat(sut.firstStableIndex(new int[] {9, 0}, 8)).isEqualTo(-1);
    assertThat(sut.firstStableIndex(new int[] {0}, 0)).isZero();
  }

  /** 100 values descending from 10^9 to 10^7 in steps of 10^7, so every index scores the same. */
  private static int[] descendingRamp() {
    int[] nums = new int[100];
    Arrays.setAll(nums, i -> 1_000_000_000 - i * 10_000_000);
    return nums;
  }

  /** 10^9 then 99 values climbing from 0 in steps of 10^7, so only index 99 can be stable. */
  private static int[] lastIndexOnlyRamp() {
    int[] nums = new int[100];
    Arrays.setAll(nums, i -> i == 0 ? 1_000_000_000 : (i - 1) * 10_000_000);
    return nums;
  }
}
