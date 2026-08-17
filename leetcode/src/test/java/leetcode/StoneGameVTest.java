package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class StoneGameVTest {
  StoneGameV sut = new StoneGameV();

  // Step 1: smallest valid input (1 <= stoneValue.length) and LeetCode Example 3 — the game ends
  //         when one stone remains, so it has already ended and Alice never scores
  @Test
  void singleStoneEndsTheGameBeforeAnyRound() {
    assertThat(sut.stoneGameV(new int[] {4})).isEqualTo(0);
  }

  // Step 2: two stones leave exactly one legal division. Bob throws away the heavier row, so Alice
  //         banks the lighter one. A solution that keeps the maximum row answers 3, and one that
  //         assumes Alice always collects half the pile answers 2
  @Test
  void twoStonesBankTheLighterRow() {
    assertThat(sut.stoneGameV(new int[] {1, 3})).isEqualTo(1);
  }

  // Step 3: the mirror of Step 2 — which row survives is decided by value, not by side
  @Test
  void twoStonesBankTheLighterRowFromEitherSide() {
    assertThat(sut.stoneGameV(new int[] {3, 1})).isEqualTo(1);
  }

  // Step 4: the first tie. The rows are equal so Bob hands Alice the choice, but both leftovers are
  //         single stones worth nothing further, which pins the tie payout at the row value once —
  //         not twice (14) and not zero
  @Test
  void tiedRowsOfOneStoneBankTheRowValueExactlyOnce() {
    assertThat(sut.stoneGameV(new int[] {7, 7})).isEqualTo(7);
  }

  // ===========================================================================================
  // The tie rule (Steps 5-8).
  //
  // "If the value of the two rows are equal, Bob lets Alice decide which row will be thrown away"
  // is the whole difficulty of this problem. The value banked is identical either way, so the
  // choice is purely about which subgame Alice keeps playing.
  // ===========================================================================================

  // Step 5: dividing [2,1,1] into [2] and [1,1] ties at 2, and Alice must keep the right row —
  //         [2] is spent, while [1,1] still yields 1 more. The other division banks only 1.
  //         A solution that resolves ties by keeping the left row answers 2, and so does one that
  //         picks the division maximizing this round's score, or the most evenly balanced one
  @Test
  void tieIsResolvedByKeepingTheRightRowWhenItStillHasPlay() {
    assertThat(sut.stoneGameV(new int[] {2, 1, 1})).isEqualTo(3);
  }

  // Step 6: the mirror of Step 5, where the same tie must be resolved the other way. Steps 5 and 6
  //         are the pair no fixed tie rule satisfies at once — the choice has to be made by
  //         comparing the two subgames. A solution that banks the tied row and abandons both
  //         subgames answers 2 here
  @Test
  void tieIsResolvedByKeepingTheLeftRowWhenItStillHasPlay() {
    assertThat(sut.stoneGameV(new int[] {1, 1, 2})).isEqualTo(3);
  }

  // Step 7: Step 6 with the discarded stone made a million times heavier, and the answer does not
  //         move. A thrown-away row never enters the score, so only the comparison between the two
  //         row sums matters, never the margin
  @Test
  void discardedRowMagnitudeDoesNotChangeTheScore() {
    assertThat(sut.stoneGameV(new int[] {1, 1, 1000000})).isEqualTo(3);
  }

  // Step 8: the same point stated at its extreme — a pile summing to 1,000,002 is worth 1, because
  //         every division puts the heavy stone in the surviving row's opposite number. Any score
  //         derived from the total rather than from the divisions fails here
  @Test
  void oneHeavyStoneCanMakeTheWholePileNearlyWorthless() {
    assertThat(sut.stoneGameV(new int[] {1, 1000000, 1})).isEqualTo(1);
  }

  // ===========================================================================================
  // Choosing the division (Steps 9-11).
  // ===========================================================================================

  // Step 9: the only good division is the interior one. [1,1] against [1,1] ties at 2 and leaves a
  //         row worth 1 more, while both end divisions bank just 1. A solution that only tries
  //         peeling one stone off an end answers 1
  @Test
  void onlyTheInteriorDivisionPaysOnFourEqualStones() {
    assertThat(sut.stoneGameV(new int[] {1, 1, 1, 1})).isEqualTo(3);
  }

  // Step 10: banking less this round buys more later, with no tie anywhere to blame. The division
  //          [2,2,2] against [5] banks 5 and ends the game, but [2,2] against [2,5] banks only 4
  //          and leaves [2,2] alive for 2 more. Both the highest-scoring division and the most
  //          evenly balanced one are that losing choice, so neither greedy rule survives
  @Test
  void bankingLessThisRoundCanBuyMoreLater() {
    assertThat(sut.stoneGameV(new int[] {2, 2, 2, 5})).isEqualTo(6);
    assertThat(sut.stoneGameV(new int[] {3, 3, 3, 8})).isEqualTo(9);
  }

  // Step 11: the tie rule and the division search meeting in one row — [1,1,1,1] against [4] ties
  //          at 4, and Alice banks that 4 and keeps the four light stones precisely because Step 9
  //          showed they are worth 3 more, while the lone heavy stone is worth nothing further
  @Test
  void tieIsResolvedTowardTheRowWithTheRicherSubgame() {
    assertThat(sut.stoneGameV(new int[] {1, 1, 1, 1, 4})).isEqualTo(7);
    assertThat(sut.stoneGameV(new int[] {4, 1, 1, 1, 1})).isEqualTo(7);
  }

  // ===========================================================================================
  // The worked examples (Steps 12-13).
  // ===========================================================================================

  // Step 12: LeetCode Example 1, whose three rounds are spelled out in the statement — [6,2,3]
  //          against [4,5,5] banks 11, then [6] against [2,3] banks 5, then [2] against [3] banks 2
  @Test
  void leetCodeExampleOneScoresEighteenOverThreeRounds() {
    assertThat(sut.stoneGameV(new int[] {6, 2, 3, 4, 5, 5})).isEqualTo(18);
  }

  // Step 13: LeetCode Example 2 — seven equal stones, so every division of an even-length row ties
  //          and the tie rule fires at nearly every level
  @Test
  void leetCodeExampleTwoScoresTwentyEightOnSevenEqualStones() {
    assertThat(sut.stoneGameV(new int[] {7, 7, 7, 7, 7, 7, 7})).isEqualTo(28);
  }

  // ===========================================================================================
  // Structure the small cases cannot fake (Steps 14-18).
  // ===========================================================================================

  // Step 14: n equal stones of value 1, for n = 1 to 12. The answers plateau in pairs — 2 and 3
  //          both give 1, 4 and 5 both give 3, 6 and 7 both give 4 — but the jumps between plateaus
  //          run 1, 2, 1, 3, 1, 2, so no closed form in n reproduces the sequence. A solution that
  //          fits a formula to the short prefix diverges by n = 8
  @Test
  void equalStoneLadderPlateausInPairsWithIrregularJumps() {
    assertThat(sut.stoneGameV(equalStones(1, 1))).isEqualTo(0);
    assertThat(sut.stoneGameV(equalStones(2, 1))).isEqualTo(1);
    assertThat(sut.stoneGameV(equalStones(3, 1))).isEqualTo(1);
    assertThat(sut.stoneGameV(equalStones(4, 1))).isEqualTo(3);
    assertThat(sut.stoneGameV(equalStones(5, 1))).isEqualTo(3);
    assertThat(sut.stoneGameV(equalStones(6, 1))).isEqualTo(4);
    assertThat(sut.stoneGameV(equalStones(7, 1))).isEqualTo(4);
    assertThat(sut.stoneGameV(equalStones(8, 1))).isEqualTo(7);
    assertThat(sut.stoneGameV(equalStones(9, 1))).isEqualTo(7);
    assertThat(sut.stoneGameV(equalStones(10, 1))).isEqualTo(8);
    assertThat(sut.stoneGameV(equalStones(11, 1))).isEqualTo(8);
    assertThat(sut.stoneGameV(equalStones(12, 1))).isEqualTo(10);
  }

  // Step 15: every comparison in the game is between two sums of the same stones, so multiplying
  //          the whole row by a constant picks the same divisions and multiplies the score by that
  //          constant. Step 13's 28 is Step 14's 4 scaled by 7
  @Test
  void scalingEveryStoneScalesTheScoreByTheSameFactor() {
    assertThat(sut.stoneGameV(equalStones(7, 7))).isEqualTo(28);
    assertThat(sut.stoneGameV(equalStones(6, 1000000))).isEqualTo(4000000);
  }

  // Step 16: reversing the row mirrors every division and the tie rule is symmetric, so the score
  //          is unchanged. This is the one rearrangement that is guaranteed safe
  @Test
  void reversingTheRowLeavesTheScoreUnchanged() {
    assertThat(sut.stoneGameV(new int[] {1, 2, 3, 4, 5, 6})).isEqualTo(14);
    assertThat(sut.stoneGameV(new int[] {6, 5, 4, 3, 2, 1})).isEqualTo(14);
    assertThat(sut.stoneGameV(new int[] {5, 2, 2, 2})).isEqualTo(6);
  }

  // Step 17: and no other rearrangement is. These two rows hold the same six stones, yet shuffling
  //          them is worth one more point, because divisions can only cut between adjacent
  //          positions. A solution that sorts its input answers 14 for both
  @Test
  void reorderingTheSameStonesChangesTheScore() {
    assertThat(sut.stoneGameV(new int[] {1, 2, 3, 4, 5, 6})).isEqualTo(14);
    assertThat(sut.stoneGameV(new int[] {3, 1, 6, 2, 5, 4})).isEqualTo(15);
  }

  // Step 18: a twelve-stone row with no exploitable shape — no sorted order, no repeats to lean on,
  //          and ties appearing only deep in the recursion. It is the broad regression the pointed
  //          cases above are not
  @Test
  void mixedTwelveStoneRowScoresFifty() {
    assertThat(sut.stoneGameV(new int[] {9, 3, 7, 1, 8, 2, 6, 4, 10, 5, 11, 2})).isEqualTo(50);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= stoneValue.length <= 500, 1 <= stoneValue[i] <= 10^6).
  //
  // These pin down the property the small rows cannot: each of the ~125,000 subrows has to be
  // settled once and reused, not re-derived every time it is reached. Trying every division of
  // every subrow is about 2.1 * 10^7 (i, j, k) triples, which is milliseconds once subrow sums come
  // from a prefix table — but a recursion that does not memoize revisits those subrows an
  // exponential number of times and never finishes, and one that re-adds each subrow's stones
  // instead of consulting a prefix table pays an extra factor of n on top.
  //
  // The timeouts below are set at roughly a hundred times what the intended solution needs, so they
  // are evidence about complexity class rather than about constant factors.
  //
  // Note that the score stays inside int: the largest possible total is 500 * 10^6 = 5 * 10^8, and
  // Alice's take is strictly less than that because each round banks at most half of what remains.
  // ===========================================================================================

  // Step 19: both constraints at maximum — 500 stones of 10^6. Every division of an even-length
  //          subrow ties, so this is Step 14's ladder scaled by 10^6 and run to the end of the
  //          range: 494 * 10^6
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maximumRowOfEqualStonesScoresFourHundredNinetyFourMillion() {
    assertThat(sut.stoneGameV(equalStones(500, 1000000))).isEqualTo(494000000);
  }

  // Step 20: 500 stones spread across the value range by a deterministic ramp that wraps four
  //          times, so runs of increasing values are broken by sudden drops. Ties are rare here,
  //          which makes the answer depend on the full table of divisions rather than on the tie
  //          rule — the complement of Step 19
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maximumRowOfSpreadValuesDependsOnEveryDivision() {
    int[] arr = new int[500];
    Arrays.setAll(arr, i -> 1 + (i * 7919) % 1000000);

    assertThat(sut.stoneGameV(arr)).isEqualTo(244404494);
  }

  // Step 21: 500 stones in strictly increasing order and the same row reversed, which Step 16 says
  //          must agree. At this length the agreement is not a coincidence a wrong solution can
  //          stumble into — it requires the same table to be built correctly from both ends
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void maximumRowIsWorthTheSameForwardsAndBackwards() {
    int[] increasing = new int[500];
    Arrays.setAll(increasing, i -> i + 1);
    int[] decreasing = new int[500];
    Arrays.setAll(decreasing, i -> 500 - i);

    assertThat(sut.stoneGameV(increasing)).isEqualTo(124094);
    assertThat(sut.stoneGameV(decreasing)).isEqualTo(124094);
  }

  // Step 22: several rows answered by one instance, largest last and lengths deliberately out of
  //          order. A solution that memoizes into a table held on the instance and sized to the
  //          first row it sees answers the later, longer rows from a table that is too small, or
  //          from entries left over from a different row
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void oneInstanceAnswersRowsOfAnyLengthInAnyOrder() {
    assertThat(sut.stoneGameV(new int[] {6, 2, 3, 4, 5, 5})).isEqualTo(18);
    assertThat(sut.stoneGameV(equalStones(500, 1000000))).isEqualTo(494000000);
    assertThat(sut.stoneGameV(new int[] {4})).isEqualTo(0);
    assertThat(sut.stoneGameV(new int[] {2, 1, 1})).isEqualTo(3);
    assertThat(sut.stoneGameV(equalStones(12, 1))).isEqualTo(10);
  }

  // Step 23: the row is the caller's, and building a prefix table in place over it is a tempting
  //          way to avoid a second array. Step 22 would only catch that by accident; this catches
  //          it directly
  @Test
  void inputRowIsNotModified() {
    int[] arr = {6, 2, 3, 4, 5, 5};
    int[] original = arr.clone();

    sut.stoneGameV(arr);

    assertThat(arr).containsExactly(original);
  }

  private static int[] equalStones(int n, int value) {
    int[] arr = new int[n];
    Arrays.fill(arr, value);
    return arr;
  }
}
