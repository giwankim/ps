package leetcode.p3901_4000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SmallestStableIndexIITest {
  SmallestStableIndexII sut = new SmallestStableIndexII();

  // ===========================================================================================
  // The two windows around an index (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor — one element (1 <= nums.length), at the value ceiling. Both windows are just
  //         that element, so its score is 0 and even k = 0 accepts index 0. A strict comparison, or
  //         a scan that starts at index 1, answers -1, and so does a suffix minimum seeded with 0,
  //         which sees 10^9 - 0
  @Test
  void singleElementAlwaysScoresZero() {
    assertThat(sut.firstStableIndex(new int[] {1_000_000_000}, 0)).isZero();
  }

  // Step 2: the prefix window nums[0..i] includes nums[i]. scores = [3, 3, 3, 3, 1] -> index 4. A
  //         running maximum folded in only after the check sees 4 - 5 at index 2, where the 8 has
  //         not been counted yet, and answers 2. Scoring nums[i] against the suffix minimum without
  //         any earlier element sees 1 - 1 at index 1 and answers 1
  @Test
  void prefixMaximumIncludesTheIndexItself() {
    assertThat(sut.firstStableIndex(new int[] {4, 1, 8, 5, 7}, 2)).isEqualTo(4);
  }

  // Step 3: the mirror of Step 2 — the suffix window nums[i..n-1] includes nums[i] too. scores =
  //         [5, 5, 1, 0] -> index 2. Taking the minimum over nums[i+1..n-1] instead sees 7 - 6 = 1
  //         at index 1 and answers 1, and scoring the prefix maximum against nums[i] alone sees
  //         7 - 7 at index 0 and answers 0
  @Test
  void suffixMinimumIncludesTheIndexItself() {
    assertThat(sut.firstStableIndex(new int[] {7, 2, 6, 9}, 2)).isEqualTo(2);
  }

  // Step 4: stable means less than OR EQUAL to k. Both scores are exactly 3, so index 0 is stable.
  //         A strict comparison answers -1
  @Test
  void scoreEqualToKIsStable() {
    assertThat(sut.firstStableIndex(new int[] {4, 1}, 3)).isZero();
  }

  // ===========================================================================================
  // Which index is reported (Steps 5-9).
  // ===========================================================================================

  // Step 5: no stable index at all — both scores are 8 against k = 7, and the answer is -1, not a
  //         default of 0, n - 1 or n
  @Test
  void returnsMinusOneWhenNoIndexIsStable() {
    assertThat(sut.firstStableIndex(new int[] {8, 0}, 7)).isEqualTo(-1);
  }

  // Step 6: the smallest stable index, not the best one. scores = [4, 4, 3, 3, 4, 4, 0] with k = 3:
  //         index 2 is the first stable index even though index 6 scores a perfect 0. A solution
  //         that returns the index with the minimum score, or the last stable index, answers 6
  @Test
  void smallestStableIndexBeatsABetterScoreLater() {
    assertThat(sut.firstStableIndex(new int[] {7, 3, 6, 4, 9, 5, 9}, 3)).isEqualTo(2);
  }

  // Step 7: index 0 is judged on nums[0] against the global minimum alone. scores =
  //         [1, 48, 48, 0], so index 0 is stable no matter how wild the rest of the array is. A
  //         solution that compares the whole array's spread against k answers -1, and one that
  //         begins scanning at index 1 answers 3
  @Test
  void indexZeroIsStableWhenItsValueIsWithinKOfTheGlobalMinimum() {
    assertThat(sut.firstStableIndex(new int[] {3, 50, 2, 50}, 1)).isZero();
  }

  // Step 8: the last index counts too — it is judged on the global maximum against nums[n-1].
  //         scores = [8, 8, 1], so index 2 is the only stable one. A loop that stops at n - 2
  //         answers -1
  @Test
  void lastIndexCanBeTheOnlyStableOne() {
    assertThat(sut.firstStableIndex(new int[] {9, 1, 8}, 1)).isEqualTo(2);
  }

  // Step 9: scores are not monotonic — the prefix maximum and the suffix minimum both rise with i,
  //         so their difference can go either way. scores = [5, 5, 4, 5, 5, 5, 5, 5, 0] with k = 4:
  //         the answer is 2, but a lower-bound binary search for the first score <= k probes
  //         indices 4, 7 and 8 and answers 8
  @Test
  void scoresRiseAndFallSoTheFirstStableIndexCannotBeBinarySearched() {
    assertThat(sut.firstStableIndex(new int[] {8, 3, 7, 9, 4, 4, 9, 4, 9}, 4)).isEqualTo(2);
  }

  // ===========================================================================================
  // k = 0, zeros and the value ceiling (Steps 10-14).
  // ===========================================================================================

  // Step 10: k = 0 accepts only an index whose element is at least everything before it AND at most
  //          everything after it. scores = [2, 2, 0, 1, 1] -> index 2. Checking the prefix side
  //          alone answers 0 and checking the suffix side alone answers 1
  @Test
  void k0NeedsAnElementThatIsBothPrefixMaximumAndSuffixMinimum() {
    assertThat(sut.firstStableIndex(new int[] {3, 1, 4, 6, 5}, 0)).isEqualTo(2);
  }

  // Step 11: equal values satisfy both sides at once. Every score of a constant array is 0, so
  //          k = 0 accepts index 0. A solution that demands a strict maximum or strict minimum
  //          answers -1
  @Test
  void k0OnAConstantArrayIsIndexZero() {
    assertThat(sut.firstStableIndex(new int[] {5, 5, 5, 5}, 0)).isZero();
  }

  // Step 12: the suffix minimum comes from the array, not from a sentinel. Every value here is
  //          positive, so scores = [0, 1, 1] and index 0 is stable at k = 1. A running minimum
  //          seeded with 0 because "0 <= nums[i]" scores every index against a phantom 0, sees
  //          3, 5 and 5, and answers -1
  @Test
  void suffixMinimumIsNotSeededWithZero() {
    assertThat(sut.firstStableIndex(new int[] {3, 5, 4}, 1)).isZero();
  }

  // Step 13: the mirror of Step 12 — 0 is a legitimate value, and it is the smallest one allowed.
  //          The 0 at index 1 drags both suffix minimums down to 0, so scores = [1, 1] and k = 0
  //          accepts nothing. A minimum table that treats 0 as "no value yet" reads the suffix
  //          minimum at index 0 as 1 and answers 0
  @Test
  void zeroIsARealValueThatLowersTheSuffixMinimum() {
    assertThat(sut.firstStableIndex(new int[] {1, 0}, 0)).isEqualTo(-1);
  }

  // Step 14: the widest spread the constraints allow, 10^9 over 0, against k at its ceiling. Both
  //          scores are exactly 10^9 = k, so index 0 is stable and nothing overflows an int. A
  //          strict comparison answers -1
  @Test
  void kAtItsCeilingAcceptsTheWidestPossibleSpread() {
    assertThat(sut.firstStableIndex(new int[] {1_000_000_000, 0}, 1_000_000_000))
        .isZero();
  }

  // ===========================================================================================
  // The official examples (Steps 15-17).
  // ===========================================================================================

  // Step 15: scores = [5, 5, 4, 1] -> index 3, the last one. The Explanation walks all four indices
  //          to rule out the global-spread reading, which answers -1 because 5 - 0 > 3, and the
  //          exclusive-suffix reading of Step 3, which answers 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.firstStableIndex(new int[] {5, 0, 1, 4}, 3)).isEqualTo(3);
  }

  // Step 16: a descending array scores nums[0] - nums[n-1] = 2 at every index, and k = 1 accepts
  //          none of them
  @Test
  void leetCodeExample2() {
    assertThat(sut.firstStableIndex(new int[] {3, 2, 1}, 1)).isEqualTo(-1);
  }

  // Step 17: the floor with the smallest value and the smallest k — 0 - 0 = 0 <= 0
  @Test
  void leetCodeExample3() {
    assertThat(sut.firstStableIndex(new int[] {0}, 0)).isZero();
  }

  // ===========================================================================================
  // The constraint bounds (Steps 18-22). This is what separates II from I: n climbs from 100 to
  // 10^5, so rescanning both windows for every index — 10^10 operations when the answer is at
  // the end or absent — cannot finish inside the timeout, while one suffix pass and one prefix
  // pass finish in milliseconds. Every score still fits in an int, since 10^9 - 0 does.
  // ===========================================================================================

  // Step 18: maximum length where only the last index is stable — 10^9 first, then a ramp from 0 in
  //          steps of 10^4. The prefix maximum stays 10^9 while the suffix minimum climbs, so the
  //          scores fall from 10^9 to exactly 20,000 at index 99,999. A loop that stops at n - 2
  //          answers -1, the exclusive-suffix reading of Step 3 answers 99,998, and a per-index
  //          rescan does all 10^10 steps before it can answer at all
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWhereOnlyTheLastIndexIsStable() {
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 20_000)).isEqualTo(99_999);
  }

  // Step 19: Step 18 with k one below the last score, so nothing is stable and the answer is -1.
  //          Together with Step 18 this pins the boundary at n - 1 exactly, and it is the other
  //          input on which a quadratic rescan has no early exit
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithKJustBelowTheLastScoreHasNoStableIndex() {
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 19_999)).isEqualTo(-1);
  }

  // Step 20: the same ramp with k = 5 * 10^8 turns stable halfway — the score at index 50,001 is
  //          10^9 - 50,000 * 10^4 = 500,000,000 exactly, and index 50,000 is 10^4 over. The
  //          answer is the first stable index, so a solution that scans from the end and keeps the
  //          last stable index answers 99,999, and the exclusive-suffix reading answers 50,000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthTurnsStableInTheMiddleOfTheRamp() {
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 500_000_000)).isEqualTo(50_001);
  }

  // Step 21: maximum length descending from 10^9 in steps of 10^4 down to 10,000. The prefix
  //          maximum is nums[0] and the suffix minimum is nums[n-1] everywhere, so every score is
  //          999,990,000, and k one below it accepts nothing. The extremes come from the two ends
  //          of the array, and a solution that seeds either extreme from the wrong end answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthDescendingWithKJustBelowTheUniformScoreHasNoStableIndex() {
    assertThat(sut.firstStableIndex(descendingRamp(), 999_989_999)).isEqualTo(-1);
  }

  // Step 22: Step 9 at scale — a stable pocket between two peaks. 6 * 10^8 leads a ramp from 0, so
  //          the first half's scores fall from 600,000,000 to 100,020,000 and dip to k = 2 * 10^8
  //          exactly at index 40,001. Then 10^9 leads a ramp down to 500,010,000, so every index
  //          from 50,000 on scores 499,990,000 again. The answer is 40,001: a lower-bound binary
  //          search over the scores probes index 50,000 first, never looks left of it, and answers
  //          -1, while a solution that keeps the last stable index answers 49,999
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithAStablePocketBetweenTwoPeaks() {
    assertThat(sut.firstStableIndex(stablePocketRamp(), 200_000_000)).isEqualTo(40_001);
  }

  // ===========================================================================================
  // Hygiene (Steps 23-24).
  // ===========================================================================================

  // Step 23: the input is not modified. Sorting nums in place to find a global extreme is a
  //          tempting shortcut that also happens to give the wrong answer, and the caller keeps
  //          the array
  @Test
  void doesNotModifyTheInput() {
    int[] nums = {5, 0, 1, 4};

    sut.firstStableIndex(nums, 3);

    assertThat(nums).containsExactly(5, 0, 1, 4);
  }

  // Step 24: several inputs answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. k = 10^9 at its ceiling accepts index 0 on any array, since
  //          no score can exceed the value range. A solution that keeps a running maximum or a
  //          suffix table on the instance instead of resetting it per call answers the later
  //          inputs from the earlier ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersInputsOfAnyLengthInAnyOrder() {
    assertThat(sut.firstStableIndex(new int[] {9, 1, 8}, 1)).isEqualTo(2);
    assertThat(sut.firstStableIndex(lastIndexOnlyRamp(), 1_000_000_000)).isZero();
    assertThat(sut.firstStableIndex(new int[] {7, 3, 6, 4, 9, 5, 9}, 3)).isEqualTo(2);
    assertThat(sut.firstStableIndex(stablePocketRamp(), 200_000_000)).isEqualTo(40_001);
    assertThat(sut.firstStableIndex(new int[] {8, 0}, 7)).isEqualTo(-1);
    assertThat(sut.firstStableIndex(new int[] {0}, 0)).isZero();
  }

  /** 10^9 then a ramp from 0 in steps of 10^4, so only index 99,999 can be stable. */
  private static int[] lastIndexOnlyRamp() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> i == 0 ? 1_000_000_000 : (i - 1) * 10_000);
    return nums;
  }

  /** Descending from 10^9 to 10,000 in steps of 10^4, so every index scores the same. */
  private static int[] descendingRamp() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> 1_000_000_000 - i * 10_000);
    return nums;
  }

  /**
   * 6 * 10^8 leading a ramp up from 0 for the first 50,000 indices, then 10^9 leading a ramp down
   * to 500,010,000 for the rest, so the scores dip below 2 * 10^8 only inside the first half.
   */
  private static int[] stablePocketRamp() {
    int[] nums = new int[100_000];
    Arrays.setAll(
        nums,
        i -> i == 0
            ? 600_000_000
            : i < 50_000
                ? (i - 1) * 10_000
                : i == 50_000 ? 1_000_000_000 : 1_000_000_000 - (i - 50_000) * 10_000);
    return nums;
  }
}
