package leetcode.p0801_0900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StoneGameTest {
  StoneGame sut = new StoneGame();

  // Step 1: smallest valid input (2 <= piles.length) — Alice claims the larger end, whichever
  //         side it sits on, and Bob is left with the other
  @Test
  void twoPilesLetAliceClaimTheLargerEnd() {
    assertThat(sut.stoneGame(new int[] {1, 2})).isTrue();
    assertThat(sut.stoneGame(new int[] {2, 1})).isTrue();
  }

  // Step 2: both value bounds at once (1 <= piles[i] <= 500) — 500 to 1 either way round
  @Test
  void twoPilesAtTheValueBoundsStillFavorAlice() {
    assertThat(sut.stoneGame(new int[] {1, 500})).isTrue();
    assertThat(sut.stoneGame(new int[] {500, 1})).isTrue();
  }

  // Step 3: LeetCode Example 1 — Alice opens with either 5; both branches leave her ahead,
  //         finishing 9 to 8
  @Test
  void leetCodeExample1AliceWinsFromEitherFive() {
    assertThat(sut.stoneGame(new int[] {5, 3, 4, 5})).isTrue();
  }

  // Step 4: LeetCode Example 2 — the two ends are both 3 but they are not interchangeable.
  //         Taking the first 3 exposes the 7 and loses 6 to 9; taking the last 3 wins 10 to 5,
  //         so a solution that treats equal ends as equivalent fails here
  @Test
  void leetCodeExample2AliceMustTakeTheLastPile() {
    assertThat(sut.stoneGame(new int[] {3, 7, 2, 3})).isTrue();
  }

  // Step 5: the game is symmetric under reversal — both ends are always available, so mirroring
  //         the row cannot change the outcome
  @Test
  void reversedRowProducesTheSameResult() {
    assertThat(sut.stoneGame(new int[] {3, 2, 7, 3})).isTrue();
  }

  // Step 6: the parity strategy — Alice commits to the odd indices and collects 10 and 11,
  //         even though the only pile she can see on turn one is a 1
  @Test
  void aliceHoldsTheRicherParityRatherThanTheRicherEnd() {
    assertThat(sut.stoneGame(new int[] {1, 10, 1, 11})).isTrue();
  }

  // Step 7: the same idea stretched to the value bound — Alice takes 1000 of the 1003 stones
  //         while every end she is ever offered starts out small
  @Test
  void parityStrategyScalesToTheLargestPiles() {
    assertThat(sut.stoneGame(new int[] {1, 500, 2, 500})).isTrue();
  }

  // Step 8: six piles decided by a single stone — Alice finishes 7 to 6, so an off-by-one in
  //         the score comparison shows up here
  @Test
  void sixPilesWonByASingleStone() {
    assertThat(sut.stoneGame(new int[] {2, 2, 2, 2, 2, 3})).isTrue();
  }

  // Step 9: the largest pile sits in the middle, unreachable on turn one — Alice must still
  //         steer the row so that the 100 falls to her, winning 104 to 3
  @Test
  void aliceReachesTheLargestPileBuriedInTheMiddle() {
    assertThat(sut.stoneGame(new int[] {1, 1, 100, 1, 1, 3})).isTrue();
  }

  // Step 10: maximum length (piles.length <= 500) with the smallest legal values — Alice takes
  //          the lone 2 first and then splits the 499 remaining ones, winning 251 to 250
  @Test
  void maximumLengthOfMinimalPilesIsWonByOneStone() {
    int[] piles = new int[500];
    Arrays.fill(piles, 1);
    piles[0] = 2;
    assertThat(sut.stoneGame(piles)).isTrue();
  }

  // Step 11: both upper bounds together (length 500, piles of 500) with one pile shaved to 499
  //          to keep the total odd — the margin is again a single stone, 125000 to 124999,
  //          and the running scores reach 10^5 so an O(n^2) table of 250k states must hold up
  @Test
  void maximumLengthAtMaximumValueIsWonByOneStone() {
    int[] piles = new int[500];
    Arrays.fill(piles, 500);
    piles[0] = 499;
    assertThat(sut.stoneGame(piles)).isTrue();
  }

  // Step 12: maximum length with the big piles alternating — Alice holds that parity for all
  //          250 of her turns and wins 125000 to 251
  @Test
  void maximumLengthZigzagGivesAliceEveryLargePile() {
    int[] piles = new int[500];
    Arrays.fill(piles, 1);
    for (int i = 1; i < piles.length; i += 2) {
      piles[i] = 500;
    }
    piles[0] = 2;
    assertThat(sut.stoneGame(piles)).isTrue();
  }

  // ===========================================================================================
  // Beyond the stated constraints.
  //
  // LeetCode guarantees piles.length is even and sum(piles) is odd. Together those make Alice
  // the guaranteed winner: she can pre-commit to taking every even-indexed or every odd-indexed
  // pile, and an odd total rules out a tie between the two. So every case above answers true,
  // and a bare `return true;` passes all of them.
  //
  // The cases below relax the even-length guarantee to pin down actual optimal play. Each one
  // still uses an odd total, so a tie — which the problem never defines a winner for — remains
  // impossible. A constant-answer solution fails this section; an interval DP over both ends
  // passes it.
  // ===========================================================================================

  // Step 13: odd length hands the extra turn to Bob's advantage instead — whichever 2 Alice
  //          takes, Bob answers with the 9 and wins 9 to 4
  @Test
  void oddLengthLetsBobTakeTheMiddlePileAndWin() {
    assertThat(sut.stoneGame(new int[] {2, 9, 2})).isFalse();
  }

  // Step 14: odd length is not automatically a loss — here the ends outweigh the middle, so
  //          Alice opens with the 9 and finishes 11 to 8
  @Test
  void oddLengthStillWinsWhenTheEndsOutweighTheMiddle() {
    assertThat(sut.stoneGame(new int[] {9, 2, 8})).isTrue();
  }

  // Step 15: appending one pile flips the Step 6 win into a loss — Alice is forced to spend her
  //          first turn on a small end, which hands Bob the even-length parity advantage and
  //          with it both large piles, 21 to 4
  @Test
  void oneExtraPileFlipsTheParityWinIntoALoss() {
    assertThat(sut.stoneGame(new int[] {1, 10, 1, 11, 2})).isFalse();
  }

  // Step 16: optimal play is not greedy, and optimal play is not always enough — Alice cannot
  //          reach the 10 no matter which end she concedes, and loses 11 to 6
  @Test
  void greedyAndOptimalPlayBothLoseWhenTheLargestPileIsOutOfReach() {
    assertThat(sut.stoneGame(new int[] {1, 2, 10, 3, 1})).isFalse();
  }
}
