package leetcode.p3101_3200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class KthSmallestAmountWithSingleDenominationCombinationTest {
  KthSmallestAmountWithSingleDenominationCombination sut =
      new KthSmallestAmountWithSingleDenominationCombination();

  // ===========================================================================================
  // One denomination (Steps 1-5).
  //
  // With a single coin the problem degenerates to "the kth multiple", which pins the indexing
  // convention before any of the union machinery can hide an off-by-one.
  // ===========================================================================================

  // Step 1: the floor the constraints permit — one coin and the first amount. Both bounds are
  //         inclusive, so this is the smallest legal call and there is no empty-coins case to spec
  @Test
  void singleCoinOfOneAtKOneIsOne() {
    assertThat(sut.findKthSmallest(new int[] {1}, 1)).isEqualTo(1);
  }

  // Step 2: k counts amounts, and the first amount a coin makes is the coin itself, never zero.
  //         A solution that admits the empty pile as an amount answers 0 here
  @Test
  void kOneOnASingleCoinIsTheCoinItself() {
    assertThat(sut.findKthSmallest(new int[] {25}, 1)).isEqualTo(25);
  }

  // Step 3: coin 5 produces 5, 10, 15, 20, so the 4th amount is 20. k is one-based — a solution
  //         that treats it as a zero-based index answers 15, and one that starts the stream at 0
  //         answers 15 as well
  @Test
  void singleCoinReturnsTheKthMultiple() {
    assertThat(sut.findKthSmallest(new int[] {5}, 4)).isEqualTo(20);
  }

  // Step 4: coin 1 produces every positive integer, so the answer collapses to k itself. This is
  //         the one shape where the answer is not driven by any coin larger than 1
  @Test
  void coinOneMakesEveryPositiveIntegerSoTheAnswerIsK() {
    assertThat(sut.findKthSmallest(new int[] {1}, 10)).isEqualTo(10);
  }

  // Step 5: the first amount overall is the smallest denomination, not the first one listed. The
  //         input is not sorted, so a solution that reaches for coins[0] answers 7
  @Test
  void kOneIsTheSmallestDenominationNotTheFirstListed() {
    assertThat(sut.findKthSmallest(new int[] {7, 3, 5}, 1)).isEqualTo(3);
  }

  // ===========================================================================================
  // Denominations are never combined (Steps 6-10).
  //
  // "You are not allowed to combine coins of different denominations" is the sentence that makes
  // this a union-of-multiples problem rather than an unbounded coin change. The amounts are
  // exactly the union of each coin's own multiples, each amount counted once however many coins
  // can make it.
  // ===========================================================================================

  // Step 6: coins 2 and 5 give 2, 4, 5, 6, 8, 10 ... — 7 is absent because it needs one of each.
  //         A solution that reads this as unbounded coin change admits 7 and answers 7
  @Test
  void amountsNeedingTwoDenominationsAreNotProduced() {
    assertThat(sut.findKthSmallest(new int[] {2, 5}, 5)).isEqualTo(8);
  }

  // Step 7: the mirror of Step 6 with the same coins listed the other way around. Nothing about
  //         the answer depends on the input order, so a solution that leans on coins[0] as the
  //         smallest, or that merges the streams in the order given, is caught by the pair
  @Test
  void theAnswerDoesNotDependOnTheOrderTheCoinsAreListedIn() {
    assertThat(sut.findKthSmallest(new int[] {5, 2}, 5)).isEqualTo(8);
  }

  // Step 8: coins 3 and 5 interleave — 3, 5, 6, 9, 10, 12 — so consecutive answers alternate
  //         between the two streams. A solution that exhausts one denomination before the other
  //         answers 12
  @Test
  void twoCoprimeCoinsInterleaveTheirMultiples() {
    assertThat(sut.findKthSmallest(new int[] {3, 5}, 4)).isEqualTo(9);
  }

  // Step 9: 4 is a multiple of 2, so both coins make it, and it is still one amount. The union is
  //         just the multiples of 2 — 2, 4, 6 — so the 3rd is 6. A merge of the two streams that
  //         keeps both copies of every shared multiple answers 4
  @Test
  void anAmountTwoCoinsCanBothMakeIsCountedOnce() {
    assertThat(sut.findKthSmallest(new int[] {2, 4}, 3)).isEqualTo(6);
  }

  // Step 10: a chain in which every coin is a multiple of the smallest contributes nothing beyond
  //          it, so the union is the multiples of 2 and the 5th is 10. A merge that keeps
  //          duplicates answers 8, since 4 and 8 each arrive more than once
  @Test
  void coinsThatAreMultiplesOfASmallerCoinAddNoNewAmounts() {
    assertThat(sut.findKthSmallest(new int[] {2, 4, 8}, 5)).isEqualTo(10);
  }

  // ===========================================================================================
  // Counting the union (Steps 11-14).
  //
  // Deciding whether an amount x is the answer means counting how many amounts are at most x,
  // which is an inclusion-exclusion over subsets of the coins: each subset contributes plus or
  // minus x divided by the subset's least common multiple, by the parity of its size. Truncating
  // that sum at any depth undercounts or overcounts, and each step below fires at the exact
  // amount where the omitted term first matters.
  // ===========================================================================================

  // Step 11: coins 2 and 3 overlap at every multiple of 6. Up to 8 there are four multiples of 2
  //          and two of 3, but 6 is in both, so the union holds 2, 3, 4, 6, 8 and the 5th is 8.
  //          A count of x/2 + x/3 with no correction reaches 5 already at 6 and answers 6
  @Test
  void thePairwiseOverlapOfTwoCoinsIsSubtractedOnce() {
    assertThat(sut.findKthSmallest(new int[] {2, 3}, 5)).isEqualTo(8);
  }

  // Step 12: three pairwise coprime coins, at the first amount all three share. Up to 30 the
  //          pairwise terms remove the multiples of 6, 10 and 15, which strips 30 away three
  //          times after adding it three times, so it has to be added back once. Inclusion-
  //          exclusion truncated after the pairwise terms undercounts by one from 30 onward and
  //          answers 32
  @Test
  void theTripleOverlapOfThreeCoinsIsAddedBack() {
    assertThat(sut.findKthSmallest(new int[] {2, 3, 5}, 22)).isEqualTo(30);
  }

  // Step 13: the same third-order correction where the coins are not coprime, so the subset least
  //          common multiples are 12, 36, 18 and 36 rather than plain products. A solution that
  //          multiplies a subset out instead of folding a least common multiple through it
  //          divides by 216 where it should divide by 36, and truncating after the pairwise terms
  //          answers 40
  @Test
  void subsetOverlapsUseTheLeastCommonMultipleNotTheProduct() {
    assertThat(sut.findKthSmallest(new int[] {4, 6, 9}, 14)).isEqualTo(36);
  }

  // Step 14: coin 1 alongside larger coins swallows every other stream, so the union is again all
  //          positive integers and the answer is k. Every subset that contains 1 has the same
  //          least common multiple as the subset without it, which is where a sign error in the
  //          parity handling shows up
  @Test
  void coinOneAlongsideOthersStillMakesTheAnswerK() {
    assertThat(sut.findKthSmallest(new int[] {1, 7, 9}, 10)).isEqualTo(10);
  }

  // ===========================================================================================
  // The official examples (Steps 15-16).
  // ===========================================================================================

  // Step 15: LeetCode Example 1. Every coin is a multiple of 3, so the union is the multiples of
  //          3 and the 3rd is 9 — the Explanation spells the three streams out precisely because
  //          6 and 9 look like they should be counted more than once. A count of x/3 + x/6 + x/9
  //          with no correction answers 6
  @Test
  void leetCodeExample1() {
    assertThat(sut.findKthSmallest(new int[] {3, 6, 9}, 3)).isEqualTo(9);
  }

  // Step 16: LeetCode Example 2. The Explanation lists the combined amounts as 2, 4, 5, 6, 8, 10,
  //          12 — 7, 9 and 11 never appear, which is the example's way of ruling out the coin
  //          change reading. A solution that sums denominations answers 9, and the coins arrive
  //          largest first so it also re-checks Step 7
  @Test
  void leetCodeExample2() {
    assertThat(sut.findKthSmallest(new int[] {5, 2}, 7)).isEqualTo(12);
  }

  // ===========================================================================================
  // The constraint bounds (Steps 17-21).
  //
  // k reaches 2*10^9 and coins[i] reaches 25, so the answer reaches 5*10^10 — past int by more
  // than an order of magnitude, and far past anything that can be enumerated. Binary searching
  // the answer over that range costs about 36 iterations, each folding a least common multiple
  // through at most 2^15 - 1 subsets, which is roughly a million operations in total. The
  // timeouts separate that from any solution that walks the amounts one at a time, materializes
  // the union, or drives a heap k times.
  // ===========================================================================================

  // Step 17: the largest answer the constraints allow — the biggest coin at the biggest k. A
  //          solution that computes the search bound, the midpoint, or the answer in an int
  //          wraps 50000000000 to -1539607552
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largestCoinAtLargestKOverflowsInt() {
    assertThat(sut.findKthSmallest(new int[] {25}, 2_000_000_000)).isEqualTo(50_000_000_000L);
  }

  // Step 18: two coins that share only multiples of 600, so the union is dense and the answer is
  //          still well past int. This one also pins the search bound: the smallest coin times k
  //          is 48000000000, and a bound taken from Integer.MAX_VALUE stops far below the answer
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void twoLargeCoinsAtLargestKStayPastIntRange() {
    assertThat(sut.findKthSmallest(new int[] {24, 25}, 2_000_000_000)).isEqualTo(25_000_000_000L);
  }

  // Step 19: the maximum number of denominations, 15, at the maximum k. Every count is a sum over
  //          32767 subsets, so a solution that rebuilds the subset lattice from scratch inside
  //          the loop still finishes, but one that enumerates amounts does not
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void fifteenDenominationsAtLargestK() {
    assertThat(sut.findKthSmallest(denominationsFrom(11, 25), 2_000_000_000))
        .isEqualTo(3_980_739_261L);
  }

  // Step 20: nine coins chosen so that four of their subsets have a least common multiple past
  //          int range — the whole set folds to 26771144400. Those subsets sit above the answer
  //          and should contribute nothing, but an int accumulator wraps them negative, the
  //          usual "stop once the least common multiple exceeds x" guard never fires, and the
  //          resulting negative quotients answer 3927456572
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void subsetLeastCommonMultiplesThatExceedIntRange() {
    int[] coins = {16, 9, 25, 7, 11, 13, 17, 19, 23};

    assertThat(sut.findKthSmallest(coins, 2_000_000_000)).isEqualTo(3_927_456_609L);
  }

  // Step 21: coin 1 at the maximum k, where the union saturates and the answer is exactly k. The
  //          search bound is the smallest coin times k, which is k itself here, so a solution
  //          that starts the search one short of that bound never reaches the answer
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void coinOneAtLargestKAnswersKExactly() {
    int[] coins = {1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};

    assertThat(sut.findKthSmallest(coins, 2_000_000_000)).isEqualTo(2_000_000_000L);
  }

  // ===========================================================================================
  // Hygiene (Steps 22-23).
  // ===========================================================================================

  // Step 22: several inputs answered by one instance, deliberately out of order with the largest
  //          in the middle. A solution that caches the subset lattice, the sorted coins, or the
  //          search bound on the instance rather than rebuilding it per call answers the later,
  //          smaller inputs from state left over from the 15-coin call
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersInputsOfAnySizeInAnyOrder() {
    assertThat(sut.findKthSmallest(new int[] {2, 3}, 5)).isEqualTo(8);
    assertThat(sut.findKthSmallest(denominationsFrom(11, 25), 2_000_000_000))
        .isEqualTo(3_980_739_261L);
    assertThat(sut.findKthSmallest(new int[] {5}, 4)).isEqualTo(20);
    assertThat(sut.findKthSmallest(new int[] {2, 3, 5}, 22)).isEqualTo(30);
  }

  // Step 23: the coins array belongs to the caller. Sorting it to find the smallest denomination,
  //          or compacting away the coins that are multiples of another, are both tempting ways
  //          to avoid a copy. Step 22 would only catch that by accident
  @Test
  void inputCoinsAreNotModified() {
    int[] coins = {5, 2};
    int[] original = coins.clone();

    sut.findKthSmallest(coins, 7);

    assertThat(coins).containsExactly(original);
  }

  private static int[] denominationsFrom(int lo, int hi) {
    int[] coins = new int[hi - lo + 1];
    Arrays.setAll(coins, i -> lo + i);
    return coins;
  }
}
